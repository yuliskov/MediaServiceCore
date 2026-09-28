package com.liskovsoft.youtubeapi.app.nsigsolver.impl

import com.eclipsesource.v8.V8
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.nsigsolver.common.CacheService
import com.liskovsoft.youtubeapi.app.nsigsolver.common.CachedData
import com.liskovsoft.youtubeapi.app.nsigsolver.common.loadScript
import com.liskovsoft.youtubeapi.app.nsigsolver.common.withLock

/**
 * Solves the n-parameter challenge for TCL-flavored players (tv-player-*-tcl.vflset), whose
 * n-function has a different call shape than the regular web/TV player and isn't recognized by
 * the generic (yt-dlp ejs) [V8ChallengeProvider].
 *
 * Unlike the regular player, the TCL n-function is invoked with a full URL object (it mutates the
 * "n" query param in place) rather than the bare n-param string — [solveN] hides that behind the
 * same bare-nParam contract the rest of the codebase uses, by wrapping/unwrapping a synthetic
 * placeholder URL.
 */
internal object TclChallengeProvider {
    private val tag = TclChallengeProvider::class.simpleName
    private const val libPrefix = "nsigsolver/"
    private val libFilenames = listOf(
        "${libPrefix}polyfill.js",
        "${libPrefix}meriyah-6.1.4.min.js",
        "${libPrefix}astring-1.9.0.min.js",
        "${libPrefix}tcl.solver.js"
    )
    private val solverOutputType = object : TypeToken<SolverOutput>() {}.type
    private var v8Runtime: V8? = null
    private val v8Lock = Any()
    private const val cacheSection = "tcl-challenge-solver"

    private data class SolverOutput(val type: String?, val results: Map<String, String>?, val error: String?, val preprocessed_player: String?)

    /**
     * @param playerUrl the TCL player's url (cache key)
     * @param playerCode the TCL player's JS source
     * @param nParams bare n-parameter values (as seen in a stream url's "n" query param)
     * @return map from each input nParam to its decoded value, or null for one that failed
     */
    fun solveN(playerUrl: String, playerCode: String, nParams: List<String>): Map<String, String?> {
        if (nParams.isEmpty()) {
            return emptyMap()
        }

        // n-params are opaque URL-safe tokens; wrap each in a throwaway url so the TCL
        // n-function (which expects a real URL object, not a bare string) has something valid
        // to parse. The wrapper's own shape doesn't matter beyond being a well-formed https url.
        val challengeByParam = nParams.distinct().associateWith { "https://www.youtube.com/videoplayback?n=$it" }

        val cacheKey = "player:$playerUrl"
        val cachedPreprocessed = CacheService.load(cacheSection, cacheKey)?.code

        val request = if (cachedPreprocessed != null) {
            mapOf(
                "type" to "preprocessed",
                "preprocessed_player" to cachedPreprocessed,
                "challenges" to challengeByParam.values.toList()
            )
        } else {
            mapOf(
                "type" to "player",
                "player" to playerCode,
                "challenges" to challengeByParam.values.toList(),
                "output_preprocessed" to true
            )
        }

        val stdin = "JSON.stringify(tclSolver(${Gson().toJson(request)}));"

        val stdout = try {
            synchronized(v8Lock) {
                initRuntime()
                val result = runV8(stdin)
                shutdownIfNeeded()
                result
            }
        } catch (e: Exception) {
            Log.e(tag, "TCL n-function solve failed: ${e.message}")
            return nParams.associateWith { null }
        }

        val output: SolverOutput = try {
            Gson().fromJson(stdout, solverOutputType)
        } catch (e: JsonSyntaxException) {
            Log.e(tag, "Cannot parse TCL solver output: ${e.message}")
            return nParams.associateWith { null }
        }

        if (output.type == "error") {
            Log.e(tag, "TCL solver error: ${output.error}")
            if (cachedPreprocessed != null) {
                // cached preprocessed script may be stale (e.g. player build rotated)
                CacheService.clear(cacheSection)
            }
            return nParams.associateWith { null }
        }

        output.preprocessed_player?.let { CacheService.store(cacheSection, cacheKey, CachedData(it)) }

        val results = output.results ?: emptyMap()

        return nParams.associateWith { param -> results[challengeByParam[param]] }
    }

    private fun runV8(stdin: String): String {
        val runtime = v8Runtime ?: throw IllegalStateException("V8 runtime not initialized yet")
        return runtime.withLock {
            it.executeStringScript(stdin) ?: throw IllegalStateException("V8 runtime error: empty response")
        }
    }

    private fun initRuntime() {
        if (v8Runtime != null) {
            return
        }
        v8Runtime = V8.createV8Runtime()
        runV8(constructCommonStdin())
    }

    private fun constructCommonStdin(): String {
        return """
        ${loadScript(libFilenames, "Failed to read TCL challenge solver script")}
        "";
        """
    }

    private fun shutdownIfNeeded() {
        val runtime = v8Runtime ?: return
        runtime.withLock {
            it.release(false)
        }
        v8Runtime = null
    }
}

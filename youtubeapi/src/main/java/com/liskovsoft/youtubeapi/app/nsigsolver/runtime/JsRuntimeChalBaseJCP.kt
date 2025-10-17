package com.liskovsoft.youtubeapi.app.nsigsolver.runtime

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.nsigsolver.common.CachedData
import com.liskovsoft.youtubeapi.app.nsigsolver.common.loadScript
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.ChallengeOutput
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderError
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderRejectedRequest
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderResponse
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeRequest
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeResponse
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeType

internal abstract class JsRuntimeChalBaseJCP: JsChallengeProvider() {
    private val tag = JsRuntimeChalBaseJCP::class.simpleName
    protected val cacheSection = "challenge-solver"

    private val jcpGuideUrl = "https://github.com/yt-dlp/yt-dlp/wiki/YouTube-JS-Challenges"
    private val repository = "yt-dlp/ejs"
    override val supportedTypes = listOf(JsChallengeType.N, JsChallengeType.SIG)
    protected val scriptVersion = "0.0.1"
    protected val libPrefix = "nsigsolver/"

    private val scriptFilenames = mapOf(
        ScriptType.LIB to "${libPrefix}yt.solver.lib.js",
        ScriptType.CORE to "${libPrefix}yt.solver.core.js"
    )

    private val minScriptFilenames = mapOf(
        ScriptType.LIB to "yt.solver.lib.min.js",
        ScriptType.CORE to "yt.solver.core.min.js"
    )

    protected abstract fun runJsRuntime(stdin: String): String

    override fun realBulkSolve(requests: List<JsChallengeRequest>): Sequence<JsChallengeProviderResponse> = sequence {
        val grouped: Map<String, List<JsChallengeRequest>> = requests.groupBy { it.input.playerUrl }

        for ((playerUrl, groupedRequests) in grouped) {
            val data = ie.cache.load(cacheSection, "player:$playerUrl")
            var player = data?.code

            val cached = if (player != null) {
                true
            } else {
                player = getPlayer(playerUrl)
                false
            }

            val stdin = constructStdin(player, cached, groupedRequests)
            val stdout = runJsRuntime(stdin)

            val gson = Gson()
            val output: SolverOutput = try {
                gson.fromJson(stdout, solverOutputType)
            } catch (e: JsonSyntaxException) {
                throw JsChallengeProviderError("Cannot parse solver output", e)
            }

            if (output.type == "error")
                throw JsChallengeProviderError(output.error ?: "Unknown solver output error")

            val preprocessed = output.preprocessed_player
            if (preprocessed != null)
                ie.cache.store(cacheSection, "player:$playerUrl", CachedData(preprocessed))

            for ((request, responseData) in groupedRequests.zip(output.responses)) {
                if (responseData.type == "error") {
                    yield(JsChallengeProviderResponse(
                        request, null, JsChallengeProviderError(responseData.error ?: "Unknown solver output error")))
                } else {
                    yield(JsChallengeProviderResponse(
                        request, JsChallengeResponse(request.type, ChallengeOutput(responseData.data))
                    ))
                }
            }
        }
    }

    private fun constructStdin(player: String, preprocessed: Boolean, requests: List<JsChallengeRequest>): String {
        val jsonRequests = requests.map { request ->
            mapOf(
                // TODO: i despise nsig name
                //"type" to if (request.type.value == "n") "nsig" else request.type.value,
                "type" to request.type.value,
                "challenges" to request.input.challenges
            )
        }
        val data = if (preprocessed) {
            mapOf(
                "type" to "preprocessed",
                "preprocessed_player" to player,
                "requests" to jsonRequests
            )
        } else {
            mapOf(
                "type" to "player",
                "player" to player,
                "requests" to jsonRequests,
                "output_preprocessed" to true
            )
        }
        val gson = Gson()
        val jsonData = gson.toJson(data)
        return """
        JSON.stringify(jsc($jsonData));
        """
    }

    protected fun constructCommonStdin(): String {
        return """
        ${libScript.code}
        ${coreScript.code}
        "";
        """
    }

    // region: challenge solver script

    private val libScript: Script by lazy {
        getScript(ScriptType.LIB)
    }

    private val coreScript: Script by lazy {
        getScript(ScriptType.CORE)
    }

    private fun getScript(scriptType: ScriptType): Script {
        for ((_, fromSource) in iterScriptSources()) {
            val script = fromSource(scriptType)
            if (script == null)
                continue
            if (script.version != scriptVersion)
                Log.w(tag, "Challenge solver ${scriptType.value} script version ${script.version} " +
                        "is not supported (source: ${script.source.value}, supported version: $scriptVersion)")

            Log.d(tag, "Using challenge solver ${script.type.value} script v${script.version} " +
                    "(source: ${script.source.value}, variant: ${script.variant.value})")
            return script
        }
        throw JsChallengeProviderRejectedRequest("No usable challenge solver ${scriptType.value} script available")
    }

    protected open fun iterScriptSources(): Sequence<Pair<ScriptSource, (scriptType: ScriptType) -> Script?>> = sequence {
        yieldAll(listOf(
            Pair(ScriptSource.CACHE, ::cachedSource),
            Pair(ScriptSource.BUILTIN, ::builtinSource),
            Pair(ScriptSource.WEB, ::webReleaseSource)
        ))
    }

    private fun cachedSource(scriptType: ScriptType): Script? {
        val data = ie.cache.load(cacheSection, scriptType.value) ?: return null
        return Script(scriptType,
            ScriptVariant.valueOf(data.variant ?: "unknown"), ScriptSource.CACHE, data.version ?: "unknown", data.code)
    }

    private fun builtinSource(scriptType: ScriptType): Script? {
        val fileName = scriptFilenames[scriptType] ?: return null
        val code = loadScript(fileName, "Failed to read builtin challenge solver ${scriptType.value}")
        return Script(scriptType, ScriptVariant.UNMINIFIED, ScriptSource.BUILTIN, scriptVersion, code)
    }

    private fun webReleaseSource(scriptType: ScriptType): Script? {
        val fileName = minScriptFilenames[scriptType] ?: return null
        val url = "https://github.com/$repository/releases/download/$scriptVersion/$fileName"
        val code = ie.downloadWebpageWithRetries(url, "[${tag}] Failed to download challenge solver ${scriptType.value} script")
        Log.d(tag, "[${tag}] Downloading challenge solver ${scriptType.value} script from $url")
        ie.cache.store(cacheSection, scriptType.value, CachedData(code))
        return Script(scriptType, ScriptVariant.MINIFIED, ScriptSource.WEB, scriptVersion, code)
    }

    // endregion: challenge solver script
}
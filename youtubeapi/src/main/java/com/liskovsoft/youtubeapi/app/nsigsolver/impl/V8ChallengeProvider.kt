package com.liskovsoft.youtubeapi.app.nsigsolver.impl

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8ScriptExecutionException
import com.google.gson.Gson
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.nsigsolver.common.loadScript
import com.liskovsoft.youtubeapi.app.nsigsolver.common.withLock
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderError
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.JsRuntimeChalBaseJCP
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.Script
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptSource
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptType
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptVariant

internal object V8ChallengeProvider: JsRuntimeChalBaseJCP() {
    private val tag = V8ChallengeProvider::class.simpleName
    private val v8NpmLibFilename = listOf("${libPrefix}polyfill.js", "${libPrefix}meriyah-6.1.4.min.js", "${libPrefix}astring-1.9.0.min.js")
    private var v8Runtime: V8? = null
    private val v8Lock = Any()

    override fun iterScriptSources(): Sequence<Pair<ScriptSource, (ScriptType) -> Script?>> = sequence {
        for ((source, func) in super.iterScriptSources()) {
            if (source == ScriptSource.WEB || source == ScriptSource.BUILTIN)
                yield(Pair(ScriptSource.BUILTIN, ::v8NpmSource))
            yield(Pair(source, func))
        }
    }

    private fun v8NpmSource(scriptType: ScriptType): Script? {
        if (scriptType != ScriptType.LIB)
            return null
        // V8-specific lib scripts that uses Deno NPM imports
        val code = loadScript(v8NpmLibFilename, "Failed to read v8 challenge solver lib script")
        return Script(scriptType, ScriptVariant.V8_NPM, ScriptSource.BUILTIN, scriptVersion, code)
    }

    // Keep the runtime warm between videos: creating V8 + evaluating the solver libs on every call
    // costs seconds on low-end TV SoCs.
    private const val KEEP_ALIVE = true

    // Solves requests with solvers memoized in the warm runtime (globalThis.__stSolvers).
    private const val FAST_SOLVE_FN = """
        globalThis.__stSolve = function(reqs) {
            var s = globalThis.__stSolvers;
            return { type: 'result', responses: reqs.map(function(q) {
                var f = s && s[q.type];
                if (!f) return { type: 'error', error: 'Failed to extract ' + q.type + ' function' };
                try {
                    var d = {};
                    q.challenges.forEach(function(c) { d[c] = f(c); });
                    return { type: 'result', data: d };
                } catch (e) {
                    return { type: 'error', error: '' + e };
                }
            }) };
        };
        "";
    """

    override fun runJsRuntime(stdin: String): String {
        synchronized(v8Lock) {
            initRuntime()

            val result = runV8(stdin)

            if (!KEEP_ALIVE)
                shutdownIfNeeded()

            return result
        }
    }

    override fun fastSolve(playerUrl: String, preprocessedPlayer: String, requestsJson: String): String? {
        if (!KEEP_ALIVE)
            return null

        synchronized(v8Lock) {
            return try {
                initRuntime()
                val runtime = v8Runtime ?: return null
                val gson = Gson()
                val key = gson.toJson(playerUrl)
                runtime.withLock {
                    if (!it.executeBooleanScript("globalThis.__stKey === $key")) {
                        it.executeVoidScript("""
                            (function() {
                                var r = { n: null, sig: null };
                                Function('_result', ${gson.toJson(preprocessedPlayer)})(r);
                                globalThis.__stSolvers = r;
                                globalThis.__stKey = $key;
                            })();
                        """)
                    }
                    it.executeStringScript("JSON.stringify(globalThis.__stSolve($requestsJson));")
                }
            } catch (e: Exception) {
                Log.e(tag, "Fast solve failed, falling back: ${e.message}")
                resetMemo()
                null
            }
        }
    }

    private fun resetMemo() {
        try {
            v8Runtime?.withLock { it.executeVoidScript("globalThis.__stKey = null; globalThis.__stSolvers = null;") }
        } catch (e: Exception) {
            disposeRuntime()
        }
    }

    private fun runV8(stdin: String): String {
        val runtime = v8Runtime ?: throw JsChallengeProviderError("V8 runtime not initialized yet")
        try {
            return runtime.withLock {
                it.executeStringScript(stdin) ?: throw JsChallengeProviderError("V8 runtime error: empty response")
            }
        } catch (e: V8ScriptExecutionException) {
            if (e.message?.contains("Invalid or unexpected token") ?: false)
                ie.cache.clear(cacheSection) // cached data broken?
            throw JsChallengeProviderError("V8 runtime error: ${e.message}", e)
        }
    }

    private fun initRuntime() {
        if (v8Runtime != null)
            return
        v8Runtime = V8.createV8Runtime()
        runV8(constructCommonStdin()) // ignore the result, just warm up
        runV8(FAST_SOLVE_FN)
    }

    private fun disposeRuntime() {
        val runtime = v8Runtime ?: return

        // NOTE: getting lock fixes "Invalid V8 thread access: the locker has been released!"
        runtime.withLock {
            it.release(false)
        }
        v8Runtime = null
    }

    private fun shutdownIfNeeded() {
        // NOTE: Possible Invalid thread access if using RxHelper runAsync
        // NOTE: Shutdown should run on the same thread that created V8 engine.
        disposeRuntime()
    }
}
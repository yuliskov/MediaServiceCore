package com.liskovsoft.youtubeapi.app.nsigsolver.impl

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8ScriptExecutionException
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
    private val v8NpmLibFilename = listOf("${libPrefix}polyfill.js", "${libPrefix}meriyah.bundle.min.js", "${libPrefix}astring.bundle.min.js")
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

    override fun runJsRuntime(stdin: String): String {
        warmup()

        return runV8(stdin)
    }

    private fun runV8(stdin: String): String {
        synchronized(v8Lock) {
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
    }
    
    fun warmup() {
        synchronized(v8Lock) {
            if (v8Runtime != null)
                return
            v8Runtime = V8.createV8Runtime()
        }
        runV8(constructCommonStdin()) // ignore the result, just warm up
    }

    fun shutdown() {
        synchronized(v8Lock) {
            val runtime = v8Runtime ?: return

            // NOTE: getting lock fixes "Invalid V8 thread access: the locker has been released!"
            runtime.withLock {
                it.release(false)
            }
            v8Runtime = null
        }
    }

    fun forceRecreate() {
        shutdown()
        warmup()
    }
}
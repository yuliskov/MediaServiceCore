package com.liskovsoft.youtubeapi.app.nsigsolver.impl

import com.liskovsoft.googlecommon.common.js.JavaScriptRuntime
import com.liskovsoft.googlecommon.common.js.JavaScriptRuntime.JavaScriptRuntimeException
import com.liskovsoft.youtubeapi.app.nsigsolver.common.loadScript
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderError
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.JsRuntimeChalBaseJCP
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.Script
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptSource
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptType
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptVariant

/** Runs player challenges in the System WebView's isolated JavaScript engine. */
internal object JsChallangeProvider : JsRuntimeChalBaseJCP() {
    private val npmLibFilenames = listOf(
        "${libPrefix}polyfill.js",
        "${libPrefix}meriyah-6.1.4.min.js",
        "${libPrefix}astring-1.9.0.min.js"
    )
    private val runtimeLock = Any()
    private var session: JavaScriptRuntime.Session? = null

    override fun iterScriptSources(): Sequence<Pair<ScriptSource, (ScriptType) -> Script?>> = sequence {
        for ((source, func) in super.iterScriptSources()) {
            if (source == ScriptSource.WEB || source == ScriptSource.BUILTIN) {
                yield(Pair(ScriptSource.BUILTIN, ::npmSource))
            }
            yield(Pair(source, func))
        }
    }

    private fun npmSource(scriptType: ScriptType): Script? {
        if (scriptType != ScriptType.LIB) return null

        val code = loadScript(npmLibFilenames, "Failed to read JS challenge solver lib script")
        return Script(scriptType, ScriptVariant.V8_NPM, ScriptSource.BUILTIN, scriptVersion, code)
    }

    override fun runJsRuntime(stdin: String): String = synchronized(runtimeLock) {
        val activeSession = initRuntime()

        try {
            activeSession.evaluate(stdin).takeIf { it.isNotEmpty() }
                ?: throw JsChallengeProviderError("JavaScriptEngine returned an empty response")
        } catch (error: JavaScriptRuntimeException) {
            if (error.message?.contains("Invalid or unexpected token") == true) {
                ie.cache.clear(cacheSection)
            }
            disposeRuntime()
            throw JsChallengeProviderError("JavaScriptEngine error: ${error.message}", error)
        }
    }

    private fun initRuntime(): JavaScriptRuntime.Session {
        session?.let { return it }

        val newSession = try {
            JavaScriptRuntime.createSession()
        } catch (error: JavaScriptRuntimeException) {
            throw JsChallengeProviderError("Could not initialize JavaScriptEngine", error)
        }

        try {
            newSession.evaluate(constructCommonStdin())
        } catch (error: JavaScriptRuntimeException) {
            newSession.close()
            throw JsChallengeProviderError("Could not initialize the JS challenge solver", error)
        }

        session = newSession
        return newSession
    }

    fun warmup() {
        synchronized(runtimeLock) {
            initRuntime()
        }
    }

    fun shutdown() {
        synchronized(runtimeLock) {
            disposeRuntime()
        }
    }

    fun forceRecreate() {
        synchronized(runtimeLock) {
            disposeRuntime()
            initRuntime()
        }
    }

    private fun disposeRuntime() {
        session?.close()
        session = null
    }
}

package com.liskovsoft.youtubeapi.app.nsigsolver.impl

import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.app.nsigsolver.common.loadScript
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeProviderError
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.JsRuntimeChalBaseJCP
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.Script
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptSource
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptType
import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.ScriptVariant
import io.reactivex.disposables.Disposable

internal object V8ChallengeProvider: JsRuntimeChalBaseJCP() {
    private val tag = V8ChallengeProvider::class.simpleName
    private val v8NpmLibFilename = listOf("${libPrefix}polyfill.js", "${libPrefix}meriyah.bundle.min.js", "${libPrefix}astring.bundle.min.js")
    private var v8Runtime: V8? = null
    private var releaseV8: Disposable? = null

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
        val runtime = v8Runtime ?: throw JsChallengeProviderError("V8 runtime not initialized yet")

        synchronized(runtime) {
            try {
                runtime.locker?.acquire()
                return runtime.executeStringScript(stdin) ?: throw JsChallengeProviderError("V8 runtime error: empty response")
            } catch (e: V8ScriptExecutionException) {
                throw JsChallengeProviderError("V8 runtime error", e)
            } finally {
                runtime.locker?.release()
            }
        }
    }

    fun warmup() {
        if (v8Runtime == null) {
            v8Runtime = V8.createV8Runtime()
            runV8(constructCommonStdin()) // ignore the result, just warm up
        }

        //RxHelper.disposeActions(releaseV8)
        //releaseV8 = RxHelper.runAsync(::shutdown, 180_000)
    }

    fun shutdown() {
        v8Runtime?.release(false)
        v8Runtime = null
    }
}
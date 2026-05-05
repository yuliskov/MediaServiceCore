package com.liskovsoft.youtubeapi.app.potokennp2.misc

import com.eclipsesource.v8.JavaVoidCallback
import com.eclipsesource.v8.V8
import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.youtubeapi.app.nsigsolver.common.withLock
import com.liskovsoft.youtubeapi.app.potokennp2.V8WrapperException

internal class V8Wrapper {
    private val tag = V8Wrapper::class.simpleName
    private var v8Runtime: V8? = null
    private val v8Lock = Any()

    fun executeStringScript(stdin: String): String {
        synchronized(v8Lock) {
            return executeInternal {
                it.executeStringScript(stdin) ?: throw V8WrapperException("V8 runtime error: empty response")
            }
        }
    }

    fun executeVoidScript(stdin: String) {
        synchronized(v8Lock) {
            executeInternal {
                it.executeVoidScript(stdin)
            }
        }
    }

    private fun <T> executeInternal(block: (V8) -> T): T {
        initRuntime()

        val runtime = v8Runtime ?: throw V8WrapperException("V8 runtime not initialized yet")

        try {
            return runtime.withLock {
                block(it)
            }
        } catch (e: V8ScriptExecutionException) {
            throw V8WrapperException("V8 runtime error: ${e.message}", e)
        }
    }

    private fun initRuntime() {
        if (v8Runtime != null)
            return
        v8Runtime = V8.createV8Runtime()
    }

    fun shutdownRuntime() {
        synchronized(v8Lock) {
            disposeRuntime()
        }
    }

    private fun disposeRuntime() {
        val runtime = v8Runtime ?: return

        // NOTE: getting lock fixes "Invalid V8 thread access: the locker has been released!"
        runtime.withLock {
            it.release(false)
        }
        v8Runtime = null
    }

    fun registerJavaMethod(callback: JavaVoidCallback, jsFunctionName: String) {
        synchronized(v8Lock) {
            initRuntime()
            v8Runtime?.registerJavaMethod(callback, jsFunctionName)
        }
    }
}
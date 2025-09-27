package com.liskovsoft.youtubeapi.app.nsigsolver.impl

import com.liskovsoft.youtubeapi.app.nsigsolver.runtime.JsRuntimeChalBaseJCP

internal object V8ChallengeProvider: JsRuntimeChalBaseJCP() {
    override fun runJsRuntime(stdin: String): String {
        return ""
    }
}
package com.liskovsoft.youtubeapi.app.nsigsolver.common

import com.liskovsoft.youtubeapi.app.AppService

internal open class ScriptLoaderError(message: String, cause: Exception? = null): Exception(message, cause)

internal fun loadScript(filename: String, errorMsg: String? = null): String {
    val context = AppService.instance().context ?:
        throw ScriptLoaderError("Context isn't available".let { msg -> errorMsg?.let { "$it: $msg" } ?: msg })

    return context.assets.open(filename).bufferedReader()
        .use { it.readText() }
}

internal fun loadScript(filenames: List<String>, errorMsg: String? = null): String {
    return buildString {
        for (filename in filenames) {
            append(loadScript(filename, errorMsg))
        }
    }
}
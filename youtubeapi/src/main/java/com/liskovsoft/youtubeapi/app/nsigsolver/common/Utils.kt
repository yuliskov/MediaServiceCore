package com.liskovsoft.youtubeapi.app.nsigsolver.common

import com.liskovsoft.youtubeapi.app.AppService

internal class ScriptLoaderError(message: String, cause: Exception? = null): Exception(message, cause)

internal fun loadScript(filename: String, errorMsg: String? = null): String {
    val context = try {
        AppService.instance().context
    } catch (e: Exception) {
        throw ScriptLoaderError(formatError(errorMsg, "Context isn't available"), e)
    }

    try {
        return context.assets.open(filename).bufferedReader()
            .use { it.readText() }
    } catch (e: Exception) {
        throw ScriptLoaderError(formatError(errorMsg, "Error reading file: $filename"), e)
    }
}

internal fun loadScript(filenames: List<String>, errorMsg: String? = null): String {
    return buildString {
        for (filename in filenames) {
            append(loadScript(filename, errorMsg))
        }
    }
}

internal fun formatError(firstMsg: String?, secondMsg: String) = firstMsg?.let { "$it: $secondMsg" } ?: secondMsg
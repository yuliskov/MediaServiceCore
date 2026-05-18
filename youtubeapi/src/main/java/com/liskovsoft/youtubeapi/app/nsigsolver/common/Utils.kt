package com.liskovsoft.youtubeapi.app.nsigsolver.common

import com.eclipsesource.v8.V8
import com.liskovsoft.sharedutils.helpers.FileHelpers
import com.liskovsoft.youtubeapi.app.AppService
import java.io.File

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

internal fun loadFromCache(fileName: String?): String? {
    if (fileName == null || fileName.length > 50)
        return null

    val cache = FileHelpers.getCacheDir(AppService.instance().context)

    return FileHelpers.getFileContents(File(cache, fileName))
}

internal fun persistToCache(fileName: String, content: String) {
    if (fileName.length > 50)
        return

    val cache = FileHelpers.getCacheDir(AppService.instance().context)

    FileHelpers.stringToFile(content, File(cache, fileName))
}

internal fun formatError(firstMsg: String?, secondMsg: String) = firstMsg?.let { "$it: $secondMsg" } ?: secondMsg

internal inline fun <T> V8.withLock(block: (V8) -> T): T {
    locker.acquire()
    try {
        return block(this)
    } finally {
        locker.release()
    }
}
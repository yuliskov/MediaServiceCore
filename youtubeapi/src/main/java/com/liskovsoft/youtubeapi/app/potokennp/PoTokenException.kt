package com.liskovsoft.youtubeapi.app.potokennp

internal class PoTokenException(message: String) : Exception(message)

// to be thrown if the WebView provided by the system is broken
internal class BadWebViewException(message: String) : Exception(message)

internal fun buildExceptionForJsError(error: String): Exception {
    return if (error.contains("SyntaxError"))
        BadWebViewException(error)
    else
        PoTokenException(error)
}

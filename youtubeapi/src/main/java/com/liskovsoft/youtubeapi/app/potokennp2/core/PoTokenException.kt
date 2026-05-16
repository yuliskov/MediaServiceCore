package com.liskovsoft.youtubeapi.app.potokennp2.core

internal class PoTokenException(message: String) : RuntimeException(message)

// to be thrown if the WebView provided by the system is broken
internal class BadWebViewException(message: String) : RuntimeException(message)

internal class V8WrapperException(message: String, cause: Exception? = null) : RuntimeException(message, cause)

internal fun buildExceptionForJsError(error: String): Throwable {
    return if (error.contains("SyntaxError"))
        BadWebViewException(error)
    else
        PoTokenException(error)
}

package com.liskovsoft.youtubeapi.common.helpers

import com.liskovsoft.sharedutils.okhttp.OkHttpCommons
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request

object RetrofitOkHttpClient {
    @JvmStatic
    val authHeaders = mutableMapOf<String, String>()

    @JvmStatic
    val instance: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
        addCommonHeaders(builder)
        OkHttpCommons.setupBuilder(builder)
        builder.build()
    }

    private val headers = mapOf(
        "User-Agent" to DefaultHeaders.APP_USER_AGENT,
        // Enable compression in production
        "Accept-Encoding" to DefaultHeaders.ACCEPT_ENCODING,
        "Referer" to "https://www.youtube.com/tv"
    )

    private const val API_URL = "https://www.youtube.com/youtubei/v1/"

    private fun addCommonHeaders(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
            val request = chain.request()
            val headers = request.headers()
            val requestBuilder = request.newBuilder()

            apply(this.headers, headers, requestBuilder)

            if (request.url().toString().startsWith(API_URL)) {
                apply(authHeaders, headers, requestBuilder)
            }

            chain.proceed(requestBuilder.build())
        }
    }

    private fun apply(newHeaders: Map<String, String>, oldHeaders: Headers, builder: Request.Builder) {
        for (header in newHeaders) {
            // Don't override existing headers
            oldHeaders[header.key] ?: builder.header(header.key, header.value)
        }
    }
}
package com.liskovsoft.youtubeapi.common.helpers

import com.liskovsoft.sharedutils.okhttp.OkHttpCommons
import okhttp3.OkHttpClient

object RetrofitOkHttpClient {
    @JvmStatic
    val HEADERS = mutableMapOf(
        "User-Agent" to DefaultHeaders.APP_USER_AGENT,
        // Enable compression in production
        "Accept-Encoding" to DefaultHeaders.ACCEPT_ENCODING,
        "Referer" to "https://www.youtube.com/tv"
    )

    @JvmStatic
    val instance: OkHttpClient by lazy {
        val builder = OkHttpCommons.createBuilder()
        addCommonHeaders(builder)
        builder.build()
    }

    private fun addCommonHeaders(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            for (header in HEADERS) {
                requestBuilder.header(header.key, header.value)
            }

            chain.proceed(requestBuilder.build())
        }
    }
}
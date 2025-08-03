package com.liskovsoft.googlecommon.common.helpers

import com.google.net.cronet.okhttptransport.CronetInterceptor
import com.liskovsoft.sharedutils.cronet.CronetManager
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.okhttp.OkHttpCommons
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.googlecommon.app.AppConstants
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.search.SearchApi
import okhttp3.Headers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

internal object RetrofitOkHttpHelper {
    private val authSkipList = mutableListOf<Request>()

    @JvmStatic
    val authHeaders = mutableMapOf<String, String>()

    @JvmStatic
    val authHeaders2 = mutableMapOf<String, String>()

    @JvmStatic
    val client: OkHttpClient by lazy { createClient() }

    @JvmStatic
    var disableCompression: Boolean = false

    @JvmStatic
    fun addAuthSkip(request: Request) {
        if (!authSkipList.contains(request))
            authSkipList.add(request)
    }

    private val commonHeaders = mapOf(
        // Enable compression in production
        "Accept-Encoding" to DefaultHeaders.ACCEPT_ENCODING,
    )

    private val apiHeaders = mapOf(
        "User-Agent" to DefaultHeaders.APP_USER_AGENT,
        "Referer" to DefaultHeaders.REFERER
    )

    private val apiPrefixes = arrayOf(
        "https://www.googleapis.com/upload/drive/v3",
        "https://www.googleapis.com/drive/v3",
        "https://m.youtube.com/youtubei/v1/",
        "https://www.youtube.com/youtubei/v1/",
        "https://www.youtube.com/api/stats/",
        "https://clients1.google.com/complete/"
    )

    private fun createClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()
        addCommonHeaders(builder)
        OkHttpCommons.setupBuilder(builder)
        //addCronetInterceptor(builder)
        return builder.build()
    }

    private fun addCommonHeaders(builder: OkHttpClient.Builder) {
        builder.addInterceptor { chain ->
            val request = chain.request()
            val headers = request.headers()
            val requestBuilder = request.newBuilder()

            applyHeaders(this.commonHeaders, headers, requestBuilder)

            val url = request.url().toString()

            if (Helpers.startsWithAny(url, *apiPrefixes)) {
                val doSkipAuth = authSkipList.remove(request)

                // Empty Home fix (anonymous user) and improve Recommendations for everyone
                headers["X-Goog-Visitor-Id"] ?: AppService.instance().visitorData?.let { requestBuilder.header("X-Goog-Visitor-Id", it) }

                if (doSkipAuth) // visitor generation fix
                    requestBuilder.removeHeader("X-Goog-Visitor-Id")

                applyHeaders(this.apiHeaders, headers, requestBuilder)

                if (authHeaders.isEmpty() || doSkipAuth) {
                    applyQueryKeys(mapOf("key" to AppConstants.API_KEY, "prettyPrint" to "false"), request, requestBuilder)
                } else {
                    applyQueryKeys(mapOf("prettyPrint" to "false"), request, requestBuilder)
                    // Fix suggestions on non branded accounts
                    if (url.startsWith(SearchApi.TAGS_URL) && authHeaders2.isNotEmpty()) {
                        applyHeaders(authHeaders2, headers, requestBuilder)
                    } else {
                        applyHeaders(authHeaders, headers, requestBuilder)
                    }
                }
            }

            chain.proceed(requestBuilder.build())
        }
    }

    private fun applyHeaders(newHeaders: Map<String, String?>, oldHeaders: Headers, builder: Request.Builder) {
        for (header in newHeaders) {
            if (disableCompression && header.key == "Accept-Encoding") {
                continue
            }

            // Don't override existing headers
            oldHeaders[header.key] ?: header.value?.let { builder.header(header.key, it) } // NOTE: don't remove null check
        }
    }

    private fun applyQueryKeys(keys: Map<String, String>, request: Request, builder: Request.Builder) {
        val originUrl = request.url()

        var newUrlBuilder: HttpUrl.Builder? = null

        for (entry in keys) {
            // Don't override existing keys
            originUrl.queryParameter(entry.key) ?: run {
                if (newUrlBuilder == null) {
                    newUrlBuilder = originUrl.newBuilder()
                }

                newUrlBuilder?.addQueryParameter(entry.key, entry.value)
            }
        }

        newUrlBuilder?.run {
            builder.url(build())
        }
    }

    private fun addCronetInterceptor(builder: OkHttpClient.Builder) {
        val engine = CronetManager.getEngine(GlobalPreferences.sInstance.context)
        if (engine != null) {
            builder.addInterceptor(CronetInterceptor.newBuilder(engine).build())
        }
    }
}
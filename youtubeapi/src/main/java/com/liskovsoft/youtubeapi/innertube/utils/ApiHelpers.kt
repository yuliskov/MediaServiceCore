package com.liskovsoft.youtubeapi.innertube.utils

import com.liskovsoft.youtubeapi.innertube.models.InnertubeContext
import com.liskovsoft.youtubeapi.innertube.models.SessionDataResult

internal object ApiHelpers {
    fun createSessionDataHeaders(): Map<String, String> {
        // TODO: replace with the real values
        return mapOf(
            "Accept-Language" to "en-US",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 OPR/121.0.0.0",
            "Cookie" to "PREF=tz=Europe.Kiev;VISITOR_INFO1_LIVE=mCer5fzOkXs;"
        )
    }

    fun createInnertubeConfigHeaders(sessionData: SessionDataResult): Map<String, String> {
        // TODO: replace with the real values
        return buildMap {
            put("Accept-Language", "")

            sessionData.ytcfg?.deviceInfo?.visitorData
                ?.let { put("X-Goog-Visitor-Id", it) }

            sessionData.ytcfg?.deviceInfo?.clientVersion
                ?.let { put("X-Youtube-Client-Version", it) }
        }
    }

    fun createInnertubeJsonConfig(innertubeContext: InnertubeContext): String {
        return toJsonString(mapOf("context" to innertubeContext))
    }
}
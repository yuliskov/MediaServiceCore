package com.liskovsoft.youtubeapi.session

internal object SessionApiHelper {
    fun createSessionDataHeaders(): Map<String, String> {
        // TODO: replace with the real values
        return mapOf(
            "Accept-Language" to "en-US",
            "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/137.0.0.0 Safari/537.36 OPR/121.0.0.0",
            "Cookie" to "PREF=tz=Europe.Kiev;VISITOR_INFO1_LIVE=mCer5fzOkXs;"
        )
    }
}
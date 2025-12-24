package com.liskovsoft.youtubeapi.innertube.helpers

internal object URLS {
    const val YT_BASE = "https://www.youtube.com"
    const val YT_MUSIC_BASE = "https://music.youtube.com"
    const val YT_SUGGESTIONS = "https://suggestqueries-clients6.youtube.com"
    const val YT_UPLOAD = "https://upload.youtube.com/"
    const val GOOGLE_SEARCH_BASE = "https://www.google.com/"

    internal object API {
        const val BASE = "https://youtubei.googleapis.com"
        const val PRODUCTION_1 = "https://www.youtube.com/youtubei/"
        const val PRODUCTION_2 = "https://youtubei.googleapis.com/youtubei/"
        const val STAGING = "https://green-youtubei.sandbox.googleapis.com/youtubei/"
        const val RELEASE = "https://release-youtubei.sandbox.googleapis.com/youtubei/"
        const val TEST = "https://test-youtubei.sandbox.googleapis.com/youtubei/"
        const val CAMI = "http://cami-youtubei.sandbox.googleapis.com/youtubei/"
        const val UYTFE = "https://uytfe.sandbox.google.com/youtubei/"
    }
}

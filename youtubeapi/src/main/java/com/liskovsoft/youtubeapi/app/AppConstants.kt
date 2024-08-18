package com.liskovsoft.youtubeapi.app

internal object AppConstants {
    @JvmField
    val playerUrls = listOf(
        "https://www.youtube.com/s/player/1f8742dc/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/20dfca59/player_ias.vflset/en_US/base.js",
        "https://www.youtube.com/s/player/b12cc44b/tv-player-ias.vflset/tv-player-ias.js")
    private const val API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8"
    private const val API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"

    /**
     * Used when parsing video_info data
     */
    const val VIDEO_INFO_JSON_CONTENT_PARAM = "player_response"

    const val VISITOR_COOKIE_NAME = "VISITOR_INFO1_LIVE"
    
    const val SCRIPTS_URL_BASE = "https://www.youtube.com"
    const val API_KEY = API_KEY_NEW

    const val GET_VIDEO_INFO_OLD =
        "https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv"

    const val GET_VIDEO_INFO_OLD2 =
        "https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv"
}
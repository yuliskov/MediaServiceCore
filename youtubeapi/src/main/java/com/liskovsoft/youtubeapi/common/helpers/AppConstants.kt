package com.liskovsoft.youtubeapi.common.helpers

internal object AppConstants {
    @JvmField
    val playerUrls = listOf(
        // NOTE: tv player should be in the top (ias ones may not validate correctly)
        "https://www.youtube.com/s/player/ab89db3f/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/21176969/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/bcd893b3/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/17ad44a3/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/a61444a1/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/2b83d2e0/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/0004de42/tv-player-ias.vflset/tv-player-ias.js", // the lastest working old type
        "https://www.youtube.com/s/player/9f49a55a/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/010fbc8d/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/69b31e11/tv-player-es6-tcc.vflset/tv-player-es6-tcc.js", // 503 error
        "https://www.youtube.com/s/player/69b31e11/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/69b31e11/tv-player-es6-tce.vflset/tv-player-es6-tce.js", // implements global helper functions
        "https://www.youtube.com/s/player/69b31e11/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/6e20d3a8/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/ef259203/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/8e20cb06/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/e12fbea4/player_ias_tce.vflset/en_US/base.js", // implements global helper functions
        "https://www.youtube.com/s/player/e12fbea4/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/a10d7fcc/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/5dcb2c1f/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/14397202/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/680f8c75/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/20c72c18/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/6275f73c/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/a74bf670/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/612f74a3/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/94f771d8/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/9fe2e06e/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/fc2a56a5/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/59b252b9/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/8102da6c/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/22f02d3d/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/6450230e/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/9599b765/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/73381ccc/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/8a8ac953/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/20830619/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/69f581a5/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/4fcd6e4a/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/643afba4/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/363db69b/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/c8dbda2a/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/e7567ecf/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/2f1832d2/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/baafab19/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/fb725ac8/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/1f8742dc/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/b12cc44b/tv-player-ias.vflset/tv-player-ias.js",
    )

    private const val API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8"
    private const val API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"

    /**
     * Used when parsing video_info data
     */
    const val VIDEO_INFO_JSON_CONTENT_PARAM = "player_response"

    const val VISITOR_INFO_COOKIE = "VISITOR_INFO1_LIVE"
    const val VISITOR_PRIVACY_COOKIE = "VISITOR_PRIVACY_METADATA"

    const val SCRIPTS_URL_BASE = "https://www.youtube.com"
    const val API_KEY = API_KEY_NEW

    const val GET_VIDEO_INFO_OLD =
        "https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv"

    const val GET_VIDEO_INFO_OLD2 =
        "https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv"
}
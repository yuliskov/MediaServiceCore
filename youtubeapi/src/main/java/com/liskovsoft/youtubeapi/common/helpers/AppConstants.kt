package com.liskovsoft.youtubeapi.common.helpers

internal object AppConstants {
    @JvmField
    val playerUrls = listOf(
        // NOTE: TV player should be in the top (ias ones may not validate correctly)
        "https://www.youtube.com/s/player/7460dd14/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/4fd832e7/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/1c642fb9/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/8c3fda2d/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/f572e43c/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/e937390a/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/06ab6907/tv-player-es6.vflset/tv-player-es6.js", // the recent one with common nParam among all the clients
        "https://www.youtube.com/s/player/854a788e/player_es6.vflset/en_US/base.js",
        "https://www.youtube.com/s/player/b81a9a58/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/c2f7551f/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/8180e7ff/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/76ad2fe8/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/76ad2fe8/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/21cd2156/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/74edf1a3/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/18d29a11/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/e42f4bf8/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/56211dc2/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/6c5cb4f4/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/99f55c01/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/ecc3e9a7/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/140dafda/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/3510b6ff/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/721caf0b/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/ab89db3f/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/21176969/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/bcd893b3/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/17ad44a3/tv-player-es6.vflset/tv-player-es6.js",
        "https://www.youtube.com/s/player/a61444a1/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/2b83d2e0/tv-player-ias.vflset/tv-player-ias.js",
        "https://www.youtube.com/s/player/0004de42/tv-player-ias.vflset/tv-player-ias.js",
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
        "https://www.youtube.com/s/player/14397202/tv-player-es6.vflset/tv-player-es6.js"
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
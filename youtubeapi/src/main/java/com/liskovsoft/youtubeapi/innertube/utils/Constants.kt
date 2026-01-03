package com.liskovsoft.youtubeapi.innertube.utils

internal object URLS {
    const val YT_BASE = "https://www.youtube.com"
    const val YT_MUSIC_BASE = "https://music.youtube.com"
    const val YT_SUGGESTIONS = "https://suggestqueries-clients6.youtube.com"
    const val YT_UPLOAD = "https://upload.youtube.com/"
    const val GOOGLE_SEARCH_BASE = "https://www.google.com/"

    object API {
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

internal object CLIENTS {
    val IOS = CLIENT(
        NAME = "iOS",
        VERSION = "20.11.6",
        USER_AGENT = "com.google.ios.youtube/20.11.6 (iPhone10,4; U; CPU iOS 16_7_7 like Mac OS X)",
        DEVICE_MODEL = "iPhone10,4",
        OS_NAME = "iOS",
        OS_VERSION = "16.7.7.20H330"
    )

    val WEB = CLIENT(
        NAME = "WEB",
        VERSION = "2.20250222.10.00",
        API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8",
        API_VERSION = "v1",
        STATIC_VISITOR_ID = "6zpwvWUNAco",
        SUGG_EXP_ID = "ytzpb5_e2,ytpo.bo.lqp.elu=1,ytpo.bo.lqp.ecsc=1,ytpo.bo.lqp.mcsc=3,ytpo.bo.lqp.mec=1,ytpo.bo.lqp.rw=0.8,ytpo.bo.lqp.fw=0.2,ytpo.bo.lqp.szp=1,ytpo.bo.lqp.mz=3,ytpo.bo.lqp.al=en_us,ytpo.bo.lqp.zrm=1,ytpo.bo.lqp.er=1,ytpo.bo.ro.erl=1,ytpo.bo.ro.mlus=3,ytpo.bo.ro.erls=3,ytpo.bo.qfo.mlus=3,ytzprp.ppp.e=1,ytzprp.ppp.st=772,ytzprp.ppp.p=5"
    )

    val MWEB = CLIENT(
        NAME = "MWEB",
        VERSION = "2.20250224.01.00",
        API_VERSION = "v1"
    )

    val WEB_KIDS = CLIENT(
        NAME = "WEB_KIDS",
        VERSION = "2.20250221.11.00"
    )

    val YTMUSIC = CLIENT(
        NAME = "WEB_REMIX",
        VERSION = "1.20250219.01.00"
    )

    val ANDROID = CLIENT(
        NAME = "ANDROID",
        VERSION = "19.35.36",
        SDK_VERSION = 33,
        USER_AGENT = "com.google.android.youtube/19.35.36(Linux; U; Android 13; en_US; SM-S908E Build/TP1A.220624.014) gzip"
    )

    val YTSTUDIO_ANDROID = CLIENT(
        NAME = "ANDROID_CREATOR",
        VERSION = "22.43.101"
    )

    val YTMUSIC_ANDROID = CLIENT(
        NAME = "ANDROID_MUSIC",
        VERSION = "5.34.51"
    )

    val TV = CLIENT(
        NAME = "TVHTML5",
        VERSION = "7.20250219.14.00",
        USER_AGENT = "Mozilla/5.0 (ChromiumStylePlatform) Cobalt/Version"
    )

    val TV_SIMPLY = CLIENT(
        NAME = "TVHTML5_SIMPLY",
        VERSION = "1.0"
    )

    val TV_EMBEDDED = CLIENT(
        NAME = "TVHTML5_SIMPLY_EMBEDDED_PLAYER",
        VERSION = "2.0"
    )

    val WEB_EMBEDDED = CLIENT(
        NAME = "WEB_EMBEDDED_PLAYER",
        VERSION = "1.20250219.01.00",
        API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8",
        API_VERSION = "v1",
        STATIC_VISITOR_ID = "6zpwvWUNAco"
    )

    val WEB_CREATOR = CLIENT(
        NAME = "WEB_CREATOR",
        VERSION = "1.20241203.01.00",
        API_KEY = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8",
        API_VERSION = "v1",
        STATIC_VISITOR_ID = "6zpwvWUNAco"
    )

    val ALl = mapOf(
        "IOS" to IOS,
        "WEB" to WEB,
        "MWEB" to MWEB,
        "WEB_KIDS" to WEB_KIDS,
        "YTMUSIC" to YTMUSIC,
        "ANDROID" to ANDROID,
        "YTSTUDIO_ANDROID" to YTSTUDIO_ANDROID,
        "YTMUSIC_ANDROID" to YTMUSIC_ANDROID,
        "TV" to TV,
        "TV_SIMPLY" to TV_SIMPLY,
        "TV_EMBEDDED" to TV_EMBEDDED,
        "WEB_EMBEDDED" to WEB_EMBEDDED,
        "WEB_CREATOR" to WEB_CREATOR
    )
}

internal data class CLIENT(
    val NAME: String,
    val VERSION: String,
    val SDK_VERSION: Int? = null,
    val DEVICE_MODEL: String? = null,
    val USER_AGENT: String? = null,
    val OS_NAME: String? = null,
    val OS_VERSION: String? = null,
    val API_KEY: String? = null,
    val API_VERSION: String = "v1",
    val STATIC_VISITOR_ID: String? = null,
    val SUGG_EXP_ID: String? = null
)

/**
 * The keys correspond to the `NAME` fields in {@linkcode CLIENTS} constant
 */
internal val CLIENT_NAME_IDS: Map<String, String> = mapOf(
    "iOS" to "5",
    "WEB" to "1",
    "MWEB" to "2",
    "WEB_KIDS" to "76",
    "WEB_REMIX" to "67",
    "ANDROID" to "3",
    "ANDROID_CREATOR" to "14",
    "ANDROID_MUSIC" to "21",
    "TVHTML5" to "7",
    "TVHTML5_SIMPLY" to "74",
    "TVHTML5_SIMPLY_EMBEDDED_PLAYER" to "85",
    "WEB_EMBEDDED_PLAYER" to "56",
    "WEB_CREATOR" to "62"
)

internal val SUPPORTED_CLIENTS = listOf(
    "IOS",
    "WEB",
    "MWEB",
    "YTKIDS",
    "YTMUSIC",
    "ANDROID",
    "YTSTUDIO_ANDROID",
    "YTMUSIC_ANDROID",
    "TV",
    "TV_SIMPLY",
    "TV_EMBEDDED",
    "WEB_EMBEDDED",
    "WEB_CREATOR"
)

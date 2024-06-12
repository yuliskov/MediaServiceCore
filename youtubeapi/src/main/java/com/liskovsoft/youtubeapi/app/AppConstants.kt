package com.liskovsoft.youtubeapi.app

import com.liskovsoft.youtubeapi.common.helpers.DefaultHeaders

internal object AppConstants {
    private const val API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8"
    private const val API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"

    /**
     * Used when parsing video_info data
     */
    const val VIDEO_INFO_JSON_CONTENT_PARAM = "player_response"

    const val VISITOR_COOKIE_NAME = "VISITOR_INFO1_LIVE"
    
    const val SCRIPTS_URL_BASE = "https://www.youtube.com"
    const val API_KEY = API_KEY_NEW
    
    private const val JSON_POST_DATA_BASE = "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"userAgent\":\"%s\",%s\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
            "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},%%s\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}}," +
            "\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"

    private const val JSON_POST_DATA_IOS = "\"deviceModel\":\"%s\","
    private const val JSON_POST_DATA_ANDROID = "\"androidSdkVersion\":\"%s\","
    private const val JSON_POST_DATA_BROWSE = "\"tvAppInfo\":{\"zylonLeftNav\":true},\"webpSupport\":false,\"animatedWebpSupport\":true,"

    // ATV
    // 7.20211013.10.00
    // 7.20220118.09.00
    // 7.20230612.10.00
    // 7.20231119.10.02
    const val CLIENT_VERSION_TV = "7.20240424.00.00"
    private const val CLIENT_NAME_TV = "TVHTML5"

    // BROWSER
    // 2.20211014.05.00-canary_control
    // 2.20220119.01.00
    // 2.20230613.01.00
    // 2.20231121.08.00
    const val CLIENT_VERSION_WEB = "2.20240425.07.00"
    private const val CLIENT_NAME_WEB = "WEB"
    private const val CLIENT_NAME_MWEB = "MWEB"

    // ATV KIDS
    // 3.20221025.01.00
    // 3.20230425.01.00
    private const val CLIENT_VERSION_KIDS = "3.20231113.03.00"
    private const val CLIENT_NAME_KIDS = "TVHTML5_KIDS"

    // EMBED
    // Special embed version (thanks for github thread)
    private const val CLIENT_VERSION_EMBED = "2.0"
    private const val CLIENT_NAME_EMBED = "TVHTML5_SIMPLY_EMBEDDED_PLAYER"

    // ANDROID
    // 16.20
    // 16.49
    private const val CLIENT_VERSION_ANDROID = "17.31.35"
    private const val CLIENT_NAME_ANDROID = "ANDROID"
    private const val PARAMS_ANDROID = "CgIQBg=="
    private const val ANDROID_SDK_VERSION = "30"
    private const val API_KEY_ANDROID = "AIzaSyA8eiZmM1FaDVjRy-df2KTyQ_vz_yYM39w"

    // IOS
    private const val CLIENT_VERSION_IOS = "17.33.2"
    private const val CLIENT_NAME_IOS = "IOS"
    private const val DEVICE_MODEL_IOS = "iPhone14,3"
    private const val API_KEY_IOS = "AIzaSyB-63vPrdThhKuerbB2N_l7Kwwcxj6yUAc"

    private const val CLIENT_SCREEN_WATCH = "WATCH" // won't play 18+ restricted videos
    private const val CLIENT_SCREEN_EMBED = "EMBED" // no 18+ restriction but not all video embeddable, and no descriptions

    const val GET_VIDEO_INFO_OLD =
        "https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=leanback&el=leanback&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=$CLIENT_VERSION_TV"

    const val GET_VIDEO_INFO_OLD2 =
        "https://www.youtube.com/get_video_info?html5=1&c=TVHTML5&ps=default&eurl=https%3A%2F%2Fwww.youtube.com%2Ftv&cver=$CLIENT_VERSION_TV"

    /**
     * Used in browse, next, search<br></br>
     * Previous client version: 7.20190214<br></br>
     * racyCheckOk - confirm age<br></br>
     * contentCheckOk - ?
     */
    @JvmField
    val JSON_POST_DATA_TEMPLATE_TV = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_TV, CLIENT_VERSION_TV, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_TV,
        JSON_POST_DATA_BROWSE)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_WEB = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_WEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_WEB,
        JSON_POST_DATA_BROWSE)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_MWEB = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_MWEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_MOBILE_WEB,
        JSON_POST_DATA_BROWSE)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_ANDROID = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_ANDROID, CLIENT_VERSION_ANDROID, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_ANDROID,
        String.format(JSON_POST_DATA_ANDROID, ANDROID_SDK_VERSION) + JSON_POST_DATA_BROWSE)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_KIDS = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_KIDS, CLIENT_VERSION_KIDS, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_TV,
        JSON_POST_DATA_BROWSE)

    @JvmField
    val JSON_POST_DATA_PLAYER_ANDROID = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_ANDROID, CLIENT_VERSION_ANDROID, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_ANDROID,
        String.format(JSON_POST_DATA_ANDROID, ANDROID_SDK_VERSION))

    @JvmField
    val JSON_POST_DATA_PLAYER_IOS = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_IOS, CLIENT_VERSION_IOS, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_IOS,
        String.format(JSON_POST_DATA_IOS, DEVICE_MODEL_IOS))

    @JvmField
    val JSON_POST_DATA_PLAYER_WEB = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_WEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_WEB,
        "")

    @JvmField
    val JSON_POST_DATA_PLAYER_TV = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_TV, CLIENT_VERSION_TV, CLIENT_SCREEN_WATCH, DefaultHeaders.USER_AGENT_TV,
        "")

    @JvmField
    val JSON_POST_DATA_PLAYER_EMBED = String.format(JSON_POST_DATA_BASE, CLIENT_NAME_EMBED, CLIENT_VERSION_EMBED, CLIENT_SCREEN_EMBED, DefaultHeaders.USER_AGENT_WEB,
        "")
}
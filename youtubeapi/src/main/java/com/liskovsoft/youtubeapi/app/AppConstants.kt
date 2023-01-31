package com.liskovsoft.youtubeapi.app

object AppConstants {
    private const val API_KEY_OLD = "AIzaSyDCU8hByM-4DrUqRUYnGn-3llEO78bcxq8"
    private const val API_KEY_NEW = "AIzaSyAO_FJ2SlqU8Q4STEHLGCilw_Y9_11qcW8"

    private const val JSON_POST_DATA_TEMPLATE = "{\"context\":{\"client\":{\"tvAppInfo\":{\"zylonLeftNav\":true},\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
            "\"clientScreen\":\"%s\",\"webpSupport\":false,\"animatedWebpSupport\":true,\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
            "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}},\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"

    const val SCRIPTS_URL_BASE = "https://www.youtube.com"
    const val API_KEY = API_KEY_NEW
    const val FUNCTION_RANDOM_BYTES =
        "var window={};window.crypto={getRandomValues:function(arr){for(var i=0;i<arr.length;i++){arr[i]=Math.floor(Math.random()*Math.floor(Math.pow(2,8*arr.BYTES_PER_ELEMENT)))}}};"

    // 7.20211013.10.00
    const val CLIENT_VERSION_TV = "7.20220118.09.00"

    // 2.20211014.05.00-canary_control
    const val CLIENT_VERSION_WEB = "2.20220119.01.00"

    // Special embed version (thanks for github thread)
    const val CLIENT_VERSION_EMBED = "2.0"

    // 16.20
    const val CLIENT_VERSION_ANDROID = "16.49"
    const val CLIENT_NAME_TV = "TVHTML5"
    const val CLIENT_NAME_WEB = "WEB"
    const val CLIENT_NAME_EMBED = "TVHTML5_SIMPLY_EMBEDDED_PLAYER"
    const val CLIENT_NAME_ANDROID = "ANDROID"
    const val CLIENT_SCREEN_WATCH = "WATCH" // won't play 18+ restricted videos
    const val CLIENT_SCREEN_EMBED = "EMBED" // no 18+ restriction but not all video embeddable, and no descriptions

    /**
     * Used in browse, next, search<br></br>
     * Previous client version: 7.20190214<br></br>
     * racyCheckOk - confirm age<br></br>
     * contentCheckOk - ?
     */
    @JvmField
    val JSON_POST_DATA_TEMPLATE_TV = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_TV, CLIENT_VERSION_TV, CLIENT_SCREEN_WATCH)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_WEB = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_WEB, CLIENT_VERSION_WEB, CLIENT_SCREEN_WATCH)

    @JvmField
    val JSON_POST_DATA_TEMPLATE_ANDROID = String.format(JSON_POST_DATA_TEMPLATE, CLIENT_NAME_ANDROID, CLIENT_VERSION_ANDROID, CLIENT_SCREEN_WATCH)

    /**
     * Used when parsing video_info data
     */
    const val VIDEO_INFO_JSON_CONTENT_PARAM = "player_response"

    const val VISITOR_COOKIE_NAME = "VISITOR_INFO1_LIVE"
}
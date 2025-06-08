package com.liskovsoft.youtubeapi.common.helpers

private const val JSON_POST_DATA_BASE = "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
        "\"clientScreen\":\"%s\",\"userAgent\":\"%s\",%s\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
        "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},%%s\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}}," +
        "\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"
// Include Shorts: "browserName":"Cobalt"
private const val POST_DATA_BROWSE =
    "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_FULL_ANIMATION\",\"zylonLeftNav\":true},\"browserName\":\"Cobalt\",\"webpSupport\":false,\"animatedWebpSupport\":true,"
private const val POST_DATA_IOS = "\"deviceModel\":\"%s\",\"osVersion\":\"%s\","
private const val POST_DATA_ANDROID = "\"androidSdkVersion\":\"%s\","
private const val CLIENT_SCREEN_WATCH = "WATCH" // won't play 18+ restricted videos
private const val CLIENT_SCREEN_EMBED = "EMBED" // no 18+ restriction but not all video embeddable, and no descriptions

/**
 * https://github.com/gamer191/yt-dlp/blob/3ad3676e585d144c16a2c5945eb6e422fb918d44/yt_dlp/extractor/youtube/_base.py#L41
 */
internal enum class AppClient(
    val clientName: String, val clientVersion: String, val innerTubeName: Int, val userAgent: String, val referer: String?,
    val clientScreen: String = CLIENT_SCREEN_WATCH, val params: String? = null, val postData: String? = null
) {
    TV("TVHTML5", "7.20250402.11.00", 7, userAgent = DefaultHeaders.USER_AGENT_TV, referer = "https://www.youtube.com/tv"),
    TV_SIMPLE("TVHTML5_SIMPLY", "1.0", 75, userAgent = DefaultHeaders.USER_AGENT_TV, referer = "https://www.youtube.com/tv"),
    // Use WEB_EMBEDDED_PLAYER instead of WEB. Some videos have 403 error on WEB.
    WEB_EMBED("WEB_EMBEDDED_PLAYER", "2.20250222.10.01", 56, userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    ANDROID_VR("ANDROID_VR", "1.37", 28, userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    WEB("WEB", "2.20250222.10.01", 1, userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    MWEB("MWEB", "2.20250213.05.00", 2, userAgent = DefaultHeaders.USER_AGENT_MOBILE_WEB, referer = "https://m.youtube.com/"),
    // Request contains an invalid argument.
    WEB_CREATOR("WEB_CREATOR", "1.20220726.00.00", 62, userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    WEB_REMIX("WEB_REMIX", "1.20240819.01.00", 67, userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://music.youtube.com/"),
    KIDS("TVHTML5_KIDS", "3.20231113.03.00", -1, userAgent = DefaultHeaders.USER_AGENT_TV, referer = "https://www.youtube.com/tv/kids"),
    TV_EMBED("TVHTML5_SIMPLY_EMBEDDED_PLAYER", "2.0", 85, userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/",
        clientScreen = CLIENT_SCREEN_EMBED),
    ANDROID("ANDROID", "19.26.37", 3, userAgent = DefaultHeaders.USER_AGENT_ANDROID, referer = null,
        postData = String.format(POST_DATA_ANDROID, 30)),
    IOS("IOS", "19.29.1", 5, userAgent = DefaultHeaders.USER_AGENT_IOS, referer = null,
        postData = String.format(POST_DATA_IOS, "iPhone16,2", "17.5.1.21F90"));

    val browseTemplate by lazy { String.format(JSON_POST_DATA_BASE, clientName, clientVersion, clientScreen, userAgent, (postData ?: "") + POST_DATA_BROWSE) }
    val playerTemplate by lazy { String.format(JSON_POST_DATA_BASE, clientName, clientVersion, clientScreen, userAgent, (postData ?: "")) }
}
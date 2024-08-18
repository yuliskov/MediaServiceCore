package com.liskovsoft.youtubeapi.common.helpers

private const val JSON_POST_DATA_BASE = "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
        "\"clientScreen\":\"%s\",\"userAgent\":\"%s\",%s\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
        "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},%%s\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}}," +
        "\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"
private const val POST_DATA_BROWSE = "\"tvAppInfo\":{\"zylonLeftNav\":true},\"webpSupport\":false,\"animatedWebpSupport\":true,"
private const val POST_DATA_IOS = "\"deviceModel\":\"%s\",\"osVersion\":\"%s\","
private const val POST_DATA_ANDROID = "\"androidSdkVersion\":\"%s\","
private const val CLIENT_SCREEN_WATCH = "WATCH" // won't play 18+ restricted videos
private const val CLIENT_SCREEN_EMBED = "EMBED" // no 18+ restriction but not all video embeddable, and no descriptions

internal enum class AppClient(
    val clientName: String, val clientVersion: String, val userAgent: String,
    private val clientScreen: String = CLIENT_SCREEN_WATCH, private val params: String? = null, private val postData: String? = null
) {
    TV("TVHTML5", "7.20240424.00.00", userAgent = DefaultHeaders.USER_AGENT_TV),
    WEB("WEB", "2.20240726.00.00", userAgent = DefaultHeaders.USER_AGENT_WEB),
    MWEB("MWEB", "2.20240726.00.00", userAgent = DefaultHeaders.USER_AGENT_MOBILE_WEB),
    WEB_CREATOR("WEB_CREATOR", "1.20220726.00.00", userAgent = DefaultHeaders.USER_AGENT_WEB),
    KIDS("TVHTML5_KIDS", "3.20231113.03.00", userAgent = DefaultHeaders.USER_AGENT_TV),
    EMBED("TVHTML5_SIMPLY_EMBEDDED_PLAYER", "2.0", userAgent = DefaultHeaders.USER_AGENT_WEB, clientScreen = CLIENT_SCREEN_EMBED),
    ANDROID("ANDROID", "19.26.37", userAgent = DefaultHeaders.USER_AGENT_ANDROID, postData = String.format(POST_DATA_ANDROID, 30)),
    IOS("IOS", "19.29.1", userAgent = DefaultHeaders.USER_AGENT_IOS, postData = String.format(POST_DATA_IOS, "iPhone16,2", "17.5.1.21F90"));

    val browseTemplate by lazy { String.format(JSON_POST_DATA_BASE, clientName, clientVersion, clientScreen, userAgent, (postData ?: "") + POST_DATA_BROWSE) }
    val playerTemplate by lazy { String.format(JSON_POST_DATA_BASE, clientName, clientVersion, clientScreen, userAgent, (postData ?: "")) }
}
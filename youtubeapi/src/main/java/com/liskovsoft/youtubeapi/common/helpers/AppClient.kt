package com.liskovsoft.youtubeapi.common.helpers

import com.liskovsoft.googlecommon.common.helpers.DefaultHeaders
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.innertube.utils.CLIENTS
import com.liskovsoft.youtubeapi.innertube.utils.CLIENT_NAME_IDS

private const val JSON_POST_DATA_BASE = "{\"context\":{\"client\":{\"clientName\":\"%s\",\"clientVersion\":\"%s\"," +
        "\"clientScreen\":\"%s\",\"userAgent\":\"%s\",%s\"acceptLanguage\":\"%%s\",\"acceptRegion\":\"%%s\"," +
        "\"utcOffsetMinutes\":\"%%s\",\"visitorData\":\"%%s\"},%%s\"user\":{\"enableSafetyMode\":false,\"lockedSafetyMode\":false}}," +
        "\"racyCheckOk\":true,\"contentCheckOk\":true,%%s}"
// Merge Shorts with Subscriptions: TV_APP_QUALITY_LIMITED_ANIMATION
// Separate Shorts from Subscriptions: TV_APP_QUALITY_FULL_ANIMATION
private const val POST_DATA_BROWSE_TV =
    "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_FULL_ANIMATION\",\"zylonLeftNav\":true},\"webpSupport\":false,\"animatedWebpSupport\":true,"
private const val POST_DATA_BROWSE_TV_LEGACY =
    "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\",\"zylonLeftNav\":true},\"webpSupport\":false,\"animatedWebpSupport\":true,"
private const val POST_DATA_IOS_MODEL = "\"deviceModel\":\"%s\",\"osVersion\":\"%s\","
private const val POST_DATA_ANDROID_OS = "\"osName\":\"Android\",\"osVersion\":\"%s\","
private const val POST_DATA_ANDROID_SDK = "\"androidSdkVersion\":\"%s\","
private const val POST_DATA_ANDROID_MODEL = "\"deviceModel\":\"%s\",\"deviceMake\":\"%s\","
private const val POST_DATA_BROWSER = "\"browserName\":\"%s\",\"browserVersion\":\"%s\","
private const val CLIENT_SCREEN_WATCH = "WATCH" // won't play 18+ restricted videos
private const val CLIENT_SCREEN_EMBED = "EMBED" // no 18+ restriction but not all video embeddable, and no descriptions

/**
 * https://github.com/gamer191/yt-dlp/blob/3ad3676e585d144c16a2c5945eb6e422fb918d44/yt_dlp/extractor/youtube/_base.py#L41
 */
internal enum class AppClient(
    @JvmField val clientName: String, @JvmField val clientVersion: String, val innerTubeName: String?, val userAgent: String, val referer: String?,
    val clientScreen: String = CLIENT_SCREEN_WATCH, val params: String? = null, val postData: String? = null, val postDataBrowse: String? = null
): MediaItemFormatInfo.ClientInfo {
    // Doesn't support 8AEB2AMB param if X-Goog-Pageid is set!
    TV(CLIENTS.TV.NAME, CLIENTS.TV.VERSION, CLIENT_NAME_IDS[CLIENTS.TV.NAME],
        userAgent = DefaultHeaders.USER_AGENT_TV, referer = "https://www.youtube.com/tv", postDataBrowse = POST_DATA_BROWSE_TV),
    TV_LEGACY(TV, postDataBrowse = POST_DATA_BROWSE_TV_LEGACY),
    TV_EMBED(CLIENTS.TV_EMBEDDED.NAME, CLIENTS.TV_EMBEDDED.VERSION, CLIENT_NAME_IDS[CLIENTS.TV_EMBEDDED.NAME],
        userAgent = DefaultHeaders.USER_AGENT_TV, referer = "https://www.youtube.com/tv", clientScreen = CLIENT_SCREEN_EMBED, postDataBrowse = POST_DATA_BROWSE_TV),
    // Can't use authorization
    TV_SIMPLY(CLIENTS.TV_SIMPLY.NAME, CLIENTS.TV_SIMPLY.VERSION, CLIENT_NAME_IDS[CLIENTS.TV_SIMPLY.NAME],
        userAgent = DefaultHeaders.USER_AGENT_TV, referer = "https://www.youtube.com/tv", postDataBrowse = POST_DATA_BROWSE_TV),
    TV_KIDS("TVHTML5_KIDS", "3.20231113.03.00", null, userAgent = DefaultHeaders.USER_AGENT_TV,
        referer = "https://www.youtube.com/tv/kids", postDataBrowse = POST_DATA_BROWSE_TV),
    TV_DOWNGRADED(TV, clientVersion = "5.20251105", userAgent = DefaultHeaders.USER_AGENT_COBALT_DOWNGRADED),
    // 8AEB2AMB - web client premium formats?
    WEB(CLIENTS.WEB.NAME, CLIENTS.WEB.VERSION, CLIENT_NAME_IDS[CLIENTS.WEB.NAME],
        userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    // Use WEB_EMBEDDED_PLAYER instead of WEB. Some videos have 403 error on WEB.
    WEB_EMBED(CLIENTS.WEB_EMBEDDED.NAME, CLIENTS.WEB_EMBEDDED.VERSION, CLIENT_NAME_IDS[CLIENTS.WEB_EMBEDDED.NAME],
        userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    // Request contains an invalid argument.
    WEB_CREATOR(CLIENTS.WEB_CREATOR.NAME, CLIENTS.WEB_CREATOR.VERSION, CLIENT_NAME_IDS[CLIENTS.WEB_CREATOR.NAME],
        userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://www.youtube.com/"),
    WEB_REMIX(CLIENTS.YTMUSIC.NAME, CLIENTS.YTMUSIC.VERSION, CLIENT_NAME_IDS[CLIENTS.YTMUSIC.NAME],
        userAgent = DefaultHeaders.USER_AGENT_WEB, referer = "https://music.youtube.com/"),
    WEB_SAFARI(CLIENTS.WEB.NAME, CLIENTS.WEB.VERSION, CLIENT_NAME_IDS[CLIENTS.WEB.NAME],
        userAgent = DefaultHeaders.USER_AGENT_SAFARI, referer = "https://www.youtube.com/"),
    MWEB(CLIENTS.MWEB.NAME, CLIENTS.MWEB.VERSION, CLIENT_NAME_IDS[CLIENTS.MWEB.NAME],
        userAgent = DefaultHeaders.USER_AGENT_MOBILE_WEB, referer = "https://m.youtube.com/"),
    ANDROID(CLIENTS.ANDROID.NAME, CLIENTS.ANDROID.VERSION, CLIENT_NAME_IDS[CLIENTS.ANDROID.NAME],
        userAgent = DefaultHeaders.USER_AGENT_ANDROID, referer = null,
        postData = String.format(POST_DATA_ANDROID_SDK, CLIENTS.ANDROID.SDK_VERSION) + String.format(POST_DATA_ANDROID_OS, CLIENTS.ANDROID.OS_VERSION)),
    ANDROID_SDK_LESS(baseClient = ANDROID, postData = String.format(POST_DATA_ANDROID_OS, CLIENTS.ANDROID.OS_VERSION)),
    ANDROID_REEL(ANDROID),
    ANDROID_VR(CLIENTS.ANDROID_VR.NAME, CLIENTS.ANDROID_VR.VERSION, CLIENT_NAME_IDS[CLIENTS.ANDROID_VR.NAME],
        userAgent = CLIENTS.ANDROID_VR.USER_AGENT!!, referer = null, postData = String.format(POST_DATA_ANDROID_SDK, CLIENTS.ANDROID_VR.SDK_VERSION)
                + String.format(POST_DATA_ANDROID_OS, CLIENTS.ANDROID_VR.OS_VERSION)
                + String.format(POST_DATA_ANDROID_MODEL, CLIENTS.ANDROID_VR.DEVICE_MODEL, CLIENTS.ANDROID_VR.DEVICE_MAKE)),
    IOS(CLIENTS.IOS.NAME, CLIENTS.IOS.VERSION, CLIENT_NAME_IDS[CLIENTS.IOS.NAME],
        userAgent = CLIENTS.IOS.USER_AGENT!!, referer = null, postData = String.format(POST_DATA_IOS_MODEL, CLIENTS.IOS.DEVICE_MODEL, CLIENTS.IOS.OS_VERSION)),
    INITIAL(WEB),
    GEO(WEB);

    constructor(baseClient: AppClient, clientVersion: String? = null, userAgent: String? = null, postData: String? = null, postDataBrowse: String? = null):
            this(baseClient.clientName, clientVersion ?: baseClient.clientVersion, baseClient.innerTubeName,
        userAgent ?: baseClient.userAgent, baseClient.referer, baseClient.clientScreen, baseClient.params,
        postData ?: baseClient.postData, postDataBrowse ?: baseClient.postDataBrowse)

    override fun getClientName() = clientName
    override fun getClientVersion() = clientVersion
    override fun getOsName() = "Macintosh" // TODO: change later
    override fun getOsVersion() = "10_15_7" // TODO: change later

    private val browserInfo by lazy { extractBrowserInfo(userAgent) }
    private val postDataBrowser by lazy { if (browserName != null && browserVersion != null) String.format(POST_DATA_BROWSER, browserName, browserVersion) else null }

    val browserName by lazy { browserInfo?.first }
    val browserVersion by lazy { browserInfo?.second }
    val browseTemplate by lazy { String.format(JSON_POST_DATA_BASE, clientName, clientVersion, clientScreen, userAgent,
        (postDataBrowser ?: "") + (postData ?: "") + (postDataBrowse ?: "")) }
    val baseTemplate by lazy { String.format(JSON_POST_DATA_BASE, clientName, clientVersion, clientScreen, userAgent,
        (postDataBrowser ?: "") + (postData ?: "")) }

    val isAuthSupported by lazy { Helpers.equalsAny(this, TV, TV_LEGACY, TV_EMBED, TV_KIDS, TV_DOWNGRADED) } // NOTE: TV_SIMPLY doesn't support auth
    val isWebPotRequired by lazy { Helpers.equalsAny(this, WEB, MWEB, WEB_EMBED, WEB_SAFARI, INITIAL, GEO) }
    // TODO: remove after implement SABR
    val isPlaybackBroken by lazy { Helpers.equalsAny(this, INITIAL, WEB, WEB_CREATOR, WEB_REMIX, WEB_SAFARI, ANDROID_VR, GEO, MWEB, WEB_EMBED, TV_EMBED, IOS) }
    val isReelClient by lazy { Helpers.equalsAny(this, ANDROID_REEL) }
    val isTVClient by lazy { name.startsWith("TV") }
    val isWebClient by lazy { Helpers.startsWithAny(name, "WEB", "MWEB", "INITIAL", "GEO") }
    val isEmbedded by lazy { Helpers.equalsAny(this, WEB_EMBED, TV_EMBED) }

    private fun extractBrowserInfo(userAgent: String): Pair<String, String>? {
        // Include Shorts: "browserName":"Cobalt"
        //val browserName = "Cobalt"
        //val browserVersion = "22.lts.3.306369-gold"

        for (name in listOf("SamsungBrowser", "LG Browser", "Cobalt", "Chrome", "Safari")) {
            val version = extractBrowserVersion(userAgent, name)
            if (version != null)
                return Pair(name, version)
        }

        //return Pair(browserName, browserVersion)
        return null
    }

    private fun extractBrowserVersion(userAgent: String, name: String): String? {
        if (userAgent.contains(name, ignoreCase = true)) {
            val browserVersionMatch = "$name/([a-zA-Z0-9.-]+)".toRegex().find(userAgent)
            return browserVersionMatch?.groupValues?.getOrNull(1)
        }

        return null
    }

    companion object {
        fun hasName(name: String): Boolean = values().any { it.name == name }
    }
}
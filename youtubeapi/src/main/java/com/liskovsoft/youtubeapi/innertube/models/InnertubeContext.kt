package com.liskovsoft.youtubeapi.innertube.models

import com.liskovsoft.youtubeapi.innertube.utils.CLIENTS
import java.util.Date
import java.util.TimeZone

internal class InnertubeContext(
    options: SessionArgs, deviceInfo: DeviceInfo
) {
    val client: Client = Client(options, deviceInfo)
    val user: User = User(options)
    val request: Request = Request()

    class Client(options: SessionArgs, deviceInfo: DeviceInfo) {
        val hl = options.lang ?: deviceInfo.hl ?: "en"
        val gl = options.location ?: deviceInfo.gl ?: "US"
        val remoteHost = deviceInfo.remoteHost
        val screenDensityFloat = 1
        val screenHeightPoints = 1440
        val screenPixelDensity = 1
        val screenWidthPoints = 2560
        val visitorData = options.visitorData ?: deviceInfo.visitorData
        var clientName = options.clientName
        var clientVersion = if (options.clientName == "WEB") deviceInfo.clientVersion
            else CLIENTS.ALl[options.clientName]?.VERSION ?: deviceInfo.clientVersion
        var osName = deviceInfo.osName
        var osVersion = deviceInfo.osVersion
        var androidSdkVersion: Int? = null
        var userAgent: String? = options.userAgent
        var platform = options.deviceCategory.uppercase()
        var clientFormFactor = "UNKNOWN_FORM_FACTOR"
        val userInterfaceTheme = "USER_INTERFACE_THEME_LIGHT"
        val timeZone = deviceInfo.timeZone ?: options.timeZone
        val originalUrl = "https://www.youtube.com" // Constants.URLS.YT_BASE
        var deviceMake = deviceInfo.deviceMake
        var deviceModel = deviceInfo.deviceModel
        var browserName = deviceInfo.browserName
        var browserVersion = deviceInfo.browserVersion
        val utcOffsetMinutes = -(TimeZone.getDefault().getOffset(Date().time) / 60000) // -Math.floor((new Date()).getTimezoneOffset())
        val memoryTotalKbytes = "8000000"
        val rolloutToken = deviceInfo.rolloutToken
        val deviceExperimentId = deviceInfo.deviceExperimentId
        val mainAppWebInfo: MainAppWebInfo = MainAppWebInfo()
        var configInfo: ConfigInfo? = deviceInfo.appInstallData?.let { ConfigInfo(it) }

        class MainAppWebInfo {
            val graftUrl = "https://www.youtube.com" // Constants.URLS.YT_BASE
            val pwaInstallabilityStatus = "PWA_INSTALLABILITY_STATUS_UNKNOWN"
            val webDisplayMode = "WEB_DISPLAY_MODE_BROWSER"
            val isWebNativeShareAvailable = true
        }

        class ConfigInfo(val appInstallData: String) {
            lateinit var coldConfigData: String
            lateinit var coldHashData: String
            lateinit var hotHashData: String
        }
    }

    class User(options: SessionArgs) {
        val enableSafetyMode = options.enableSafetyMode
        val lockedSafetyMode = false
    }

    class Request {
        val useSsl = true
        val internalExperimentFlags = listOf<String>()
    }
}

// TODO: replace with the real values
internal data class SessionArgs(
    val lang: String? = null,
    val location: String? = null,
    val visitorData: String? = null,
    val clientName: String = "WEB",
    val userAgent: String = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/140.0.0.0 Safari/537.36",
    val deviceCategory: String = "desktop",
    val timeZone: String = "Europe/Kiev",
    val enableSafetyMode: Boolean = false,
    val onBehalfOfUser: String? = null
)

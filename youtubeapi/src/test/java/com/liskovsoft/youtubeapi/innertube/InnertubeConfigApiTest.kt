package com.liskovsoft.youtubeapi.innertube

import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.innertube.api.InnertubeConfigApi
import com.liskovsoft.youtubeapi.innertube.api.SessionApi
import com.liskovsoft.youtubeapi.innertube.helpers.ApiHelpers
import com.liskovsoft.youtubeapi.innertube.helpers.DeviceCategory
import com.liskovsoft.youtubeapi.innertube.helpers.URLS
import com.liskovsoft.youtubeapi.innertube.helpers.getRandomUserAgent
import com.liskovsoft.youtubeapi.innertube.helpers.getStringBetweenStrings
import com.liskovsoft.youtubeapi.innertube.models.ContextInfo
import com.liskovsoft.youtubeapi.innertube.models.InnertubeConfigResult
import com.liskovsoft.youtubeapi.innertube.models.SessionArgs
import com.liskovsoft.youtubeapi.innertube.models.SessionDataProcessed
import com.liskovsoft.youtubeapi.innertube.models.SessionDataResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class InnertubeConfigApiTest {
    private lateinit var mSessionApi: SessionApi
    private lateinit var mInnertubeConfigApi: InnertubeConfigApi
    private lateinit var mFileApi: FileApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.disableCompression = true
        mSessionApi = RetrofitHelper.create(SessionApi::class.java)
        mInnertubeConfigApi = RetrofitHelper.create(InnertubeConfigApi::class.java)
        mFileApi = RetrofitHelper.create(FileApi::class.java)
    }

    @Test
    fun testThatSessionDataNotEmpty() {
        val sessionData = getSessionDataResult()

        Assert.assertNotNull("ytcfg not null", sessionData?.ytcfg?.apiKey)
    }

    @Test
    fun testThatDeviceInfoNotEmpty() {
        val sessionData = getSessionDataResult()

        Assert.assertNotNull("Device info not null", sessionData?.ytcfg?.deviceInfo)
        Assert.assertNotNull("Has visitor data", sessionData?.ytcfg?.deviceInfo?.visitorData)
    }

    @Test
    fun testThatInnertubeConfigNotEmpty() {
        val sessionData = getSessionDataResult()
        Assert.assertNotNull("session data not null", sessionData)
        val deviceInfo = sessionData!!.deviceInfo
        Assert.assertNotNull("session data/device info not null", deviceInfo)

        val options = SessionArgs()
        val context = ContextInfo(options, deviceInfo!!) // needed later

        val innertubeConfig = retrieveInnertubeConfig(sessionData, context)

        Assert.assertNotNull("innertubeConfig not null", innertubeConfig)
        Assert.assertNotNull("Has coldConfigData", innertubeConfig!!.responseContext?.globalConfigGroup?.rawColdConfigGroup?.configData)
        Assert.assertNotNull("Has coldHashData", innertubeConfig.responseContext?.globalConfigGroup?.coldHashData)
        Assert.assertNotNull("Has hotHashData", innertubeConfig.responseContext?.globalConfigGroup?.hotHashData)
        Assert.assertNotNull("Has configData", innertubeConfig.configData)
    }

    @Test
    fun testCreateProcessedSessionData() {
        val sessionData = getSessionDataResult()
        Assert.assertNotNull("session data not null", sessionData)
        val deviceInfo = sessionData!!.deviceInfo
        Assert.assertNotNull("session data/device info not null", deviceInfo)

        val options = SessionArgs()
        val context = ContextInfo(options, deviceInfo!!) // needed later

        val innertubeConfig = retrieveInnertubeConfig(sessionData, context)
        val coldConfigData = innertubeConfig?.responseContext?.globalConfigGroup?.rawColdConfigGroup?.configData
        val coldHashData = innertubeConfig?.responseContext?.globalConfigGroup?.coldHashData
        val hotHashData = innertubeConfig?.responseContext?.globalConfigGroup?.hotHashData
        val configData = innertubeConfig?.configData

        val sessionDataProcessed = SessionDataProcessed(
            sessionData.apiKey!!,
            "v1", // Constants.CLIENTS.WEB.API_VERSION
            configData!!,
            context.apply {
                client.configInfo!!.coldConfigData = coldConfigData!!
                client.configInfo.coldHashData = coldHashData!!
                client.configInfo.hotHashData = hotHashData!!
            }
        )
    }

    @Test
    fun testGetPlayerId() {
        val playerId = getPlayerId()

        Assert.assertNotNull("playerId not null", playerId)
    }

    @Test
    fun testGetPlayerJs() {
        val playerId = getPlayerId()
        
        val playerUrl = "${URLS.YT_BASE}/s/player/${playerId!!}/player_ias.vflset/en_US/base.js"

        val js = RetrofitHelper.get(mFileApi.getContent(mapOf("User-Agent" to getRandomUserAgent(DeviceCategory.DESKTOP)), playerUrl))

        Assert.assertNotNull("js not null", js)
    }

    private fun getSessionDataResult(): SessionDataResult? {
        val sessionDataResult = mSessionApi.getSessionData(ApiHelpers.createSessionDataHeaders())
        return RetrofitHelper.get(sessionDataResult)
    }

    private fun retrieveInnertubeConfig(sessionData: SessionDataResult, context: ContextInfo): InnertubeConfigResult? {
        val innertubeConfigResult =
            mInnertubeConfigApi.retrieveInnertubeConfig(
                ApiHelpers.createInnertubeConfigHeaders(sessionData),
                ApiHelpers.createInnertubeJsonConfig(context)
            )
        return RetrofitHelper.get(innertubeConfigResult)
    }

    private fun getPlayerId(): String? {
        val js = RetrofitHelper.get(mFileApi.getContent("${URLS.YT_BASE}/iframe_api"))
        Assert.assertNotNull("js not null", js)

        return getStringBetweenStrings(js!!.content!!, "player\\/", "\\/")
    }
}
package com.liskovsoft.youtubeapi.innertube

import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.innertube.core.Player
import com.liskovsoft.youtubeapi.innertube.core.Session
import com.liskovsoft.youtubeapi.innertube.models.InnertubeContext
import com.liskovsoft.youtubeapi.innertube.models.SessionArgs
import com.liskovsoft.youtubeapi.innertube.core.HTTPClient
import com.liskovsoft.youtubeapi.innertube.core.RequestInit
import com.liskovsoft.youtubeapi.innertube.core.RequestInitBody
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class InnertubeServiceTest {
    private lateinit var mFileApi: FileApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.disableCompression = true
        mFileApi = RetrofitHelper.create(FileApi::class.java)
    }

    @Test
    fun testThatSessionDataNotEmpty() {
        val sessionData = Session.getSessionDataResult()

        Assert.assertNotNull("ytcfg not null", sessionData?.ytcfg?.apiKey)
    }

    @Test
    fun testThatDeviceInfoNotEmpty() {
        val sessionData = Session.getSessionDataResult()

        Assert.assertNotNull("Device info not null", sessionData?.ytcfg?.deviceInfo)
        Assert.assertNotNull("Has visitor data", sessionData?.ytcfg?.deviceInfo?.visitorData)
    }

    @Test
    fun testThatInnertubeConfigNotEmpty() {
        val sessionData = Session.getSessionDataResult()
        Assert.assertNotNull("session data not null", sessionData)
        val deviceInfo = sessionData!!.deviceInfo
        Assert.assertNotNull("session data/device info not null", deviceInfo)

        val options = SessionArgs()
        val context = InnertubeContext(options, deviceInfo!!) // needed later

        val innertubeConfig = Session.retrieveInnertubeConfig(sessionData, context)

        Assert.assertNotNull("innertubeConfig not null", innertubeConfig)
        Assert.assertNotNull("Has coldConfigData", innertubeConfig!!.responseContext?.globalConfigGroup?.rawColdConfigGroup?.configData)
        Assert.assertNotNull("Has coldHashData", innertubeConfig.responseContext?.globalConfigGroup?.coldHashData)
        Assert.assertNotNull("Has hotHashData", innertubeConfig.responseContext?.globalConfigGroup?.hotHashData)
        Assert.assertNotNull("Has configData", innertubeConfig.configData)
    }

    @Test
    fun testCreateProcessedSessionData() {
        val sessionDataProcessed = Session.getSessionData()

        Assert.assertNotNull("context not null", sessionDataProcessed?.context)
    }

    @Test
    fun testGetPlayerId() {
        val playerId = Player.getPlayerId()

        Assert.assertNotNull("playerId not null", playerId)
    }

    @Test
    fun testGetPlayerJs() {
        val playerId = Player.getPlayerId()
        val playerUrl = Player.getPlayerUrl(playerId!!)

        val js = Player.getPlayerJs(playerUrl)

        Assert.assertNotNull("js not null", js)
    }

    @Test
    fun testGetVideoResults() {
        val session = Session.create()
        val httpClient = HTTPClient(session!!)

        val playerResult = httpClient.fetch("/player", RequestInit(body = RequestInitBody("K04WmBtVsOs", session = session)))

        Assert.assertNotNull("Player result not null", playerResult)
    }
}
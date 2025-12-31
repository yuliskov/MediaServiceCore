package com.liskovsoft.youtubeapi.innertube

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.innertube.core.HTTPClient
import com.liskovsoft.youtubeapi.innertube.core.Player
import com.liskovsoft.youtubeapi.innertube.core.RequestInit
import com.liskovsoft.youtubeapi.innertube.core.RequestInitBody
import com.liskovsoft.youtubeapi.innertube.core.Session
import com.liskovsoft.youtubeapi.innertube.impl.MediaItemFormatInfoImpl
import com.liskovsoft.youtubeapi.innertube.models.InnertubeContext
import com.liskovsoft.youtubeapi.innertube.models.PlayerResult
import com.liskovsoft.youtubeapi.innertube.models.SessionArgs
import com.liskovsoft.youtubeapi.innertube.utils.getServerAbrStreamingUrl
import com.liskovsoft.youtubeapi.innertube.utils.getVideoPlaybackUstreamerConfig
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val DEFAULT_VIDEO_ID = "K04WmBtVsOs"

class InnertubeServiceTest {
    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
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
        val playerResult = getPlayerResult(session!!)

        Assert.assertNotNull("Player result not null", playerResult!!)
        Assert.assertNotNull("Player has server abr url", playerResult.getServerAbrStreamingUrl())
        Assert.assertNotNull("Player has server streaming config", playerResult.getVideoPlaybackUstreamerConfig())
    }

    @Test
    fun testDecipherVideoResults() {
        val session = Session.create()
        val playerResult = getPlayerResult(session!!)

        val formatInfo = MediaItemFormatInfoImpl(playerResult!!)

        val sabrUrl = formatInfo.serverAbrStreamingUrl

        Assert.assertNotNull("sabrUrl not null", sabrUrl)

        session.player.decipher(formatInfo)

        val sabrUrl2 = formatInfo.serverAbrStreamingUrl

        Assert.assertNotNull("sabrUrl2 not null", sabrUrl2)

        Assert.assertNotEquals("sabr urls not equals", sabrUrl, sabrUrl2)
    }

    private fun getPlayerResult(session: Session): PlayerResult? {
        val httpClient = HTTPClient(session)

        return httpClient.fetch("/player", RequestInit(body = RequestInitBody(DEFAULT_VIDEO_ID, session = session)))
    }
}
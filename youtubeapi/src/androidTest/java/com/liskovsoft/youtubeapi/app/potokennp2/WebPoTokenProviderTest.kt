package com.liskovsoft.youtubeapi.app.potokennp2

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.potokennp2.generators.PoTokenWebView3
import com.liskovsoft.youtubeapi.app.potokennp2.generators.PoTokenWebView4
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test

private const val VIDEO_ID = "K04WmBtVsOs"

class WebPoTokenProviderTest {
    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
        WebPoTokenProvider.reset()
    }

    @Test
    fun testWebPoTokenIsNotEmpty() {
        assertWebPoTokenIsNotEmpty()
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId() {
        assertWebPoTokenOnEmptyVideoId()
    }

    @Test
    fun testPoTokenResponse() {
        assertPoTokenResponse()
    }

    @Test
    fun testWebPoTokenIsNotEmpty3() {
        WebPoTokenProvider.poTokenFactory = PoTokenWebView3

        assertWebPoTokenLength(124) // SABR pot length
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId3() {
        WebPoTokenProvider.poTokenFactory = PoTokenWebView3

        assertWebPoTokenOnEmptyVideoId()
    }

    @Test
    fun testPoTokenResponse3() {
        WebPoTokenProvider.poTokenFactory = PoTokenWebView3

        assertPoTokenResponse()
    }

    @Test
    fun testWebPoTokenIsNotEmpty4() {
        WebPoTokenProvider.poTokenFactory = PoTokenWebView4

        // NOTE: the new pot is shorter
        assertWebPoTokenLength(120) // SABR pot length
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId4() {
        WebPoTokenProvider.poTokenFactory = PoTokenWebView4

        assertWebPoTokenOnEmptyVideoId()
    }

    @Test
    fun testPoTokenResponse4() {
        WebPoTokenProvider.poTokenFactory = PoTokenWebView4

        assertPoTokenResponse()
    }

    private fun assertWebPoTokenLength(length: Int) {
        val webClientPoToken = WebPoTokenProvider.getWebClientPoToken(VIDEO_ID)

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
        Assert.assertEquals("PoToken length is $length", length, webClientPoToken?.playerRequestPoToken?.length)
    }

    private fun assertWebPoTokenIsNotEmpty() {
        val webClientPoToken = WebPoTokenProvider.getWebClientPoToken(VIDEO_ID)

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    private fun assertWebPoTokenOnEmptyVideoId() {
        val webClientPoToken = WebPoTokenProvider.getWebClientPoToken("")

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    private fun assertPoTokenResponse() {
        val videoIdMusic = TestHelpers.VIDEO_ID_MUSIC_2

        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(videoIdMusic)

        val url = mediaItemDetails.adaptiveFormats[0].url ?: mediaItemDetails.serverAbrStreamingUrl

        val webClientPoToken = WebPoTokenProvider.getWebClientPoToken(videoIdMusic)

        Assert.assertTrue("Video url is working", TestHelpers.urlExists("$url&pot=${webClientPoToken?.streamingDataPoToken}"))
    }
}
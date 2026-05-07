package com.liskovsoft.youtubeapi.app.potokennp2

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PoTokenProviderImplTest {
    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
        PoTokenProviderImpl.resetCache()
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
    fun testWebPoTokenIsNotEmpty2() {
        PoTokenProviderImpl.poTokenFactory = PoTokenV8

        assertWebPoTokenIsNotEmpty()
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId2() {
        PoTokenProviderImpl.poTokenFactory = PoTokenV8

        assertWebPoTokenOnEmptyVideoId()
    }

    @Test
    fun testPoTokenResponse2() {
        PoTokenProviderImpl.poTokenFactory = PoTokenV8

        assertPoTokenResponse()
    }

    private fun assertWebPoTokenIsNotEmpty() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken("K04WmBtVsOs")

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    private fun assertWebPoTokenOnEmptyVideoId() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken("")

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    private fun assertPoTokenResponse() {
        val videoIdMusic = TestHelpers.VIDEO_ID_MUSIC_2

        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(videoIdMusic)

        val url = mediaItemDetails.adaptiveFormats[0].url

        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken(videoIdMusic)

        Assert.assertTrue("Video url is working", TestHelpers.urlExists("$url&pot=${webClientPoToken?.streamingDataPoToken}"))
    }
}
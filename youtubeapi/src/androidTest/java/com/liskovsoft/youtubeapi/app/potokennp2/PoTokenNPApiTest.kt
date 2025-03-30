package com.liskovsoft.youtubeapi.app.potokennp2

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PoTokenNPApiTest {
    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
    }

    @Test
    fun testWebPoTokenIsNotEmpty() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken(TestHelpersV2.VIDEO_ID_3)

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken("")

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    @Test
    fun testPoTokenResponse() {
        val videoIdMusic = TestHelpersV2.VIDEO_ID_MUSIC_2

        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(videoIdMusic)

        val url = mediaItemDetails.dashFormats[0].url

        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken(videoIdMusic)

        Assert.assertTrue("Video url is working", TestHelpersV2.urlExists("$url&pot=${webClientPoToken?.streamingDataPoToken}"))
    }
}
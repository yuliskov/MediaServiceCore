package com.liskovsoft.youtubeapi.app.potokennp2

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
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
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken(TestHelpers.VIDEO_ID_3)

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    @Test
    fun testWebPoTokenOnEmptyVideoId() {
        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken("")

        Assert.assertNotNull("PoToken not empty", webClientPoToken)
    }

    @Test
    fun testPoTokenResponse() {
        val videoIdMusic = TestHelpers.VIDEO_ID_MUSIC_2

        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(videoIdMusic)

        val url = mediaItemDetails.adaptiveFormats[0].url

        val webClientPoToken = PoTokenProviderImpl.getWebClientPoToken(videoIdMusic)

        Assert.assertTrue("Video url is working", TestHelpers.urlExists("$url&pot=${webClientPoToken?.streamingDataPoToken}"))
    }
}
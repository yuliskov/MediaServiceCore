package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemFormatInfo
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal class PoTokenCloudApiTest {
    private lateinit var api: PoTokenCloudApi

    @Before
    fun setUp() {
        api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    }

    @Test
    fun testThatPoTokenNotEmpty() {
        getPoToken()
    }

    @Test
    fun testPoTokenOnVideoUrl() {
        val poToken = getPoToken()

        val mediaItemDetails: MediaItemFormatInfo = YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(TestHelpersV1.VIDEO_ID_MUSIC_2)

        val url = mediaItemDetails.dashFormats[0].url

        assertTrue("Video url is working", TestHelpersV1.urlExists("$url&pot=${poToken?.poToken}"))
    }

    @Test
    fun testHealth() {
        val tickle = api.healthCheck()

        val response = RetrofitHelper.getResponse(tickle)

        assertTrue("Health OK", response?.isSuccessful == true)
    }

    private fun getPoToken(): PoTokenResponse? = runBlocking {
        var poToken: PoTokenResponse? = null

        val times = Int.MAX_VALUE
        for (i in 0 until times) {
            poToken = RetrofitHelper.get(api.getPoToken())
            if (poToken?.poToken != null)
                break

            if (i < times - 1)
                delay(100)
        }

        assertNotNull("PoToken is not empty", poToken?.poToken)

        return@runBlocking poToken
    }
}
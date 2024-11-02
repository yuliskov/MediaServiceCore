package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemFormatInfo
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Ignore
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

    @Ignore("Server could block us")
    @Test
    fun testPoTokenHighLoad() {
        getPoTokenHighLoad()
    }

    @Test
    fun testPoTokenOnVideoUrl() {
        val poToken = getPoToken()

        val mediaItemDetails: MediaItemFormatInfo = YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(TestHelpersV1.VIDEO_ID_MUSIC_2)

        val url = mediaItemDetails.dashFormats[0].url

        assertTrue("Video url is working", TestHelpersV1.urlExists("$url&pot=${poToken?.poToken}"))
    }

    @Test
    fun testHealth() = runBlocking {
        val tickle = api.healthCheck()

        val response = RetrofitHelper.getResponse(tickle)

        assertTrue("Health OK", response?.isSuccessful == true)
    }

    private fun getPoToken(): PoTokenResponse? = runBlocking {
        var poToken: PoTokenResponse? = null

        val times = 10
        for (i in 0.. times) {
            poToken = RetrofitHelper.get(api.getPoToken())
            if (poToken?.poToken != null)
                break

            if (i < times)
                delay(1_000)
        }

        assertNotNull("PoToken is not empty", poToken?.poToken)

        return@runBlocking poToken
    }

    private fun getPoTokenHighLoad() = runBlocking {
        val deferred = (0..Int.MAX_VALUE).map {
            async {
                RetrofitHelper.get(api.getPoToken()) ?: fail("pot is null")
            }
        }

        deferred.awaitAll()
    }
}
package com.liskovsoft.youtubeapi.app.potokencloud2

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

private const val VIDEO_ID = "K04WmBtVsOs"

@Ignore("Not actual")
internal class PoTokenCloudServiceTest {
    private lateinit var api: PoTokenCloudApi

    @Before
    fun setUp() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)
        api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    }

    @Test
    fun testThatPoTokenNotEmpty() {
        getPoToken()
    }

    @Test
    fun testPoTokenOnVideoUrl() {
        testPoTokenResponse(getPoToken())
    }

    @Ignore("not used")
    @Test
    fun testHealth() = runBlocking {
        val tickle = api.healthCheck("${PO_TOKEN_CLOUD_BASE_URLS.random()}/health-check")

        val response = RetrofitHelper.getResponse(tickle)

        assertTrue("Health OK", response?.isSuccessful == true)
    }

    private fun getPoToken(): PoTokenResponse? = runBlocking {
        var poToken: PoTokenResponse? = null

        var baseUrl: String = PO_TOKEN_CLOUD_BASE_URLS.random()
        val retryTimes = PO_TOKEN_CLOUD_BASE_URLS.size
        for (i in 0 until retryTimes) {
            poToken = RetrofitHelper.get(api.getPoToken(baseUrl, VIDEO_ID))
            if (poToken?.poToken != null)
                break

            baseUrl = Helpers.getNextValue(PO_TOKEN_CLOUD_BASE_URLS, PO_TOKEN_CLOUD_BASE_URLS[i])

            if (i < (retryTimes - 1))
                delay(50_000)
        }

        assertNotNull("PoToken is not empty", poToken?.poToken)

        return@runBlocking poToken
    }

    private fun testPoTokenResponse(poToken: PoTokenResponse?) {
        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(VIDEO_ID)

        val url = mediaItemDetails.adaptiveFormats[0].url

        assertTrue("Video url is working", TestHelpers.urlExists("$url&pot=${poToken?.poToken}"))
    }
}
package com.liskovsoft.youtubeapi.app.potokencloud

import com.google.gson.Gson
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.app.potoken.PoTokenService
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.service.YouTubeServiceManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test

@Ignore("Not actual")
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
        testPoTokenResponse(getPoToken())
    }

    //@Test
    //fun testPoTokenOnVideoUrlAlt() {
    //    testPoTokenResponse(getPoTokenAlt())
    //}

    @Ignore("server is down")
    @Test
    fun testPoTokenOnVideoUrlPart() {
        testPoTokenResponse(getPoTokenPart())
    }

    @Ignore("not used")
    @Test
    fun testHealth() = runBlocking {
        val tickle = api.healthCheck("${PO_TOKEN_CLOUD_BASE_URLS[0]}/health-check")

        val response = RetrofitHelper.getResponse(tickle)

        assertTrue("Health OK", response?.isSuccessful == true)
    }

    private fun getPoToken(): PoTokenResponse? = runBlocking {
        var poToken: PoTokenResponse? = null

        var baseUrl: String = PO_TOKEN_CLOUD_BASE_URLS.random()
        val retryTimes = PO_TOKEN_CLOUD_BASE_URLS.size
        for (i in 0 until retryTimes) {
            poToken = RetrofitHelper.get(api.getPoToken(baseUrl))
            if (poToken?.poToken != null)
                break

            baseUrl = Helpers.getNextValue(PO_TOKEN_CLOUD_BASE_URLS, PO_TOKEN_CLOUD_BASE_URLS[i])

            if (i < (retryTimes - 1))
                delay(50_000)
        }

        assertNotNull("PoToken is not empty", poToken?.poToken)

        return@runBlocking poToken
    }

    //private fun getPoTokenAlt(): PoTokenResponse? = runBlocking {
    //    var poToken: PoTokenResponse? = null
    //
    //    val times = 1
    //    for (i in 0.. times) {
    //        poToken = PoTokenService.getChallenge()?.program?.let { RetrofitHelper.get(api.getPoTokenAlt(it)) }
    //        if (poToken?.poToken != null)
    //            break
    //
    //        if (i < times)
    //            delay(50_000)
    //    }
    //
    //    assertNotNull("PoToken is not empty", poToken?.poToken)
    //
    //    return@runBlocking poToken
    //}

    private fun getPoTokenPart(): PoTokenResponse? = runBlocking {
        var poToken: PoTokenResponse? = null

        var baseUrl: String = PO_TOKEN_CLOUD_BASE_URLS.random()
        val times = PO_TOKEN_CLOUD_BASE_URLS.size
        for (i in 0 until times) {
            val part1 = RetrofitHelper.get(api.getPoTokenPart1("$baseUrl/part1"))

            if (part1?.requestKey == null || part1.botguardResponse == null)
                continue

            val part2 = PoTokenService.generateIntegrityToken(part1.requestKey, part1.botguardResponse)

            if (part2?.integrityTokenData == null)
                continue

            poToken = RetrofitHelper.get(api.getPoTokenPart2("$baseUrl/part2", Gson().toJson(part2)))

            if (poToken?.poToken != null)
                break

            baseUrl = Helpers.getNextValue(PO_TOKEN_CLOUD_BASE_URLS, baseUrl)

            if (i < (times - 1))
                delay(50_000)
        }

        assertNotNull("PoToken is not empty", poToken?.poToken)

        return@runBlocking poToken
    }

    private fun testPoTokenResponse(poToken: PoTokenResponse?) {
        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(TestHelpers.VIDEO_ID_MUSIC_2)

        val url = mediaItemDetails.adaptiveFormats[0].url

        assertTrue("Video url is working", TestHelpers.urlExists("$url&pot=${poToken?.poToken}"))
    }
}
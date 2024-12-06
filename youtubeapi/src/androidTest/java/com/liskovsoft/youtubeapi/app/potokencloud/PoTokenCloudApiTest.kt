package com.liskovsoft.youtubeapi.app.potokencloud

import com.google.gson.Gson
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemFormatInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.app.potoken.PoTokenService
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
    private var baseUrl: String = PO_TOKEN_CLOUD_BASE_URLS[0]

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

    @Test
    fun testHealth() = runBlocking {
        val tickle = api.healthCheck("$baseUrl/health-check")

        val response = RetrofitHelper.getResponse(tickle)

        assertTrue("Health OK", response?.isSuccessful == true)
    }

    private fun getPoToken(): PoTokenResponse? = runBlocking {
        var poToken: PoTokenResponse? = null

        val times = 1
        for (i in 0.. times) {
            poToken = RetrofitHelper.get(api.getPoToken(baseUrl))
            if (poToken?.poToken != null)
                break

            baseUrl = Helpers.getNextValue(baseUrl, PO_TOKEN_CLOUD_BASE_URLS)

            if (i < times)
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

        val times = 1
        for (i in 0.. times) {
            val part1 = RetrofitHelper.get(api.getPoTokenPart1("$baseUrl/part1"))

            if (part1?.requestKey == null || part1.botguardResponse == null)
                continue

            val part2 = PoTokenService.generateIntegrityToken(part1.requestKey, part1.botguardResponse)

            if (part2?.integrityTokenData == null)
                continue

            poToken = RetrofitHelper.get(api.getPoTokenPart2("$baseUrl/part2", Gson().toJson(part2)))

            if (poToken?.poToken != null)
                break

            baseUrl = Helpers.getNextValue(baseUrl, PO_TOKEN_CLOUD_BASE_URLS)

            if (i < times)
                delay(50_000)
        }

        assertNotNull("PoToken is not empty", poToken?.poToken)

        return@runBlocking poToken
    }

    private fun getPoTokenHighLoad() = runBlocking {
        val deferred = (0..Int.MAX_VALUE).map {
            async {
                RetrofitHelper.get(api.getPoToken(baseUrl)) ?: fail("pot is null")
            }
        }

        deferred.awaitAll()
    }

    private fun testPoTokenResponse(poToken: PoTokenResponse?) {
        val mediaItemDetails: MediaItemFormatInfo =
            YouTubeServiceManager.instance().getMediaItemService().getFormatInfo(TestHelpersV1.VIDEO_ID_MUSIC_2)

        val url = mediaItemDetails.dashFormats[0].url

        assertTrue("Video url is working", TestHelpersV1.urlExists("$url&pot=${poToken?.poToken}"))
    }
}
package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal object PoTokenCloudService {
    private const val RETRY_DELAY_MS: Long = 20_000
    private const val PO_TOKEN_LIFETIME_MS: Long = 12 * 60 * 60 * 1_000
    private val api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    //private val RETRY_TIMES: Int = PO_TOKEN_CLOUD_BASE_URLS.size
    //private var baseUrl: String = PO_TOKEN_CLOUD_BASE_URLS.random()

    @Synchronized
    @JvmStatic
    fun updatePoToken() = runBlocking {
        val poToken = MediaServiceData.instance().poToken

        if (isTokenActual(poToken))
            return@runBlocking

        val newPoToken = getPoTokenResponse()

        if (newPoToken != null) {
            newPoToken.visitorData = AppService.instance().visitorData
            MediaServiceData.instance().poToken = newPoToken
        }
    }

    @JvmStatic
    fun getPoToken(): String? {
        val poToken = MediaServiceData.instance().poToken
        return poToken?.poToken
    }

    private fun isTokenActual(poToken: PoTokenResponse?) =
        poToken?.poToken != null && poToken.visitorData == AppService.instance().visitorData
                && (System.currentTimeMillis() - poToken.timestamp < PO_TOKEN_LIFETIME_MS)

    private suspend fun getPoTokenResponse(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null
        val baseUrls = PO_TOKEN_CLOUD_BASE_URLS.toMutableList()

        while (baseUrls.isNotEmpty()) {
            val baseUrl = baseUrls[Helpers.getRandomNumber(0, baseUrls.size - 1)]
            poToken = RetrofitHelper.get(api.getPoToken(baseUrl, AppService.instance().visitorData))
            if (poToken?.poToken != null) {
                break
            }

            baseUrls.remove(baseUrl)

            if (baseUrls.isNotEmpty()) {
                delay(RETRY_DELAY_MS)
            }
        }

        return poToken
    }

    fun resetCache() {
        MediaServiceData.instance().poToken = null
    }

    //private suspend fun getPoTokenResponsePart(): PoTokenResponse? {
    //    var poToken: PoTokenResponse? = null
    //
    //    for (i in 0 until RETRY_TIMES) {
    //        val part1 = RetrofitHelper.get(api.getPoTokenPart1("$baseUrl/part1"))
    //
    //        if (part1?.requestKey == null || part1.botguardResponse == null)
    //            continue
    //
    //        val part2 = PoTokenService.generateIntegrityToken(part1.requestKey, part1.botguardResponse)
    //
    //        if (part2?.integrityTokenData == null)
    //            continue
    //
    //        poToken = RetrofitHelper.get(api.getPoTokenPart2("$baseUrl/part2", Gson().toJson(part2)))
    //
    //        if (poToken?.poToken != null)
    //            break
    //
    //        baseUrl = Helpers.getNextValue(baseUrl, PO_TOKEN_CLOUD_BASE_URLS)
    //
    //        if (i < (RETRY_TIMES - 1))
    //            delay(RETRY_DELAY_MS)
    //    }
    //
    //    return poToken
    //}
}
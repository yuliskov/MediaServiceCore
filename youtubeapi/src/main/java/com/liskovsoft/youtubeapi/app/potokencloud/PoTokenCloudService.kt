package com.liskovsoft.youtubeapi.app.potokencloud

import com.google.gson.Gson
import com.liskovsoft.sharedutils.helpers.DateHelper
import com.liskovsoft.youtubeapi.app.potoken.PoTokenService
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal object PoTokenCloudService {
    private const val RETRY_TIMES: Int = 1
    private const val RETRY_DELAY_MS: Long = 50_000
    private const val ONE_DAY_MS: Long = 24 * 60 * 60 * 1_000
    private val api = RetrofitHelper.create(PoTokenCloudApi::class.java)

    @JvmStatic
    fun updatePoToken() = runBlocking {
        val poToken = MediaServiceData.instance().poToken

        if (isTokenActual(poToken))
            return@runBlocking

        val newPoToken = getPoTokenResponse()

        if (newPoToken != null)
            MediaServiceData.instance().poToken = newPoToken
    }

    @JvmStatic
    fun getPoToken(): String? {
        val poToken = MediaServiceData.instance().poToken
        //return if (isTokenAlmostActual(poToken)) poToken?.poToken else null
        return poToken?.poToken
    }

    private fun isTokenActual(poToken: PoTokenResponse?) =
        poToken != null && DateHelper.toUnixTimeMs(poToken.mintRefreshDate) > System.currentTimeMillis()

    //private fun isTokenAlmostActual(poToken: PoTokenResponse?) =
    //    poToken != null && (DateHelper.toUnixTimeMs(poToken.mintRefreshDate) + ONE_DAY_MS) > System.currentTimeMillis()

    private suspend fun getPoTokenResponse(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null

        for (i in 0 .. RETRY_TIMES) {
            val isEven = i % 2 == 0
            poToken = RetrofitHelper.get(if (isEven) api.getPoToken2() else api.getPoToken())
            if (poToken?.poToken != null)
                break

            if (i < RETRY_TIMES)
                delay(RETRY_DELAY_MS)
        }

        return poToken
    }

    private suspend fun getPoTokenResponseAlt(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null

        for (i in 0 .. RETRY_TIMES) {
            poToken = PoTokenService.getChallenge()?.program?.let { RetrofitHelper.get(api.getPoTokenAlt(it)) }
            if (poToken?.poToken != null)
                break

            if (i < RETRY_TIMES)
                delay(RETRY_DELAY_MS)
        }

        return poToken
    }

    private suspend fun getPoTokenResponsePart(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null

        for (i in 0 .. RETRY_TIMES) {
            val part1 = RetrofitHelper.get(api.getPoTokenPart1())

            if (part1?.requestKey == null || part1.botguardResponse == null)
                continue

            val part2 = PoTokenService.generateIntegrityToken(part1.requestKey, part1.botguardResponse)

            if (part2?.integrityTokenData == null)
                continue

            poToken = RetrofitHelper.get(api.getPoTokenPart2(Gson().toJson(part2)))

            if (poToken?.poToken != null)
                break

            if (i < RETRY_TIMES)
                delay(RETRY_DELAY_MS)
        }

        return poToken
    }
}
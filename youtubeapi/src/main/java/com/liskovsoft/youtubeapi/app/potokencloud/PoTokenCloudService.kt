package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.sharedutils.helpers.DateHelper
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal object PoTokenCloudService {
    private const val RETRY_TIMES: Int = 1
    private const val RETRY_DELAY_MS: Long = 30_000
    private const val ONE_DAY_MS: Long = 24 * 60 * 60 * 1_000
    private val api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    private val appService = AppService.instance()

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
        return if (isTokenAlmostActual(poToken)) poToken?.poToken else null
    }

    private fun isTokenActual(poToken: PoTokenResponse?) =
        poToken != null && DateHelper.toUnixTimeMs(poToken.mintRefreshDate) > System.currentTimeMillis()

    private fun isTokenAlmostActual(poToken: PoTokenResponse?) =
        poToken != null && (DateHelper.toUnixTimeMs(poToken.mintRefreshDate) + ONE_DAY_MS) > System.currentTimeMillis()

    private suspend fun getPoTokenResponse(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null

        for (i in 0 .. RETRY_TIMES) {
            poToken = RetrofitHelper.get(api.getPoToken(appService.visitorData));
            if (poToken?.poToken != null)
                break

            if (i < RETRY_TIMES)
                delay(RETRY_DELAY_MS)
        }

        return poToken
    }
}
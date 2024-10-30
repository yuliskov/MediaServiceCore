package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.sharedutils.helpers.DateHelper
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal object PoTokenCloudService {
    private const val RETRY_TIMES: Int = Int.MAX_VALUE
    private const val RETRY_DELAY_MS: Long = 100
    private val api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    private val appService = AppService.instance()

    @JvmStatic
    fun updatePoToken() = runBlocking {
        val poToken = MediaServiceData.instance().poToken

        if (DateHelper.toUnixTimeMs(poToken?.mintRefreshDate) > System.currentTimeMillis())
            return@runBlocking

        val newPoToken = getPoTokenResponse()

        if (newPoToken != null)
            MediaServiceData.instance().poToken = newPoToken
    }

    @JvmStatic
    fun getPoToken(): String? {
        return MediaServiceData.instance().poToken?.poToken
    }

    private suspend fun getPoTokenResponse(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null

        for (i in 0 until RETRY_TIMES) {
            poToken = RetrofitHelper.get(api.getPoToken(appService.visitorData));
            if (poToken?.poToken != null)
                break

            if (i < RETRY_TIMES - 1)
                delay(RETRY_DELAY_MS)
        }

        return poToken
    }
}
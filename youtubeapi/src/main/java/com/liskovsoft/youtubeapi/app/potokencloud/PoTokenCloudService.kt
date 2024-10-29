package com.liskovsoft.youtubeapi.app.potokencloud

import com.liskovsoft.sharedutils.helpers.DateHelper
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal object PoTokenCloudService {
    private val api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    private val appService = AppService.instance()

    @JvmStatic
    fun updatePoToken() = runBlocking {
        val poToken = MediaServiceData.instance().poToken

        if (DateHelper.toUnixTimeMs(poToken?.mintRefreshDate) > System.currentTimeMillis())
            return@runBlocking

        val newPoToken = getPoTokenResponse()

        if (newPoToken != null) {
            MediaServiceData.instance().poToken = newPoToken
        }
    }

    @JvmStatic
    fun getPoToken(): String? {
        return MediaServiceData.instance().poToken?.poToken
    }

    private suspend fun getPoTokenResponse(): PoTokenResponse? {
        var poToken: PoTokenResponse? = null

        val times = 3
        repeat(times) { iteration ->
            poToken = RetrofitHelper.get(api.getPoToken(appService.visitorData))
            if (poToken?.poToken != null)
                return@repeat
            else if (iteration != times - 1) delay(10_000)
        }

        return poToken
    }
}
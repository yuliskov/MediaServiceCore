package com.liskovsoft.youtubeapi.app.potokencloud2

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.sharedutils.helpers.Helpers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

internal object PoTokenCloudService {
    private const val RETRY_DELAY_MS: Long = 20_000
    private const val PO_TOKEN_LIFETIME_MS: Long = 12 * 60 * 60 * 1_000
    private val api = RetrofitHelper.create(PoTokenCloudApi::class.java)
    private var contentPot: Pair<String, String>? = null // TODO: replace with the cached token list or even make it persistent

    @JvmStatic
    fun getContentPoToken(videoId: String): String? = runBlocking {
        if (contentPot?.first != videoId) {
            contentPot = null
            getPoTokenResponse(videoId)?.poToken?.let {
                contentPot = Pair(videoId, it)
            }
        }

        contentPot?.second
    }

    private suspend fun getPoTokenResponse(identifier: String): PoTokenResponse? {
        var poToken: PoTokenResponse? = null
        val baseUrls = PO_TOKEN_CLOUD_BASE_URLS.toMutableList()

        while (baseUrls.isNotEmpty()) {
            val baseUrl = baseUrls[Helpers.getRandomNumber(0, baseUrls.size - 1)]
            poToken = RetrofitHelper.get(api.getPoToken(baseUrl, identifier))
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
}
package com.liskovsoft.youtubeapi.innertube.core

import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.api.FileContent
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.innertube.utils.DeviceCategory
import com.liskovsoft.youtubeapi.innertube.utils.URLS
import com.liskovsoft.youtubeapi.innertube.utils.getRandomUserAgent
import com.liskovsoft.youtubeapi.innertube.utils.getStringBetweenStrings

internal class Player private constructor(
    val jsContent: String?,
    val playerUrl: String?
) {
    val signatureTimestamp: String? = null

    companion object {
        private val fileApi = RetrofitHelper.create(FileApi::class.java)

        fun create(poToken: String?, playerId: String?): Player? {
            val realPLayerId = playerId ?: getPlayerId() ?: return null
            val playerUrl = getPlayerUrl(realPLayerId)
            val js = getPlayerJs(playerUrl)

            return Player(js?.content, playerUrl)
        }

        fun getPlayerId(): String? {
            val js = RetrofitHelper.get(fileApi.getContent("${URLS.YT_BASE}/iframe_api"))

            return getStringBetweenStrings(js!!.content!!, "player\\/", "\\/")
        }

        fun getPlayerJs(playerUrl: String): FileContent? {
            return RetrofitHelper.get(
                fileApi.getContent(mapOf("User-Agent" to getRandomUserAgent(DeviceCategory.DESKTOP)), playerUrl))
        }

        fun getPlayerUrl(playerId: String): String {
            return "${URLS.YT_BASE}/s/player/${playerId}/player_ias.vflset/en_US/base.js"
        }
    }
}

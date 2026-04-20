package com.liskovsoft.youtubeapi.innertube.ytcfg

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.innertube.utils.traverseObj

internal object YtCfgService {
    private val api = RetrofitHelper.create(YtCfgApi::class.java)
    private var cachedEncryptedHostFlags: String? = null

    fun getCachedEncryptedHostFlags(videoId: String?): String? {
        return cachedEncryptedHostFlags ?: getEncryptedHostFlags(videoId)?.also { cachedEncryptedHostFlags = it }
    }

    fun getEncryptedHostFlags(videoId: String?): String? {
        if (videoId == null)
            return null

        val root = getYtCfg(AppClient.WEB_EMBED, videoId)

        return traverseObj(
            root,
            "WEB_PLAYER_CONTEXT_CONFIGS",
            "WEB_PLAYER_CONTEXT_CONFIG_ID_EMBEDDED_PLAYER",
            "encryptedHostFlags"
        )?.asString
    }

    /**
     * https://github.com/yt-dlp/yt-dlp/blob/48a61d0f38b156785d24df628d42892441e008c4/yt_dlp/extractor/youtube/_base.py#L956
     */
    private fun getYtCfg(client: AppClient, videoId: String?): JsonObject? {
        val configUrl = client.getRefererUrl(videoId) ?: return null
        val wrapper = api.getYtCfg(configUrl, client.userAgent)
        val ytCfgStr = RetrofitHelper.get(wrapper)

        val parser = JsonParser()
        return parser.parse(ytCfgStr?.ytCfg).asJsonObject
    }
}
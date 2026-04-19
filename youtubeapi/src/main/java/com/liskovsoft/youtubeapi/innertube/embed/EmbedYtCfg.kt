package com.liskovsoft.youtubeapi.innertube.embed

import com.google.gson.JsonParser
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.innertube.utils.traverseObj

internal object EmbedYtCfg {
    private val api = RetrofitHelper.create(EmbedYtCfgApi::class.java)

    val cachedEncryptedHostFlags by lazy {
        getEncryptedHostFlags("6sJZNpPgys4")
    }

    fun getEncryptedHostFlags(videoId: String?): String? {
        if (videoId == null)
            return null

        val wrapper = api.getYtCfg(videoId)
        val ytCfgStr = RetrofitHelper.get(wrapper)

        val parser = JsonParser()
        val root = parser.parse(ytCfgStr?.ytCfg).asJsonObject

        return traverseObj(
            root,
            "WEB_PLAYER_CONTEXT_CONFIGS",
            "WEB_PLAYER_CONTEXT_CONFIG_ID_EMBEDDED_PLAYER",
            "encryptedHostFlags"
        )?.asString
    }
}
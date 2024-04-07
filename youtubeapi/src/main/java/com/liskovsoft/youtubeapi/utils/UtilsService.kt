package com.liskovsoft.youtubeapi.utils

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

internal object UtilsService {
    private val mUtilsApi = RetrofitHelper.withRegExp(UtilsApi::class.java)

    @JvmStatic
    fun canonicalChannelId(channelId: String?): String? {
        if (channelId == null || !channelId.contains("@")) return channelId

        val canonicalChannelId = mUtilsApi.canonicalChannelId(channelId)

        return RetrofitHelper.get(canonicalChannelId)?.channelId
    }
}
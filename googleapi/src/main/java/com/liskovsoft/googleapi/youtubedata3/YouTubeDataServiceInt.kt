package com.liskovsoft.googleapi.youtubedata3

import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadataImpl

object YouTubeDataServiceInt {
    private val mYouTubeDataApi = RetrofitHelper.withGson(YouTubeDataApi::class.java)

    @JvmStatic
    fun getVideoMetadata(vararg videoIds: String): List<ItemMetadata>? {
        val ids = videoIds.joinToString(",")
        val response = RetrofitHelper.get(mYouTubeDataApi.getVideoMetadata(ids))
        return response?.items?.mapNotNull { it?.let { ItemMetadataImpl(it) } }
    }

    @JvmStatic
    fun getChannelMetadata(vararg channelIds: String): List<ItemMetadata>? {
        val ids = channelIds.joinToString(",")
        val response = RetrofitHelper.get(mYouTubeDataApi.getChannelMetadata(ids))
        return response?.items?.mapNotNull { it?.let { ItemMetadataImpl(it) } }
    }
}
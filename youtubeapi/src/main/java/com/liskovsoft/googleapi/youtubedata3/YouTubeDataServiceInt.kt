package com.liskovsoft.googleapi.youtubedata3

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.youtubedata3.data.SnippetResponse
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadataImpl
import retrofit2.Call

internal object YouTubeDataServiceInt {
    private val mYouTubeDataApi = RetrofitHelper.create(YouTubeDataApi::class.java)
    private const val MAX_SIZE = 50

    @JvmStatic
    fun getVideoMetadata(vararg videoIds: String): List<ItemMetadata>? {
        return mergeResult(videoIds) { getMetadata(it) { mYouTubeDataApi.getVideoMetadata(it) } }
    }

    @JvmStatic
    fun getChannelMetadata(vararg channelIds: String): List<ItemMetadata>? {
        return mergeResult(channelIds) { getMetadata(it) { mYouTubeDataApi.getChannelMetadata(it) } }
    }

    @JvmStatic
    fun getPlaylistMetadata(vararg playlistIds: String): List<ItemMetadata>? {
        return mergeResult(playlistIds) { getMetadata(it) { mYouTubeDataApi.getPlaylistMetadata(it) } }
    }

    private fun getMetadata(ids: List<String>, callback: (String) -> Call<SnippetResponse?>): List<ItemMetadata>? {
        val mergedIds = ids.joinToString(",")
        val response = RetrofitHelper.get(callback(mergedIds))
        return response?.items?.mapNotNull { it?.let { ItemMetadataImpl(it) } }
    }

    private fun mergeResult(ids: Array<out String>, callback: (List<String>) -> List<ItemMetadata>?): List<ItemMetadata>? {
        val result = mutableListOf<ItemMetadata>()

        ids.toList().chunked(MAX_SIZE).forEach {
            callback(it)?.let { result.addAll(it) }
        }

        return result.ifEmpty { null }
    }
}
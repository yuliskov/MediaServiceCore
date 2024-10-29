package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.yt.data.DislikeData
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SuggestionsGroup
import com.liskovsoft.youtubeapi.next.v2.gen.DislikesResult
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getDislikeCount
import com.liskovsoft.youtubeapi.next.v2.gen.getLikeCount
import com.liskovsoft.youtubeapi.next.v2.gen.isEmpty
import com.liskovsoft.youtubeapi.next.v2.impl.MediaItemMetadataImpl

internal object WatchNextService {
    private var mWatchNextApi = RetrofitHelper.create(WatchNextApi::class.java)
    private val mAppService = AppService.instance()

    @JvmStatic
    fun getMetadata(videoId: String): MediaItemMetadata? {
        return getMetadata(videoId, null, 0)
    }

    @JvmStatic
    fun getMetadata(item: MediaItem): MediaItemMetadata? {
        return getMetadata(item.videoId, item.playlistId, item.playlistIndex)
    }

    @JvmStatic
    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int): MediaItemMetadata? {
        return getMetadata(videoId, playlistId, playlistIndex, null)
    }

    @JvmStatic
    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        val watchNextResult = getWatchNextResult(videoId, playlistId, playlistIndex, playlistParams)
        var suggestionsResult: WatchNextResult? = null

        //if (watchNextResult?.isEmpty() == true) { // 3 items in a row temporal fix
        //    RetrofitOkHttpHelper.skipAuth(true)
        //    suggestionsResult = getWatchNextResult(videoId, playlistId, playlistIndex, playlistParams)
        //    RetrofitOkHttpHelper.skipAuth(false)
        //}

        return if (watchNextResult != null) MediaItemMetadataImpl(watchNextResult, suggestionsResult) else null
    }

    @JvmStatic
    fun continueGroup(mediaGroup: MediaGroup?): MediaGroup? {
        val nextKey = YouTubeHelper.extractNextKey(mediaGroup)

        if (nextKey == null) {
            return null;
        }

        var continuation = continueWatchNext(BrowseApiHelper.getContinuationQuery(nextKey))

        if (continuation == null || continuation.isEmpty()) {
            continuation = continueWatchNext(BrowseApiHelper.getContinuationQuery(nextKey), true)
        }

        return SuggestionsGroup.from(continuation, mediaGroup)
    }

    @JvmStatic
    fun getDislikeData(videoId: String?): DislikeData? {
        return getDislikesResult(videoId)?.let {
             object : DislikeData {
                 override fun getVideoId(): String? {
                     return it.id
                 }

                 override fun getLikeCount(): String? {
                     return it.getLikeCount()
                 }

                 override fun getDislikeCount(): String? {
                     return it.getDislikeCount()
                 }

                 override fun getViewCount(): Long {
                     return it.viewCount ?: 0
                 }
             }
        }
    }

    private fun getWatchNextResult(videoId: String?): WatchNextResult? {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId!!))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int): WatchNextResult? {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId, playlistId, playlistIndex))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): WatchNextResult? {
        return getWatchNext(WatchNextApiHelper.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams))
    }

    private fun getWatchNext(query: String): WatchNextResult? {
        val wrapper = mWatchNextApi.getWatchNextResult(query, mAppService.visitorData)

        return RetrofitHelper.get(wrapper)
    }

    private fun continueWatchNext(query: String, skipAuth: Boolean = false): WatchNextResultContinuation? {
        val wrapper = mWatchNextApi.continueWatchNextResult(query, mAppService.visitorData)

        return RetrofitHelper.get(wrapper, skipAuth)
    }

    private fun getDislikesResult(videoId: String?): DislikesResult? {
        if (videoId == null) {
            return null
        }

        val wrapper = mWatchNextApi.getDislikes(videoId)

        return RetrofitHelper.get(wrapper)
    }

    /**
     * For testing (mocking) purposes only
     */
    fun setWatchNextApi(watchNextApi: WatchNextApi) {
        mWatchNextApi = watchNextApi
    }
}
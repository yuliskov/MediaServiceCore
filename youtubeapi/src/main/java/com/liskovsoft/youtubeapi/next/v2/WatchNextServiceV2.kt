package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v2.impl.MediaItemMetadataImpl
import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager

class WatchNextServiceV2 private constructor() {
    private val mWatchNextManagerSigned = RetrofitHelper.withGson(WatchNextManagerSigned::class.java)
    private val mWatchNextManagerUnsigned = RetrofitHelper.withGson(WatchNextManagerUnsigned::class.java)
    private val mSignInManager = YouTubeSignInManager.instance()

    fun getMetadata(videoId: String): MediaItemMetadata? {
        return getMetadata(videoId, null, 0)
    }

    fun getMetadata(item: MediaItem): MediaItemMetadata? {
        return getMetadata(item.videoId, item.playlistId, item.playlistIndex)
    }

    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int): MediaItemMetadata? {
        return getMetadata(videoId, playlistId, playlistIndex, null)
    }

    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        val watchNextResult = getWatchNextResult(videoId, playlistId, playlistIndex, playlistParams)

        return if (watchNextResult != null) MediaItemMetadataImpl(watchNextResult) else null
    }

    private fun getWatchNextResult(videoId: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId!!))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams))
    }

    private fun getWatchNext(query: String): WatchNextResult? {
        val wrapper = if (mSignInManager.checkAuthHeader())
            mWatchNextManagerSigned.getWatchNextResult(query, mSignInManager.authorizationHeader)
        else
            mWatchNextManagerUnsigned.getWatchNextResult(query)

        return RetrofitHelper.get(wrapper)
    }

    //fun continueWatchNext(nextKey: String?, authorization: String?): WatchNextResultContinuation {
    //    val wrapper = mWatchNextManagerSigned.continueWatchNextResult(BrowseManagerParams.getContinuationQuery(nextKey), authorization)
    //    return RetrofitHelper.get(wrapper)
    //}

    companion object {
        private var sInstance: WatchNextServiceV2? = null
        fun instance(): WatchNextServiceV2? {
            if (sInstance == null) {
                sInstance = WatchNextServiceV2()
            }
            return sInstance
        }

        fun unhold() {
            sInstance = null
        }
    }
}
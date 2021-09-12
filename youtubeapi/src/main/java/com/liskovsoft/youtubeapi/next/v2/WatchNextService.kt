package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v2.impl.MediaItemImpl
import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager

class WatchNextService private constructor() {
    private val mWatchNextManagerSigned: WatchNextManagerSigned = RetrofitHelper.withGson(WatchNextManagerSigned::class.java)
    private val mWatchNextManagerUnsigned: WatchNextManagerUnsigned = RetrofitHelper.withGson(WatchNextManagerUnsigned::class.java)
    private val mSignInManager: YouTubeSignInManager = YouTubeSignInManager.instance()

    fun getMetadata(item: MediaItem?): MediaItemMetadata? {
        item as MediaItemImpl
        return null;
    }

    fun getMetadata(videoId: String?): MediaItemMetadata? {
        return null;
    }

    fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int): MediaItemMetadata? {
        return null;
    }

    private fun getWatchNextResult(videoId: String?, authorization: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId!!), authorization)
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, authorization: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex), authorization)
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?, authorization: String?): WatchNextResult? {
        return getWatchNext(WatchNextManagerParams.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams), authorization)
    }

    private fun getWatchNext(query: String, authorization: String?): WatchNextResult? {
        val wrapper = mWatchNextManagerSigned.getWatchNextResult(query, authorization)
        return RetrofitHelper.get(wrapper)
    }

    //fun continueWatchNext(nextKey: String?, authorization: String?): WatchNextResultContinuation {
    //    val wrapper = mWatchNextManagerSigned.continueWatchNextResult(BrowseManagerParams.getContinuationQuery(nextKey), authorization)
    //    return RetrofitHelper.get(wrapper)
    //}

    companion object {
        private var sInstance: WatchNextService? = null
        fun instance(): WatchNextService? {
            if (sInstance == null) {
                sInstance = WatchNextService()
            }
            return sInstance
        }

        fun unhold() {
            sInstance = null
        }
    }
}
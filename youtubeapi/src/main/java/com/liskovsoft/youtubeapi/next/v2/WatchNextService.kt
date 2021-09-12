package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager

class WatchNextService private constructor() {
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
        TODO("Implement conversion: Retrofit result => Metadata interface")
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
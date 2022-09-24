package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.BrowseManagerParams
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v2.impl.MediaItemMetadataImpl
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper
import com.liskovsoft.youtubeapi.service.YouTubeSignInService

class WatchNextService private constructor() {
    private var mWatchNextManager = RetrofitHelper.withGson(WatchNextApi::class.java)
    private val mSignInManager = YouTubeSignInService.instance()
    private val mAppService = AppService.instance();

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

    fun continueGroup(mediaGroup: MediaGroup?): MediaGroup? {
        val nextKey = YouTubeMediaServiceHelper.extractNextKey(mediaGroup)

        if (nextKey == null) {
            return null;
        }

        val continuation = continueWatchNext(BrowseManagerParams.getContinuationQuery(nextKey))

        return MediaGroupImpl.from(continuation, mediaGroup)
    }

    private fun getWatchNextResult(videoId: String?): WatchNextResult? {
        return getWatchNext(WatchNextApiParams.getWatchNextQuery(videoId!!))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int): WatchNextResult? {
        return getWatchNext(WatchNextApiParams.getWatchNextQuery(videoId, playlistId, playlistIndex))
    }

    private fun getWatchNextResult(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): WatchNextResult? {
        return getWatchNext(WatchNextApiParams.getWatchNextQuery(videoId, playlistId, playlistIndex, playlistParams))
    }

    private fun getWatchNext(query: String): WatchNextResult? {
        val wrapper = if (mSignInManager.checkAuthHeader())
            mWatchNextManager.getWatchNextResultSigned(query, mSignInManager.authorizationHeader, mAppService.visitorId)
        else
            mWatchNextManager.getWatchNextResultUnsigned(query, mAppService.visitorId)

        return RetrofitHelper.get(wrapper)
    }

    private fun continueWatchNext(query: String): WatchNextResultContinuation? {
        val wrapper = if (mSignInManager.checkAuthHeader())
            mWatchNextManager.continueWatchNextResultSigned(query, mSignInManager.authorizationHeader, mAppService.visitorId)
        else
            mWatchNextManager.continueWatchNextResultUnsigned(query, mAppService.visitorId)
        return RetrofitHelper.get(wrapper)
    }

    /**
     * For testing (mocking) purposes only
     */
    fun setWatchNextManager(watchNextApi: WatchNextApi) {
        mWatchNextManager = watchNextApi
    }

    companion object {
        private var sInstance: WatchNextService? = null
        @JvmStatic
        fun instance(): WatchNextService? {
            if (sInstance == null) {
                sInstance = WatchNextService()
            }
            return sInstance
        }

        @JvmStatic
        fun unhold() {
            sInstance = null
        }
    }
}
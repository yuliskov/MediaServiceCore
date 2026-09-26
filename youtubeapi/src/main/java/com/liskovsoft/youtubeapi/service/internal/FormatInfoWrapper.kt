package com.liskovsoft.youtubeapi.service.internal

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.PoTokenGate
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.innertube.InnertubeService
import com.liskovsoft.youtubeapi.service.YouTubeSignInService
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService

internal object FormatInfoWrapper {
    private var mCachedFormatInfo: MediaItemFormatInfo? = null
    private var mTryInnertubeFirst: Boolean = true
    private val mInnertubeResult = object { var isUnplayable: Boolean = false; var client: AppClient? = null }
    private val mInnertubeProvider: (String, String?) -> MediaItemFormatInfo? =
        { videoId, clickTrackingParams ->
            getFormatInfoInnertube(videoId, clickTrackingParams).also {
                mInnertubeResult.isUnplayable = it?.isUnplayable ?: true
                mInnertubeResult.client = it?.clientInfo as? AppClient
                persistFormatInfoType(it)
            }
        }
    private val mLegacyProvider: (String, String?) -> MediaItemFormatInfo? =
        { videoId, clickTrackingParams -> getFormatInfoLegacy(videoId, clickTrackingParams) }

    @JvmStatic
    fun getFormatInfo(videoId: String, clickTrackingParams: String?): MediaItemFormatInfo? {
        return selectPlaybackFormatInfo(videoId, clickTrackingParams)
    }

    @JvmStatic
    fun switchNextFormat(force: Boolean) {
        if (mTryInnertubeFirst) {
            if (!mInnertubeResult.isUnplayable && mInnertubeResult.client?.let { PoTokenGate.resetCache(it) } ?: false)
                return

            mTryInnertubeFirst = false
        } else {
            val endReached: Boolean = getVideoInfoService().switchNextFormat(force)
            if (endReached)
                mTryInnertubeFirst = true
        }
    }

    @JvmStatic
    fun resetFormat() {
        getVideoInfoService().resetInfoType()
    }

    @JvmStatic
    fun invalidateCache() {
        mCachedFormatInfo = null
    }

    @JvmStatic
    fun syncWithAuthInfoIfNeeded(formatInfo: MediaItemFormatInfo?) {
        if (formatInfo == null) {
            return
        }

        if (shouldBeSynced(formatInfo) && !formatInfo.isSynced()) {
            val videoInfo = getVideoInfoService().getAuthVideoInfo(formatInfo.getVideoId(), formatInfo.getClickTrackingParams())
            formatInfo.sync(YouTubeMediaItemFormatInfo.from(videoInfo))
        }

        if (shouldBeSynced(formatInfo)) { // still not synced
            throw IllegalStateException("Update history error: the format should be synced first")
        }
    }

    private fun shouldBeSynced(formatInfo: MediaItemFormatInfo): Boolean {
        return !formatInfo.isAuth() && !formatInfo.isUnplayable() && getSignInService().isSigned
    }

    private fun selectPlaybackFormatInfo(videoId: String, clickTrackingParams: String?): MediaItemFormatInfo? {
        var formatInfo = getPrimaryProvider()(videoId, clickTrackingParams)

        if (formatInfo != null && formatInfo.isUnplayable()) {
            formatInfo = getSecondaryProvider()(videoId, clickTrackingParams)
        }

        return formatInfo
    }

    private fun getPrimaryProvider(): (String, String?) -> MediaItemFormatInfo? {
        return if (mTryInnertubeFirst) mInnertubeProvider else mLegacyProvider
    }

    private fun getSecondaryProvider(): (String, String?) -> MediaItemFormatInfo? {
        return if (mTryInnertubeFirst) mLegacyProvider else mInnertubeProvider
    }

    private fun getFormatInfoInnertube(videoId: String, clickTrackingParams: String?): MediaItemFormatInfo? {
        //videoId = "K04WmBtVsOs"; // the testing video: Memories of Memories

        val cachedFormatInfo: MediaItemFormatInfo? = getCachedFormatInfo(videoId)

        if (cachedFormatInfo != null) {
            return cachedFormatInfo
        }

        checkSigned()

        val formatInfo = InnertubeService.createFormatInfo(videoId)

        setCachedFormatInfo(formatInfo, clickTrackingParams)

        return formatInfo
    }

    private fun getFormatInfoLegacy(videoId: String?, clickTrackingParams: String?): MediaItemFormatInfo? {
        val cachedFormatInfo = getCachedFormatInfo(videoId)

        if (cachedFormatInfo != null) {
            return cachedFormatInfo
        }

        checkSigned()

        val videoInfo = getVideoInfoService().getVideoInfo(videoId, clickTrackingParams)

        val formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo)

        setCachedFormatInfo(formatInfo, clickTrackingParams)

        return formatInfo
    }

    private fun getCachedFormatInfo(videoId: String?): MediaItemFormatInfo? {
        return if (mCachedFormatInfo?.getVideoId() != null &&
            mCachedFormatInfo?.getVideoId() == videoId &&
            mCachedFormatInfo?.isCacheActual() ?: false
        ) mCachedFormatInfo else null
    }

    private fun setCachedFormatInfo(formatInfo: MediaItemFormatInfo?, clickTrackingParams: String?) {
        if (formatInfo == null || formatInfo.isUnplayable()) {
            mCachedFormatInfo = null
            return
        }

        mCachedFormatInfo = formatInfo
        formatInfo.setClickTrackingParams(clickTrackingParams)
    }

    private fun persistFormatInfoType(formatInfo: MediaItemFormatInfo?) {
        if (!GlobalPreferences.isInitialized()) {
            return
        }

        val appClient = formatInfo?.clientInfo as? AppClient

        val videoInfoType = appClient?.ordinal ?: -1

        if (getData().videoInfoType != videoInfoType)
            getData().videoInfoType = videoInfoType
    }

    private fun checkSigned() {
        getSignInService().checkAuth()
    }

    private fun getSignInService(): YouTubeSignInService {
        return YouTubeSignInService.instance()
    }

    private fun getVideoInfoService(): VideoInfoService {
        return VideoInfoService.instance()
    }

    private fun getData(): MediaServiceData {
        return MediaServiceData.instance()
    }
}
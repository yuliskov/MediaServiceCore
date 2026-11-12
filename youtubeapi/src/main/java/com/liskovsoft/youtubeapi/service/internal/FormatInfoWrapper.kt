package com.liskovsoft.youtubeapi.service.internal

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.app.PoTokenGate
import com.liskovsoft.youtubeapi.app.ReloadPlaybackGate
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.innertube.InnertubeService
import com.liskovsoft.youtubeapi.service.YouTubeSignInService
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService

internal object FormatInfoWrapper {
    private enum class ProviderType { INNERTUBE, LEGACY }
    private var mCachedFormatInfo: MediaItemFormatInfo? = null
    private val mInnertubeProvider: (AppClient, String, String?) -> MediaItemFormatInfo? =
        { client, videoId, clickTrackingParams -> getFormatInfoInnertube(client, videoId, clickTrackingParams) }
    private val mLegacyProvider: (AppClient, String, String?) -> MediaItemFormatInfo? =
        { client, videoId, clickTrackingParams -> getFormatInfoLegacy(client, videoId, clickTrackingParams) }
    private val lock = Any()
    private val CLIENT_LIST = arrayOf(
        Pair(AppClient.WEB, ProviderType.INNERTUBE), // Fix video clip blocked in current location
        Pair(AppClient.TV, ProviderType.LEGACY), // Supports auth. Fixes "please sign in" bug! (the best for Premium users)
        Pair(AppClient.VISIONOS, ProviderType.LEGACY), // no url formats
        Pair(AppClient.TV_DOWNGRADED, ProviderType.LEGACY), // works with old UAs like old Cobalt and old Xbox (non-tcl players)
        Pair(AppClient.WEB_EMBED, ProviderType.LEGACY), // Restricted (18+) videos (not working)
        //AppClient.ANDROID_REEL, // doesn't require pot and cipher (hangs on all engines)
        Pair(AppClient.WEB_SAFARI, ProviderType.LEGACY),
        Pair(AppClient.IOS, ProviderType.LEGACY),
        Pair(AppClient.GEO, ProviderType.LEGACY), // Fix video clip blocked in current location
        Pair(AppClient.MWEB, ProviderType.LEGACY), // single audio language
        //AppClient.TV_LEGACY,
        //AppClient.TV_EMBED, // single audio language
        Pair(AppClient.ANDROID_VR, ProviderType.LEGACY), // doesn't require pot and cipher (often hangs?)
        //AppClient.TV_SIMPLY, // hangs?
        //AppClient.ANDROID_SDK_LESS, // doesn't require pot (hangs on Cronet!)
    )
    private var mActualClient: Pair<AppClient, ProviderType>? = null
    private var mNextClient: Pair<AppClient, ProviderType>? = null
    private var mIsUnplayable: Boolean = false

    @JvmStatic
    fun getFormatInfo(videoId: String?, clickTrackingParams: String?): MediaItemFormatInfo? {
        if (videoId == null)
            return null

        try {
            synchronized(lock) {
                //videoId = "K04WmBtVsOs"; // the testing video: Memories of Memories

                val cachedFormatInfo = getCachedFormatInfo(videoId)

                if (cachedFormatInfo != null) {
                    return cachedFormatInfo
                }

                val formatInfo = firstPlayable(videoId, clickTrackingParams)

                mIsUnplayable = formatInfo?.isUnplayable ?: true

                setCachedFormatInfo(formatInfo, clickTrackingParams)

                return formatInfo
            }
        } finally {
            ReloadPlaybackGate.reset(videoId)
        }
    }

    @JvmStatic
    fun switchNextClient(force: Boolean) {
        // Try to reset pot cache for the last video
        if (!force && !mIsUnplayable && (mActualClient?.let { PoTokenGate.resetCache(it.first) } ?: false)) {
            return
        }

        // And last, try to switch the client
        switchNextClientInt()
    }

    @JvmStatic
    fun resetClient() {
        resetClientInt()
        PoTokenGate.resetCache()
    }

    @JvmStatic
    fun invalidateCache() {
        mCachedFormatInfo = null
    }

    @JvmStatic
    fun switchNextSubtitle() {
        getVideoInfoService().switchNextSubtitle()
    }

    @JvmStatic
    fun allowRestrictedVideos(allow: Boolean) {
        getVideoInfoService().allowRestrictedVideos(allow)
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

    private fun firstPlayable(videoId: String, clickTrackingParams: String?): MediaItemFormatInfo? {
        return firstInfoWith(videoId, clickTrackingParams) { !it.isUnplayable() }
            ?: firstInfoWith(videoId, clickTrackingParams) { it.urlFormats != null } // fallback to regular formats (480p)
            ?: firstInfoWith(videoId, clickTrackingParams) { true } // fallback to error result
    }

    private fun firstInfoWith(videoId: String, clickTrackingParams: String?, infoTester: (MediaItemFormatInfo) -> Boolean): MediaItemFormatInfo? {
        val beginClient = getClient()
        var nextClient = beginClient

        do {
            val provider = getProvider(nextClient)

            checkSigned()

            AppService.instance().resetClientPlaybackNonce() // history/pot fix: unique value per each format info

            val result = provider(nextClient.first, videoId, clickTrackingParams)

            if (result != null && infoTester(result)) {
                persistRecentTypeIfNeeded(nextClient, result)
                return result
            }

            nextClient = Helpers.getNextValue(CLIENT_LIST, nextClient)
        } while (nextClient != beginClient)

        return null
    }

    private fun getProvider(client: Pair<AppClient, ProviderType>): (AppClient, String, String?) -> MediaItemFormatInfo? {
        return if (client.second == ProviderType.INNERTUBE)
            mInnertubeProvider
        else
            mLegacyProvider
    }

    private fun getFormatInfoInnertube(client: AppClient, videoId: String, clickTrackingParams: String?): MediaItemFormatInfo? {
        return InnertubeService.createFormatInfo(client, videoId)
    }

    private fun getFormatInfoLegacy(client: AppClient, videoId: String, clickTrackingParams: String?): MediaItemFormatInfo? {
        val videoInfo = getVideoInfoService().getVideoInfo(client, videoId, clickTrackingParams)

        return YouTubeMediaItemFormatInfo.from(videoInfo)
    }

    private fun getCachedFormatInfo(videoId: String?): MediaItemFormatInfo? {
        return mCachedFormatInfo?.takeIf { videoId != null && it.getVideoId() == videoId && it.isCacheActual() }
    }

    private fun setCachedFormatInfo(formatInfo: MediaItemFormatInfo?, clickTrackingParams: String?) {
        if (formatInfo == null || formatInfo.isUnplayable()) {
            mCachedFormatInfo = null
            return
        }

        mCachedFormatInfo = formatInfo
        formatInfo.setClickTrackingParams(clickTrackingParams)
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

    private fun getClient(): Pair<AppClient, ProviderType> {
        return mNextClient ?: CLIENT_LIST[0]
    }

    @JvmStatic
    fun getActualClient(): AppClient {
        return (mNextClient ?: CLIENT_LIST[0]).first
    }

    private fun switchNextClientInt() {
        mNextClient = Helpers.getNextValue(CLIENT_LIST, mActualClient)
    }

    private fun resetClientInt() {
        mNextClient = null
        mActualClient = CLIENT_LIST[0]
        persistClient()
    }

    private fun persistClient() {
        if (!GlobalPreferences.isInitialized()) {
            return;
        }

        val appClient = mActualClient?.first

        val ordinal = appClient?.ordinal ?: -1

        if (getData().clientType != ordinal)
            getData().clientType = ordinal
    }

    private fun persistRecentTypeIfNeeded(client: Pair<AppClient, ProviderType>, videoInfo: MediaItemFormatInfo?) {
        if (videoInfo == null || videoInfo.isUnplayable() || client == mActualClient) {
            return
        }

        mActualClient = client
        persistClient()
    }
}
package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.WrapperMediaItem
import android.util.Log

internal abstract class BaseMediaGroup(private val options: MediaGroupOptions): MediaGroup {
	companion object {
		private const val DUPLICATE_TAG = "DuplicateFilter"
		private val sGlobalSeenVideoIds = mutableSetOf<String>()
		private var sCurrentTabType = -1
	}

	private fun shouldFilterDuplicate(item: ItemWrapper): Boolean {
		// Check if we should skip filtering for recommended sections
		val shouldSkipFiltering = options.removeDuplicatesExcludeRecommended &&
			getTitle()?.lowercase()?.contains("recommended") == true

		if (shouldSkipFiltering) {
			return false // Don't filter duplicates in recommended sections
		}

		val currentTabType = options.groupType

		// Clear cached video IDs when switching to a different tab
		if (sCurrentTabType != currentTabType) {
			Log.d(DUPLICATE_TAG, "Tab type changed from $sCurrentTabType to $currentTabType, clearing ${sGlobalSeenVideoIds.size} seen IDs")
			sGlobalSeenVideoIds.clear()
			sCurrentTabType = currentTabType
		}

		val videoId = item.getVideoId()
		if (videoId == null) {
			return false // Don't filter items without video IDs
		}

		if (sGlobalSeenVideoIds.contains(videoId)) {
			Log.d(DUPLICATE_TAG, "Filtering duplicate video: $videoId from group '${getTitle()}'")
			return true // Filter out duplicate
		} else {
			sGlobalSeenVideoIds.add(videoId)
			Log.d(DUPLICATE_TAG, "Adding new video: $videoId from group '${getTitle()}'")
			return false // Keep unique video
		}
	}
    private val filter: ((ItemWrapper) -> Boolean) = {
        it.isEmpty() ||
        (options.removeShorts && if (options.enableLegacyUI) it.isShortsLegacy() else it.isShorts()) ||
        (options.removeLive && it.isLive()) ||
        (options.removeUpcoming && it.isUpcoming()) ||
        (options.removeWatched && (it.getPercentWatched() ?: 0) > 80 && !it.isLive()) ||
        (options.removeDuplicates && shouldFilterDuplicate(it)) ||
        (options.removeMixes && it.isMix())
    }
    private var _titleItem: String? = null
        get() = field ?: titleItem
    private var _mediaItemList: List<MediaItem?>? = null
        get() = field ?: mediaItemList
    private var _nextPageKeyVal: String? = null
        get() = if (field == "") null else field ?: nextPageKeyItem
        set(value) { field = value ?: "" }

    private val titleItem by lazy { getTitleInt() }
    protected open val mediaItemList: List<MediaItem?>? by lazy { getItemWrappersInt()
        ?.mapIndexedNotNull { index, it -> it
            ?.let { if (filter.invoke(it)) null else it }
            ?.let { WrapperMediaItem(it).apply { playlistIndex = index } }
        }?.let {
            // Move Watch Later to the top
            if (options.groupType != MediaGroup.TYPE_USER_PLAYLISTS)
                return@let it

            val idx = it.indexOfFirst { it.channelId == AppConstants.WATCH_LATER_CHANNEL_ID }

            if (idx == -1)
                return@let it

            val mutable = it.toMutableList()
            val item = mutable.removeAt(idx)
            mutable.add(0, item)
            mutable
        }
    }
    private val nextPageKeyItem by lazy { getNextPageKeyInt() }
    private val paramsItem by lazy { getParamsInt() }
    private val channelIdItem by lazy { getChannelIdInt() }

    protected abstract fun getItemWrappersInt(): List<ItemWrapper?>?
    protected abstract fun getNextPageKeyInt(): String?
    protected abstract fun getTitleInt(): String?
    protected open fun getParamsInt(): String? = null
    protected open fun getChannelIdInt(): String? = null

    override fun getType(): Int {
        return options.groupType
    }

    override fun getMediaItems(): List<MediaItem?>? {
        return _mediaItemList
    }

    fun setMediaItems(list: List<MediaItem?>?) {
        _mediaItemList = list
    }

    override fun getTitle(): String? {
        return _titleItem
    }

    fun setTitle(title: String?) {
        _titleItem = title
    }

    override fun getChannelId(): String? {
        return channelIdItem
    }

    override fun getParams(): String? {
        return paramsItem
    }

    override fun getReloadPageKey(): String? {
        return null
    }

    override fun getNextPageKey(): String? {
        return _nextPageKeyVal
    }

    fun setNextPageKey(key: String?) {
        _nextPageKeyVal = key
    }

    override fun getChannelUrl(): String? {
        return null
    }

    override fun isEmpty(): Boolean {
        return _mediaItemList.isNullOrEmpty()
    }
}

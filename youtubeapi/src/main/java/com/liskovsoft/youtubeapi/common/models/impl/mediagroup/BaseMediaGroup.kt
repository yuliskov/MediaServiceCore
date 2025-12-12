package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.browse.v2.BrowseApiHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.WrapperMediaItem

internal abstract class BaseMediaGroup(private val options: MediaGroupOptions): MediaGroup {
    private val filter: ((ItemWrapper) -> Boolean) = {
        it.isEmpty() ||
        (options.removeShorts && it.isShorts()) ||
        (options.removeLive && it.isLive()) ||
        (options.removeUpcoming && it.isUpcoming()) ||
        (options.removeWatched && (it.getPercentWatched() ?: 0) > 80 && !it.isLive())
    }
    private val legacyFilter: ((WrapperMediaItem) -> Boolean) = {
        options.removeShorts && if (options.enableLegacyUI) YouTubeHelper.isShortsLegacy(it) else false
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
            ?.let { if (legacyFilter.invoke(it)) null else it }
        }?.let {
            // Move Watch Later to the top
            if (options.groupType != MediaGroup.TYPE_USER_PLAYLISTS)
                return@let it

            val idx = it.indexOfFirst { it.channelId == BrowseApiHelper.WATCH_LATER_CHANNEL_ID }

            if (idx == -1 || idx >= it.size)
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

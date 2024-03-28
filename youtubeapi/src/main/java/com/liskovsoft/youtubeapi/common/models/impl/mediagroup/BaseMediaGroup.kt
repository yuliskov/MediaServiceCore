package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.WrapperMediaItem

internal data class MediaGroupOptions(val removeShorts: Boolean = true,
                             val removeLive: Boolean = false,
                             val removeUpcoming: Boolean = false,
                             val removeWatched: Boolean = false,
                             val groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS)

internal abstract class BaseMediaGroup(private val options: MediaGroupOptions = MediaGroupOptions()): MediaGroup {
    private val filter: ((ItemWrapper) -> Boolean) = {
        (options.removeShorts && it.isShorts() == true) ||
        (options.removeLive && it.isLive() == true) ||
        (options.removeUpcoming && it.isUpcoming() == true) ||
        (options.removeWatched && (it.getPercentWatched() ?: 0) > 80 && it.isLive() == false)
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
            ?.let { WrapperMediaItem(it).let { if (YouTubeHelper.isEmpty(it)) null else it }?.apply { playlistIndex = index } }
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

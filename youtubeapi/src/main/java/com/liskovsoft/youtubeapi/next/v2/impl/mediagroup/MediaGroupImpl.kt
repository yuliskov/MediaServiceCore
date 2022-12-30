package com.liskovsoft.youtubeapi.next.v2.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.next.v2.gen.*
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.MediaItemImpl
import java.util.*

data class MediaGroupImpl(val shelf: ShelfItem): MediaGroup {
    private var _titleItem: String? = null
                    get() = field ?: titleItem
    private var _mediaItemList: List<MediaItem?>? = null
                    get() = field ?: mediaItemList
    private var _nextPageKeyVal: String? = null
                    get() = if (field == "") null else field ?: nextPageKeyVal
                    set(value) { field = value ?: "" }

    private val titleItem by lazy { shelf.getTitle() }
    private val mediaItemList by lazy { shelf.getItemWrappers()?.mapIndexed { index, it -> it?.let { MediaItemImpl(it).apply { playlistIndex = index } } } }
    private val nextPageKeyVal by lazy { shelf.getNextPageKey() }

    override fun getId(): Int = title?.hashCode() ?: hashCode()

    override fun getType(): Int {
        return MediaGroup.TYPE_SUGGESTIONS;
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

    override fun setTitle(title: String?) {
        _titleItem = title
    }

    override fun getChannelId(): String? {
        return null
    }

    override fun getParams(): String? {
        return null
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
        return mediaItemList.isNullOrEmpty()
    }

    companion object {
        fun from(continuation: WatchNextResultContinuation?, baseGroup: MediaGroup?): MediaGroup? {
            val mediaItems = ArrayList<MediaItem>()

            val items = continuation?.continuationContents?.horizontalListContinuation?.items

            if (items != null) {
                for (i in items.indices) {
                    val itemWrapper = items[i]

                    if (itemWrapper != null) {
                        val mediaItem = MediaItemImpl(itemWrapper)

                        // In case can't find a position of item inside browse playlist query. So using position inside group instead.
                        if (mediaItem.playlistIndex == -1 && mediaItem.playlistId != null) {
                            mediaItem.playlistIndex = i
                        }

                        mediaItem.params = baseGroup?.params
                        mediaItems.add(mediaItem)
                    }
                }
            }

            // Fix duplicated items after previous group reuse
            baseGroup as MediaGroupImpl
            baseGroup.mediaItems = if (mediaItems.isNotEmpty()) mediaItems else null
            val nextKey = continuation?.continuationContents?.horizontalListContinuation?.continuations?.firstNotNullOfOrNull { it?.nextContinuationData?.continuation }
            baseGroup._nextPageKeyVal = nextKey

            return baseGroup
        }
    }
}

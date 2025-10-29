package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.next.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.WrapperMediaItem
import java.util.*

internal data class SuggestionsGroup(val shelf: ShelfRenderer): MediaGroup {
    private var shelves: List<ShelfRenderer>? = null
    constructor(shelves: List<ShelfRenderer>): this(shelves.last()) {
        this.shelves = shelves
    }
    private var _titleItem: String? = null
                    get() = field ?: titleItem
    private var _mediaItemList: List<MediaItem?>? = null
                    get() = field ?: mediaItemList
    private var _nextPageKeyVal: String? = null
                    get() = if (field == "") null else field ?: nextPageKeyVal
                    set(value) { field = value ?: "" }

    private val titleItem by lazy { shelf.getTitle() }
    private val mediaItemList by lazy { (shelves?.flatMap { it.getItemWrappers() ?: emptyList() } ?: shelf.getItemWrappers())
        ?.mapIndexed { index, it -> it?.let { WrapperMediaItem(it).apply { playlistIndex = index } } } }
    private val nextPageKeyVal by lazy { shelf.getContinuationToken() }

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

    fun setTitle(title: String?) {
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
        return mediaItems.isNullOrEmpty()
    }

    companion object {
        fun from(continuation: WatchNextResultContinuation?, baseGroup: MediaGroup?): MediaGroup? {
            if (continuation == null || baseGroup == null) {
                return null
            }

            val newGroup = SuggestionsGroup(ShelfRenderer(null, null, null, null, null))

            val mediaItems = ArrayList<MediaItem>()

            val items = continuation.getItems()

            if (items != null) {
                for (i in items.indices) {
                    val itemWrapper = items[i]

                    if (itemWrapper != null) {
                        val mediaItem = WrapperMediaItem(itemWrapper)

                        // In case can't find a position of item inside browse playlist query. So using position inside group instead.
                        if (mediaItem.playlistIndex == -1 && mediaItem.playlistId != null) {
                            mediaItem.playlistIndex = i
                        }

                        mediaItem.params = baseGroup.params
                        mediaItems.add(mediaItem)
                    }
                }
            }

            // Fix duplicated items after previous group reuse
            newGroup.mediaItems = if (mediaItems.isNotEmpty()) mediaItems else null
            val nextKey = continuation.getContinuationToken()
            newGroup.nextPageKey = nextKey
            newGroup.title = baseGroup.title

            return newGroup
        }
    }
}

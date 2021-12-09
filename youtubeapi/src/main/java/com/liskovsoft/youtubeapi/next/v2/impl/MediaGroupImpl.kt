package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.next.v2.helpers.getText
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.MediaItemImpl
import com.liskovsoft.youtubeapi.next.v2.result.gen.ShelfItem

data class MediaGroupImpl(val shelf: ShelfItem): MediaGroup {
    private var _titleItem: String? = null
    private val titleItem by lazy { shelf.title?.getText() }
    private val mediaItemList by lazy { shelf.content?.horizontalListRenderer?.items?.map { it?.let { MediaItemImpl(it) } } }

    override fun getId(): Int = title?.hashCode() ?: hashCode()

    override fun getType(): Int {
        return MediaGroup.TYPE_SUGGESTIONS;
    }

    override fun getMediaItems(): List<MediaItem?>? {
        return mediaItemList
    }

    override fun getTitle(): String? {
        return _titleItem ?: titleItem
    }

    override fun setTitle(title: String?) {
        _titleItem = title
    }

    override fun getChannelId(): String? {
        return null
    }

    override fun getPlaylistParams(): String? {
        return null
    }

    override fun getChannelUrl(): String? {
        return null
    }

    override fun isEmpty(): Boolean {
        return mediaItemList.isNullOrEmpty()
    }
}

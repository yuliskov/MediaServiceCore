package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.next.v2.helpers.getText
import com.liskovsoft.youtubeapi.next.v2.result.gen.ShelfItem

data class MediaGroupImpl(val shelf: ShelfItem): MediaGroup {
    private val _title by lazy { shelf.title?.getText() }
    private val _mediaItems by lazy { shelf.content?.horizontalListRenderer?.items?.map { it?.let { MediaItemImpl(it) } } }

    override fun getId(): Int = title?.hashCode() ?: hashCode()

    override fun getType(): Int {
        TODO("Not yet implemented")
    }

    override fun getMediaItems(): List<MediaItem?>? {
        return _mediaItems
    }

    override fun getTitle(): String? {
        return _title
    }

    override fun setTitle(title: String?) {
        TODO("Not yet implemented")
    }

    override fun getChannelId(): String {
        TODO("Not yet implemented")
    }

    override fun getPlaylistParams(): String {
        TODO("Not yet implemented")
    }

    override fun getChannelUrl(): String {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }
}

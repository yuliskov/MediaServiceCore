package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem

data class MediaGroupImpl(val value: Any): MediaGroup {
    override fun getType(): Int {
        TODO("Not yet implemented")
    }

    override fun getMediaItems(): MutableList<MediaItem> {
        TODO("Not yet implemented")
    }

    override fun setMediaItems(tabs: MutableList<MediaItem>?) {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
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

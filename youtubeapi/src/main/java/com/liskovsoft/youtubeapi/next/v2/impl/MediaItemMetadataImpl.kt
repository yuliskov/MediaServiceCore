package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.next.v2.result.WatchNextResult

data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult): MediaItemMetadata {
    private val _title: String? by lazy { null }
    private val _description: String? by lazy { null }

    override fun getTitle(): String? {
        return _title
    }

    override fun getDescription(): String? {
        return _description
    }

    override fun getFullDescription(): String? {
        return null;
    }

    override fun getAuthor(): String? {
        return null
    }

    override fun getViewCount(): String? {
        return null
    }

    override fun getLikesCount(): String? {
        return null
    }

    override fun getDislikesCount(): String? {
        return null
    }

    override fun getPublishedDate(): String? {
        return null
    }

    override fun getVideoId(): String? {
        return null
    }

    override fun getNextVideo(): MediaItem? {
        return null
    }

    override fun isSubscribed(): Boolean {
        return false
    }

    override fun isLive(): Boolean {
        return false
    }

    override fun isUpcoming(): Boolean {
        return false
    }

    override fun getChannelId(): String? {
        return null
    }

    override fun getPercentWatched(): Int {
        return 0
    }

    override fun getLikeStatus(): Int {
        return 0
    }

    override fun getSuggestions(): MutableList<MediaGroup?>? {
        return null
    }

    override fun getDescriptionAlt(): String? {
        return null
    }
}

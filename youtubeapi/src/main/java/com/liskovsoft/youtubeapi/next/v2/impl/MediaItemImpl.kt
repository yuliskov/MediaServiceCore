package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.next.v2.helpers.*
import com.liskovsoft.youtubeapi.next.v2.result.gen.ItemWrapper

data class MediaItemImpl(var itemWrapper: ItemWrapper): BaseMediaItemImpl() {
    private val BADGE_STYLE_LIVE = "LIVE"
    private val BADGE_STYLE_UPCOMING = "UPCOMING"
    private val BADGE_STYLE_DEFAULT = "DEFAULT"

    override val reloadPageKey: String? = null

    private val _videoId by lazy { itemWrapper.getVideoItem()?.videoId ?: itemWrapper.getMusicItem()?.videoId ?: itemWrapper.getTileItem()?.videoId }

    override fun getType(): Int {
        if (itemWrapper.getChannelItem() != null)
            return MediaItem.TYPE_CHANNEL
        if (itemWrapper.getPlaylistItem() != null)
            return MediaItem.TYPE_PLAYLIST
        if (itemWrapper.getRadioItem() != null)
            return MediaItem.TYPE_PLAYLIST
        if (itemWrapper.getVideoItem() != null)
            return MediaItem.TYPE_VIDEO
        if (itemWrapper.getMusicItem() != null)
            return MediaItem.TYPE_MUSIC

        return MediaItem.TYPE_UNDEFINED;
    }

    override fun isLive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isUpcoming(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPercentWatched(): Int {
        TODO("Not yet implemented")
    }

    override fun getAuthor(): String {
        TODO("Not yet implemented")
    }

    override fun getFeedbackToken(): String {
        TODO("Not yet implemented")
    }

    override fun getPlaylistId(): String {
        TODO("Not yet implemented")
    }

    override fun getPlaylistParams(): String {
        TODO("Not yet implemented")
    }

    override fun hasNewContent(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String {
        TODO("Not yet implemented")
    }

    override fun getVideoUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getVideoId(): String? {
        return _videoId
    }

    override fun getContentType(): String {
        TODO("Not yet implemented")
    }

    override fun getDurationMs(): Int {
        TODO("Not yet implemented")
    }

    override fun getBadgeText(): String {
        TODO("Not yet implemented")
    }

    override fun getProductionDate(): String {
        TODO("Not yet implemented")
    }

    override fun getCardImageUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getBackgroundImageUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getChannelId(): String {
        TODO("Not yet implemented")
    }

    override fun getChannelUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getVideoPreviewUrl(): String {
        TODO("Not yet implemented")
    }

    override fun getPlaylistIndex(): Int {
        TODO("Not yet implemented")
    }

    override fun hasUploads(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getClickTrackingParams(): String {
        TODO("Not yet implemented")
    }
}

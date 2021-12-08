package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.next.v2.helpers.*
import com.liskovsoft.youtubeapi.next.v2.result.gen.ItemWrapper
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

data class MediaItemImpl(var itemWrapper: ItemWrapper): BaseMediaItemImpl() {
    override val reloadPageKey: String? = null

    private val _videoId by lazy { itemWrapper.videoId }
    private val _title by lazy { itemWrapper.title }
    private val descBadgeText by lazy { itemWrapper.descBadgeText }
    private val userName by lazy { itemWrapper.userName }
    private val publishedTime by lazy { itemWrapper.publishedTime }
    private val viewCountText by lazy { itemWrapper.viewCountText }
    private val upcomingEventText by lazy { itemWrapper.upcomingEventText }
    private val _description by lazy { YouTubeMediaServiceHelper.createDescription(descBadgeText, userName, publishedTime, viewCountText, upcomingEventText) }

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

    override fun getTitle(): String? {
        return _title
    }

    override fun getDescription(): String {
        return _description
    }

    override fun getVideoId(): String? {
        return _videoId
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

    override fun getVideoUrl(): String {
        TODO("Not yet implemented")
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

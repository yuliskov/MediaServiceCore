package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.sharedutils.helpers.Helpers

abstract class BaseMediaItemImpl: MediaItem {
    protected var newTitle: String? = null
    protected var newDescription: String? = null
    protected var newChannelId: String? = null

    protected open val reloadPageKey: String? = null
    private val _id by lazy { videoId?.hashCode() ?: channelId?.hashCode() ?: sId++ }

    protected companion object {
        var sId: Int = 0
    }

    override fun getId(): Int {
        return _id
    }

    override fun getWidth(): Int {
        return 1280
    }

    override fun getHeight(): Int {
        return 720
    }

    override fun getAudioChannelConfig(): String {
        return "2.0"
    }

    override fun getPurchasePrice(): String {
        return "$5.99"
    }

    override fun getRentalPrice(): String {
        return "$4.99"
    }

    override fun getRatingStyle(): Int {
        return 5
    }

    override fun getRatingScore(): Double {
        return 4.0;
    }

    open fun isEmpty(): Boolean {
        return title == null && cardImageUrl == null
    }

    fun setTitle(title: String?) {
        newTitle = title
    }

    fun setDescription(description: String?) {
        newDescription = description
    }

    fun setChannelId(channelId: String?) {
        newChannelId = channelId
    }

    open fun sync(metadata: MediaItemMetadata?) {
        if (metadata == null) {
            return
        }
        title = metadata.title
        description = metadata.description
        channelId = metadata.channelId
    }

    override fun toString(): String {
        return String.format("%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s", reloadPageKey, title, description, cardImageUrl, videoId, playlistId, channelId)
    }

    override fun equals(other: Any?): Boolean {
        if (other is BaseMediaItemImpl) {
            if (videoId != null) {
                return videoId == other.videoId
            }
            if (playlistId != null) {
                return playlistId == other.playlistId
            }
            if (channelId != null) {
                return channelId == other.channelId
            }
            if (reloadPageKey != null) {
                return reloadPageKey == other.reloadPageKey
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return Helpers.hashCode(videoId, playlistId, channelId, reloadPageKey)
    }

    // Not implemented

    override fun isLive(): Boolean {
        return false
    }

    override fun isUpcoming(): Boolean {
        return false
    }

    override fun getPercentWatched(): Int {
        return 0
    }

    override fun getAuthor(): String? {
        return null
    }

    override fun getFeedbackToken(): String? {
        return null
    }

    override fun getPlaylistParams(): String? {
        return null
    }

    override fun hasNewContent(): Boolean {
        return false
    }

    override fun getContentType(): String? {
        return null
    }

    override fun getDurationMs(): Int {
        return 0;
    }

    override fun getBadgeText(): String? {
        return null
    }

    override fun getProductionDate(): String? {
        return null
    }

    override fun getChannelUrl(): String? {
        return null
    }

    override fun getVideoPreviewUrl(): String? {
        return null
    }

    override fun hasUploads(): Boolean {
        return false
    }

    override fun getClickTrackingParams(): String? {
        return null
    }
}

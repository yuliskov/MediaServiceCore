package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.next.v2.gen.kt.TextItem
import kotlin.math.abs

open class BaseMediaItemImpl : MediaItem {
    protected val BADGE_STYLE_LIVE = "LIVE"
    protected val BADGE_STYLE_UPCOMING = "UPCOMING"
    protected val BADGE_STYLE_DEFAULT = "DEFAULT"

    private var _titleItem: String? = null
    private var _descriptionItem: String? = null
    private var _channelIdItem: String? = null
    private var _reloadPageKey: String? = null
    private var _cardThumbImageUrl: String? = null
    private var _videoIdItem: String? = null
    private var _playlistIdItem: String? = null
    private var _playlistIndexItem: Int = 0
    private var _playlistParamsItem: String? = null

    protected open val reloadPageKey: String?
        get() = _reloadPageKey
    private val _id by lazy { videoId?.hashCode() ?: channelId?.hashCode() ?: sId++ }

    protected open val badgeTextItem: String? = null
    protected open val typeItem: Int = MediaItem.TYPE_VIDEO
    protected open val videoIdItem: String?
        get() = _videoIdItem
    protected open val titleItem: String?
        get() = _titleItem
    protected open val descBadgeText: String? = null
    protected open val userName: String? = null
    protected open val publishedTime: String? = null
    protected open val viewCountText: String? = null
    protected open val upcomingEventText: String? = null
    protected open val durationItemMs: Int = 0
    protected open val descriptionItem: String?
        get() = _descriptionItem
    protected open val cardThumbImageUrl: String?
        get() = _cardThumbImageUrl
    protected open val playlistIdItem: String?
        get() = _playlistIdItem
    protected open val playlistIndexItem: Int
        get() = _playlistIndexItem
    protected open val channelIdItem: String?
        get() = _channelIdItem
    protected open val mediaUrl: String? = null
    protected open val playlistParamsItem: String?
        get() = _playlistParamsItem
    protected open val isLiveItem: Boolean = false
    protected open val isUpcomingItem: Boolean = false

    protected companion object {
        var sId: Int = 0
        fun fromString(spec: String?): MediaItem? {
            if (spec == null) {
                return null
            }
            val split = spec.split("&mi;".toRegex()).toTypedArray()
            if (split.size != 7) {
                return null
            }
            val mediaItem = BaseMediaItemImpl()
            mediaItem._reloadPageKey = Helpers.parseStr(split[0])
            mediaItem._titleItem = Helpers.parseStr(split[1])
            mediaItem._descriptionItem = Helpers.parseStr(split[2])
            mediaItem._cardThumbImageUrl = Helpers.parseStr(split[3])
            mediaItem._videoIdItem = Helpers.parseStr(split[4])
            mediaItem._playlistIdItem = Helpers.parseStr(split[5])
            mediaItem._channelIdItem = Helpers.parseStr(split[6])
            return mediaItem
        }
    }

    override fun getId(): Int {
        return abs(_id)
    }

    override fun getType(): Int {
        return typeItem
    }

    override fun getTitle(): String? {
        return _titleItem ?: titleItem
    }

    override fun getDescription(): String? {
        return _descriptionItem ?: descriptionItem
    }

    override fun getVideoId(): String? {
        return videoIdItem
    }

    override fun getProductionDate(): String? {
        return publishedTime
    }

    override fun getCardImageUrl(): String? {
        return cardThumbImageUrl
    }

    override fun getBackgroundImageUrl(): String? {
        return cardThumbImageUrl
    }

    override fun getPlaylistParams(): String? {
        return _playlistParamsItem ?: playlistParamsItem
    }

    fun setPlaylistParams(params: String?) {
        _playlistParamsItem = params
    }

    override fun getDurationMs(): Int {
        return durationItemMs;
    }

    override fun getBadgeText(): String? {
        return badgeTextItem
    }

    override fun getPlaylistId(): String? {
        return playlistIdItem
    }

    override fun getVideoUrl(): String? {
        return mediaUrl
    }

    override fun getChannelId(): String? {
        return _channelIdItem ?: channelIdItem
    }

    override fun getPlaylistIndex(): Int {
        return _playlistIndexItem ?: playlistIndexItem
    }

    fun setPlaylistIndex(index: Int) {
        _playlistIndexItem = index
    }

    override fun isLive(): Boolean {
        return isLiveItem
    }

    override fun isUpcoming(): Boolean {
        return isUpcomingItem
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

    override fun hasNewContent(): Boolean {
        return false
    }

    override fun getContentType(): String? {
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

    // Fake params

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

    // End Fake params

    open fun isEmpty(): Boolean {
        return title == null && cardImageUrl == null
    }

    fun setTitle(title: String?) {
        _titleItem = title
    }

    fun setDescription(description: String?) {
        _descriptionItem = description
    }

    fun setChannelId(channelId: String?) {
        _channelIdItem = channelId
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
}

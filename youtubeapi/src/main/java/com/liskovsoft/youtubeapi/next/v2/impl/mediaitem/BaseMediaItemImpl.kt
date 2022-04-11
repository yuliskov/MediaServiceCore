package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import kotlin.math.abs

open class BaseMediaItemImpl : MediaItem {
    private var _titleItem: String? = null
        get() = field ?: titleItem
    private var _secondTitleItem: String? = null
        get() = field ?: infoItem
    private var _channelIdItem: String? = null
        get() = field ?: channelIdItem
    private var _reloadPageKeyItem: String? = null
        get() = field ?: reloadPageKeyItem
    private var _cardThumbImageUrl: String? = null
        get() = field ?: cardThumbImageUrl
    private var _videoIdItem: String? = null
        get() = field ?: videoIdItem
    private var _playlistIdItem: String? = null
        get() = field ?: playlistIdItem
    private var _playlistIndexItem: Int? = null
        get() = field ?: playlistIndexItem
    private var _playlistParamsItem: String? = null
        get() = field ?: playlistParamsItem

    private val _id by lazy { videoId?.hashCode() ?: channelId?.hashCode() ?: sId++ }

    protected open val reloadPageKeyItem: String? = null // TODO: override in the subclasses
    protected open val badgeTextItem: String? = null
    protected open val typeItem: Int = MediaItem.TYPE_VIDEO
    protected open val videoIdItem: String? = null
    protected open val titleItem: String? = null
    protected open val infoItem: String? = null
    protected open val descBadgeText: String? = null
    protected open val userName: String? = null
    protected open val publishedTime: String? = null
    protected open val viewCountText: String? = null
    protected open val upcomingEventText: String? = null
    protected open val lengthText: String? = null
    protected open val cardThumbImageUrl: String? = null
    protected open val playlistIdItem: String? = null
    protected open val playlistIndexItem: Int? = null
    protected open val channelIdItem: String? = null
    protected open val mediaUrl: String? = null
    protected open val playlistParamsItem: String? = null
    protected open val isLiveItem: Boolean? = null
    protected open val isUpcomingItem: Boolean? = null

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
            mediaItem._reloadPageKeyItem = Helpers.parseStr(split[0])
            mediaItem._titleItem = Helpers.parseStr(split[1])
            mediaItem._secondTitleItem = Helpers.parseStr(split[2])
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
        return _titleItem
    }

    fun setTitle(title: String?) {
        _titleItem = title
    }

    override fun getSecondTitle(): String? {
        return _secondTitleItem
    }

    fun setSecondTitle(details: String?) {
        _secondTitleItem = details
    }

    override fun getVideoId(): String? {
        return _videoIdItem
    }

    override fun getProductionDate(): String? {
        return publishedTime
    }

    override fun getCardImageUrl(): String? {
        return _cardThumbImageUrl
    }

    override fun getBackgroundImageUrl(): String? {
        return _cardThumbImageUrl
    }

    override fun getPlaylistParams(): String? {
        return _playlistParamsItem
    }

    fun setPlaylistParams(params: String?) {
        _playlistParamsItem = params
    }

    override fun getReloadPageKey(): String? {
        return _reloadPageKeyItem
    }

    override fun getDurationMs(): Int {
        return lengthText?.let { ServiceHelper.timeTextToMillis(it) } ?: -1
    }

    override fun getBadgeText(): String? {
        return badgeTextItem
    }

    override fun getPlaylistId(): String? {
        return _playlistIdItem
    }

    override fun getVideoUrl(): String? {
        return mediaUrl
    }

    override fun getChannelId(): String? {
        return _channelIdItem
    }

    fun setChannelId(channelId: String?) {
        _channelIdItem = channelId
    }

    override fun getPlaylistIndex(): Int {
        return _playlistIndexItem ?: -1
    }

    fun setPlaylistIndex(index: Int) {
        _playlistIndexItem = index
    }

    override fun isLive(): Boolean {
        return isLiveItem ?: false
    }

    override fun isUpcoming(): Boolean {
        return isUpcomingItem ?: false
    }

    override fun getPercentWatched(): Int {
        return -1
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
        return 4.0
    }

    // End Fake params

    open fun isEmpty(): Boolean {
        return title == null && cardImageUrl == null
    }

    override fun sync(metadata: MediaItemMetadata?) {
        if (metadata == null) {
            return
        }
        title = metadata.title
        secondTitle = metadata.secondTitle
        channelId = metadata.channelId
    }

    override fun toString(): String {
        return String.format("%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s&mi;%s", reloadPageKey, title, secondTitle, cardImageUrl, videoId, playlistId, channelId)
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

package com.liskovsoft.youtubeapi.common.models.impl.mediaitem

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import kotlin.math.abs

open class BaseMediaItem : MediaItem {
    private var _titleItem: String? = null
        get() = field ?: titleItem
    private var _secondTitleItem: CharSequence? = null
        get() = field ?: secondTitleItem
    private var _channelIdItem: String? = null
        get() = field ?: channelIdItem
    private var _reloadPageKeyItem: String? = null
        get() = field ?: reloadPageKeyItem
    private var _cardThumbImageUrl: String? = null
        get() = field ?: cardThumbImageUrl
    private var _backgroundThumbImageUrl: String? = null
        get() = field ?: backgroundThumbImageUrl
    private var _videoIdItem: String? = null
        get() = field ?: videoIdItem
    private var _playlistIdItem: String? = null
        get() = field ?: playlistIdItem
    private var _playlistIndexItem: Int? = null
        get() = field ?: playlistIndexItem
    private var _paramsItem: String? = null
        get() = field ?: playlistParamsItem
    private var _isShortsItem: Boolean? = null
        get() = field ?: isShortsItem

    //private val _id by lazy { videoId?.hashCode() ?: channelId?.hashCode() ?: sId++ }
    private val _id by lazy { hashCode() }
    // TODO: time conversion doesn't take into account locale specific delimiters
    private val durationMsItem by lazy { ServiceHelper.timeTextToMillis(lengthText ?: badgeTextItem) }

    protected open val reloadPageKeyItem: String? = null
    protected open val badgeTextItem: String? = null
    protected open val typeItem: Int = MediaItem.TYPE_VIDEO
    protected open val videoIdItem: String? = null
    protected open val titleItem: String? = null
    protected open val secondTitleItem: CharSequence? = null
    protected open val subTitle: CharSequence? = null
    protected open val userName: String? = null
    protected open val publishedTime: String? = null
    protected open val viewCountText: CharSequence? = null
    protected open val upcomingEventText: String? = null
    protected open val lengthText: String? = null
    protected open val cardThumbImageUrl: String? = null
    protected open val backgroundThumbImageUrl: String? = null
    protected open val previewUrl: String? = null
    protected open val playlistIdItem: String? = null
    protected open val playlistIndexItem: Int? = null
    protected open val channelIdItem: String? = null
    protected open val playlistParamsItem: String? = null
    protected open val isLiveItem: Boolean? = null
    protected open val isUpcomingItem: Boolean? = null
    protected open val isShortsItem: Boolean? = null
    protected open val isMovieItem: Boolean? = null
    protected open val feedbackTokenItem: String? = null
    protected open val feedbackTokenItem2: String? = null
    protected open val percentWatchedItem: Int? = null
    protected open val startTimeSecondsItem: Int? = null
    protected open val hasNewContentItem: Boolean? = null
    protected open val hasUploadsItem: Boolean? = null
    protected open val searchQueryItem: String? = null

    protected companion object {
        //var sId: Int = 0
        fun fromString(spec: String?): MediaItem? {
            if (spec == null) {
                return null
            }
            val split = spec.split("&mi;".toRegex()).toTypedArray()
            if (split.size != 7) {
                return null
            }
            val mediaItem = BaseMediaItem()
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

    override fun getSecondTitle(): CharSequence? {
        return _secondTitleItem
    }

    fun setSecondTitle(details: CharSequence?) {
        _secondTitleItem = details
    }

    override fun getVideoId(): String? {
        return _videoIdItem
    }

    override fun getProductionDate(): String? {
        return publishedTime
    }

    override fun getPublishedDate(): Long {
        return -1
    }

    override fun getCardImageUrl(): String? {
        return _cardThumbImageUrl
    }

    override fun getBackgroundImageUrl(): String? {
        return _backgroundThumbImageUrl
    }

    override fun getParams(): String? {
        return _paramsItem
    }

    fun setParams(params: String?) {
        _paramsItem = params
    }

    override fun getReloadPageKey(): String? {
        return _reloadPageKeyItem
    }

    override fun getDurationMs(): Long {
        return durationMsItem
    }

    override fun getBadgeText(): String? {
        return badgeTextItem
    }

    override fun getPlaylistId(): String? {
        return _playlistIdItem
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

    override fun isShorts(): Boolean {
        return _isShortsItem ?: false
    }

    fun setShorts(isShorts: Boolean) {
        _isShortsItem = isShorts
    }

    override fun isMovie(): Boolean {
        return isMovieItem ?: false
    }

    override fun hasNewContent(): Boolean {
        return hasNewContentItem ?: false
    }

    override fun hasUploads(): Boolean {
        return hasUploadsItem ?: false
    }

    override fun getPercentWatched(): Int {
        return percentWatchedItem ?: -1
    }

    override fun getStartTimeSeconds(): Int {
        return startTimeSecondsItem ?: -1
    }

    override fun getAuthor(): String? {
        return null
    }

    override fun getFeedbackToken(): String? {
        return feedbackTokenItem
    }

    override fun getFeedbackToken2(): String? {
        return feedbackTokenItem2
    }

    override fun getContentType(): String? {
        return null
    }

    override fun getVideoPreviewUrl(): String? {
        return previewUrl
    }

    override fun getClickTrackingParams(): String? {
        return null
    }

    override fun getSearchQuery(): String? {
        return searchQueryItem
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

    fun sync(metadata: MediaItemMetadata?) {
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
        if (other is MediaItem) {
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
        //return Helpers.hashCode(videoId, playlistId, channelId, reloadPageKey)
        val hashCodeAny = YouTubeHelper.hashCodeAny(this)
        return if (hashCodeAny != -1) hashCodeAny else super.hashCode()
    }
}

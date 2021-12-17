package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.helpers.*
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.NextMediaItemImpl
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult) : MediaItemMetadata {
    private val channelIdItem by lazy {
        videoDetails?.getChannelId() ?: videoOwner?.getChannelId()
    }
    private val percentWatchedItem by lazy {
        videoMetadata?.getPercentWatched() ?: 0
    }
    private val suggestedSections by lazy {
        watchNextResult.getSuggestedSections()
    }
    private val videoMetadata by lazy {
        watchNextResult.getVideoMetadata()
    }
    private val nextVideoItem by lazy {
        watchNextResult.getNextVideoItem()
    }
    private val videoOwner by lazy {
        videoMetadata?.getVideoOwner()
    }
    private val videoDetails by lazy {
        watchNextResult.getVideoDetails()
    }
    private val nextMediaItem by lazy {
        nextVideoItem?.let { NextMediaItemImpl(it) }
    }
    private val isSubscribedItem by lazy {
        videoOwner?.isSubscribed() ?: false
    }
    private val replayItemWrapper by lazy {
        watchNextResult.getReplayItemWrapper()
    }
    private val buttonStateItem by lazy {
        watchNextResult.getButtonStateItem()
    }
    private val videoTitle by lazy {
        videoMetadata?.getTitle()
    }
    private val isUpcomingItem by lazy {
        videoMetadata?.isUpcoming() ?: false
    }
    private val likeStatusItem by lazy {
        when (videoMetadata?.getLikeStatus()) {
            LIKE_STATUS_LIKE -> MediaItemMetadata.LIKE_STATUS_LIKE
            LIKE_STATUS_DISLIKE -> MediaItemMetadata.LIKE_STATUS_DISLIKE
            LIKE_STATUS_INDIFFERENT -> MediaItemMetadata.LIKE_STATUS_INDIFFERENT
            else -> {
                when {
                    watchNextResult.transportControls?.transportControlsRenderer?.isLikeToggled() == true -> MediaItemMetadata.LIKE_STATUS_LIKE
                    watchNextResult.transportControls?.transportControlsRenderer?.isDislikeToggled() == true -> MediaItemMetadata.LIKE_STATUS_DISLIKE
                    else -> MediaItemMetadata.LIKE_STATUS_INDIFFERENT
                }
            }
        }
    }
    private val videoFullDescription by lazy { videoMetadata?.description?.getText() }
    private val videoDescription by lazy {
        YouTubeMediaServiceHelper.createDescription(
                videoAuthor, viewCountText, publishedTime,
                if (isLiveStream) "LIVE" else ""
        )
    }
    private val videoDescriptionAlt by lazy {
        YouTubeMediaServiceHelper.createDescription(
                videoAuthor, viewCountText, publishedDate,
                if (isLiveStream) "LIVE" else ""
        )
    }
    private val videoAuthor by lazy { videoDetails?.getUserName() }
    private val suggestionList by lazy {
        val list = suggestedSections?.mapNotNull { if (it?.getItemWrappers() != null) MediaGroupImpl(it) else null }
        if (list?.size ?: 0 > 0)
            list
        else
            // In rare cases first chip item contains all shelfs
            suggestedSections?.firstOrNull()?.getChipItems()?.firstOrNull()?.run {
                val chipTitle = getTitle() // shelfs inside a chip aren't have a titles
                getShelfItems()?.map { it?.let { MediaGroupImpl(it).apply { title = title ?: chipTitle } } }
            }
    }

    private val viewCountText by lazy {
        videoMetadata?.getViewCountText()
    }

    private val publishedTime by lazy {
        videoMetadata?.getPublishedTime()
    }

    private val publishedDateText by lazy {
        videoMetadata?.getDateText() ?: videoDetails?.getPublishedTimeText()
    }

    private val videoIdItem by lazy {
        videoMetadata?.videoId
    }

    private val isLiveStream by lazy {
        videoMetadata?.isLive() ?: false
    }

    override fun getTitle(): String? {
        return videoTitle
    }

    override fun getDescription(): String? {
        return videoDescription
    }

    override fun getDescriptionAlt(): String? {
        return videoDescriptionAlt
    }

    override fun getFullDescription(): String? {
        return videoFullDescription;
    }

    override fun getAuthor(): String? {
        return videoAuthor
    }

    override fun getViewCount(): String? {
        return viewCountText
    }

    override fun getPublishedDate(): String? {
        return publishedDateText
    }

    override fun getVideoId(): String? {
        return videoIdItem
    }

    override fun getNextVideo(): MediaItem? {
        return nextMediaItem
    }

    override fun isSubscribed(): Boolean {
        return isSubscribedItem
    }

    override fun isLive(): Boolean {
        return isLiveStream
    }

    override fun isUpcoming(): Boolean {
        return isUpcomingItem
    }

    override fun getChannelId(): String? {
        return channelIdItem
    }

    override fun getPercentWatched(): Int {
        return percentWatchedItem
    }

    override fun getLikeStatus(): Int {
        return likeStatusItem
    }

    override fun getSuggestions(): List<MediaGroup?>? {
        return suggestionList
    }

    override fun getLikesCount(): String? {
        return null
    }

    override fun getDislikesCount(): String? {
        return null
    }
}

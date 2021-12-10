package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.NextMediaItemImpl
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.helpers.*
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult) : MediaItemMetadata {
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
        suggestedSections?.map { it?.let { MediaGroupImpl(it) } }
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

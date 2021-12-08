package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.next.v2.helpers.getText
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.NextMediaItemImpl
import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

// TODO: implement full conversion
data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult) : MediaItemMetadata {
    private val suggestedSections by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.pivot?.pivot?.contents?.map { it?.shelfRenderer }
    }
    private val videoMetadata by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.results?.results?.contents?.getOrNull(0)?.itemSectionRenderer?.contents?.map {
            it?.videoMetadataRenderer ?: it?.musicWatchMetadataRenderer
        }?.firstOrNull()
    }
    private val nextVideoItem by lazy {
        val nextVideoRenderer = watchNextResult.contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.sets?.getOrNull(0)?.nextVideoRenderer
        nextVideoRenderer?.maybeHistoryEndpointRenderer ?: nextVideoRenderer?.autoplayEndpointRenderer
    }
    private val nextVideo by lazy {
        nextVideoItem?.let { NextMediaItemImpl(it) }
    }
    private val replayItemWrapper by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.replayVideoRenderer
    }
    private val buttonStateItem by lazy {
        watchNextResult.transportControls?.transportControlsRenderer
    }
    private val videoOwner by lazy {
        videoMetadata?.owner?.videoOwnerRenderer
    }
    private val videoTitle by lazy {
        val textItem = videoMetadata?.title
        textItem?.getText()
    }
    private val videoFullDescription by lazy { videoMetadata?.description?.getText() }
    private val videoDescription by lazy {
        YouTubeMediaServiceHelper.createDescription(
                videoAuthor, publishedTime, _viewCount,
                if (_isLive == true) "LIVE" else ""
        )
    }
    private val videoDescriptionAlt by lazy {
        YouTubeMediaServiceHelper.createDescription(
                videoAuthor, publishedDate, _viewCount,
                if (_isLive == true) "LIVE" else ""
        )
    }
    private val videoAuthor by lazy { videoMetadata?.title?.getText() }
    private val suggestionList by lazy {
        suggestedSections?.map { it?.let { MediaGroupImpl(it) } }
    }

    private val _viewCount by lazy {
        val viewCount1 = videoMetadata?.viewCount?.videoViewCountRenderer?.viewCount;
        val viewCount2 = videoMetadata?.viewCountText;
        viewCount1?.getText() ?: viewCount2?.getText()
    }

    private val publishedTime by lazy {
        videoMetadata?.publishedTimeText ?: videoMetadata?.albumName
    }

    private val videoDetails by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.replayVideoRenderer?.pivotVideoRenderer
    }

    private val _publishedDate by lazy {
        videoMetadata?.dateText?.getText() ?: videoDetails?.publishedTimeText?.getText()
    }

    private val _videoId by lazy {
        videoMetadata?.videoId
    }

    private val _isLive by lazy {
        videoMetadata?.viewCount?.videoViewCountRenderer?.isLive
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
        return _viewCount
    }

    override fun getPublishedDate(): String? {
        return _publishedDate
    }

    override fun getVideoId(): String? {
        return _videoId
    }

    override fun getNextVideo(): MediaItem? {
        return nextVideo
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

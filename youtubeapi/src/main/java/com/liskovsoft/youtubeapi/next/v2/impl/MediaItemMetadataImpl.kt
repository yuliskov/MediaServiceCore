package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.mediaserviceinterfaces.data.VideoPlaylistInfo
import com.liskovsoft.youtubeapi.common.models.kt.*
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.gen.kt.*
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.NextMediaItemImpl
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper

data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult) : MediaItemMetadata {
    private val channelIdItem by lazy {
        videoDetails?.getChannelId() ?: videoOwner?.getChannelId() ?: channelOwner?.getChannelId()
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
    private val liveChatKeyItem by lazy {
        watchNextResult.getLiveChatKey()
    }
    private val videoOwner by lazy {
        videoMetadata?.getVideoOwner()
    }
    private val channelOwner by lazy {
        watchNextResult.transportControls?.transportControlsRenderer?.getChannelOwner()
    }
    private val videoDetails by lazy {
        watchNextResult.getVideoDetails()
    }
    private val nextMediaItem by lazy {
        nextVideoItem?.let { NextMediaItemImpl(it) }
    }
    private val isSubscribedItem by lazy {
        videoOwner?.isSubscribed() ?: channelOwner?.isSubscribed() ?: false
    }
    private val paramsItem by lazy {
        videoOwner?.getParams() ?: channelOwner?.getParams()
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
    private val videoDescription by lazy { videoMetadata?.description?.getText() ?:
        // Scroll to the end till we find description tile
        suggestionList?.lastOrNull()?.shelf?.getItemWrappers()?.firstOrNull()?.getDescriptionText()
    }
    private val videoSecondTitle by lazy {
        YouTubeHelper.createInfo(
                videoAuthor, viewCountText, publishedTime
        )
    }
    private val videoSecondTitleAlt by lazy {
        YouTubeHelper.createInfo(
                videoAuthor, viewCountText, publishedDate
        )
    }
    private val videoAuthor by lazy { videoDetails?.getUserName() }
    private val videoAuthorImageUrl by lazy { (videoOwner?.getThumbnails() ?: channelOwner?.getThumbnails())?.findOptimalResThumbnailUrl() }
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
        videoMetadata?.videoId ?: videoDetails?.videoId
    }

    private val isLiveStream by lazy {
        videoMetadata?.isLive() ?: false
    }

    private val playlistInfoItem by lazy {
        watchNextResult.getPlaylistInfo()?.let {
            object: VideoPlaylistInfo {
                override fun getTitle() = it.title
                override fun getPlaylistId() = it.playlistId
                override fun isSelected() = false
                override fun getSize() = it.totalVideos ?: -1
                override fun getCurrentIndex() = it.currentIndex ?: -1
            }
        }
    }

    override fun getTitle(): String? {
        return videoTitle
    }

    override fun getSecondTitle(): String? {
        return videoSecondTitle
    }

    override fun getSecondTitleAlt(): String? {
        return videoSecondTitleAlt
    }

    override fun getDescription(): String? {
        return videoDescription;
    }

    override fun getAuthor(): String? {
        return videoAuthor
    }

    override fun getAuthorImageUrl(): String? {
        return videoAuthorImageUrl
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

    override fun getParams(): String? {
        return paramsItem
    }

    override fun isLive(): Boolean {
        return isLiveStream
    }

    override fun getLiveChatKey(): String? {
        return liveChatKeyItem
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

    override fun getPlaylistInfo(): VideoPlaylistInfo? {
        return playlistInfoItem
    }

    override fun getLikesCount(): String? {
        return null
    }

    override fun getDislikesCount(): String? {
        return null
    }
}

package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.*
import com.liskovsoft.mediaserviceinterfaces.data.ChapterItem
import com.liskovsoft.mediaserviceinterfaces.data.NotificationState
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.SuggestionsGroup
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.NextMediaItem
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.ShuffleMediaItem
import com.liskovsoft.youtubeapi.next.v2.gen.*
import com.liskovsoft.youtubeapi.notifications.NotificationStateImplWrapper

internal data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult,
                                 val suggestionsResult: WatchNextResult? = null) : MediaItemMetadata {
    private val channelIdItem by lazy {
        videoDetails?.getChannelId() ?: videoOwner?.getChannelId() ?: channelOwner?.getChannelId()
    }
    private val percentWatchedItem by lazy {
        videoMetadata?.getPercentWatched() ?: 0
    }
    private val suggestedSections by lazy {
        (suggestionsResult ?: watchNextResult).getSuggestedSections()
    }
    private val videoMetadata by lazy {
        watchNextResult.getVideoMetadata()
    }
    private val commentsPanel by lazy {
        watchNextResult.getCommentPanel()
    }
    private val descriptionPanel by lazy {
        watchNextResult.getDescriptionPanel()
    }
    private val collaboratorPanel by lazy {
        watchNextResult.getCollaboratorPanel()
    }
    private val liveChatKeyItem by lazy {
        watchNextResult.getLiveChatToken()
    }
    private val commentsKeyItem: String? by lazy {
        commentsPanel?.getTopCommentsToken()
    }
    private val videoOwner by lazy {
        videoMetadata?.getVideoOwner()
    }
    private val channelOwner by lazy {
        watchNextResult.getButtonStateItem()?.getChannelOwner()
    }
    private val notificationPreference by lazy {
        videoOwner?.getNotificationPreference()
    }
    private val videoDetails by lazy {
        watchNextResult.getVideoDetails()
    }
    private val nextMediaItem by lazy {
        watchNextResult.getNextVideoItem()?.let { NextMediaItem(it) }
    }
    private val shuffleMediaItem by lazy {
        watchNextResult.getShuffleVideoItem()?.let { ShuffleMediaItem(it) }
    }
    var isSubscribedOverrideItem: Boolean? = null
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
        videoDetails?.getTitle() ?: videoMetadata?.getTitle()
    }
    private val isUpcomingItem by lazy {
        videoMetadata?.isUpcoming() ?: false
    }
    private val likeStatusItem by lazy {
        when (videoMetadata?.getLikeStatus() ?: watchNextResult.getButtonStateItem()?.getLikeStatus()) {
            LIKE_STATUS_LIKE -> MediaItemMetadata.LIKE_STATUS_LIKE
            LIKE_STATUS_DISLIKE -> MediaItemMetadata.LIKE_STATUS_DISLIKE
            LIKE_STATUS_INDIFFERENT -> MediaItemMetadata.LIKE_STATUS_INDIFFERENT
            else -> {
                when {
                    watchNextResult.getButtonStateItem()?.isLikeToggled() == true -> MediaItemMetadata.LIKE_STATUS_LIKE
                    watchNextResult.getButtonStateItem()?.isDislikeToggled() == true -> MediaItemMetadata.LIKE_STATUS_DISLIKE
                    else -> MediaItemMetadata.LIKE_STATUS_INDIFFERENT
                }
            }
        }
    }
    private val videoDescription by lazy {
        descriptionPanel?.getDescriptionText() ?:
        videoMetadata?.description?.getText() ?:
        // Scroll to the end till we find description tile
        suggestionList?.lastOrNull()?.shelf?.getItemWrappers()?.firstOrNull()?.getDescriptionText()
    }
    private val videoSecondTitle by lazy {
        YouTubeHelper.createInfo(
            videoAuthor, albumName, viewCountText, publishedTime
        )
    }
    private val videoAuthor by lazy { videoDetails?.getUserName() }
    private val subscriberCountItem by lazy { videoOwner?.getSubscriberCount() ?: collaboratorPanel?.getSubscribersCount() }
    private val videoAuthorImageUrl by lazy {
        (videoOwner?.getThumbnails() ?: channelOwner?.getThumbnails() ?: collaboratorPanel?.getThumbnails())?.getOptimalResThumbnailUrl()
    }
    private val suggestionList by lazy {
        // NOTE: the result also contains unnamed sections (new suggestions type)
        val list = suggestedSections
            ?.filter { it.getItemWrappers() != null }
            ?.mapIndexed { idx, it -> SuggestionsGroup(it).apply {
                // Replace "Up Next" with real playlist name
                if (idx == 0 && playlistInfo?.title != null) {
                    title = playlistInfo?.title
                }
                // Unnamed sections have space in names " ". Remove spaces to improve further parsing
                title = title?.trim()
            }}
        if (list?.isNotEmpty() == true)
            list
        else
            // In rare cases first chip item contains all shelfs
            suggestedSections?.firstOrNull()?.getChipItems()?.firstOrNull()?.run {
                val chipTitle = getTitle() // shelfs inside a chip aren't have a titles
                getShelfItems()?.map { it?.let { SuggestionsGroup(it).apply { title = title ?: chipTitle } } }
            }
    }

    private val albumName by lazy { videoMetadata?.getAlbumName() }

    private val viewCountText by lazy {
        videoMetadata?.getViewCountText() ?: videoMetadata?.getLongViewCountText()
    }

    private val publishedTime by lazy {  // absolute date (e.g 1 september 2019)
        videoMetadata?.getPublishedTime() ?: descriptionPanel?.getPublishDate()
    }

    private val publishedDateText by lazy { // relative date (e.g. 1 hour ago)
        videoMetadata?.getDateText() ?: videoDetails?.getPublishedTimeText()
    }

    private val videoIdItem by lazy {
        videoMetadata?.videoId ?: videoDetails?.videoId
    }

    private val isLiveStream by lazy {
        videoMetadata?.isLive() ?: (liveChatKeyItem != null)
    }

    private val playlistInfoItem by lazy {
        watchNextResult.getPlaylistInfo()?.let {
            object: PlaylistInfo {
                override fun getTitle() = it.title
                override fun getPlaylistId() = it.playlistId
                override fun isSelected() = false
                override fun getSize() = it.totalVideos ?: -1
                override fun getCurrentIndex() = it.currentIndex ?: -1
            }
        }
    }

    private val chapterList by lazy {
        watchNextResult.getChapters()?.map {
            object: ChapterItem {
                override fun getTitle() = it?.getTitle()
                override fun getStartTimeMs() = it?.getStartTimeMs() ?: -1
                override fun getCardImageUrl() = it?.getThumbnailUrl()
            }
        }
    }

    private val notificationStateList by lazy {
        val currentId = notificationPreference?.getCurrentStateId()
        val result = notificationPreference?.getItems()?.mapNotNull {
            it?.let { NotificationStateImplWrapper(it, currentId, channelId, params, isSubscribed) }
        }

        result?.forEach { it.allStates = result }

        result
    }

    private val likeCountItem by lazy {
        videoMetadata?.getLikeCountInt()?.let { ServiceHelper.prettyCount(it) } ?: descriptionPanel?.getLikeCount()
    }

    private val dislikeCountItem by lazy {
        // Fake count based on 'returnyoutubedislike' plugin algorithm
        videoMetadata?.getLikeCountInt()?.let { if (it > 0) ServiceHelper.prettyCount(it * 0.032) else null }
    }

    private val durationMsItem by lazy { ServiceHelper.timeTextToMillis(videoDetails?.getLengthText()) }

    private val badgeTextItem by lazy { videoDetails?.getLengthText() }

    override fun getTitle(): String? {
        return videoTitle
    }

    override fun getSecondTitle(): CharSequence? {
        return videoSecondTitle
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
        return publishedTime
    }

    override fun getVideoId(): String? {
        return videoIdItem
    }

    override fun getNextVideo(): MediaItem? {
        return nextMediaItem
    }

    override fun getShuffleVideo(): MediaItem? {
        return shuffleMediaItem
    }

    override fun isSubscribed(): Boolean {
        return isSubscribedOverrideItem ?: isSubscribedItem
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

    override fun getCommentsKey(): String? {
        return commentsKeyItem
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

    override fun getLikeCount(): String? {
        return likeCountItem
    }

    override fun getDislikeCount(): String? {
        return dislikeCountItem
    }

    override fun getSubscriberCount(): String? {
        return subscriberCountItem
    }

    override fun getSuggestions(): List<MediaGroup?>? {
        return suggestionList
    }

    override fun getPlaylistInfo(): PlaylistInfo? {
        return playlistInfoItem
    }

    override fun getChapters(): List<ChapterItem>? {
        return chapterList
    }

    override fun getNotificationStates(): List<NotificationState?>? {
        return notificationStateList
    }

    override fun getDurationMs(): Long {
        return durationMsItem
    }

    override fun getBadgeText(): String? {
        return badgeTextItem
    }
}

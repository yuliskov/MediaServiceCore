package com.liskovsoft.youtubeapi.common.models.impl.mediaitem

import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.next.v2.gen.*
import com.liskovsoft.youtubeapi.notifications.gen.*

internal data class WrapperMediaItem(var itemWrapper: ItemWrapper): BaseMediaItem() {
    override val typeItem by lazy { itemWrapper.getType() }
    override val videoIdItem by lazy { itemWrapper.getVideoId() }
    override val titleItem by lazy { itemWrapper.getTitle() }
    // Don't tag live streams. However tagging 4K videos is useful.
    override val secondTitleItem by lazy {
        YouTubeHelper.createInfo(if (isLiveItem == true) null else descBadgeText, userName, viewCountText, publishedTime, upcomingEventText)
    }
    override val descBadgeText by lazy { itemWrapper.getDescBadgeText() }
    override val userName by lazy { itemWrapper.getUserName() }
    override val publishedTime by lazy { itemWrapper.getPublishedTime() }
    override val viewCountText by lazy { itemWrapper.getViewCountText() }
    override val upcomingEventText by lazy { itemWrapper.getUpcomingEventText() }
    override val cardThumbImageUrl by lazy { itemWrapper.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { itemWrapper.getThumbnails()?.getHighResThumbnailUrl() }
    override val previewUrl: String? by lazy { itemWrapper.getMovingThumbnails()?.getOptimalResThumbnailUrl() }
    override val playlistIdItem by lazy { itemWrapper.getPlaylistId() }
    override val playlistIndexItem by lazy { itemWrapper.getPlaylistIndex() }
    override val badgeTextItem by lazy { itemWrapper.getBadgeText() }
    override val lengthText by lazy { itemWrapper.getLengthText() }
    override val channelIdItem by lazy { itemWrapper.getChannelId() }
    override val isLiveItem by lazy { itemWrapper.isLive() }
    override val isUpcomingItem by lazy { itemWrapper.isUpcoming() }
    override val isShortsItem by lazy { itemWrapper.isShorts() }
    override val isMovieItem by lazy { itemWrapper.isMovie() }
    override val feedbackTokenItem by lazy { itemWrapper.getFeedbackToken() }
    override val feedbackTokenItem2 by lazy { itemWrapper.getFeedbackToken2() }
    override val mediaUrl by lazy { ServiceHelper.videoIdToFullUrl(videoIdItem) ?: null }
    override val percentWatchedItem by lazy { itemWrapper.getPercentWatched() }
    //override val playlistParamsItem by lazy { itemWrapper.getParams() }
    val descriptionText by lazy { itemWrapper.getDescriptionText() }
}

internal data class NextMediaItem(var nextVideoItem: NextVideoItem): BaseMediaItem() {
    override val videoIdItem by lazy { nextVideoItem.getVideoId() }
    override val channelIdItem: String? = null
    override val titleItem by lazy { nextVideoItem.getTitle() }
    override val secondTitleItem by lazy { YouTubeHelper.createInfo(nextVideoItem.getAuthor()) ?: null }
    override val cardThumbImageUrl by lazy { nextVideoItem.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { nextVideoItem.getThumbnails()?.getHighResThumbnailUrl() }
    override val playlistIdItem by lazy { nextVideoItem.getPlaylistId() }
    override val playlistIndexItem by lazy { nextVideoItem.getPlaylistIndex() }
    override val mediaUrl by lazy { ServiceHelper.videoIdToFullUrl(videoIdItem) ?: null }
    override val playlistParamsItem by lazy { nextVideoItem.getParams() }
}

internal data class GuideMediaItem(val guideItem: GuideItem): BaseMediaItem() {
    override val titleItem by lazy { guideItem.getTitle() }
    override val channelIdItem by lazy { guideItem.getBrowseId() }
    override val cardThumbImageUrl by lazy { guideItem.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { guideItem.getThumbnails()?.getHighResThumbnailUrl() }
    override val hasNewContentItem by lazy { guideItem.hasNewContent() }
    override val hasUploadsItem = true
}

internal data class ShortsMediaItem(val reel: ReelWatchEndpoint?, val reelDetails: ReelResult): BaseMediaItem() {
    override val videoIdItem by lazy { reel?.getVideoId() ?: reelDetails.getVideoId() }
    override val cardThumbImageUrl by lazy { getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { getThumbnails()?.getHighResThumbnailUrl() }
    override val titleItem by lazy { reelDetails.getTitle() }
    override val secondTitleItem by lazy { reelDetails.getSubtitle() }
    override val channelIdItem by lazy { reelDetails.getBrowseId() }
    override val isShortsItem by lazy { true }
    override val feedbackTokenItem by lazy { reelDetails.getFeedbackTokens()?.getOrNull(0) }
    override val feedbackTokenItem2 by lazy { reelDetails.getFeedbackTokens()?.getOrNull(1) }
    private fun getThumbnails() = reel?.getThumbnails() ?: reelDetails.getThumbnails()
}

internal data class NotificationMediaItem(private val item: NotificationItem): BaseMediaItem() {
    override val videoIdItem by lazy { item.getVideoId() }
    override val cardThumbImageUrl by lazy { item.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { item.getThumbnails()?.getHighResThumbnailUrl() }
    override val titleItem by lazy { item.getTitle() }
    override val secondTitleItem by lazy { item.getSecondTitle() }
    val hideNotificationToken: String? by lazy { item.getNotificationToken() }
}

package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper

data class MediaItemImpl(var itemWrapper: ItemWrapper): BaseMediaItemImpl() {
    override val typeItem by lazy { itemWrapper.getType() }
    override val videoIdItem by lazy { itemWrapper.getVideoId() }
    override val titleItem by lazy { itemWrapper.getTitle() }
    // Don't tag live streams. However tagging 4K videos is useful.
    override val infoItem by lazy {
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
    override val isMovieItem by lazy { itemWrapper.isMovie() }
    override val feedbackTokenItem by lazy { itemWrapper.getFeedbackToken() }
    override val feedbackTokenItem2 by lazy { itemWrapper.getFeedbackToken2() }
    override val mediaUrl by lazy { ServiceHelper.videoIdToFullUrl(videoIdItem) ?: null }
    override val percentWatchedItem by lazy { itemWrapper.getPercentWatched() }
    //override val playlistParamsItem by lazy { itemWrapper.getParams() }
    val descriptionText by lazy { itemWrapper.getDescriptionText() }
}

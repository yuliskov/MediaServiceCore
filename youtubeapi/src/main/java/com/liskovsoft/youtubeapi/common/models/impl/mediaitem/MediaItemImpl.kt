package com.liskovsoft.youtubeapi.common.models.impl.mediaitem

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.next.v2.gen.*
import com.liskovsoft.youtubeapi.notifications.gen.*

internal class WrapperMediaItem(private val itemWrapper: ItemWrapper): BaseMediaItem() {
    override val typeItem by lazy { itemWrapper.getType() }
    override val videoIdItem by lazy { itemWrapper.getVideoId() }
    override val titleItem by lazy { itemWrapper.getTitle() }
    override val secondTitleItem by lazy {
        YouTubeHelper.createInfo(if (isLiveItem && !Helpers.allNulls(userName, viewCountText, publishedTime, upcomingEventText)) null else
            subTitle, userName, viewCountText, publishedTime, upcomingEventText)
    }
    override val subTitle by lazy { itemWrapper.getSubTitle() } // quality tag (e.g. 4K, LIVE) or full second title
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
    override val playlistParamsItem by lazy { itemWrapper.getChannelParams() }
    override val isLiveItem by lazy { itemWrapper.isLive() }
    override val isUpcomingItem by lazy { itemWrapper.isUpcoming() }
    override val isShortsItem by lazy { itemWrapper.isShorts() }
    override val isMovieItem by lazy { itemWrapper.isMovie() }
    override val feedbackTokenItem by lazy { itemWrapper.getFeedbackToken() }
    override val feedbackTokenItem2 by lazy { itemWrapper.getFeedbackToken2() }
    override val percentWatchedItem by lazy { itemWrapper.getPercentWatched() }
    override val startTimeSecondsItem by lazy { itemWrapper.getStartTimeSeconds() }
    override val searchQueryItem by lazy { itemWrapper.getQuery() }
    //override val playlistParamsItem by lazy { itemWrapper.getParams() }
    val descriptionText by lazy { itemWrapper.getDescriptionText() }
}

internal class NextMediaItem(private val nextVideoItem: NextVideoItem): BaseMediaItem() {
    override val videoIdItem by lazy { nextVideoItem.getVideoId() }
    override val channelIdItem: String? = null
    override val titleItem by lazy { nextVideoItem.getTitle() }
    override val secondTitleItem by lazy { YouTubeHelper.createInfo(nextVideoItem.getAuthor()) }
    override val cardThumbImageUrl by lazy { nextVideoItem.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { nextVideoItem.getThumbnails()?.getHighResThumbnailUrl() }
    override val playlistIdItem by lazy { nextVideoItem.getPlaylistId() }
    override val playlistIndexItem by lazy { nextVideoItem.getPlaylistIndex() }
    override val playlistParamsItem by lazy { nextVideoItem.getParams() }
}

internal class ShuffleMediaItem(private val navigationEndpointItem: NavigationEndpointItem): BaseMediaItem() {
    override val videoIdItem by lazy { navigationEndpointItem.getVideoId() }
    override val channelIdItem: String? = null
    override val titleItem by lazy { navigationEndpointItem.getTitle() }
    override val playlistIdItem by lazy { navigationEndpointItem.getPlaylistId() }
    override val playlistParamsItem by lazy { navigationEndpointItem.getParams() }
}

internal class GuideMediaItem(private val guideItem: GuideItem): BaseMediaItem() {
    override val titleItem by lazy { guideItem.getTitle() }
    override val channelIdItem by lazy { guideItem.getBrowseId() }
    override val cardThumbImageUrl by lazy { guideItem.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { guideItem.getThumbnails()?.getHighResThumbnailUrl() }
    override val hasNewContentItem by lazy { guideItem.hasNewContent() }
    override val hasUploadsItem = true
}

internal class TabMediaItem(private val tabItem: TabRenderer, private val groupType: Int): BaseMediaItem() {
    override val titleItem by lazy { tabItem.getTitle() }
    override val reloadPageKeyItem by lazy { tabItem.getReloadToken() }
    //override val channelIdItem by lazy { tabItem.getBrowseId() }
    //override val playlistParamsItem by lazy { tabItem.getBrowseParams() }
    override val cardThumbImageUrl by lazy { tabItem.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { tabItem.getThumbnails()?.getHighResThumbnailUrl() }
    override val hasNewContentItem by lazy { tabItem.hasNewContent() }
    override val hasUploadsItem = true
    override val typeItem = groupType
}

internal class ShortsMediaItem(private val reel: ReelWatchEndpoint?, private val reelDetails: ReelResult): BaseMediaItem() {
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

internal class NotificationMediaItem(private val item: NotificationItem): BaseMediaItem() {
    override val videoIdItem by lazy { item.getVideoId() }
    override val cardThumbImageUrl by lazy { item.getThumbnails()?.getOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { item.getThumbnails()?.getHighResThumbnailUrl() }
    override val titleItem by lazy { item.getTitle() }
    override val secondTitleItem by lazy { YouTubeHelper.createInfo(item.getUserName(), item.getPublishedTime()) }
    val hideNotificationToken: String? by lazy { item.getNotificationToken() }
}

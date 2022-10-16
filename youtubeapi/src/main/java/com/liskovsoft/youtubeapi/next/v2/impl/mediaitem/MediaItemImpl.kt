package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.youtubeapi.common.models.kt.*
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper

data class MediaItemImpl(var itemWrapper: ItemWrapper): BaseMediaItemImpl() {
    override val typeItem by lazy { itemWrapper.getType() }
    override val videoIdItem by lazy { itemWrapper.getVideoId() }
    override val titleItem by lazy { itemWrapper.getTitle() }
    override val infoItem by lazy { YouTubeHelper.createInfo(descBadgeText, userName, viewCountText, publishedTime, upcomingEventText) ?: null }
    override val descBadgeText by lazy { itemWrapper.getDescBadgeText() }
    override val userName by lazy { itemWrapper.getUserName() }
    override val publishedTime by lazy { itemWrapper.getPublishedTime() }
    override val viewCountText by lazy { itemWrapper.getViewCountText() }
    override val upcomingEventText by lazy { itemWrapper.getUpcomingEventText() }
    override val cardThumbImageUrl by lazy { itemWrapper.getThumbnails()?.findOptimalResThumbnailUrl() }
    override val backgroundThumbImageUrl by lazy { itemWrapper.getThumbnails()?.findHighResThumbnailUrl() }
    override val playlistIdItem by lazy { itemWrapper.getPlaylistId() }
    override val playlistIndexItem by lazy { itemWrapper.getPlaylistIndex() }
    override val badgeTextItem by lazy { itemWrapper.getBadgeText() }
    override val lengthText by lazy { itemWrapper.getLengthText() }
    override val channelIdItem by lazy { itemWrapper.getChannelId() }
    override val isLiveItem by lazy { itemWrapper.isLive() }
    override val isUpcomingItem by lazy { itemWrapper.isUpcoming() }
    val descriptionText by lazy { itemWrapper.getDescriptionText() }
}

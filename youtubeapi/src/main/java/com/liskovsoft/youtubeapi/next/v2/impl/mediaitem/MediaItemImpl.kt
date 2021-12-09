package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.youtubeapi.next.v2.helpers.*
import com.liskovsoft.youtubeapi.next.v2.result.gen.ItemWrapper
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

data class MediaItemImpl(var itemWrapper: ItemWrapper): BaseMediaItemImpl() {
    override val typeItem by lazy { itemWrapper.getType() }
    override val videoIdItem by lazy { itemWrapper.videoId }
    override val titleItem by lazy { itemWrapper.title }
    override val descBadgeText by lazy { itemWrapper.descBadgeText }
    override val userName by lazy { itemWrapper.userName }
    override val publishedTime by lazy { itemWrapper.publishedTime }
    override val viewCountText by lazy { itemWrapper.viewCountText }
    override val upcomingEventText by lazy { itemWrapper.upcomingEventText }
    override val descriptionItem by lazy { YouTubeMediaServiceHelper.createDescription(descBadgeText, userName, publishedTime, viewCountText, upcomingEventText) ?: null }
    override val cardThumbImageUrl by lazy { itemWrapper.thumbnail?.findHighResThumbnailUrl() }
}

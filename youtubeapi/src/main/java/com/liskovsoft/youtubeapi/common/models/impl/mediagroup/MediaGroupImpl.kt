package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.getBrowseId
import com.liskovsoft.youtubeapi.common.models.gen.getParams
import com.liskovsoft.youtubeapi.common.models.gen.isLive
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.GuideMediaItem
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.NotificationMediaItem
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.TabMediaItem
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getItems
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.next.v2.gen.getShelves
import com.liskovsoft.youtubeapi.notifications.gen.NotificationsResult
import com.liskovsoft.youtubeapi.notifications.gen.getItems

/**
 *  Always renders first tab
 */
internal data class BrowseMediaGroup(
    private val browseResult: BrowseResult,
    private val options: MediaGroupOptions,
    private val liveResult: BrowseResult? = null
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?> =
        listOfNotNull(liveResult?.getLiveItems(), browseResult.getItems()).flatten()
    override fun getNextPageKeyInt(): String? = browseResult.getContinuationToken()
    override fun getTitleInt(): String? = browseResult.getTitle()
}

internal data class BrowseMediaGroupTV(
    private val browseResult: BrowseResultTV,
    private val options: MediaGroupOptions,
    private val overrideItems: List<ItemWrapper?>? = null,
    private val overrideKey: String? = null
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? =
        overrideItems?.sortedByDescending { it?.isLive() ?: false } ?: browseResult.getItems()
    override fun getNextPageKeyInt(): String? = if (overrideItems != null) overrideKey else browseResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

internal data class LiveMediaGroup(
    private val liveResult: BrowseResult,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?> = listOfNotNull(liveResult.getLiveItems(), liveResult.getPastLiveItems()).flatten()
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = liveResult.getTitle()
}

internal data class ContinuationMediaGroup(
    private val continuationResult: ContinuationResult,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = continuationResult.getItems()
    override fun getNextPageKeyInt(): String? = continuationResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

internal data class WatchNexContinuationMediaGroup(
    private val continuation: WatchNextResultContinuation,
    private val options: MediaGroupOptions,
    private val overrideItems: List<ItemWrapper?>? = null,
    private val overrideKey: String? = null
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = overrideItems?.sortedByDescending { it?.isLive() ?: false } ?: continuation.getItems() ?: getLastShelf()?.getItems()
    override fun getNextPageKeyInt(): String? = if (overrideItems != null) overrideKey else continuation.getContinuationToken() ?: getLastShelf()?.getContinuationToken()
    override fun getTitleInt(): String? = null
    private fun getLastShelf() = continuation.getShelves()?.lastOrNull() // Get main content of Channels section and skip SHORTS
}

internal data class RichSectionMediaGroup(
    private val richSectionRenderer: RichSectionRenderer,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = richSectionRenderer.getItems()
    override fun getNextPageKeyInt(): String? = richSectionRenderer.getContinuationToken()
    override fun getTitleInt(): String? = richSectionRenderer.getTitle()
}

internal data class ShelfSectionMediaGroup(
    private val shelf: Shelf,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = shelf.getItems()
    override fun getNextPageKeyInt(): String? = shelf.getContinuationToken()
    override fun getTitleInt(): String? = shelf.getTitle()
}

internal data class ItemSectionMediaGroup(
    private val itemSectionRenderer: ShelfListWrapper,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    // Fix row continuation (no next key but has channel) by reporting empty content (will be continued as a chip). Example https://www.youtube.com/@hdtvtest
    private val fixContinuation = nextPageKey == null && channelId != null
    override fun getItemWrappersInt(): List<ItemWrapper?>? = if (fixContinuation) null else itemSectionRenderer.getItems()
    override fun getNextPageKeyInt(): String? = if (fixContinuation) null else itemSectionRenderer.getContinuationToken()
    override fun getTitleInt(): String? = itemSectionRenderer.getTitle()
    override fun getChannelIdInt(): String? = itemSectionRenderer.getBrowseId()
    override fun getParamsInt(): String? = itemSectionRenderer.getParams()
}

internal data class TabMediaGroup(
    private val tabRenderer: TabRenderer,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = tabRenderer.getItems()
    override fun getNextPageKeyInt(): String? = tabRenderer.getContinuationToken()
    override fun getTitleInt(): String? = tabRenderer.getTitle()
    override fun getChannelIdInt(): String? = tabRenderer.endpoint?.getBrowseId()
    override fun getParamsInt(): String? = tabRenderer.endpoint?.getParams()
}

internal data class KidsSectionMediaGroup(
    private val anchoredSectionRenderer: AnchoredSectionRenderer,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = anchoredSectionRenderer.getItems()
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = anchoredSectionRenderer.getTitle()
}

internal data class ChipMediaGroup(
    private val chipCloudChipRenderer: ChipCloudChipRenderer,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = chipCloudChipRenderer.getContinuationToken()
    override fun getTitleInt(): String? = chipCloudChipRenderer.getTitle()
}

internal const val SORT_DEFAULT: Int = 0
internal const val SORT_BY_NAME: Int = 1
internal const val SORT_BY_NEW_CONTENT: Int = 2

internal data class GuideMediaGroup(
    private val guideResult: GuideResult,
    private val options: MediaGroupOptions,
    private val sort: Int = SORT_DEFAULT
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = null
    override val mediaItemList by lazy {
        val result = mutableListOf<MediaItem>()

        guideResult.getFirstSubs()?.forEach {
            it?.let { if (it.thumbnail != null) result.add(GuideMediaItem(it)) } // exclude 'special' items
        }

        guideResult.getCollapsibleSubs()?.forEach {
            it?.let { if (it.thumbnail != null) result.add(GuideMediaItem(it)) } // exclude 'special' items
        }

        if (sort == SORT_BY_NAME) result.sortBy { it.title?.lowercase() }

        result
    }
}

internal data class ChannelListMediaGroup(
    private val tabs: List<TabRenderer>,
    private val options: MediaGroupOptions,
    private val sortBy: Int = SORT_DEFAULT
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = null
    override val mediaItemList by lazy {
        val result = mutableListOf<MediaItem>()

        tabs.forEachIndexed { idx, it ->
            // Skip All subscriptions tab
            if (idx == 0 && it.getThumbnails() == null) {
                return@forEachIndexed
            }

            result.add(TabMediaItem(it, options.groupType))
        }

        if (sortBy == SORT_BY_NAME) result.sortBy { it.title?.lowercase() }

        if (sortBy == SORT_BY_NEW_CONTENT) {
            result.sortBy { it.title?.lowercase() }
            result.sortByDescending { it.hasNewContent() }
        }

        result
    }
}

internal data class RecommendedMediaGroup(
    private val guideItem: GuideItem,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = guideItem.getTitle()
    override fun getChannelIdInt(): String? = guideItem.getBrowseId()
    override fun getParamsInt(): String? = guideItem.getParams()
}

internal data class ShortsMediaGroup(
    private val items: List<MediaItem?>,
    private val continuation: String? = null,
    private val options: MediaGroupOptions
): BaseMediaGroup(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = continuation
    override fun getTitleInt(): String? = null
    override val mediaItemList = items
}

internal data class NotificationsMediaGroup(
    private val result: NotificationsResult
): BaseMediaGroup(MediaGroupOptions.create(groupType = MediaGroup.TYPE_NOTIFICATIONS)) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = null
    override val mediaItemList by lazy { result.getItems()?.mapNotNull { it?.let { NotificationMediaItem(it) } } }
}

internal data class SubscribedShortsMediaGroup(
    private val items: List<ItemWrapper?>
): BaseMediaGroup(MediaGroupOptions.create(groupType = MediaGroup.TYPE_SHORTS)) {
    override fun getItemWrappersInt(): List<ItemWrapper?> = items
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = null
}

internal data class EmptyMediaGroup(
    private val reloadPageKey: String,
    private val type: Int,
    private val title: String? = null
): BaseMediaGroup(MediaGroupOptions.create(groupType = type)) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String = reloadPageKey
    override fun getTitleInt(): String? = title
}

package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.ThumbnailItem
import com.liskovsoft.youtubeapi.common.models.gen.getBrowseId
import com.liskovsoft.youtubeapi.common.models.gen.getContinuationToken
import com.liskovsoft.youtubeapi.common.models.gen.getParams
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackTokens
import com.liskovsoft.youtubeapi.common.models.gen.getSubtitle
import com.liskovsoft.youtubeapi.common.models.gen.getSuggestToken
import com.liskovsoft.youtubeapi.common.models.gen.getText
import com.liskovsoft.youtubeapi.common.models.gen.getTitle
import com.liskovsoft.youtubeapi.common.models.gen.isLive
import com.liskovsoft.youtubeapi.common.models.gen.isUpcoming
import com.liskovsoft.youtubeapi.next.v2.gen.EngagementPanel
import com.liskovsoft.youtubeapi.next.v2.gen.getChannelName
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.next.v2.gen.getItemWrappers
import com.liskovsoft.youtubeapi.next.v2.gen.getPublishDate
import com.liskovsoft.youtubeapi.next.v2.gen.getTitle
import com.liskovsoft.youtubeapi.next.v2.gen.getViews
import com.liskovsoft.youtubeapi.next.v2.gen.containsShorts

/**
 *  Always renders first tab
 */
internal fun BrowseResult.getItems(): List<ItemWrapper?>? = getRootTab()?.getItems()
internal fun BrowseResult.getLiveItems(): List<ItemWrapper?>? =
    getItems()?.filter { it?.isLive() == true || it?.isUpcoming() == true }?.sortedByDescending { it?.isLive() }
internal fun BrowseResult.getPastLiveItems(maxItems: Int = -1): List<ItemWrapper?>? =
    getItems()?.filter { it != null && !it.isLive() && !it.isUpcoming() }?.let { if (maxItems > 0) it.take(maxItems) else it }
internal fun BrowseResult.getShortItems(): List<ItemWrapper?>? = getRootTab()?.getShortItems()
internal fun BrowseResult.getNestedShelves(): List<ShelfListWrapper?>? = getRootTab()?.getNestedShelves()
internal fun BrowseResult.getContinuationToken(): String? = getRootTab()?.getContinuationToken()
internal fun BrowseResult.getTabs(): List<TabRenderer?>? = (contents?.twoColumnBrowseResultsRenderer ?: contents?.singleColumnBrowseResultsRenderer)
    ?.tabs?.mapNotNull { it?.tabRenderer ?: it?.expandableTabRenderer }
internal fun BrowseResult.getSections(): List<RichSectionRenderer?>? = getRootTab()?.getSections()
internal fun BrowseResult.getChips(): List<ChipCloudChipRenderer?>? = getRootTab()?.getChips()
/**
 *  Always renders first tab
 *  
 *  First tab on HOME page has no title. Use first chip instead.
 */
internal fun BrowseResult.getTitle(): String? =
    getRootTab()?.title ?: (header?.playlistHeaderRenderer ?: header?.musicHeaderRenderer)?.getTitle() ?: getChips()?.getOrNull(0)?.getTitle()
internal fun BrowseResult.isPlaylist(): Boolean = header?.playlistHeaderRenderer != null
internal fun BrowseResult.isHome(): Boolean = getTabs()?.getOrNull(0)?.getItems() != null
internal fun BrowseResult.getRedirectBrowseId(): String? = onResponseReceivedActions?.firstNotNullOfOrNull { it?.navigateAction?.endpoint?.getBrowseId() }
private fun BrowseResult.getRootTab() = getTabs()?.firstNotNullOfOrNull { if (it?.content != null) it else null }

/////

private const val TAB_STYLE_NEW_CONTENT = "NEW_CONTENT"

internal fun TabRenderer.getItems(): List<ItemWrapper?>? = getListRenderer()?.getItems()
    ?: getGridRenderer()?.getItems() ?: getTVGridRenderer()?.getItems() ?: getTVListRenderer()?.getItems()
internal fun TabRenderer.getShortItems(): List<ItemWrapper?>? = getGridRenderer()?.getShortItems() ?: getTVListRenderer()?.getShortItems()
internal fun TabRenderer.getContinuationToken(): String? = getListRenderer()?.getContinuationToken()
    ?: getGridRenderer()?.getContinuationToken()
    ?: getTVGridRenderer()?.getContinuationToken()
    ?: getTVListRenderer()?.getContinuationToken()
internal fun TabRenderer.getTitle(): String? = title
internal fun TabRenderer.getBrowseId(): String? = endpoint?.getBrowseId()
internal fun TabRenderer.getReloadToken(): String? = content?.tvSurfaceContentRenderer?.continuation?.getContinuationToken()
internal fun TabRenderer.getParams(): String? = endpoint?.getParams()
internal fun TabRenderer.getThumbnails(): ThumbnailItem? = thumbnail
internal fun TabRenderer.hasNewContent(): Boolean = presentationStyle?.style == TAB_STYLE_NEW_CONTENT
internal fun TabRenderer.getNestedShelves(): List<ShelfListWrapper?>? = getListRenderer()?.getNestedShelves()
internal fun TabRenderer.getSections(): List<RichSectionRenderer?>? = getGridRenderer()?.getSections()
internal fun TabRenderer.getChips(): List<ChipCloudChipRenderer?>? = getChipRenderer()?.getChips()
private fun TabRenderer.getListRenderer() = content?.sectionListRenderer
private fun TabRenderer.getGridRenderer() = content?.richGridRenderer
private fun TabRenderer.getChipRenderer() = content?.richGridRenderer?.header?.feedFilterChipBarRenderer
private fun TabRenderer.getTVGridRenderer() = content?.tvSurfaceContentRenderer?.content?.gridRenderer
internal fun TabRenderer.getTVListRenderer() = content?.tvSurfaceContentRenderer?.content?.sectionListRenderer

/////

private const val CONTINUATION_HEADER = "RELOAD_CONTINUATION_SLOT_HEADER" // channel sorting continuation header

internal fun ContinuationResult.getItems(): List<ItemWrapper?>? = getContinuations()?.flatMap { it?.getItems() ?: listOfNotNull(it?.getItem()) }
internal fun ContinuationResult.getContinuationToken(): String? =
    getContinuations()?.firstNotNullOfOrNull { it?.getContinuationToken() }
    ?: getContinuations()?.lastOrNull()?.getContinuationToken()
internal fun ContinuationResult.getSections(): List<RichSectionRenderer?>? = getContinuations()?.mapNotNull { it?.richSectionRenderer }
private fun ContinuationResult.getContinuations() = onResponseReceivedActions?.firstNotNullOfOrNull {
        it?.appendContinuationItemsAction?.continuationItems
            ?: it?.reloadContinuationItemsCommand?.let { if (it.slot != CONTINUATION_HEADER) it.continuationItems else null }
    }

/////

internal fun RichSectionRenderer.getTitle(): String? = content?.richShelfRenderer?.title?.getText()
internal fun RichSectionRenderer.getItems(): List<ItemWrapper?>? = getContents()?.mapNotNull { it?.richItemRenderer?.content }
internal fun RichSectionRenderer.getContinuationToken(): String? = getContents()?.lastOrNull()?.continuationItemRenderer?.getContinuationToken()
private fun RichSectionRenderer.getContents() = content?.richShelfRenderer?.contents

/////

internal fun ShelfListWrapper.getTitle(): String? = getFirstShelfRenderer()?.title?.getText()
internal fun ShelfListWrapper.getItems(): List<ItemWrapper?>? =
    // Skip special rows like "Most relevant", "Shorts". Such rows always have a title.
    getContents()?.flatMap { it?.takeIf { it.getTitle() == null }?.getItems() ?: emptyList() }
internal fun ShelfListWrapper.getShortItems(): List<ItemWrapper?>? =
    getContents()?.firstNotNullOfOrNull { if (it?.containsShorts() == true) it.getItems() else null }
internal fun ShelfListWrapper.getContinuationToken() = getContents()?.lastOrNull()?.getContinuationToken() ?: continuations?.getContinuationToken()
internal fun ShelfListWrapper.getBrowseId() = getFirstShelfRenderer()?.endpoint?.getBrowseId()
internal fun ShelfListWrapper.getParams() = getFirstShelfRenderer()?.endpoint?.getParams()
private fun ShelfListWrapper.getContents() = contents // Contains shelves with items (3 in a row) and single row for shorts
private fun ShelfListWrapper.getFirstShelfRenderer() = contents?.firstNotNullOfOrNull { it?.shelfRenderer }
private fun ShelfListWrapper.getFirstGridRenderer() = contents?.firstNotNullOfOrNull { it?.gridRenderer }

/////

internal fun SectionListRenderer.getItems(): List<ItemWrapper?>? = getContents()?.flatMap { it?.getItems() ?: emptyList() }
internal fun SectionListRenderer.getNestedShelves(): List<ShelfListWrapper?>? = getContents()?.mapNotNull { it?.itemSectionRenderer }
internal fun SectionListRenderer.getContinuationToken(): String? = getContents()?.firstNotNullOfOrNull { it?.getContinuationToken() }
private fun SectionListRenderer.getContents() = contents // Contains shelves with items (3 in a row) and single row for shorts

///////

internal fun GridRenderer.getItems(): List<ItemWrapper?>? = items
internal fun GridRenderer.getContinuationToken() = continuations?.getContinuationToken() ?: items?.lastOrNull()?.getContinuationToken()

///////

internal fun RichGridRenderer.getItems(): List<ItemWrapper?>? = getContents()?.mapNotNull { it?.getItem() }
internal fun RichGridRenderer.getContinuationToken(): String? = getContents()?.lastOrNull()?.getContinuationToken()
internal fun RichGridRenderer.getShortItems(): List<ItemWrapper?>? = getContents()?.flatMap { it?.getItems() ?: emptyList() }
internal fun RichGridRenderer.getSections(): List<RichSectionRenderer?>? = getContents()?.mapNotNull { it?.richSectionRenderer }
private fun RichGridRenderer.getContents() = contents

///////

internal fun FeedFilterChipBarRenderer.getChips(): List<ChipCloudChipRenderer?>? = getContents()?.mapNotNull { it?.chipCloudChipRenderer }
private fun FeedFilterChipBarRenderer.getContents() = contents

/////

internal fun ChipCloudChipRenderer.getTitle(): String? = text?.getText()

/////

internal fun SectionWrapper.getItem() = richItemRenderer?.content ?: playlistVideoRenderer?.let { ItemWrapper(playlistVideoRenderer = it) }
    ?: gridPlaylistRenderer?.let { ItemWrapper(gridPlaylistRenderer = it) } ?: gridVideoRenderer?.let { ItemWrapper(gridVideoRenderer = it) }

internal fun SectionWrapper.getItems() = itemSectionRenderer?.getItems() ?: richSectionRenderer?.getItems() ?: gridRenderer?.items
internal fun SectionWrapper.getContinuationToken() = continuationItemRenderer?.getContinuationToken() ?: itemSectionRenderer?.getContinuationToken()

/////

internal fun ContinuationItemRenderer.getContinuationToken() = continuationEndpoint?.continuationCommand?.token

/////

internal fun ChipCloudChipRenderer.getContinuationToken() = navigationEndpoint?.continuationCommand?.token


/////

private const val GUIDE_STYLE_NEW_CONTENT = "GUIDE_ENTRY_PRESENTATION_STYLE_NEW_CONTENT"
private const val GUIDE_STYLE_NONE = "GUIDE_ENTRY_PRESENTATION_STYLE_NONE"

internal fun GuideResult.getFirstSubs(): List<GuideItem?>? = getSubsRoot()?.items?.mapNotNull { it?.guideEntryRenderer }
internal fun GuideResult.getCollapsibleSubs(): List<GuideItem?>? =
    getSubsRoot()?.items?.firstNotNullOfOrNull { it?.guideCollapsibleEntryRenderer }?.expandableItems?.mapNotNull { it?.guideEntryRenderer }
internal fun GuideResult.getRecommended(): List<GuideItem?>? = items?.mapNotNull { it?.guideSectionRenderer }?.getOrNull(1)?.items?.mapNotNull { it?.guideEntryRenderer }
internal fun GuideResult.getSuggestToken(): String? = responseContext?.getSuggestToken()
private fun GuideResult.getSubsRoot() = items?.firstNotNullOfOrNull { it?.guideSubscriptionsSectionRenderer }

internal fun GuideItem.getBrowseId() = navigationEndpoint?.getBrowseId()
internal fun GuideItem.getParams() = navigationEndpoint?.getParams()
internal fun GuideItem.getThumbnails() = thumbnail
internal fun GuideItem.getTitle() = formattedTitle?.getText()
internal fun GuideItem.hasNewContent() = presentationStyle == GUIDE_STYLE_NEW_CONTENT
internal fun GuideItem.isLive() = badges?.liveBroadcasting

///////

internal fun BrowseResultKids.getSections(): List<AnchoredSectionRenderer?>? = contents?.kidsHomeScreenRenderer?.anchors?.mapNotNull { it?.anchoredSectionRenderer }
internal fun BrowseResultKids.getRootSection(): AnchoredSectionRenderer? = getSections()?.getOrNull(0)
internal fun AnchoredSectionRenderer.getItems(): List<ItemWrapper?>? = content?.sectionListRenderer?.contents?.getOrNull(0)?.itemSectionRenderer?.contents
internal fun AnchoredSectionRenderer.getTitle(): String? = title
internal fun AnchoredSectionRenderer.getBrowseId(): String? = navigationEndpoint?.getBrowseId()
internal fun AnchoredSectionRenderer.getParams(): String? = navigationEndpoint?.getParams()

//////

private fun ReelResult.getWatchEndpoint(): ReelWatchEndpoint? = replacementEndpoint?.reelWatchEndpoint
private fun ReelResult.getPlayerHeader(): ReelPlayerHeaderRenderer? = overlay?.reelPlayerOverlayRenderer?.reelPlayerHeaderSupportedRenderers?.reelPlayerHeaderRenderer
internal fun ReelResult.getVideoId(): String? = getWatchEndpoint()?.videoId
internal fun ReelResult.getTitle(): String? = getPlayerHeader()?.reelTitleOnClickCommand?.getTitle() ?: getVideoInfo()?.getTitle()
internal fun ReelResult.getSubtitle(): CharSequence? = getPlayerHeader()?.reelTitleOnClickCommand?.getSubtitle() ?:
    YouTubeHelper.createInfo(getVideoInfo()?.getChannelName(), getVideoInfo()?.getViews(), getVideoInfo()?.getPublishDate())
private fun ReelResult.getVideoInfo(): EngagementPanel? = engagementPanels?.firstNotNullOfOrNull { if (it?.getTitle() != null) it else null }
private fun ReelResult.getChannelName(): String? = getPlayerHeader()?.channelTitleText?.getText()
private fun ReelResult.getUploadDate(): String? = getPlayerHeader()?.timestampText?.getText()
internal fun ReelResult.getThumbnails(): ThumbnailItem? = getWatchEndpoint()?.thumbnail
internal fun ReelResult.getBrowseId(): String? = getPlayerHeader()?.channelNavigationEndpoint?.getBrowseId()
internal fun ReelResult.getFeedbackTokens(): List<String?>? = overlay?.reelPlayerOverlayRenderer?.menu?.getFeedbackTokens()
internal fun ReelResult.getContinuationToken(): String? = sequenceContinuation ?: continuationEndpoint?.continuationCommand?.token

internal fun ReelContinuationResult.getItems(): List<ReelWatchEndpoint?>? = entries?.mapNotNull { it?.command?.reelWatchEndpoint }
internal fun ReelContinuationResult.getContinuationToken(): String? = continuation ?: continuationEndpoint?.continuationCommand?.token

internal fun ReelWatchEndpoint.getVideoId(): String? = videoId
internal fun ReelWatchEndpoint.getThumbnails(): ThumbnailItem? = thumbnail

///////

private const val SUBSCRIPTIONS_BROWSE_ID = "FEsubscriptions"

internal fun BrowseResultTV.getShelves(): List<Shelf?>? = getContent()?.sectionListRenderer?.contents
    ?.filter { it?.shelfRenderer != null } // skip promoShelfRenderer
    ?.sortedByDescending { it?.shelfRenderer?.endpoint?.getParams()?.let {
        // Move Live, Past Streams and Videos to the top
        Helpers.startsWithAny(it,"EgZ2aWRlb3MYAyACOAJwA", "EgZ2aWRlb3MYAyAAcA", "EgZ2aWRlb3MYAyACOARwA")
    } ?: false }
internal fun BrowseResultTV.getItems(): List<ItemWrapper?>? = getContent()?.gridRenderer?.items
    ?: getContent()?.twoColumnRenderer?.rightColumn?.playlistVideoListRenderer?.contents
    ?: getSubscriptionsTab()?.getItems()
    ?: getShelves()?.getOrNull(0)?.getItems()
internal fun BrowseResultTV.getShortItems(): List<ItemWrapper?>? = getSubscriptionsTab()?.getShortItems()
internal fun BrowseResultTV.getContinuationToken(): String? = getSubscriptionsTab()?.getContinuationToken()
    ?: getContent()?.twoColumnRenderer?.rightColumn?.playlistVideoListRenderer?.continuations?.getContinuationToken()
    ?: getContent()?.sectionListRenderer?.continuations?.getContinuationToken()
    ?: getShelves()?.getOrNull(0)?.getContinuationToken()
// Get tabs, e.g. Subscriptions section with a channel list (first one is All)
internal fun BrowseResultTV.getTabs() = getSections()?.getOrNull(0)?.tvSecondaryNavSectionRenderer?.tabs?.mapNotNull { it.tabRenderer ?: it.expandableTabRenderer }
private fun BrowseResultTV.getContent() = contents?.tvBrowseRenderer?.content?.tvSurfaceContentRenderer?.content
private fun BrowseResultTV.getSections() = contents?.tvBrowseRenderer?.content?.tvSecondaryNavRenderer?.sections
private fun BrowseResultTV.getSubscriptionsTab() = getTabs()?.firstOrNull { it.getBrowseId() == SUBSCRIPTIONS_BROWSE_ID } ?: getTabs()?.getOrNull(0)

///////////

internal fun Shelf.getTitle(): String? = shelfRenderer?.getTitle()
internal fun Shelf.getItems(): List<ItemWrapper?>? = shelfRenderer?.getItemWrappers()
    ?: gridRenderer?.items
    ?: playlistVideoListRenderer?.contents
    ?: videoRenderer?.let { listOf(ItemWrapper(videoRenderer = it)) }
internal fun Shelf.getContinuationToken(): String? = shelfRenderer?.getContinuationToken()
    ?: (gridRenderer ?: shelfRenderer?.content?.gridRenderer)?.getContinuationToken()
    ?: playlistVideoListRenderer?.getContinuationToken()
internal fun Shelf.containsShorts(): Boolean = shelfRenderer?.containsShorts() == true

///////////


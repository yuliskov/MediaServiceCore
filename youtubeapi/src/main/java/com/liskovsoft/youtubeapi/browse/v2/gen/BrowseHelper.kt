package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.models.gen.*
import com.liskovsoft.youtubeapi.next.v2.gen.*

internal fun BrowseResult.getItems(): List<ItemWrapper?>? = getRootTab()?.getItems()
internal fun BrowseResult.getLiveItems(): List<ItemWrapper?>? = getItems()?.filter { it?.isLive() == true || it?.isUpcoming() == true }?.sortedByDescending { it?.isLive() }
internal fun BrowseResult.getPastLiveItems(maxItems: Int = -1): List<ItemWrapper?>? =
    getItems()?.filter { it?.isLive() == false && it.isUpcoming() == false }?.let { if (maxItems > 0) it.take(maxItems) else it }
internal fun BrowseResult.getShortItems(): List<ItemWrapper?>? = getRootTab()?.getShortItems()
internal fun BrowseResult.getContinuationToken(): String? = getRootTab()?.getContinuationToken()
internal fun BrowseResult.getTabs(): List<TabRenderer?>? = contents?.twoColumnBrowseResultsRenderer?.tabs?.mapNotNull { it?.tabRenderer }
internal fun BrowseResult.getSections(): List<RichSectionRenderer?>? = getRootTab()?.getGridContents()?.mapNotNull { it?.richSectionRenderer }
internal fun BrowseResult.getChips(): List<ChipCloudChipRenderer?>? = getRootTab()?.getChipContents()?.mapNotNull { it?.chipCloudChipRenderer }
internal fun BrowseResult.getTitle(): String? = getRootTab()?.title
private fun BrowseResult.getRootTab() = getTabs()?.firstNotNullOfOrNull { if (it?.content != null) it else null }

/////

internal fun TabRenderer.getItems(): List<ItemWrapper?>? = getListContents()?.flatMap { it?.getItems() ?: emptyList() } ?:
    getGridContents()?.mapNotNull { it?.getItem() }
internal fun TabRenderer.getShortItems(): List<ItemWrapper?>? = getGridContents()?.flatMap { it?.getItems() ?: emptyList() }
internal fun TabRenderer.getContinuationToken(): String? = getListContents()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getGridContents()?.lastOrNull()?.getContinuationToken()
internal fun TabRenderer.getTitle(): String? = title
private fun TabRenderer.getListContents() = content?.sectionListRenderer?.contents
private fun TabRenderer.getGridContents() = content?.richGridRenderer?.contents
private fun TabRenderer.getChipContents() = content?.richGridRenderer?.header?.feedFilterChipBarRenderer?.contents

/////

internal fun ContinuationResult.getItems(): List<ItemWrapper?>? = getContinuations()?.flatMap { it?.getItems() ?: listOfNotNull(it?.getItem()) }
internal fun ContinuationResult.getContinuationToken(): String? = getContinuations()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getContinuations()?.lastOrNull()?.getContinuationToken()
internal fun ContinuationResult.getSections(): List<RichSectionRenderer?>? = getContinuations()?.mapNotNull { it?.richSectionRenderer }
private fun ContinuationResult.getContinuations() = onResponseReceivedActions?.getOrNull(0)?.let {
        it.appendContinuationItemsAction?.continuationItems ?: it.reloadContinuationItemsCommand?.continuationItems
    }


/////

internal fun RichSectionRenderer.getTitle(): String? = content?.richShelfRenderer?.title?.getText()
internal fun RichSectionRenderer.getItems(): List<ItemWrapper?>? = getContents()?.mapNotNull { it?.richItemRenderer?.content }
internal fun RichSectionRenderer.getContinuationToken(): String? = getContents()?.lastOrNull()?.continuationItemRenderer?.getContinuationToken()
private fun RichSectionRenderer.getContents() = content?.richShelfRenderer?.contents

/////

internal fun ItemSectionRenderer.getItems(): List<ItemWrapper?>? = getContents()?.let {
    it.shelfRenderer?.content?.let { it.gridRenderer?.items ?: it.expandedShelfContentsRenderer?.items } ?:
    it.playlistVideoListRenderer?.contents
}
internal fun ItemSectionRenderer.getContinuationToken() = getContents()?.playlistVideoListRenderer?.contents?.lastOrNull()?.getContinuationToken()
private fun ItemSectionRenderer.getContents() = contents?.getOrNull(0)

/////

internal fun ChipCloudChipRenderer.getTitle(): String? = text?.getText()

/////

internal fun Section.getItem() = richItemRenderer?.content ?: playlistVideoRenderer?.let { ItemWrapper(playlistVideoRenderer = it) }

internal fun Section.getItems() = itemSectionRenderer?.getItems() ?: richSectionRenderer?.getItems()
internal fun Section.getContinuationToken() = continuationItemRenderer?.getContinuationToken() ?: itemSectionRenderer?.getContinuationToken()

/////

internal fun ContinuationItemRenderer.getContinuationToken() = continuationEndpoint?.continuationCommand?.token

/////

internal fun ChipCloudChipRenderer.getContinuationToken() = navigationEndpoint?.continuationCommand?.token


/////

private const val STYLE_NEW_CONTENT = "GUIDE_ENTRY_PRESENTATION_STYLE_NEW_CONTENT"
private const val STYLE_NONE = "GUIDE_ENTRY_PRESENTATION_STYLE_NONE"

internal fun GuideResult.getFirstSubs(): List<GuideItem?>? = getSubsRoot()?.items?.mapNotNull { it?.guideEntryRenderer }
internal fun GuideResult.getCollapsibleSubs(): List<GuideItem?>? =
    getSubsRoot()?.items?.firstNotNullOfOrNull { it?.guideCollapsibleEntryRenderer }?.expandableItems?.mapNotNull { it?.guideEntryRenderer }
internal fun GuideResult.getRecommended(): List<GuideItem?>? = items?.mapNotNull { it?.guideSectionRenderer }?.getOrNull(1)?.items?.mapNotNull { it?.guideEntryRenderer }
private fun GuideResult.getSubsRoot() = items?.firstNotNullOfOrNull { it?.guideSubscriptionsSectionRenderer }

internal fun GuideItem.getBrowseId() = navigationEndpoint?.getBrowseId()
internal fun GuideItem.getBrowseParams() = navigationEndpoint?.getBrowseParams()
internal fun GuideItem.getThumbnails() = thumbnail
internal fun GuideItem.getTitle() = formattedTitle?.getText()
internal fun GuideItem.hasNewContent() = presentationStyle == STYLE_NEW_CONTENT
internal fun GuideItem.isLive() = badges?.liveBroadcasting

///////

internal fun BrowseResultKids.getSections(): List<AnchoredSectionRenderer?>? = contents?.kidsHomeScreenRenderer?.anchors?.mapNotNull { it?.anchoredSectionRenderer }
internal fun BrowseResultKids.getRootSection(): AnchoredSectionRenderer? = getSections()?.getOrNull(0)
internal fun AnchoredSectionRenderer.getItems(): List<ItemWrapper?>? = content?.sectionListRenderer?.contents?.getOrNull(0)?.itemSectionRenderer?.contents
internal fun AnchoredSectionRenderer.getTitle(): String? = title
internal fun AnchoredSectionRenderer.getBrowseId(): String? = navigationEndpoint?.getBrowseId()
internal fun AnchoredSectionRenderer.getBrowseParams(): String? = navigationEndpoint?.getBrowseParams()

//////

private fun ReelResult.getWatchEndpoint(): ReelWatchEndpoint? = replacementEndpoint?.reelWatchEndpoint
private fun ReelResult.getPlayerHeader(): ReelPlayerHeaderRenderer? = overlay?.reelPlayerOverlayRenderer?.reelPlayerHeaderSupportedRenderers?.reelPlayerHeaderRenderer
internal fun ReelResult.getVideoId(): String? = getWatchEndpoint()?.videoId
internal fun ReelResult.getTitle(): String? = getPlayerHeader()?.reelTitleOnClickCommand?.getTitle() ?: getVideoInfo()?.getTitle()
internal fun ReelResult.getSubtitle(): String? = getPlayerHeader()?.reelTitleOnClickCommand?.getSubtitle() ?:
    YouTubeHelper.createInfo(getVideoInfo()?.getChannelName(), getVideoInfo()?.getViews(), getVideoInfo()?.getPublishDate())
private fun ReelResult.getVideoInfo(): EngagementPanel? = engagementPanels?.firstNotNullOfOrNull { if (it?.getTitle() != null) it else null }
private fun ReelResult.getChannelName(): String? = getPlayerHeader()?.channelTitleText?.getText()
private fun ReelResult.getUploadDate(): String? = getPlayerHeader()?.timestampText?.getText()
internal fun ReelResult.getThumbnails(): ThumbnailItem? = getWatchEndpoint()?.thumbnail
internal fun ReelResult.getBrowseId(): String? = getPlayerHeader()?.channelNavigationEndpoint?.getBrowseId()
internal fun ReelResult.getFeedbackTokens(): List<String?>? = overlay?.reelPlayerOverlayRenderer?.menu?.getFeedbackTokens()
internal fun ReelResult.getContinuationKey(): String? = sequenceContinuation ?: continuationEndpoint?.continuationCommand?.token

internal fun ReelContinuationResult.getItems(): List<ReelWatchEndpoint?>? = entries?.mapNotNull { it?.command?.reelWatchEndpoint }
internal fun ReelContinuationResult.getContinuationKey(): String? = continuation ?: continuationEndpoint?.continuationCommand?.token

internal fun ReelWatchEndpoint.getVideoId(): String? = videoId
internal fun ReelWatchEndpoint.getThumbnails(): ThumbnailItem? = thumbnail


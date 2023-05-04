package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.*

fun BrowseResult.getItems(): List<ItemWrapper?>? = getListContents()?.flatMap { it?.getItems() ?: emptyList() } ?:
    getGridContents()?.map { it?.getItem() }
fun BrowseResult.getContinuationToken(): String? = getListContents()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getGridContents()?.lastOrNull()?.getContinuationToken()
fun BrowseResult.getSections(): List<RichSectionRenderer?>? = getGridContents()?.mapNotNull { it?.richSectionRenderer }
fun BrowseResult.getChips(): List<ChipCloudChipRenderer?>? = getChipContents()?.mapNotNull { it?.chipCloudChipRenderer }
fun BrowseResult.getTitle(): String? = getRootTab()?.title
private fun BrowseResult.getRootTab() =
    contents?.twoColumnBrowseResultsRenderer?.tabs?.firstNotNullOfOrNull { if (it?.tabRenderer?.content != null) it.tabRenderer else null }
private fun BrowseResult.getListContents() = getRootTab()?.content?.sectionListRenderer?.contents
private fun BrowseResult.getGridContents() = getRootTab()?.content?.richGridRenderer?.contents
private fun BrowseResult.getChipContents() = getRootTab()?.content?.richGridRenderer?.header?.feedFilterChipBarRenderer?.contents


/////

fun ContinuationResult.getItems(): List<ItemWrapper?>? = getContinuations()?.flatMap { it?.getItems() ?: listOfNotNull(it?.getItem()) }
fun ContinuationResult.getContinuationToken(): String? = getContinuations()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getContinuations()?.lastOrNull()?.getContinuationToken()
fun ContinuationResult.getSections(): List<RichSectionRenderer?>? = getContinuations()?.mapNotNull { it?.richSectionRenderer }
private fun ContinuationResult.getContinuations() = onResponseReceivedActions?.getOrNull(0)?.let {
        it.appendContinuationItemsAction?.continuationItems ?: it.reloadContinuationItemsCommand?.continuationItems
    }


/////

fun RichSectionRenderer.getTitle(): String? = content?.richShelfRenderer?.title?.getText()
fun RichSectionRenderer.getItems(): List<ItemWrapper?>? = getContents()?.mapNotNull { it?.richItemRenderer?.content }
fun RichSectionRenderer.getContinuationToken(): String? = getContents()?.lastOrNull()?.continuationItemRenderer?.getContinuationToken()
private fun RichSectionRenderer.getContents() = content?.richShelfRenderer?.contents

/////

fun ChipCloudChipRenderer.getTitle(): String? = text?.getText()

/////

fun Section.getItem() = richItemRenderer?.content

fun Section.getItems() = itemSectionRenderer?.contents?.getOrNull(0)?.shelfRenderer?.content?.let { it.gridRenderer?.items ?: it.expandedShelfContentsRenderer?.items }
fun Section.getContinuationToken() = continuationItemRenderer?.getContinuationToken()

/////

fun ContinuationItemRenderer.getContinuationToken() = continuationEndpoint?.continuationCommand?.token

/////

fun ChipCloudChipRenderer.getContinuationToken() = navigationEndpoint?.continuationCommand?.token


/////

private const val STYLE_NEW_CONTENT = "GUIDE_ENTRY_PRESENTATION_STYLE_NEW_CONTENT"
private const val STYLE_NONE = "GUIDE_ENTRY_PRESENTATION_STYLE_NONE"

fun GuideResult.getFirstItems(): List<GuideItem?>? = getRootItems()?.mapNotNull { it?.guideEntryRenderer }
fun GuideResult.getCollapsibleItems(): List<GuideItem?>? = getRootItems()?.firstNotNullOfOrNull { it?.guideCollapsibleEntryRenderer }?.expandableItems?.mapNotNull { it?.guideEntryRenderer }
private fun GuideResult.getRootItems() = items?.firstNotNullOfOrNull { it?.guideSubscriptionsSectionRenderer }?.items

fun GuideItem.getChannelId() = navigationEndpoint?.getBrowseId()
fun GuideItem.getThumbnails() = thumbnail
fun GuideItem.getTitle() = formattedTitle?.getText()
fun GuideItem.hasNewContent() = presentationStyle == STYLE_NEW_CONTENT
fun GuideItem.isLive() = badges?.liveBroadcasting

///////

fun BrowseResultKids.getSections(): List<AnchoredSectionRenderer?>? = contents?.kidsHomeScreenRenderer?.anchors?.mapNotNull { it?.anchoredSectionRenderer }
fun BrowseResultKids.getRootSection(): AnchoredSectionRenderer? = getSections()?.getOrNull(0)
fun AnchoredSectionRenderer.getItems(): List<ItemWrapper?>? = content?.sectionListRenderer?.contents?.getOrNull(0)?.itemSectionRenderer?.contents
fun AnchoredSectionRenderer.getTitle(): String? = title
fun AnchoredSectionRenderer.getBrowseId(): String? = navigationEndpoint?.getBrowseId()
fun AnchoredSectionRenderer.getBrowseParams(): String? = navigationEndpoint?.getBrowseParams()

//////

fun ReelResult.getWatchEndpoint(): ReelWatchEndpoint? = replacementEndpoint?.reelWatchEndpoint
fun ReelResult.getPlayerHeader(): ReelPlayerHeaderRenderer? = overlay?.reelPlayerOverlayRenderer?.reelPlayerHeaderSupportedRenderers?.reelPlayerHeaderRenderer
fun ReelContinuationResult.getItems(): List<ReelWatchEndpoint?>? = entries?.mapNotNull { it?.command?.reelWatchEndpoint }


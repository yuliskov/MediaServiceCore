package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.*

fun BrowseResult.getItems(): List<ItemWrapper?>? = getRootTab()?.getItems()
fun BrowseResult.getLiveItems(): List<ItemWrapper?>? = getItems()?.filter { it?.isLive() == true || it?.isUpcoming() == true }?.sortedByDescending { it?.isUpcoming() }
fun BrowseResult.getPastLiveItems(maxItems: Int = -1): List<ItemWrapper?>? =
    getItems()?.filter { it?.isLive() == false && it.isUpcoming() == false }?.let { if (maxItems > 0) it.take(maxItems) else it }
fun BrowseResult.getContinuationToken(): String? = getRootTab()?.getContinuationToken()
fun BrowseResult.getTabs(): List<TabRenderer?>? = contents?.twoColumnBrowseResultsRenderer?.tabs?.mapNotNull { it?.tabRenderer }
fun BrowseResult.getSections(): List<RichSectionRenderer?>? = getRootTab()?.getGridContents()?.mapNotNull { it?.richSectionRenderer }
fun BrowseResult.getChips(): List<ChipCloudChipRenderer?>? = getRootTab()?.getChipContents()?.mapNotNull { it?.chipCloudChipRenderer }
fun BrowseResult.getTitle(): String? = getRootTab()?.title
private fun BrowseResult.getRootTab() = getTabs()?.firstNotNullOfOrNull { if (it?.content != null) it else null }

/////

fun TabRenderer.getItems(): List<ItemWrapper?>? = getListContents()?.flatMap { it?.getItems() ?: emptyList() } ?:
    getGridContents()?.mapNotNull { it?.getItem() }
fun TabRenderer.getContinuationToken(): String? = getListContents()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getGridContents()?.lastOrNull()?.getContinuationToken()
fun TabRenderer.getTitle(): String? = title
private fun TabRenderer.getListContents() = content?.sectionListRenderer?.contents
private fun TabRenderer.getGridContents() = content?.richGridRenderer?.contents
private fun TabRenderer.getChipContents() = content?.richGridRenderer?.header?.feedFilterChipBarRenderer?.contents

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

fun GuideResult.getFirstSubs(): List<GuideItem?>? = getSubsRoot()?.items?.mapNotNull { it?.guideEntryRenderer }
fun GuideResult.getCollapsibleSubs(): List<GuideItem?>? =
    getSubsRoot()?.items?.firstNotNullOfOrNull { it?.guideCollapsibleEntryRenderer }?.expandableItems?.mapNotNull { it?.guideEntryRenderer }
fun GuideResult.getRecommended(): List<GuideItem?>? = items?.mapNotNull { it?.guideSectionRenderer }?.getOrNull(1)?.items?.mapNotNull { it?.guideEntryRenderer }
private fun GuideResult.getSubsRoot() = items?.firstNotNullOfOrNull { it?.guideSubscriptionsSectionRenderer }

fun GuideItem.getBrowseId() = navigationEndpoint?.getBrowseId()
fun GuideItem.getBrowseParams() = navigationEndpoint?.getBrowseParams()
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


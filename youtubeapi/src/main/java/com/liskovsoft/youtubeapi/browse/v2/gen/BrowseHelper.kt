package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.getText

fun BrowseResult.getItems(): List<ItemWrapper?>? = getListContents()?.flatMap { it?.getItems() ?: emptyList() } ?:
    getGridContents()?.map { it?.getItem() }
fun BrowseResult.getContinuationToken(): String? = getListContents()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getGridContents()?.lastOrNull()?.getContinuationToken()
fun BrowseResult.getSections(): List<RichSectionRenderer?>? = getGridContents()?.mapNotNull { it?.richSectionRenderer }
fun BrowseResult.getChips(): List<ChipCloudChipRenderer?>? = getChipContents()?.mapNotNull { it?.chipCloudChipRenderer }
private fun BrowseResult.getRootContent() = contents?.twoColumnBrowseResultsRenderer?.tabs?.getOrNull(0)
    ?.tabRenderer?.content
private fun BrowseResult.getListContents() = getRootContent()?.sectionListRenderer?.contents
private fun BrowseResult.getGridContents() = getRootContent()?.richGridRenderer?.contents
private fun BrowseResult.getChipContents() = getRootContent()?.richGridRenderer?.header?.feedFilterChipBarRenderer?.contents


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

fun GuideResult.getItems(): List<ItemWrapper?>? = items?.firstNotNullOfOrNull { it?.guideSubscriptionsSectionRenderer }?.items


package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

fun BrowseResult.getItems(): List<ItemWrapper?>? = getContents()?.flatMap { it?.getItems() ?: emptyList() } ?:
    getRichContents()?.map { it?.getItem() }
fun BrowseResult.getContinuationToken(): String? = getContents()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getRichContents()?.lastOrNull()?.getContinuationToken()
private fun BrowseResult.getContent() = contents?.twoColumnBrowseResultsRenderer?.tabs?.getOrNull(0)
    ?.tabRenderer?.content
private fun BrowseResult.getContents() = getContent()?.sectionListRenderer?.contents
private fun BrowseResult.getRichContents() = getContent()?.richGridRenderer?.contents


/////

fun ContinuationResult.getItems(): List<ItemWrapper?>? = getContinuations()?.flatMap { it?.getItems() ?: listOfNotNull(it?.getItem()) }
fun ContinuationResult.getContinuationToken(): String? = getContinuations()?.firstNotNullOfOrNull {
        it?.getContinuationToken()
    } ?:
    getContinuations()?.lastOrNull()?.getContinuationToken()
private fun ContinuationResult.getContinuations() = onResponseReceivedActions?.getOrNull(0)?.appendContinuationItemsAction
    ?.continuationItems


/////

fun Section.getItem() = richItemRenderer?.content

fun Section.getItems() = itemSectionRenderer?.contents?.getOrNull(0)?.shelfRenderer?.content?.let { it.gridRenderer?.items ?: it.expandedShelfContentsRenderer?.items }
fun Section.getContinuationToken() = continuationItemRenderer?.continuationEndpoint?.continuationCommand?.token


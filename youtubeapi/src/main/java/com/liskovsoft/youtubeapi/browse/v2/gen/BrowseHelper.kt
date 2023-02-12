package com.liskovsoft.youtubeapi.browse.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

fun BrowseResult.getItems(): List<ItemWrapper?>? = getContents()?.flatMap { it?.getItems() ?: emptyList() }
fun BrowseResult.getContinuationToken(): String? = getContents()?.firstNotNullOfOrNull {
    it?.getContinuationToken()
}
private fun BrowseResult.getContents() = contents?.twoColumnBrowseResultsRenderer?.tabs?.getOrNull(0)
    ?.tabRenderer?.content?.sectionListRenderer?.contents


/////

fun ContinuationResult.getItems(): List<ItemWrapper?>? = getContinuations()?.flatMap { it?.getItems() ?: emptyList() }
fun ContinuationResult.getContinuationToken(): String? = getContinuations()?.firstNotNullOfOrNull {
    it?.getContinuationToken()
}
private fun ContinuationResult.getContinuations() = onResponseReceivedActions?.getOrNull(0)?.appendContinuationItemsAction
        ?.continuationItems


/////

fun Section.getItems() = itemSectionRenderer?.contents?.getOrNull(0)?.shelfRenderer?.content?.gridRenderer?.items
fun Section.getContinuationToken() = continuationItemRenderer?.continuationEndpoint?.continuationCommand?.token


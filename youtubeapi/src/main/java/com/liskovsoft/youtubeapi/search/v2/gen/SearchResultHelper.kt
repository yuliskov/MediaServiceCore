package com.liskovsoft.youtubeapi.search.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.next.v2.gen.getItemWrappers
import com.liskovsoft.youtubeapi.next.v2.gen.getTitle

internal fun SearchResult.getNextPageKey() = getContents()?.firstOrNull()?.shelfRenderer?.getContinuationToken()
internal fun SearchResult.getItemWrappers(): List<ItemWrapper?>? = getContents()?.firstOrNull()?.shelfRenderer?.getItemWrappers()
internal fun SearchResult.getSections(): List<SearchSection?>? = getContents()?.mapNotNull {
    SearchSection(
        it?.shelfRenderer?.getTitle(),
        it?.shelfRenderer?.getContinuationToken(),
        it?.shelfRenderer?.getItemWrappers()
    )
}
private fun SearchResult.getContents() = contents?.sectionListRenderer?.contents
package com.liskovsoft.youtubeapi.browse.v2.gen

fun BrowseResult.getItems() = contents?.twoColumnBrowseResultsRenderer?.tabs?.getOrNull(0)
    ?.tabRenderer?.content?.sectionListRenderer?.contents?.getOrNull(0)?.itemSectionRenderer?.contents?.getOrNull(0)
    ?.shelfRenderer?.content?.gridRenderer?.items
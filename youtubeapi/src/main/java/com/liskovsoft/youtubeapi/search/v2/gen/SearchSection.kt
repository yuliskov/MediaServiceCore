package com.liskovsoft.youtubeapi.search.v2.gen

import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

internal data class SearchSection(
    val title: String?,
    val nextPageKey: String?,
    val itemWrappers: List<ItemWrapper?>?
)
package com.liskovsoft.youtubeapi.browse.v2.impl

import com.liskovsoft.youtubeapi.browse.v2.gen.BrowseResult
import com.liskovsoft.youtubeapi.browse.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.browse.v2.gen.getItems
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

data class MediaGroupImpl(val browseResult: BrowseResult): MediaGroupImplBase() {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = browseResult.getItems()
    override fun getNextPageKeyInt(): String? = browseResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

package com.liskovsoft.youtubeapi.browse.v2.impl

import com.liskovsoft.youtubeapi.browse.v2.gen.ContinuationResult
import com.liskovsoft.youtubeapi.browse.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.browse.v2.gen.getItems
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

data class MediaGroupImpl2(
    private val continuationResult: ContinuationResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = continuationResult.getItems()
    override fun getNextPageKeyInt(): String? = continuationResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

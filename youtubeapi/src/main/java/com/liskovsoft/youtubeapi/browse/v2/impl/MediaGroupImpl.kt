package com.liskovsoft.youtubeapi.browse.v2.impl

import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper

data class MediaGroupImpl(
    private val browseResult: BrowseResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = browseResult.getItems()
    override fun getNextPageKeyInt(): String? = browseResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

data class MediaGroupImpl2(
    private val continuationResult: ContinuationResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = continuationResult.getItems()
    override fun getNextPageKeyInt(): String? = continuationResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

data class MediaGroupImpl3(
    private val richSectionRenderer: RichSectionRenderer,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = richSectionRenderer.getItems()
    override fun getNextPageKeyInt(): String? = richSectionRenderer.getContinuationToken()
    override fun getTitleInt(): String? = richSectionRenderer.getTitle()
}

data class MediaGroupImpl4(
    private val chipCloudChipRenderer: ChipCloudChipRenderer,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = chipCloudChipRenderer.getContinuationToken()
    override fun getTitleInt(): String? = chipCloudChipRenderer.getTitle()
}

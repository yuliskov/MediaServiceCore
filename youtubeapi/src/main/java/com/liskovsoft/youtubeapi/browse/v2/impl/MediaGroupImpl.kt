package com.liskovsoft.youtubeapi.browse.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.MediaItemImpl2

data class MediaGroupImpl(
    private val browseResult: BrowseResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = browseResult.getItems()
    override fun getNextPageKeyInt(): String? = browseResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

data class MediaGroupImplContinuation(
    private val continuationResult: ContinuationResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = continuationResult.getItems()
    override fun getNextPageKeyInt(): String? = continuationResult.getContinuationToken()
    override fun getTitleInt(): String? = null
}

data class MediaGroupImplSection(
    private val richSectionRenderer: RichSectionRenderer,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = richSectionRenderer.getItems()
    override fun getNextPageKeyInt(): String? = richSectionRenderer.getContinuationToken()
    override fun getTitleInt(): String? = richSectionRenderer.getTitle()
}

data class MediaGroupImplChip(
    private val chipCloudChipRenderer: ChipCloudChipRenderer,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = chipCloudChipRenderer.getContinuationToken()
    override fun getTitleInt(): String? = chipCloudChipRenderer.getTitle()
}

data class MediaGroupImplGuide(
    private val guideResult: GuideResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = null
    override val mediaItemList by lazy {
        val result = mutableListOf<MediaItem>()

        guideResult.getFirstItems()?.forEach {
            it?.let { result.add(MediaItemImpl2(it)) }
        }

        guideResult.getCollapsibleItems()?.forEach {
            it?.let { result.add(MediaItemImpl2(it)) }
        }

        result
    }
}

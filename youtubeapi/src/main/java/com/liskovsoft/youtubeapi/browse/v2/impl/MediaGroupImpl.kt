package com.liskovsoft.youtubeapi.browse.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.getBrowseId
import com.liskovsoft.youtubeapi.common.models.gen.getBrowseParams
import com.liskovsoft.youtubeapi.next.v2.impl.mediaitem.MediaItemImplGuide

data class MediaGroupImpl(
    private val browseResult: BrowseResult,
    private val options: MediaGroupOptions = MediaGroupOptions(),
    private val liveResult: BrowseResult? = null
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?> =
        listOfNotNull(liveResult?.getLiveItems(), browseResult.getItems()).flatten()
    override fun getNextPageKeyInt(): String? = browseResult.getContinuationToken()
    override fun getTitleInt(): String? = browseResult.getTitle()
}

data class MediaGroupImplLive(
    private val liveResult: BrowseResult,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?> = listOfNotNull(liveResult.getLiveItems(), liveResult.getPastLiveItems()).flatten()
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = liveResult.getTitle()
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

data class MediaGroupImplTab(
    private val tabRenderer: TabRenderer,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = tabRenderer.getItems()
    override fun getNextPageKeyInt(): String? = tabRenderer.getContinuationToken()
    override fun getTitleInt(): String? = tabRenderer.getTitle()
    override fun getChannelIdInt(): String? = tabRenderer.endpoint?.getBrowseId()
    override fun getParamsInt(): String? = tabRenderer.endpoint?.getBrowseParams()
}

data class MediaGroupImplKidsSection(
    private val anchoredSectionRenderer: AnchoredSectionRenderer,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = anchoredSectionRenderer.getItems()
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = anchoredSectionRenderer.getTitle()
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
    private val options: MediaGroupOptions = MediaGroupOptions(),
    private val sort: Boolean = false
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = null
    override val mediaItemList by lazy {
        val result = mutableListOf<MediaItem>()

        guideResult.getFirstSubs()?.forEach {
            it?.let { if (it.thumbnail != null) result.add(MediaItemImplGuide(it)) } // exclude 'special' items
        }

        guideResult.getCollapsibleSubs()?.forEach {
            it?.let { if (it.thumbnail != null) result.add(MediaItemImplGuide(it)) } // exclude 'special' items
        }

        if (sort) result.sortBy { it.title?.lowercase() }

        result
    }
}

data class MediaGroupImplRecommended(
    private val guideItem: GuideItem,
    private val options: MediaGroupOptions = MediaGroupOptions()
): MediaGroupImplBase(options) {
    override fun getItemWrappersInt(): List<ItemWrapper?>? = null
    override fun getNextPageKeyInt(): String? = null
    override fun getTitleInt(): String? = guideItem.getTitle()
    override fun getChannelIdInt(): String? = guideItem.getBrowseId()
    override fun getParamsInt(): String? = guideItem.getBrowseParams()
}

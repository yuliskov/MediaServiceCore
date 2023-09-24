package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.*
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.ShortsMediaItem

internal object BrowseService2 {
    private val mBrowseApi = RetrofitHelper.withGson(BrowseApi::class.java)
    private val mAppService = AppService.instance()

    //@JvmStatic
    //fun getHome(): List<MediaGroup?>? {
    //    val home = getBrowseRows(BrowseApiHelper.getHomeQueryWeb(), MediaGroup.TYPE_HOME)
    //    return if (home?.size ?: 0 < 5) listOfNotNull(home, getRecommended()).flatten() else home
    //}

    @JvmStatic
    fun getHome(): List<MediaGroup?>? {
        return getBrowseRows(BrowseApiHelper.getHomeQueryWeb(), MediaGroup.TYPE_HOME)
    }

    @JvmStatic
    fun getTrending(): List<MediaGroup?>? {
        return getBrowseRows(BrowseApiHelper.getTrendingQueryWeb(), MediaGroup.TYPE_TRENDING)
    }

    @JvmStatic
    fun getKidsHome(): List<MediaGroup?>? {
        val kidsResult = mBrowseApi.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery())

        return RetrofitHelper.get(kidsResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getRootSection()?.let { result.add(KidsSectionMediaGroup(it, createOptions(MediaGroup.TYPE_KIDS_HOME))) }
            it.getSections()?.forEach {
                if (it?.getItems() == null && it?.getBrowseParams() != null) {
                    val kidsResultNested = mBrowseApi.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery(it.getBrowseParams()))
                    RetrofitHelper.get(kidsResultNested)?.getRootSection()?.let {
                        result.add(KidsSectionMediaGroup(it, createOptions(MediaGroup.TYPE_KIDS_HOME)))
                    }
                }
            }

            result
        }
    }

    @JvmStatic
    fun getSubscriptions(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        return RetrofitHelper.get(browseResult)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_SUBSCRIPTIONS)) }
    }

    @JvmStatic
    fun getSubscribedChannels(): MediaGroup? {
        val guideResult = mBrowseApi.getGuideResult(ServiceHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let { GuideMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    @JvmStatic
    fun getSubscribedChannelsByName(): MediaGroup? {
        val guideResult = mBrowseApi.getGuideResult(ServiceHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let { GuideMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS), true) }
    }

    @JvmStatic
    fun getShorts(): MediaGroup? {
        val firstResult = mBrowseApi.getReelResult(BrowseApiHelper.getReelQuery())

        return RetrofitHelper.get(firstResult)?.let { firstItem ->
            val result = continueShorts(firstItem.getContinuationKey())
            result?.mediaItems?.add(0, ShortsMediaItem(null, firstItem))

            getSubscribedShorts()?.let { result?.mediaItems?.addAll(0, it) }

            return result
        }
    }

    private fun continueShorts(continuationKey: String?): MediaGroup? {
        if (continuationKey == null) {
            return null
        }

        val continuation = mBrowseApi?.getReelContinuationResult(BrowseApiHelper.getReelContinuationQuery(continuationKey))

        return RetrofitHelper.get(continuation)?.let {
            val result = mutableListOf<MediaItem?>()

            it.getItems()?.forEach {
                it?.let {
                    val details = mBrowseApi?.getReelResult(BrowseApiHelper.getReelDetailsQuery(it.videoId, it.params))

                    RetrofitHelper.get(details)?.let {
                            info -> result.add(ShortsMediaItem(it, info))
                    }
                }
            }

            ShortsMediaGroup(result, it.getContinuationKey(), createOptions(MediaGroup.TYPE_SHORTS))
        }
    }

    @JvmStatic
    fun getChannelVideosFull(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQueryWeb(channelId))
        val live = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelLiveQueryWeb(channelId))

        RetrofitHelper.get(videos)?.let { return BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS), RetrofitHelper.get(live)) }

        RetrofitHelper.get(live)?.let { return LiveMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }

        return null
    }

    @JvmStatic
    fun getChannelVideos(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQueryWeb(channelId))

        return RetrofitHelper.get(videos)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    @JvmStatic
    fun getChannelLive(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val live = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelLiveQueryWeb(channelId))

        return RetrofitHelper.get(live)?.let { LiveMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    @JvmStatic
    fun continueGroup(group: MediaGroup?): MediaGroup? {
        return if (group is ShortsMediaGroup) continueShorts(group.nextPageKey) else continueChip(group)?.firstOrNull()
    }

    @JvmStatic
    fun continueEmptyGroup(group: MediaGroup?): List<MediaGroup?>? {
        if (group?.nextPageKey != null) {
            return continueChip(group)
        } else if (group?.channelId != null) {
            return continueTab(group)?.let { listOf(it) }
        }

        return null
    }

    private fun continueTab(group: MediaGroup?): MediaGroup? {
        if (group?.channelId == null) {
            return null
        }

        val browseResult =
            mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelQueryWeb(group.channelId, group.params))

        return RetrofitHelper.get(browseResult)?.let { BrowseMediaGroup(it, createOptions(group.type)).apply { title = group.title } }
    }

    private fun continueChip(group: MediaGroup?): List<MediaGroup?>? {
        if (group?.nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResult(BrowseApiHelper.getContinuationQueryWeb(group.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            result.add(ContinuationMediaGroup(it, createOptions(group.type)).apply { title = group.title })
            it.getSections()?.forEach { if (it != null) result.add(SectionMediaGroup(it, createOptions(group.type))) }

            result
        }
    }

    private fun createOptions(groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS): MediaGroupOptions {
        val prefs = GlobalPreferences.sInstance
        val removeShorts = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideShortsFromSubscriptionsEnabled ?: false) ||
                (MediaGroup.TYPE_HOME == groupType && prefs?.isHideShortsFromHomeEnabled ?: false) ||
                (MediaGroup.TYPE_HISTORY == groupType && prefs?.isHideShortsFromHistoryEnabled ?: false) ||
                prefs?.isHideShortsEverywhereEnabled ?: false
        val removeLive = MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideStreamsFromSubscriptionsEnabled ?: false
        val removeUpcoming = MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideUpcomingEnabled ?: false

        return MediaGroupOptions(
            removeShorts,
            removeLive,
            removeUpcoming,
            groupType
        )
    }

    private fun getBrowseRows(query: String, sectionType: Int): List<MediaGroup?>? {
        val browseResult = mBrowseApi.getBrowseResult(query)

        return RetrofitHelper.get(browseResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            // First chip is always empty and corresponds to current result.
            // Also title used as id in continuation. No good.
            // NOTE: First tab on home page has not title.
            result.add(BrowseMediaGroup(it, createOptions(sectionType)).apply { title = it.getChips()?.getOrNull(0)?.getTitle() })
            it.getTabs()?.forEach { if (it?.getTitle() != null) result.add(TabMediaGroup(it, createOptions(sectionType))) }
            it.getSections()?.forEach { if (it?.getTitle() != null) result.add(SectionMediaGroup(it, createOptions(sectionType))) }
            it.getChips()?.forEach { if (it?.getTitle() != null) result.add(ChipMediaGroup(it, createOptions(sectionType))) }

            result
        }
    }

    private fun getRecommended(): List<MediaGroup?>? {
        val guideResult = mBrowseApi.getGuideResult(ServiceHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            it.getRecommended()?.forEach { if (it != null) result.add(RecommendedMediaGroup(it, createOptions(MediaGroup.TYPE_HOME))) }

            result
        }
    }

    private fun getSubscribedShorts(): List<MediaItem?>? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        return RetrofitHelper.get(browseResult)?.let { it.getShortItems()?.let { WrapperMediaGroup(it) } }?.mediaItems
    }
}
package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.*
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.ShortsMediaItem

internal object BrowseService2 {
    private val mBrowseApi = RetrofitHelper.create(BrowseApi::class.java)

    //@JvmStatic
    //fun getHome(): List<MediaGroup?>? {
    //    val home = getBrowseRows(BrowseApiHelper.getHomeQueryWeb(), MediaGroup.TYPE_HOME)
    //    return if (home?.size ?: 0 < 5) listOfNotNull(home, getRecommended()).flatten() else home
    //}

    @JvmStatic
    fun getHome(): List<MediaGroup?>? {
        val rows = getBrowseRows(BrowseApiHelper.getHomeQueryWeb(), MediaGroup.TYPE_HOME)

        if (rows?.all { it?.isEmpty == true } != false) // in anonymous mode WEB home page is empty
            return getBrowseRowsTV(BrowseApiHelper.getHomeQueryTV(), MediaGroup.TYPE_HOME)

        return rows
    }

    @JvmStatic
    fun getTrending(): List<MediaGroup?>? {
        return getBrowseRows(BrowseApiHelper.getTrendingQueryWeb(), MediaGroup.TYPE_TRENDING)
    }

    @JvmStatic
    fun getSports(): List<MediaGroup?>? {
        return getBrowseRowsTV(BrowseApiHelper.getSportsQueryTV(), MediaGroup.TYPE_SPORTS)
    }

    @JvmStatic
    fun getMovies(): List<MediaGroup?>? {
        return getBrowseRowsTV(BrowseApiHelper.getMoviesQueryTV(), MediaGroup.TYPE_MOVIES)
    }

    @JvmStatic
    fun getKidsHome(): List<MediaGroup?>? {
        val kidsResult = mBrowseApi.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery())

        return RetrofitHelper.get(kidsResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getRootSection()?.let { result.add(KidsSectionMediaGroup(it, createOptions(MediaGroup.TYPE_KIDS_HOME))) }
            it.getSections()?.forEach {
                if (it?.getItems() == null && it?.getBrowseParams() != null) {
                    val kidsResultNested = mBrowseApi.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery(it.getBrowseParams()!!))
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

    @JvmStatic
    fun getLikedMusic(): MediaGroup? {
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getLikedMusicQuery())

        return RetrofitHelper.get(result)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_MUSIC)) }
    }

    private fun continueShorts(continuationKey: String?): MediaGroup? {
        if (continuationKey == null) {
            return null
        }

        val continuation = mBrowseApi?.getReelContinuationResult(BrowseApiHelper.getReelContinuationQuery(continuationKey))

        return RetrofitHelper.get(continuation)?.let {
            val result = mutableListOf<MediaItem?>()

            it.getItems()?.forEach {
                if (it?.videoId != null && it.params != null) {
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
    fun getChannelSearch(channelId: String?, query: String?): MediaGroup? {
        if (channelId == null || query == null) {
            return null
        }

        val search = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelSearchQueryWeb(channelId, query))

        return RetrofitHelper.get(search)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    @JvmStatic
    fun getChannelSorting(channelId: String?): List<MediaGroup?>? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQueryWeb(channelId))

        return RetrofitHelper.get(videos)?.let { it.getChips()?.mapNotNull { if (it != null) ChipMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) else null } }
    }

    @JvmStatic
    fun getChannel(channelId: String?, params: String?): List<MediaGroup?>? {
        if (channelId == null) {
            return null
        }

        val result = mutableListOf<MediaGroup>()

        val homeResult = getBrowseRedirect(channelId) {
            val home = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelHomeQueryWeb(it))
            RetrofitHelper.get(home)
        }

        var shortTab: MediaGroup? = null

        homeResult?.let { it.getTabs()?.drop(1)?.forEach { // skip first tab - Home (repeats Videos)
            if (it?.title?.contains("Shorts") == true) { // move Shorts tab lower
                shortTab = TabMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL))
                return@forEach
            }
            val title = it?.getTitle()
            if (title != null && result.firstOrNull { it.title == title } == null) // only unique rows
                result.add(TabMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL))) } }

        shortTab?.let { result.add(it) } // move Shorts tab lower

        homeResult?.let { it.getShelves()?.forEach {
            val title = it?.getTitle()
            if (title != null && result.firstOrNull { it.title == title } == null) // only unique rows
                result.add(ItemSectionMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL))) } }

        if (result.isEmpty()) {
            val playlist = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelQueryWeb(channelId))
            RetrofitHelper.get(playlist)?.let {
                if (it.getTitle() != null) result.add(BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)))
            }
        }

        return result
    }

    @JvmStatic
    fun continueGroup(group: MediaGroup?): MediaGroup? {
        return when (group) {
            is ShortsMediaGroup -> continueShorts(group.nextPageKey)
            is ShelfSectionMediaGroup -> continueTVGroup(group)
            is WatchNexContinuationMediaGroup -> continueTVGroup(group)
            else -> continueChip(group)?.firstOrNull()
        }
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
            it.getSections()?.forEach { if (it != null) result.add(RichSectionMediaGroup(it, createOptions(group.type))) }

            result
        }
    }

    private fun continueTVGroup(group: MediaGroup?): MediaGroup? {
        if (group?.nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQueryTV(group.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            WatchNexContinuationMediaGroup(it, createOptions(group.type)).apply { title = group.title }
        }
    }

    private fun createOptions(groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS): MediaGroupOptions {
        val prefs = GlobalPreferences.sInstance
        val removeShorts = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideShortsFromSubscriptionsEnabled ?: false) ||
                (MediaGroup.TYPE_HOME == groupType && prefs?.isHideShortsFromHomeEnabled ?: false) ||
                (MediaGroup.TYPE_HISTORY == groupType && prefs?.isHideShortsFromHistoryEnabled ?: false) ||
                (MediaGroup.TYPE_CHANNEL == groupType && prefs?.isHideShortsFromChannelEnabled ?: false) ||
                (MediaGroup.TYPE_TRENDING == groupType && prefs?.isHideShortsFromTrendingEnabled ?: false)
        val removeLive = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideStreamsFromSubscriptionsEnabled ?: false)
        val removeUpcoming = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideUpcomingFromSubscriptionsEnabled ?: false) ||
                (MediaGroup.TYPE_CHANNEL == groupType && prefs?.isHideUpcomingFromChannelEnabled ?: false) ||
                (MediaGroup.TYPE_HOME == groupType && prefs?.isHideUpcomingFromHomeEnabled ?: false)
        val removeWatched = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && prefs?.isHideWatchedFromSubscriptionsEnabled ?: false) ||
                (MediaGroup.TYPE_HOME == groupType && prefs?.isHideWatchedFromHomeEnabled ?: false)

        return MediaGroupOptions(
            removeShorts,
            removeLive,
            removeUpcoming,
            removeWatched,
            groupType
        )
    }

    private fun getBrowseRows(query: String, sectionType: Int): List<MediaGroup?>? {
        val browseResult = mBrowseApi.getBrowseResult(query)

        return RetrofitHelper.get(browseResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            // First chip is always empty and corresponds to current result.
            // Also title used as id in continuation. No good.
            // NOTE: First tab on home page has no title.
            result.add(BrowseMediaGroup(it, createOptions(sectionType))) // always renders first tab
            it.getTabs()?.drop(1)?.forEach { if (it?.getTitle() != null) result.add(TabMediaGroup(it, createOptions(sectionType))) }
            it.getSections()?.forEach { if (it?.getTitle() != null) addOrMerge(result, RichSectionMediaGroup(it, createOptions(sectionType))) }
            it.getChips()?.forEach { if (it?.getTitle() != null) result.add(ChipMediaGroup(it, createOptions(sectionType))) }

            result
        }
    }

    private fun getBrowseRowsTV(query: String, sectionType: Int): List<MediaGroup?>? {
        val browseResult = mBrowseApi.getBrowseResultTV(query)

        return RetrofitHelper.get(browseResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getShelves()?.forEach { if (it?.getTitle() != null) addOrMerge(result, ShelfSectionMediaGroup(it, createOptions(sectionType))) }
            result
        }
    }

    private fun addOrMerge(result: MutableList<MediaGroup?>, group: MediaGroup) {
        val filter = result.filter { it?.title == group.title }

        // Home section parsing downside: one row (e.g. Shorts) could be divided amount other videos
        if (filter.size == 1) {
            filter.first()?.mediaItems?.addAll(group.mediaItems)
        } else {
            result.add(group)
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

    private fun getBrowseRedirect(browseId: String, browseExpression: (String) -> BrowseResult?): BrowseResult? {
        val result = browseExpression(browseId)
        return if (result?.getRedirectBrowseId() != null) browseExpression(result.getRedirectBrowseId()!!) else result
    }
}
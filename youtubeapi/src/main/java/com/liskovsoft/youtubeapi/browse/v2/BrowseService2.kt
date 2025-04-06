package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.*
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.ShortsMediaItem
import com.liskovsoft.youtubeapi.next.v2.gen.getItems
import com.liskovsoft.youtubeapi.next.v2.gen.getNextPageKey
import com.liskovsoft.youtubeapi.next.v2.gen.getShelves
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal open class BrowseService2 {
    private val mBrowseApi = RetrofitHelper.create(BrowseApi::class.java)

    //fun getHome(): List<MediaGroup?>? {
    //    val home = getBrowseRows(BrowseApiHelper.getHomeQueryWeb(), MediaGroup.TYPE_HOME)
    //    return if (home?.size ?: 0 < 5) listOfNotNull(home, getRecommended()).flatten() else home
    //}

    fun getHome(): Pair<List<MediaGroup?>?, String?>? {
        //val rows = getBrowseRows(BrowseApiHelper.getHomeQueryWeb(), MediaGroup.TYPE_HOME)
        //
        //if (rows?.all { it?.isEmpty == true } != false) // in anonymous mode WEB home page is empty
        //    return getBrowseRowsTV(BrowseApiHelper.getHomeQueryTV(), MediaGroup.TYPE_HOME)
        //
        //return Pair(rows, null)

        return getBrowseRowsTV(BrowseApiHelper.getHomeQuery(AppClient.TV), MediaGroup.TYPE_HOME)
    }

    fun getTrending(): List<MediaGroup?>? {
        return getBrowseRows(BrowseApiHelper.getTrendingQuery(AppClient.WEB), MediaGroup.TYPE_TRENDING, true)
    }

    fun getSports(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper.getSportsQuery(AppClient.TV), MediaGroup.TYPE_SPORTS)
    }

    fun getLive(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper.getLiveQuery(AppClient.TV), MediaGroup.TYPE_LIVE)
    }

    fun getMyVideos(): MediaGroup? {
        return getBrowseGridTV(BrowseApiHelper.getMyVideosQuery(AppClient.TV), MediaGroup.TYPE_MY_VIDEOS)
    }

    fun getMovies(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper.getMoviesQuery(AppClient.TV), MediaGroup.TYPE_MOVIES)
    }

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

    open fun getSubscriptions(): MediaGroup? {
        return getSubscriptionsTV()
    }

    private fun getSubscriptionsWeb(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getSubscriptionsQuery(AppClient.WEB))

        return RetrofitHelper.get(browseResult)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_SUBSCRIPTIONS)) }
    }

    private fun getSubscriptionsTV(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(AppClient.TV))

        return RetrofitHelper.get(browseResult)?.let {
            // Prepare to move LIVE items to the top. Multiple results should be combined first.
            val (overrideItems, overrideKey) = continueIfNeeded(it.getItems(), it.getContinuationToken())

            BrowseMediaGroupTV(it, createOptions(MediaGroup.TYPE_SUBSCRIPTIONS), overrideItems = overrideItems, overrideKey = overrideKey)
        }
    }

    open fun getSubscribedChannels(): MediaGroup? {
        return getSubscribedChannelsTV() ?: getSubscribedChannelsWeb()
    }

    private fun getSubscribedChannelsWeb(): MediaGroup? {
        val guideResult = mBrowseApi.getGuideResult(ServiceHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let { GuideMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    private fun getSubscribedChannelsTV(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(AppClient.TV))

        return RetrofitHelper.get(browseResult)?.let { it.getTabs()?.let { ChannelListMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) } }
    }

    open fun getSubscribedChannelsByName(): MediaGroup? {
        return getSubscribedChannelsByNameTV() ?: getSubscribedChannelsByNameWeb()
    }

    private fun getSubscribedChannelsByNameWeb(): MediaGroup? {
        val guideResult = mBrowseApi.getGuideResult(ServiceHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let { GuideMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS), SORT_BY_NAME) }
    }

    private fun getSubscribedChannelsByNameTV(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(AppClient.TV))

        return RetrofitHelper.get(browseResult)?.let { it.getTabs()?.let { ChannelListMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS), SORT_BY_NAME) } }
    }

    open fun getSubscribedChannelsByNewContent(): MediaGroup? {
        return getSubscribedChannelsByNewContentTV()
    }

    private fun getSubscribedChannelsByNewContentTV(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(AppClient.TV))

        return RetrofitHelper.get(browseResult)?.let { it.getTabs()?.let { ChannelListMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS), SORT_BY_NEW_CONTENT) } }
    }

    fun getShorts(): MediaGroup? {
        return getShortsWeb(true)
    }

    private fun getShortsWeb(skipAuth: Boolean = false): MediaGroup? {
        val firstResult = mBrowseApi.getReelResult(BrowseApiHelper.getReelQuery())

        return RetrofitHelper.get(firstResult, skipAuth) ?.let { firstItem ->
            val result = continueShortsWeb(firstItem.getContinuationKey(), skipAuth)
            result?.mediaItems?.add(0, ShortsMediaItem(null, firstItem))

            if (!skipAuth)
                getSubscribedShortsWeb()?.let { result?.mediaItems?.addAll(0, it) }

            return result
        }
    }

    fun getMusic(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper.getMusicQuery(AppClient.TV), MediaGroup.TYPE_MUSIC)
    }

    fun getLikedMusic(): MediaGroup? {
        return getLikedMusicTV() ?: getLikedMusicWeb()
    }

    fun getNews(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper.getNewsQuery(AppClient.TV), MediaGroup.TYPE_NEWS)
    }

    fun getGaming(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper.getGamingQuery(AppClient.TV), MediaGroup.TYPE_GAMING)
    }

    fun getHistory(): MediaGroup? {
        return getBrowseGridTV(BrowseApiHelper.getMyLibraryQuery(AppClient.TV), MediaGroup.TYPE_HISTORY)
    }

    private fun getLikedMusicWeb(): MediaGroup? {
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getLikedMusicQuery(AppClient.WEB))

        return RetrofitHelper.get(result)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_MUSIC)) }
    }

    private fun getLikedMusicTV(): MediaGroup? {
        val result = mBrowseApi.getContinuationResultTV(BrowseApiHelper.getLikedMusicContinuation(AppClient.TV))

        return RetrofitHelper.get(result)?.let { WatchNexContinuationMediaGroup(it, createOptions(MediaGroup.TYPE_MUSIC)) }
    }

    fun getNewMusicAlbums(): MediaGroup? {
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getNewMusicAlbumsQuery())

        return RetrofitHelper.get(result, true)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_MUSIC)) }
    }

    fun getNewMusicVideos(): MediaGroup? {
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getNewMusicVideosQuery())

        return RetrofitHelper.get(result, true)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_MUSIC)) }
    }

    open fun getMyPlaylists(): MediaGroup? {
        val result = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getMyPlaylistQuery(AppClient.TV))

        return RetrofitHelper.get(result)?.let { BrowseMediaGroupTV(it, createOptions(MediaGroup.TYPE_USER_PLAYLISTS)) }
    }

    private fun continueShortsWeb(continuationKey: String?, skipAuth: Boolean = false): MediaGroup? {
        if (continuationKey == null) {
            return null
        }

        val continuation = mBrowseApi?.getReelContinuationResult(BrowseApiHelper.getReelContinuationQuery(AppClient.WEB, continuationKey))

        return RetrofitHelper.get(continuation, skipAuth)?.let {
            val result = mutableListOf<MediaItem?>()

            it.getItems()?.forEach {
                if (it?.videoId != null && it.params != null) {
                    val details = mBrowseApi?.getReelResult(BrowseApiHelper.getReelDetailsQuery(AppClient.WEB, it.videoId, it.params))

                    RetrofitHelper.get(details, skipAuth)?.let {
                            info -> result.add(ShortsMediaItem(it, info))
                    }
                }
            }

            ShortsMediaGroup(result, it.getContinuationKey(), createOptions(MediaGroup.TYPE_SHORTS))
        }
    }

    open fun getChannelAsGrid(channelId: String?): MediaGroup? {
        return getChannelVideosTV(channelId) ?: getChannelVideosWeb(channelId, true)
    }

    private fun getChannelVideosTV(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        return getBrowseRowsTV(BrowseApiHelper.getChannelVideosQuery(AppClient.TV, channelId), MediaGroup.TYPE_CHANNEL_UPLOADS)?.first?.firstOrNull()
    }

    private fun getChannelVideosWeb(channelId: String?, skipAuth: Boolean = false): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId))
        val live = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelLiveQuery(AppClient.WEB, channelId))

        RetrofitHelper.get(videos, skipAuth)?.let { return BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS), RetrofitHelper.get(live)) }

        RetrofitHelper.get(live, skipAuth)?.let { return LiveMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }

        return null
    }

    fun getChannelAsGridOld(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId))

        return RetrofitHelper.get(videos)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    fun getChannelLive(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val live = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelLiveQuery(AppClient.WEB, channelId))

        return RetrofitHelper.get(live)?.let { LiveMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    fun getChannelSearch(channelId: String?, query: String?): MediaGroup? {
        return getChannelSearchWeb(channelId, query, true)
    }

    private fun getChannelSearchWeb(channelId: String?, query: String?, skipAuth: Boolean = false): MediaGroup? {
        if (channelId == null || query == null) {
            return null
        }

        val search = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelSearchQuery(AppClient.WEB, channelId, query))

        return RetrofitHelper.get(search, skipAuth)?.let { BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    fun getChannelSorting(channelId: String?): List<MediaGroup?>? {
        return getChannelSortingWeb(channelId, true)
    }

    private fun getChannelSortingWeb(channelId: String?, skipAuth: Boolean = false): List<MediaGroup?>? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId))

        return RetrofitHelper.get(videos, skipAuth)?.let { it.getChips()?.mapNotNull { if (it != null) ChipMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)) else null } }
    }

    open fun getChannel(channelId: String?, params: String?): Pair<List<MediaGroup?>?, String?>? {
        return getChannelTV(channelId, params) ?: getChannelWeb(channelId, params, true)?.let { Pair(it, null) }
    }

    private fun getChannelWeb(channelId: String?, params: String?, skipAuth: Boolean = false): List<MediaGroup?>? {
        if (channelId == null) {
            return null
        }

        val result = mutableListOf<MediaGroup>()

        val homeResult = getBrowseRedirect(channelId) {
            val home = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelHomeQuery(AppClient.WEB, it))
            RetrofitHelper.get(home, skipAuth)
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
            if (it != null && result.firstOrNull { it.title == title } == null) // only unique rows
                result.add(ItemSectionMediaGroup(it,
                    createOptions(if (title == null) MediaGroup.TYPE_CHANNEL_UPLOADS else MediaGroup.TYPE_CHANNEL, channelId))) } } // playlists don't have a title

        if (result.isEmpty()) {
            val playlist = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, channelId))
            RetrofitHelper.get(playlist, skipAuth)?.let {
                if (it.getTitle() != null) result.add(BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS, channelId)))
            }
        }

        //if (result.isEmpty()) {
        //    getChannelResult(AppClient.WEB_REMIX, channelId)?.let {
        //        if (it.getTitle() != null) result.add(BrowseMediaGroup(it, createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)))
        //    }
        //}

        return result.ifEmpty { null }
    }

    private fun getChannelTV(channelId: String?, params: String?): Pair<List<MediaGroup?>?, String?>? {
        if (channelId == null) {
            return null
        }

        return getBrowseRowsTV(BrowseApiHelper.getChannelQuery(AppClient.TV, channelId, params), MediaGroup.TYPE_CHANNEL, MediaGroup.TYPE_CHANNEL_UPLOADS)
    }

    /**
     * A special type of a channel that could be found inside Music section (see Liked row More button)
     */
    fun getGridChannel(channelId: String): MediaGroup? {
        return getBrowseGridTV(BrowseApiHelper.getChannelQuery(AppClient.TV, channelId), MediaGroup.TYPE_CHANNEL_UPLOADS)
    }

    open fun getGroup(reloadPageKey: String, type: Int, title: String?): MediaGroup? {
        return continueGroupTV(EmptyMediaGroup(reloadPageKey, type, title), true)
    }

    fun continueGroup(group: MediaGroup?): MediaGroup? {
        return when (group) {
            is ShortsMediaGroup -> continueShortsWeb(group.nextPageKey, true)
            is ShelfSectionMediaGroup -> continueGroupTV(group)
            is BrowseMediaGroupTV -> continueGroupTV(group)
            is WatchNexContinuationMediaGroup -> continueGroupTV(group)
            else -> continueGroupWeb(group, true)?.firstOrNull()
        }
    }

    fun continueEmptyGroup(group: MediaGroup?): List<MediaGroup?>? {
        if (group?.nextPageKey != null) {
            return continueGroupTV(group)?.let { listOf(it) } ?: continueGroupWeb(group, true)
        } else if (group?.channelId != null) {
            return continueTabWeb(group, true)?.let { listOf(it) }
        }

        return null
    }

    fun continueSectionList(nextPageKey: String?, groupType: Int): Pair<List<MediaGroup?>?, String?>? {
        return continueSectionListTV(nextPageKey, groupType)
    }

    private fun continueSectionListTV(nextPageKey: String?, groupType: Int): Pair<List<MediaGroup?>?, String?>? {
        if (nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(AppClient.TV, nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getShelves()?.forEach { if (it?.getTitle() != null) addOrMerge(result, ShelfSectionMediaGroup(it, createOptions(groupType))) }
            Pair(result, it.getNextPageKey())
        }
    }

    private fun continueTabWeb(group: MediaGroup?, skipAuth: Boolean = false): MediaGroup? {
        if (group?.channelId == null) {
            return null
        }

        val browseResult =
            mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, group.channelId, group.params))

        return RetrofitHelper.get(browseResult, skipAuth)?.let { BrowseMediaGroup(it, createOptions(group.type)).apply { title = group.title } }
    }

    /**
     * NOTE: Can continue Chip or Group
     */
    private fun continueGroupWeb(group: MediaGroup?, skipAuth: Boolean = false): List<MediaGroup?>? {
        if (group?.nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResult(BrowseApiHelper.getContinuationQuery(AppClient.WEB, group.nextPageKey))

        return RetrofitHelper.get(continuationResult, skipAuth)?.let {
            val result = mutableListOf<MediaGroup?>()

            result.add(ContinuationMediaGroup(it, createOptions(group.type)).apply { title = group.title })
            it.getSections()?.forEach { if (it != null) result.add(RichSectionMediaGroup(it, createOptions(group.type))) }

            result
        }
    }

    private fun continueGroupTV(group: MediaGroup?, continueIfNeeded: Boolean = false): MediaGroup? {
        if (group?.nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(AppClient.TV, group.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            // Prepare to move LIVE items to the top. Multiple results should be combined first.
            val (overrideItems, overrideKey) = if (continueIfNeeded) continueIfNeeded(it.getItems(), it.getNextPageKey()) else Pair(null, null)

            WatchNexContinuationMediaGroup(it, createOptions(group.type), overrideItems = overrideItems, overrideKey = overrideKey).apply { title = group.title }
        }
    }

    private fun createOptions(groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS, channelId: String? = null): MediaGroupOptions {
        val prefs = GlobalPreferences.sInstance
        val data = MediaServiceData.instance()
        val removeShorts = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_SUBSCRIPTIONS)) ||
                (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HOME)) ||
                (MediaGroup.TYPE_HISTORY == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HISTORY)) ||
                (MediaGroup.TYPE_CHANNEL == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL)) ||
                (MediaGroup.TYPE_CHANNEL_UPLOADS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL)) ||
                (MediaGroup.TYPE_TRENDING == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_TRENDING))
        val removeLive = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_STREAMS_SUBSCRIPTIONS))
        val removeUpcoming = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_SUBSCRIPTIONS)) ||
                (MediaGroup.TYPE_CHANNEL == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL)) ||
                (MediaGroup.TYPE_CHANNEL_UPLOADS == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL)) ||
                (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_HOME))
        val removeWatched = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_SUBSCRIPTIONS)) ||
                (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_HOME)) ||
                (channelId == AppConstants.WATCH_LATER_CHANNEL_ID && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_WATCH_LATER))

        return MediaGroupOptions(
            removeShorts,
            removeLive,
            removeUpcoming,
            removeWatched,
            groupType
        )
    }

    private fun getBrowseRows(query: String, sectionType: Int, skipAuth: Boolean = false): List<MediaGroup?>? {
        val browseResult = mBrowseApi.getBrowseResult(query)

        return RetrofitHelper.get(browseResult, skipAuth)?.let {
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

    private fun getBrowseRowsTV(query: String, sectionType: Int, gridType: Int = MediaGroup.TYPE_UNDEFINED): Pair<List<MediaGroup?>?, String?>? {
        val browseResult = mBrowseApi.getBrowseResultTV(query)

        return RetrofitHelper.get(browseResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getShelves()?.forEach { if (it?.getTitle() != null) addOrMerge(result, ShelfSectionMediaGroup(it, createOptions(sectionType))) }

            if (result.isEmpty()) // playlist
                addOrMerge(result, BrowseMediaGroupTV(it, createOptions(gridType)))

            Pair(result, it.getContinuationToken())
        }
    }

    private fun getBrowseGridTV(query: String, sectionType: Int, shouldContinue: Boolean = false): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResultTV(query)

        return RetrofitHelper.get(browseResult)?.let {
            // Prepare to move LIVE items to the top. Multiple results should be combined first.
            var continuation: Pair<List<ItemWrapper?>?, String?>? = null
            if (shouldContinue) {
                continuation = continueIfNeeded(it.getItems(), it.getContinuationToken())
            }

            BrowseMediaGroupTV(it, createOptions(sectionType), overrideItems = continuation?.first, overrideKey = continuation?.second)
        }
    }

    private fun addOrMerge(result: MutableList<MediaGroup?>, group: MediaGroup) {
        val filter = result.filter { it?.title == group.title }

        // Home section parsing downside: one row (e.g. Shorts) could be divided amount other videos
        if (filter.size == 1) {
            group.mediaItems?.let { filter.first()?.mediaItems?.addAll(it) }
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

    private fun getSubscribedShortsWeb(): List<MediaItem?>? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getSubscriptionsQuery(AppClient.WEB))

        return RetrofitHelper.get(browseResult)?.let { it.getShortItems()?.let { SubscribedShortsMediaGroup(it) } }?.mediaItems
    }

    private fun getBrowseRedirect(browseId: String, browseExpression: (String) -> BrowseResult?): BrowseResult? {
        val result = browseExpression(browseId)
        return if (result?.getRedirectBrowseId() != null) browseExpression(result.getRedirectBrowseId()!!) else result
    }

    private fun continueIfNeeded(items: List<ItemWrapper?>?, continuationKey: String?): Pair<List<ItemWrapper?>?, String?> {
        var combinedItems: List<ItemWrapper?>? = items
        var combinedKey: String? = continuationKey
        for (i in 0 until 10) {
            if (combinedKey == null || (combinedItems?.size ?: 0) > 60)
                break

            val result =
                mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(AppClient.TV, combinedKey))

            combinedKey = null

            RetrofitHelper.get(result)?.let {
                combinedItems = (combinedItems ?: emptyList()) + (it.getItems() ?: emptyList())
                //combinedItems = (combinedItems.orEmpty() + it.getItems().orEmpty()).distinct() // remove duplicates
                combinedKey = it.getNextPageKey()
            }
        }

        return Pair(combinedItems, combinedKey)
    }

    //private fun getChannelResult(client: AppClient, channelId: String, params: String? = null): BrowseResult? {
    //    val wrapper = mBrowseApi.getBrowseResult(
    //        BrowseApiHelper.getChannelQuery(client, channelId, params), client.userAgent, client.referer)
    //    return RetrofitHelper.get(wrapper)
    //}
}
package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.browse.v2.gen.*
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.*
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper
import com.liskovsoft.youtubeapi.common.models.gen.ItemWrapper
import com.liskovsoft.youtubeapi.common.models.gen.getPlaylistId
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.ShortsMediaItem
import com.liskovsoft.youtubeapi.next.v2.gen.getItems
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.next.v2.gen.getShelves

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

        return getBrowseRowsTV(BrowseApiHelper::getHomeQuery, MediaGroup.TYPE_HOME)
    }

    fun getTrending(): List<MediaGroup?>? {
        return getBrowseRowsWeb(BrowseApiHelper.getTrendingQuery(AppClient.WEB), MediaGroup.TYPE_TRENDING)
    }

    fun getSports(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper::getSportsQuery, MediaGroup.TYPE_SPORTS)
    }

    fun getLive(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper::getLiveQuery, MediaGroup.TYPE_LIVE)
    }

    fun getMyVideos(): MediaGroup? {
        return getBrowseGridTV(BrowseApiHelper::getMyVideosQuery, MediaGroup.TYPE_MY_VIDEOS)
    }

    fun getMovies(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper::getMoviesQuery, MediaGroup.TYPE_MOVIES)
    }

    fun getKidsHome(): List<MediaGroup?>? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_KIDS_HOME)
        val kidsResult = mBrowseApi.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery())

        return RetrofitHelper.get(kidsResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getRootSection()?.let { result.add(KidsSectionMediaGroup(it, options)) }
            it.getSections()?.forEach {
                if (it?.getItems() == null && it?.getParams() != null) {
                    val kidsResultNested = mBrowseApi.getBrowseResultKids(BrowseApiHelper.getKidsHomeQuery(it.getParams()!!))
                    RetrofitHelper.get(kidsResultNested)?.getRootSection()?.let {
                        result.add(KidsSectionMediaGroup(it, options))
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

        return RetrofitHelper.get(browseResult)?.let { BrowseMediaGroup(it, MediaGroupOptions.create(MediaGroup.TYPE_SUBSCRIPTIONS)) }
    }

    private fun getSubscriptionsTV(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_SUBSCRIPTIONS)
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(options.clientTV))

        return RetrofitHelper.get(browseResult)?.let {
            // Prepare to move LIVE items to the top. Multiple results should be combined first.
            val (overrideItems, overrideKey) = continueIfNeededTV(it.getItems(), it.getContinuationToken(), options)

            BrowseMediaGroupTV(it, options, overrideItems = overrideItems, overrideKey = overrideKey)
        }
    }

    open fun getSubscribedChannels(): MediaGroup? {
        return getSubscribedChannelsTV() ?: getSubscribedChannelsWeb()
    }

    private fun getSubscribedChannelsWeb(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val guideResult = mBrowseApi.getGuideResult(PostDataHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let { GuideMediaGroup(it, options) }
    }

    private fun getSubscribedChannelsTV(sortByName: Boolean = false): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(options.clientTV))

        return RetrofitHelper.get(browseResult)?.let { it.getTabs()?.let { ChannelListMediaGroup(it, options, if (sortByName) SORT_BY_NAME else SORT_DEFAULT) } }
    }

    open fun getSubscribedChannelsByName(): MediaGroup? {
        return getSubscribedChannelsTV(sortByName = true) ?: getSubscribedChannelsByNameWeb()
    }

    private fun getSubscribedChannelsByNameWeb(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val guideResult = mBrowseApi.getGuideResult(PostDataHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let { GuideMediaGroup(it, options, SORT_BY_NAME) }
    }

    open fun getSubscribedChannelsByNewContent(): MediaGroup? {
        return getSubscribedChannelsByNewContentTV()
    }

    private fun getSubscribedChannelsByNewContentTV(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(options.clientTV))

        return RetrofitHelper.get(browseResult)?.let { it.getTabs()?.let { ChannelListMediaGroup(it, options, SORT_BY_NEW_CONTENT) } }
    }

    fun getShorts(): MediaGroup? {
        return getShortsTV()
    }

    fun getShorts2(): MediaGroup? {
        return getShortsWeb()
    }

    private fun getShortsWeb(auth: Boolean = false): MediaGroup? {
        val firstResult = mBrowseApi.getReelResult(BrowseApiHelper.getReelQuery())

        return RetrofitHelper.get(firstResult, auth) ?.let { firstItem ->
            val result = continueShortsWeb(firstItem.getContinuationToken(), auth)
            result?.mediaItems?.add(0, ShortsMediaItem(null, firstItem))

            if (auth)
                getSubscribedShortsWeb()?.let { result?.mediaItems?.addAll(0, it) }

            return result
        }
    }

    private fun getShortsTV(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_SHORTS)
        val browseResult = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getSubscriptionsQuery(options.clientTV))

        return RetrofitHelper.get(browseResult)?.let {
            it.getShortItems()?.let { SubscribedShortsMediaGroup(it) }
        }
    }

    fun getMusic(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper::getMusicQuery, MediaGroup.TYPE_MUSIC)
    }

    fun getLikedMusic(): MediaGroup? {
        return getLikedMusicTV() ?: getLikedMusicWeb()
    }

    fun getNews(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper::getNewsQuery, MediaGroup.TYPE_NEWS)
    }

    fun getGaming(): Pair<List<MediaGroup?>?, String?>? {
        return getBrowseRowsTV(BrowseApiHelper::getGamingQuery, MediaGroup.TYPE_GAMING)
    }

    fun getHistory(): MediaGroup? {
        return getBrowseGridTV(BrowseApiHelper::getMyHistoryQuery, MediaGroup.TYPE_HISTORY)
    }

    private fun getLikedMusicWeb(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_MUSIC)
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getLikedMusicQuery(AppClient.WEB))

        return RetrofitHelper.get(result)?.let { BrowseMediaGroup(it, options) }
    }

    private fun getLikedMusicTV(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_MUSIC)
        val result = mBrowseApi.getContinuationResultTV(BrowseApiHelper.getLikedMusicContinuation(options.clientTV))

        return RetrofitHelper.get(result)?.let { WatchNexContinuationMediaGroup(it, options) }
    }

    fun getNewMusicAlbums(): MediaGroup? {
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getNewMusicAlbumsQuery())

        return RetrofitHelper.get(result, false)?.let { BrowseMediaGroup(it, MediaGroupOptions.create(MediaGroup.TYPE_MUSIC)) }
    }

    fun getNewMusicVideos(): MediaGroup? {
        val result = mBrowseApi.getBrowseResult(BrowseApiHelper.getNewMusicVideosQuery())

        return RetrofitHelper.get(result, false)?.let { BrowseMediaGroup(it, MediaGroupOptions.create(MediaGroup.TYPE_MUSIC)) }
    }

    open fun getMyPlaylists(): MediaGroup? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_USER_PLAYLISTS)
        val result = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getMyPlaylistQuery(options.clientTV))

        return RetrofitHelper.get(result)?.let {
            if (it.getItems()?.firstOrNull { it?.getPlaylistId().equals(BrowseApiHelper.WATCH_LATER_PLAYLIST) } != null) {
                BrowseMediaGroupTV(it, options)
            } else { // No Watch Later (moved to the dedicated subsection)
                val library = mBrowseApi.getBrowseResultTV(BrowseApiHelper.getMyLibraryQuery(options.clientTV))

                val outer = it

                RetrofitHelper.get(library)?.let {
                    val watchLater = it.getItems()?.getOrNull(1) // Watch Later subsection
                    BrowseMediaGroupTV(outer, options, watchLater?.let { outer.getItems()?.toMutableList()?.apply { add(0, it) } })
                }
            }
        }
    }

    private fun continueShortsWeb(continuationKey: String?, auth: Boolean = false): MediaGroup? {
        if (continuationKey == null) {
            return null
        }

        val continuation = mBrowseApi?.getReelContinuationResult(BrowseApiHelper.getReelContinuationQuery(AppClient.WEB, continuationKey))

        return RetrofitHelper.get(continuation, auth)?.let {
            val result = mutableListOf<MediaItem?>()

            it.getItems()?.forEach {
                if (it?.videoId != null && it.params != null) {
                    val details = mBrowseApi?.getReelResult(BrowseApiHelper.getReelDetailsQuery(AppClient.WEB, it.videoId, it.params))

                    RetrofitHelper.get(details, auth)?.let {
                            info -> result.add(ShortsMediaItem(it, info))
                    }
                }
            }

            ShortsMediaGroup(result, it.getContinuationToken(), MediaGroupOptions.create(MediaGroup.TYPE_SHORTS))
        }
    }

    open fun getChannelAsGrid(channelId: String?): MediaGroup? {
        return getChannelVideosTV(channelId) ?: getChannelVideosWeb(channelId)
    }

    private fun getChannelVideosTV(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        return getBrowseRowsTV({ BrowseApiHelper.getChannelVideosQuery(it, channelId) }, MediaGroup.TYPE_CHANNEL_UPLOADS)?.first?.firstOrNull()
    }

    private fun getChannelVideosWeb(channelId: String?, auth: Boolean = false): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId))
        val live = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelLiveQuery(AppClient.WEB, channelId))

        RetrofitHelper.get(videos, auth)?.let { return BrowseMediaGroup(it, options, RetrofitHelper.get(live)) }

        RetrofitHelper.get(live, auth)?.let { return LiveMediaGroup(it, options) }

        return null
    }

    fun getChannelAsGridOld(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId))

        return RetrofitHelper.get(videos)?.let { BrowseMediaGroup(it, MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    fun getChannelLive(channelId: String?): MediaGroup? {
        if (channelId == null) {
            return null
        }

        val live = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelLiveQuery(AppClient.WEB, channelId))

        return RetrofitHelper.get(live)?.let { LiveMediaGroup(it, MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)) }
    }

    fun getChannelSearch(channelId: String?, query: String?): MediaGroup? {
        return getChannelSearchWeb(channelId, query)
    }

    private fun getChannelSearchWeb(channelId: String?, query: String?, auth: Boolean = false): MediaGroup? {
        if (channelId == null || query == null) {
            return null
        }

        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val search = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelSearchQuery(AppClient.WEB, channelId, query))

        return RetrofitHelper.get(search, auth)?.let { BrowseMediaGroup(it, options) }
    }

    fun getChannelSorting(channelId: String?): List<MediaGroup?>? {
        return getChannelSortingWeb(channelId)
    }

    private fun getChannelSortingWeb(channelId: String?, auth: Boolean = false): List<MediaGroup?>? {
        if (channelId == null) {
            return null
        }

        val options = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS)
        val videos = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelVideosQuery(AppClient.WEB, channelId))

        return RetrofitHelper.get(videos, auth)?.let { it.getChips()?.mapNotNull { if (it != null) ChipMediaGroup(it, options) else null } }
    }

    open fun getChannel(channelId: String?, params: String?): Pair<List<MediaGroup?>?, String?>? {
        return getChannelTV(channelId, params) ?: getChannelWeb(channelId)?.let { Pair(it, null) }
    }

    private fun getChannelWeb(channelId: String?, auth: Boolean = false): List<MediaGroup?>? {
        if (channelId == null) {
            return null
        }

        val channelOptions = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL, channelId)
        val uploadOptions = MediaGroupOptions.create(MediaGroup.TYPE_CHANNEL_UPLOADS, channelId)
        val result = mutableListOf<MediaGroup>()

        val homeResult = getBrowseRedirect(channelId) {
            val home = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelHomeQuery(AppClient.WEB, it))
            RetrofitHelper.get(home, auth)
        }

        var shortTab: MediaGroup? = null

        homeResult?.let { it.getTabs()?.drop(1)?.forEach { // skip first tab - Home (repeats Videos)
            if (it?.title?.contains("Shorts") == true) { // move Shorts tab lower
                shortTab = TabMediaGroup(it, channelOptions)
                return@forEach
            }
            val title = it?.getTitle()
            if (title != null && result.firstOrNull { it.title == title } == null) // only unique rows
                result.add(TabMediaGroup(it, channelOptions)) } }

        shortTab?.let { result.add(it) } // move Shorts tab lower

        homeResult?.let { it.getNestedShelves()?.forEach {
            val title = it?.getTitle()
            if (it != null && result.firstOrNull { it.title == title } == null) // only unique rows
                result.add(ItemSectionMediaGroup(it, if (title == null) uploadOptions else channelOptions)) } } // playlists don't have a title

        if (result.isEmpty()) {
            val playlist = mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, channelId))
            RetrofitHelper.get(playlist, auth)?.let {
                if (it.getTitle() != null) result.add(BrowseMediaGroup(it, uploadOptions))
            }
        }

        //if (result.isEmpty()) {
        //    getChannelResult(AppClient.WEB_REMIX, channelId)?.let {
        //        if (it.getTitle() != null) result.add(BrowseMediaGroup(it, MediaGroupOptions.createOptions(MediaGroup.TYPE_CHANNEL_UPLOADS)))
        //    }
        //}

        return result.ifEmpty { null }
    }

    private fun getChannelTV(channelId: String?, params: String?): Pair<List<MediaGroup?>?, String?>? {
        if (channelId == null) {
            return null
        }

        return getBrowseRowsTV({ BrowseApiHelper.getChannelQuery(it, channelId, params) }, MediaGroup.TYPE_CHANNEL, MediaGroup.TYPE_CHANNEL_UPLOADS)
    }

    /**
     * A special type of a channel that could be found inside Music section (see Liked row More button)
     */
    fun getGridChannel(channelId: String, params: String? = null): MediaGroup? {
        return getBrowseGridTV({ BrowseApiHelper.getChannelQuery(it, channelId, params) }, MediaGroup.TYPE_CHANNEL_UPLOADS)
    }

    open fun getGroup(reloadPageKey: String, type: Int, title: String?): MediaGroup? {
        return continueGroupTV(EmptyMediaGroup(reloadPageKey, type, title), true)
    }

    fun continueGroup(group: MediaGroup?): MediaGroup? {
        return when (group) {
            is ShortsMediaGroup -> continueShortsWeb(group.nextPageKey)
            is ShelfSectionMediaGroup -> continueGroupTV(group)
            is BrowseMediaGroupTV -> continueGroupTV(group)
            is WatchNexContinuationMediaGroup -> continueGroupTV(group)
            else -> continueGroupWeb(group)?.firstOrNull()
        }
    }

    fun continueEmptyGroup(group: MediaGroup?): List<MediaGroup?>? {
        if (group?.nextPageKey != null) {
            return continueGroupTV(group)?.let { listOf(it) } ?: continueGroupWeb(group)
        } else if (group?.channelId != null) {
            return continueTabWeb(group)?.let { listOf(it) }
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

        val options = MediaGroupOptions.create(groupType)
        val continuationResult =
            mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(options.clientTV, nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getShelves()?.forEach { if (it != null) addOrMerge(result, ShelfSectionMediaGroup(it, options)) }
            Pair(result, it.getContinuationToken())
        }
    }

    private fun continueTabWeb(group: MediaGroup?, auth: Boolean = false): MediaGroup? {
        if (group?.channelId == null) {
            return null
        }

        val options = MediaGroupOptions.create(group.type)
        val browseResult =
            mBrowseApi.getBrowseResult(BrowseApiHelper.getChannelQuery(AppClient.WEB, group.channelId, group.params))

        return RetrofitHelper.get(browseResult, auth)?.let { BrowseMediaGroup(it, options).apply { title = group.title } }
    }

    /**
     * NOTE: Can continue Chip or Group
     */
    private fun continueGroupWeb(group: MediaGroup?, auth: Boolean = false): List<MediaGroup?>? {
        if (group?.nextPageKey == null) {
            return null
        }

        val options = MediaGroupOptions.create(group.type)
        val continuationResult =
            mBrowseApi.getContinuationResult(BrowseApiHelper.getContinuationQuery(AppClient.WEB, group.nextPageKey))

        return RetrofitHelper.get(continuationResult, auth)?.let {
            val result = mutableListOf<MediaGroup?>()

            result.add(ContinuationMediaGroup(it, options).apply { title = group.title })
            it.getSections()?.forEach { if (it != null) result.add(RichSectionMediaGroup(it, options)) }

            result
        }
    }

    private fun continueGroupTV(group: MediaGroup?, continueIfNeeded: Boolean = false): MediaGroup? {
        if (group?.nextPageKey == null) {
            return null
        }

        val options = MediaGroupOptions.create(group.type)
        val continuationResult =
            mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(options.clientTV, group.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            // Prepare to move LIVE items to the top. Multiple results should be combined first.
            val (overrideItems, overrideKey) = if (continueIfNeeded) continueIfNeededTV(it.getItems(), it.getContinuationToken(), options) else Pair(null, null)

            WatchNexContinuationMediaGroup(it, options, overrideItems = overrideItems, overrideKey = overrideKey).apply { title = group.title }
        }
    }

    private fun getBrowseRowsWeb(query: String, sectionType: Int, auth: Boolean = false): List<MediaGroup?>? {
        val options = MediaGroupOptions.create(sectionType)
        val browseResult = mBrowseApi.getBrowseResult(query)

        return RetrofitHelper.get(browseResult, auth)?.let {
            val result = mutableListOf<MediaGroup?>()

            // First chip is always empty and corresponds to current result.
            // Also title used as id in continuation. No good.
            // NOTE: First tab on home page has no title.
            result.add(BrowseMediaGroup(it, MediaGroupOptions.create(sectionType))) // always renders first tab
            it.getTabs()?.drop(1)?.forEach { if (it?.getTitle() != null) result.add(TabMediaGroup(it, options)) }
            it.getSections()?.forEach { if (it?.getTitle() != null) addOrMerge(result, RichSectionMediaGroup(it, options)) }
            it.getChips()?.forEach { if (it?.getTitle() != null) result.add(ChipMediaGroup(it, options)) }

            result
        }
    }

    private fun getBrowseRowsTV(query: (AppClient) -> String, sectionType: Int, gridType: Int = MediaGroup.TYPE_UNDEFINED): Pair<List<MediaGroup?>?, String?>? {
        val rowsOptions = MediaGroupOptions.create(sectionType)
        val gridOptions = MediaGroupOptions.create(gridType)
        val browseResult = mBrowseApi.getBrowseResultTV(query(rowsOptions.clientTV))

        return RetrofitHelper.get(browseResult)?.let {
            val result = mutableListOf<MediaGroup?>()
            it.getShelves()?.forEach { if (it != null) addOrMerge(result, ShelfSectionMediaGroup(it, rowsOptions)) }

            if (result.isEmpty()) // playlist
                addOrMerge(result, BrowseMediaGroupTV(it, gridOptions))

            Pair(result, it.getContinuationToken())
        }
    }

    private fun getBrowseGridTV(query: (AppClient) -> String, sectionType: Int, shouldContinue: Boolean = false): MediaGroup? {
        val options = MediaGroupOptions.create(sectionType)
        val browseResult = mBrowseApi.getBrowseResultTV(query(options.clientTV))

        return RetrofitHelper.get(browseResult)?.let {
            // Prepare to move LIVE items to the top. Multiple results should be combined first.
            var continuation: Pair<List<ItemWrapper?>?, String?>? = null
            if (shouldContinue) {
                continuation = continueIfNeededTV(it.getItems(), it.getContinuationToken(), options)
            }

            BrowseMediaGroupTV(it, options, overrideItems = continuation?.first, overrideKey = continuation?.second)
        }
    }

    private fun addOrMerge(result: MutableList<MediaGroup?>, group: MediaGroup) {
        // Always add, merging will be done later
        result.add(group)
    }

    private fun getRecommendedWeb(): List<MediaGroup?>? {
        val options = MediaGroupOptions.create(MediaGroup.TYPE_HOME)
        val guideResult = mBrowseApi.getGuideResult(PostDataHelper.createQueryWeb(""))

        return RetrofitHelper.get(guideResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            it.getRecommended()?.forEach { if (it != null) result.add(RecommendedMediaGroup(it, options)) }

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

    private fun continueIfNeededTV(items: List<ItemWrapper?>?, continuationKey: String?, options: MediaGroupOptions): Pair<List<ItemWrapper?>?, String?> {
        var combinedItems: List<ItemWrapper?>? = items
        var combinedKey: String? = continuationKey
        for (i in 0 until 10) {
            // NOTE: bigger max value help moving live videos to the top (e.g. sorting)
            if (combinedKey == null || (combinedItems?.size ?: 0) > 60)
                break

            val result =
                mBrowseApi.getContinuationResultTV(BrowseApiHelper.getContinuationQuery(options.clientTV, combinedKey))

            combinedKey = null

            RetrofitHelper.get(result)?.let {
                combinedItems = (combinedItems ?: emptyList()) + (it.getItems() ?: emptyList())
                combinedKey = it.getContinuationToken()
            }
        }

        return Pair(combinedItems, combinedKey)
    }
}
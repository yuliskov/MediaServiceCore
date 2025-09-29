package com.liskovsoft.youtubeapi.rss

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2Wrapper
import com.liskovsoft.youtubeapi.app.nsigsolver.common.YouTubeInfoExtractor
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.concurrent.CopyOnWriteArrayList

internal object RssService {
    private const val RSS_URL: String = "https://www.youtube.com/feeds/videos.xml?channel_id="
    private const val MAX_ITEMS = 100 // NOTE: Limit the result. Unlimited has veeery long loading and often crashing.

    @JvmStatic
    @JvmOverloads
    fun getFeed(vararg channelIds: String, type: Int = -1): MediaGroup? {
        val items = fetchFeedsSafe(channelIds.take(MAX_ITEMS)) ?: return null

        items.sortByDescending { it.publishedDate }

        return YouTubeMediaGroup(type).apply {
            mediaItems = items
        }
    }

    private fun fetchFeedsSafe(channelIds: List<String>): MutableList<MediaItem>? {
        try {
            return fetchFeeds(channelIds)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return null
    }

    private fun fetchFeeds(channelIds: List<String>): MutableList<MediaItem> = runBlocking {
        val items = CopyOnWriteArrayList<MediaItem>()

        coroutineScope { // wait for all child coroutines complete
            for (channelId in channelIds) {
                launch {
                    fetchFeed(channelId)?.let { items.addAll(it) }
                }
            }
        }

        return@runBlocking items.toMutableList()
    }

    private fun fetchFeeds2(vararg channelIds: String): MutableList<MediaItem> = runBlocking {
        val deferreds = channelIds.map { channelId ->
            async {
                fetchFeed(channelId)
            }
        }

        return@runBlocking deferreds.mapNotNull { it.await() }.flatten().toMutableList()
    }

    private suspend fun fetchFeed(channelId: String): List<MediaItem>? = withContext(Dispatchers.IO) {
        try {
            val rssContent = YouTubeInfoExtractor.downloadWebpage(RSS_URL + channelId)
            val result = YouTubeRssParser(Helpers.toStream(rssContent)).parse()
            syncWithChannel(channelId, result)
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Add missing props and remove shorts etc
     */
    private fun syncWithChannel(channelId: String, result: List<MediaItem>) {
        val group = getBrowseService2().getChannelAsGrid(channelId)
        val originItems = group?.mediaItems ?: return

        Helpers.removeIf(result) { item ->
            val first = originItems.firstOrNull { it?.videoId == item.videoId }
            if (first != null) {
                item as YouTubeMediaItem
                item.badgeText = first.badgeText
                item.isLive = first.isLive
                item.isUpcoming = first.isUpcoming
                item.videoPreviewUrl = first.videoPreviewUrl
                item.percentWatched = first.percentWatched

                return@removeIf false
            }

            return@removeIf true
        }
    }

    private fun getBrowseService2(): BrowseService2 = BrowseService2Wrapper
}
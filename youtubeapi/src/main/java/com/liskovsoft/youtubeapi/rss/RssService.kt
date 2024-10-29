package com.liskovsoft.youtubeapi.rss

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.browse.v2.BrowseService2
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

internal object RssService {
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private const val RSS_URL: String = "https://www.youtube.com/feeds/videos.xml?channel_id="

    @JvmStatic
    fun getFeed(vararg channelIds: String): MediaGroup? {
        val items = fetchFeedsSafe(*channelIds) ?: return null

        items.sortByDescending { it.publishedDate }

        return YouTubeMediaGroup(-1).apply {
            mediaItems = items
        }
    }

    private fun fetchFeedsSafe(vararg channelIds: String): MutableList<MediaItem>? {
        try {
            return fetchFeeds(*channelIds)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return null
    }

    private fun fetchFeeds(vararg channelIds: String): MutableList<MediaItem> = runBlocking {
        val items = mutableListOf<MediaItem>()

        coroutineScope { // wait for all child coroutines complete
            for (channelId in channelIds) {
                launch {
                    appendFeed(channelId, items)
                }
            }
        }

        return@runBlocking items
    }

    private suspend fun appendFeed(channelId: String, items: MutableList<MediaItem>) = withContext(Dispatchers.IO) {
        val rssContent = RetrofitHelper.get(mFileApi.getContent(RSS_URL + channelId))?.content
        rssContent?.let {
            val result = YouTubeRssParser(Helpers.toStream(rssContent)).parse()
            syncWithChannel(channelId, result)
            items.addAll(result)
        }
    }

    /**
     * Add missing props and remove shorts etc
     */
    private fun syncWithChannel(channelId: String, result: List<MediaItem>) {
        val group = BrowseService2.getChannelVideosFull(channelId)
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
}
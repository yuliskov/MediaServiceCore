package com.liskovsoft.youtubeapi.rss

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup

internal object RssService {
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private const val RSS_URL: String = "https://www.youtube.com/feeds/videos.xml?channel_id="

    fun getFeed(vararg channelIds: String): MediaGroup {
        val items = mutableListOf<MediaItem>()

        for (channelId in channelIds) {
            val rssContent = RetrofitHelper.get(mFileApi.getContent(RSS_URL + channelId))?.content
            rssContent?.let {
                val result = YouTubeRssParser(Helpers.toStream(rssContent)).parse()
                items.addAll(result)
            }
        }
        
        items.sortByDescending { it.publishedDate }

        return YouTubeMediaGroup(-1).apply {
            mediaItems = items
        }
    }
}
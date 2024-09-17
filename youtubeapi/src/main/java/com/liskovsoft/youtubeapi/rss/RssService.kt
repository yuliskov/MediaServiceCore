package com.liskovsoft.youtubeapi.rss

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.rss.bak.parser.YouTubeParser

internal object RssService {
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private const val RSS_URL: String = "https://www.youtube.com/feeds/videos.xml?channel_id="

    fun getFeed(vararg channelIds: String): MediaGroup? {
        val items = mutableListOf<MediaItem>()

        for (channelId in channelIds) {
            // get rss for channel
            // RssStandardParser().parse(response)
            // convert to mediagroup

            val rssContent = RetrofitHelper.get(mFileApi.getContent(RSS_URL + channelId))?.content
            rssContent?.let {
                val parser = YouTubeParser().parse(it)
                parser.items
            }
        }

        // sort items by date
        // convert to group

        return null
    }
}
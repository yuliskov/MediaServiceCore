package com.liskovsoft.youtubeapi.search.v2

import com.liskovsoft.youtubeapi.common.models.gen.ChannelItem
import com.liskovsoft.youtubeapi.common.models.gen.LockupItem
import com.liskovsoft.youtubeapi.common.models.gen.MusicItem
import com.liskovsoft.youtubeapi.common.models.gen.TileItem
import com.liskovsoft.youtubeapi.common.models.gen.VideoItem
import com.liskovsoft.youtubeapi.common.models.gen.getBadgeText
import com.liskovsoft.youtubeapi.common.models.gen.getChannelId
import com.liskovsoft.youtubeapi.common.models.gen.getSubTitle
import com.liskovsoft.youtubeapi.common.models.gen.getTitle
import com.liskovsoft.youtubeapi.common.models.gen.getVideoId
import com.liskovsoft.youtubeapi.common.models.gen.getViewCountText
import com.liskovsoft.youtubeapi.common.models.gen.getViewsAndPublished
import com.liskovsoft.youtubeapi.common.models.gen.getViewsCountText
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResultContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.next.v2.gen.getItems
import com.liskovsoft.youtubeapi.search.v2.gen.SearchResult
import com.liskovsoft.youtubeapi.search.v2.gen.getItemWrappers
import com.liskovsoft.youtubeapi.search.v2.gen.getSections
import org.junit.Assert

internal open class SearchServiceTestBase {
    protected fun checkSearchResultContinuation(searchResult: WatchNextResultContinuation?) {
        Assert.assertNotNull("Search result not null", searchResult)
        Assert.assertNotNull(
            "Search result contains items",
            searchResult?.getItems()
        )
        Assert.assertTrue("Search result contains more than one item", (searchResult?.getItems()?.size ?: 0) > 2)
        Assert.assertNotNull("Search result contains next key", searchResult?.getContinuationToken())

        val firstWrapper = searchResult?.getItems()?.firstOrNull()
        val tileItem = firstWrapper?.tileRenderer
        val videoItem = firstWrapper?.videoRenderer

        if (tileItem != null) {
            checkSearchResultTileItem(tileItem)
        }

        if (videoItem != null) {
            checkSearchResultVideoItem(videoItem)
        }
    }

    protected fun checkSearchResult(searchResult: SearchResult?) {
        Assert.assertNotNull("Search result not null", searchResult)
        Assert.assertNotNull("Search result contains items", searchResult?.getItemWrappers())
        Assert.assertTrue("Search result contains more than one item", (searchResult?.getItemWrappers()?.size ?: 0) > 2)

        val nextKey = searchResult?.getSections()?.firstNotNullOfOrNull { it?.nextPageKey }

        Assert.assertNotNull("At least one search result row contains next key", nextKey)

        var atLeastOneTypeFound = false

        searchResult?.getItemWrappers()?.firstNotNullOfOrNull { it?.videoRenderer }?.let {
            checkSearchResultVideoItem(it)
            atLeastOneTypeFound = true
        }

        searchResult?.getItemWrappers()?.firstNotNullOfOrNull { it?.gridChannelRenderer ?: it?.compactChannelRenderer ?: it?.pivotChannelRenderer }?.let {
            checkSearchResultChannelItem(it)
            atLeastOneTypeFound = true
        }

        searchResult?.getItemWrappers()?.firstNotNullOfOrNull { it?.tileRenderer }?.let {
            checkSearchResultTileItem(it)
            atLeastOneTypeFound = true
        }

        searchResult?.getItemWrappers()?.firstNotNullOfOrNull { it?.lockupViewModel }?.let {
            checkSearchResultLokupItem(it)
            atLeastOneTypeFound = true
        }

        Assert.assertTrue("At least one type of search items is found", atLeastOneTypeFound)
    }

    protected fun checkSearchResultTileItem(videoItem: TileItem?) {
        Assert.assertNotNull("Search result item not null", videoItem)
        Assert.assertTrue("Search result item contains video or channel id", videoItem?.getVideoId() != null || videoItem?.getChannelId() != null)
        Assert.assertNotNull("Search result item contains title", videoItem?.getTitle())
        //assertNotNull("Search result item contains channel id", videoItem.getChannelId()); // not exists in search result
        Assert.assertNotNull("Search result item contains view count", videoItem?.getViewCountText())
        //assertNotNull("Search result item contains date", videoItem.getPublishedTime()); // not exists anymore
    }

    protected fun checkSearchResultMusicItem(videoItem: MusicItem?) {
        Assert.assertNotNull("Search result item not null", videoItem)
        Assert.assertNotNull("Search result item contains video id", videoItem?.getVideoId())
        Assert.assertNotNull("Search result item contains title", videoItem?.getTitle())
        //assertNotNull("Search result item contains channel id", videoItem.getChannelId()); // not exists in search result
        Assert.assertNotNull("Search result item contains view count", videoItem?.getViewsCountText())
        Assert.assertNotNull("Search result item contains date", videoItem?.getViewsAndPublished())
    }

    protected fun checkSearchResultVideoItem(videoItem: VideoItem?) {
        Assert.assertNotNull("Search result item not null", videoItem)
        Assert.assertNotNull("Search result item contains video id", videoItem?.getVideoId())
        Assert.assertNotNull("Search result item contains title", videoItem?.getTitle())
        //assertNotNull("Search result item contains channel id", videoItem.getChannelId()); // not exists in search result
        Assert.assertNotNull("Search result item contains view count", videoItem?.viewCountText)
        Assert.assertNotNull("Search result item contains date", videoItem?.publishedTimeText)
    }

    private fun checkSearchResultChannelItem(channelItem: ChannelItem?) {
        Assert.assertNotNull("Search result item not null", channelItem)
        Assert.assertNotNull("Search result item contains channel id", channelItem?.getChannelId())
        Assert.assertNotNull("Search result item contains title", channelItem?.getTitle())
        Assert.assertNotNull("Search result item contains video count", channelItem?.videoCountText)
        Assert.assertNotNull("Search result item contains subscriber count", channelItem?.subscriberCountText)
    }

    private fun checkSearchResultLokupItem(lockupItem: LockupItem?) {
        Assert.assertNotNull("Search result item not null", lockupItem)
        Assert.assertNotNull("Search result item contains video id", lockupItem?.getVideoId())
        Assert.assertNotNull("Search result item contains title", lockupItem?.getTitle())
        //assertNotNull("Search result item contains channel id", lockupItem.getChannelId()); // not exists in search result
        Assert.assertNotNull("Search result item contains view count", lockupItem?.getBadgeText())
        Assert.assertNotNull("Search result item contains date", lockupItem?.getSubTitle())
    }
}
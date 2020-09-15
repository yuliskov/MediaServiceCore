package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SearchManagerTestBase {
    protected void checkSearchResultContinuation(SearchResultContinuation searchResult) {
        assertNotNull("Search result not null", searchResult);
        assertNotNull("Search result contains items", searchResult.getVideoItems());
        assertTrue("Search result contains more than one item", searchResult.getVideoItems().size() > 2);
        assertNotNull("Search result contains next key", searchResult.getNextPageKey());
        VideoItem videoItem = searchResult.getVideoItems().get(0);
        checkSearchResultVideoItem(videoItem);
    }

    protected void checkSearchResult(SearchResult searchResult) {
        assertNotNull("Search result not null", searchResult);
        assertNotNull("Search result contains items", searchResult.getVideoItems());
        assertTrue("Search result contains more than one item", searchResult.getVideoItems().size() > 2);
        assertNotNull("Search result contains next key", searchResult.getNextPageKey());
        VideoItem videoItem = searchResult.getVideoItems().get(0);
        checkSearchResultVideoItem(videoItem);
        List<ChannelItem> channelItems = searchResult.getChannelItems();
        if (channelItems != null) {
            ChannelItem channelItem = channelItems.get(0);
            checkSearchResultChannelItem(channelItem);
        }
    }

    protected void checkSearchResultMusicItem(MusicItem videoItem) {
        assertNotNull("Search result item not null", videoItem);
        assertNotNull("Search result item contains video id", videoItem.getVideoId());
        assertNotNull("Search result item contains title", videoItem.getTitle());
        //assertNotNull("Search result item contains channel id", videoItem.getChannelId()); // not exists in search result
        assertNotNull("Search result item contains view count", videoItem.getViewCountText());
        assertNotNull("Search result item contains date", videoItem.getPublishedText());
    }

    protected void checkSearchResultVideoItem(VideoItem videoItem) {
        assertNotNull("Search result item not null", videoItem);
        assertNotNull("Search result item contains video id", videoItem.getVideoId());
        assertNotNull("Search result item contains title", videoItem.getTitle());
        //assertNotNull("Search result item contains channel id", videoItem.getChannelId()); // not exists in search result
        assertNotNull("Search result item contains view count", videoItem.getViewCountText());
        assertNotNull("Search result item contains date", videoItem.getPublishedTime());
    }

    private void checkSearchResultChannelItem(ChannelItem channelItem) {
        assertNotNull("Search result item not null", channelItem);
        assertNotNull("Search result item contains channel id", channelItem.getChannelId());
        assertNotNull("Search result item contains title", channelItem.getTitle());
        assertNotNull("Search result item contains video count", channelItem.getVideoCountText());
        assertNotNull("Search result item contains subscriber count", channelItem.getSubscriberCountText());
    }
}

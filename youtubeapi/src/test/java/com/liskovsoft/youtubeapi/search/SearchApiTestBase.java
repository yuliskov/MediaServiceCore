package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.models.V2.TileItem;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchSection;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SearchApiTestBase {
    protected void checkSearchResultContinuation(SearchResultContinuation searchResult) {
        assertNotNull("Search result not null", searchResult);
        assertNotNull("Search result contains items",
                searchResult.getItemWrappers());
        assertTrue("Search result contains more than one item", searchResult.getItemWrappers().size() > 2);
        assertNotNull("Search result contains next key", searchResult.getNextPageKey());

        ItemWrapper firstWrapper = searchResult.getItemWrappers().get(0);
        TileItem tileItem = firstWrapper.getTileItem();
        VideoItem videoItem = firstWrapper.getVideoItem();

        if (tileItem != null) {
            checkSearchResultTileItem(tileItem);
        }

        if (videoItem != null) {
            checkSearchResultVideoItem(videoItem);
        }
    }

    protected void checkSearchResult(SearchResult searchResult) {
        assertNotNull("Search result not null", searchResult);
        assertNotNull("Search result contains items", searchResult.getItemWrappers());
        assertTrue("Search result contains more than one item", searchResult.getItemWrappers().size() > 2);

        String nexKey = null;

        for (SearchSection section : searchResult.getSections()) {
            if (section.getNextPageKey() != null) {
                nexKey = section.getNextPageKey();
                break;
            }
        }

        assertNotNull("At least one search result row contains next key", nexKey);

        for (ItemWrapper wrapper : searchResult.getItemWrappers()) {
            if (wrapper.getVideoItem() != null) {
                checkSearchResultVideoItem(wrapper.getVideoItem());
                break;
            }
        }

        for (ItemWrapper wrapper : searchResult.getItemWrappers()) {
            if (wrapper.getChannelItem() != null) {
                checkSearchResultChannelItem(wrapper.getChannelItem());
                break;
            }
        }
    }

    protected void checkSearchResultTileItem(TileItem videoItem) {
        assertNotNull("Search result item not null", videoItem);
        assertTrue("Search result item contains video or channel id", videoItem.getVideoId() != null || videoItem.getChannelId() != null);
        assertNotNull("Search result item contains title", videoItem.getTitle());
        //assertNotNull("Search result item contains channel id", videoItem.getChannelId()); // not exists in search result
        assertNotNull("Search result item contains view count", videoItem.getViewCountText());
        //assertNotNull("Search result item contains date", videoItem.getPublishedTime()); // not exists anymore
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
        assertNotNull("Search result item contains date", videoItem.getPublishedDate());
    }

    private void checkSearchResultChannelItem(ChannelItem channelItem) {
        assertNotNull("Search result item not null", channelItem);
        assertNotNull("Search result item contains channel id", channelItem.getChannelId());
        assertNotNull("Search result item contains title", channelItem.getTitle());
        assertNotNull("Search result item contains video count", channelItem.getVideoCountText());
        assertNotNull("Search result item contains subscriber count", channelItem.getSubscriberCountText());
    }
}

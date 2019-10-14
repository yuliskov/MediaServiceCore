package com.liskovsoft.youtubeapi.common;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SearchServiceTest {
    private SearchService mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        mService = new SearchService();
    }

    @Test
    public void testSearchNotEmpty() {
        List<VideoItem> items = mService.getSearch("any search text");
        assertTrue("search not empty?", items.size() != 0);

        List<VideoItem> nextItems = mService.getNextSearch();
        assertTrue("next search not empty?", nextItems.size() != 0);
    }
}
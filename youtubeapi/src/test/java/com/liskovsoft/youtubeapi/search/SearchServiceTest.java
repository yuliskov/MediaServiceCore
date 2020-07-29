package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.search.SearchService;
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
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

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
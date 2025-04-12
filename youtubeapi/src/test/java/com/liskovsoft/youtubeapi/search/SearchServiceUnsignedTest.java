package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SearchServiceUnsignedTest {
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
        SearchResult searchResult = mService.getSearch("any search text");
        assertTrue("search not empty?", searchResult.getItemWrappers().size() != 0);

        SearchResultContinuation nextSearchResult = mService.continueSearch(searchResult.getNextPageKey());
        assertTrue("next search not empty?", nextSearchResult.getTileItems().size() != 0);
    }
}
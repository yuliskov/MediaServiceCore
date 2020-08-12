package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SearchManagerUnsignedTest {
    private static final String SEARCH_TEXT = "thrones season 8 trailer";
    private static final String SEARCH_TEXT_SPECIAL = "What's Trending";
    private SearchManagerUnsigned mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(SearchManagerUnsigned.class);
    }

    @Test
    public void testThatSearchResultNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT));

        assertTrue("List > 2", wrapper.execute().body().getVideoItems().size() > 2);
    }

    @Test
    public void testThatSearchResultNotEmpty2() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT_SPECIAL));

        assertTrue("List > 2", wrapper.execute().body().getVideoItems().size() > 2);
    }

    @Test
    public void testThatSearchResultFieldsNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT));
        SearchResult searchResult = wrapper.execute().body();
        VideoItem videoItem = searchResult.getVideoItems().get(0);

        assertNotNull(searchResult.getNextPageKey());
        assertNotNull(searchResult.getReloadPageKey());
        assertNotNull(videoItem.getVideoId());
        assertNotNull(videoItem.getTitle());
    }

    @Test
    public void testThatContinuationResultNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchManagerParams.getSearchQuery(SEARCH_TEXT));
        SearchResult result = wrapper.execute().body();
        String nextPageKey = result.getNextPageKey();

        Call<SearchResultContinuation> wrapper2 = mService.continueSearchResult(SearchManagerParams.getNextSearchQuery(nextPageKey));
        SearchResultContinuation result2 = wrapper2.execute().body();

        assertTrue("List > 3", result2.getVideoItems().size() > 3);
    }
}
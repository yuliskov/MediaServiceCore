package com.liskovsoft.youtubeapi.search;

import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.search.models.NextSearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.converter.JsonPathConverterFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class SearchManagerTest {
    private static final String SEARCH_TEXT = "thrones season 8 trailer";
    private SearchManager mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.youtube.com") // ignored in case of full url
                .addConverterFactory(JsonPathConverterFactory.create())
                .build();

        mService = retrofit.create(SearchManager.class);
    }

    @Test
    public void testThatSearchResultNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchHelper.getSearchKey(), SearchHelper.getSearchQuery(SEARCH_TEXT));

        assertTrue("List > 2", wrapper.execute().body().getVideoItems().size() > 2);
    }

    @Test
    public void testThatSearchResultFieldsNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchHelper.getSearchKey(), SearchHelper.getSearchQuery(SEARCH_TEXT));
        SearchResult searchResult = wrapper.execute().body();
        VideoItem videoItem = searchResult.getVideoItems().get(0);

        assertNotNull(searchResult.getNextPageKey());
        assertNotNull(searchResult.getReloadPageKey());
        assertNotNull(videoItem.getVideoId());
        assertNotNull(videoItem.getTitle());
    }

    @Test
    public void testThatContinuationResultNotEmpty() throws IOException {
        Call<SearchResult> wrapper = mService.getSearchResult(SearchHelper.getSearchKey(), SearchHelper.getSearchQuery(SEARCH_TEXT));
        SearchResult result = wrapper.execute().body();
        String nextPageKey = result.getNextPageKey();

        Call<NextSearchResult> wrapper2 = mService.getNextPage(SearchHelper.getSearchKey(), SearchHelper.getNextSearchQuery(nextPageKey));
        NextSearchResult result2 = wrapper2.execute().body();

        assertTrue("List > 3", result2.getVideoItems().size() > 3);
    }
}
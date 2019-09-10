package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BrowseManagerTest {
    /**
     * Note: valid period - one hour
     */
    private static final String AUTHORIZATION =
            "Bearer ya29.Gl1_B10Mu_gNOjVSeIWX2pRvlIPN4m_zkpoT6vSiRdXHHpCPoua3tr3VSNPDBESi3AHGi81g3" +
            "-By8YLkTDhlewH2E3sluVlPAH3h6Z4aJv19UbgJ6_S3N5g06sULuK4";
    private BrowseManager mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(BrowseManager.class);
    }

    @Test
    public void testThatSubscriptionsNotEmpty() throws IOException {
        Call<BrowseResult> wrapper = mService.getBrowseResult(BrowseParams.getSubscriptionsQuery(), AUTHORIZATION);

        BrowseResult execute = wrapper.execute().body();

        assertNotNull("Items not null", execute);
        assertTrue("List > 2", execute.getVideoItems().size() > 2);

        String nextPageKey = execute.getNextPageKey();

        assertNotNull("Item not null", nextPageKey);

        Call<NextBrowseResult> browseResult = mService.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), AUTHORIZATION);
        NextBrowseResult body = browseResult.execute().body();

        assertNotNull("Items not null", body);
        assertTrue("List > 2", body.getVideoItems().size() > 2);
    }

    //@Test
    //public void testThatSearchResultNotEmpty2() throws IOException {
    //    Call<SearchResult> wrapper = mService.getSearchResult(SearchParams.getSearchKey(), SearchParams.getSearchQuery(SEARCH_TEXT_SPECIAL));
    //
    //    assertTrue("List > 2", wrapper.execute().body().getVideoItems().size() > 2);
    //}
    //
    //@Test
    //public void testThatSearchResultFieldsNotEmpty() throws IOException {
    //    Call<SearchResult> wrapper = mService.getSearchResult(SearchParams.getSearchKey(), SearchParams.getSearchQuery(SEARCH_TEXT));
    //    SearchResult searchResult = wrapper.execute().body();
    //    VideoItem videoItem = searchResult.getVideoItems().get(0);
    //
    //    assertNotNull(searchResult.getNextPageKey());
    //    assertNotNull(searchResult.getReloadPageKey());
    //    assertNotNull(videoItem.getVideoId());
    //    assertNotNull(videoItem.getTitle());
    //}
    //
    //@Test
    //public void testThatContinuationResultNotEmpty() throws IOException {
    //    Call<SearchResult> wrapper = mService.getSearchResult(SearchParams.getSearchKey(), SearchParams.getSearchQuery(SEARCH_TEXT));
    //    SearchResult result = wrapper.execute().body();
    //    String nextPageKey = result.getNextPageKey();
    //
    //    Call<NextSearchResult> wrapper2 = mService.getNextSearchResult(SearchParams.getSearchKey(), SearchParams.getNextSearchQuery(nextPageKey));
    //    NextSearchResult result2 = wrapper2.execute().body();
    //
    //    assertTrue("List > 3", result2.getVideoItems().size() > 3);
    //}
}
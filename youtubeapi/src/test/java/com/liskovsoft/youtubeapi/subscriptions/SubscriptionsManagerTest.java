package com.liskovsoft.youtubeapi.subscriptions;

import com.liskovsoft.youtubeapi.subscriptions.models.Subscriptions;
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
public class SubscriptionsManagerTest {
    private SubscriptionsManager mService;

    @Before
    public void setUp() {
        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(SubscriptionsManager.class);
    }

    @Test
    public void testThatSubscriptionsNotEmpty() throws IOException {
        Call<Subscriptions> wrapper = mService.getSubscriptions(SubscriptionsParams.getBrowseQuery());

        Response<Subscriptions> execute = wrapper.execute();
        assertNotNull("Items not null", execute.body());
        assertTrue("List > 2", execute.body().getVideoItems().size() > 2);
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
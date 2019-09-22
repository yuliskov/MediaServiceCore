package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.auth.BrowserAuth;
import com.liskovsoft.youtubeapi.auth.models.AccessToken;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import okhttp3.RequestBody;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BrowseManagerTest {
    /**
     * Note: valid period - one hour
     */
    private static String AUTHORIZATION =
            "Bearer ya29.Gl2AB3K2aSTC-z_IyCjzMOhLkAiPlssW_sAehCk-2sIy6lYeCtiOh0-BkMFr8lAu0eC7NsKPAYH9hykxS_v-LdAym4PmrUFKSrZIjBdthf1E1X1tPclK2OkYO5g2Xyk";
    private BrowseManager mService;
    private boolean mRunOnce;
    private static final String RAW_POST_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&grant_type=refresh_token&refresh_token=1%2FdXXiG98cBB9lJ9YwGpNmVzboP3X24FUdLcvE1Y0M8QWtTYHpWsakvNjPKuJlk68J";

    @Before
    public void setUp() throws IOException {
        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(BrowseManager.class);

        if (!mRunOnce) {
            mRunOnce = true;
            initToken();
        }
    }

    private void initToken() throws IOException {
        BrowserAuth authService = RetrofitHelper.withGson(BrowserAuth.class);
        Call<AccessToken> wrapper = authService.getAuthToken(RequestBody.create(null, RAW_POST_DATA.getBytes()));
        AccessToken token = wrapper.execute().body();
        AUTHORIZATION = String.format("%s %s", token.getTokenType(), token.getAccessToken());
    }

    @Test
    public void testThatSubscriptionsNotEmpty() throws IOException {
        Call<BrowseResult> wrapper = mService.getBrowseResult(BrowseParams.getSubscriptionsQuery(), AUTHORIZATION);

        BrowseResult browseResult1 = wrapper.execute().body();

        assertNotNull("Items not null", browseResult1);
        assertTrue("List > 2", browseResult1.getVideoItems().size() > 2);

        String nextPageKey = browseResult1.getNextPageKey();

        assertNotNull("Item not null", nextPageKey);

        Call<NextBrowseResult> browseResult2 = mService.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), AUTHORIZATION);
        NextBrowseResult body = browseResult2.execute().body();

        assertNotNull("Items not null", body);
        assertTrue("List > 2", body.getVideoItems().size() > 2);
    }

    @Test
    public void testThatRequiredFieldsExist() throws IOException {
        VideoItem videoItem = getSubscriptions().get(0);

        testFields(videoItem);

        videoItem = getRecommended().get(0);

        testFields(videoItem);
    }

    private List<VideoItem> getSubscriptions() throws IOException {
        Call<BrowseResult> wrapper = mService.getBrowseResult(BrowseParams.getSubscriptionsQuery(), AUTHORIZATION);

        BrowseResult browseResult1 = wrapper.execute().body();

        assertNotNull("Items not null", browseResult1);
        assertTrue("List > 2", browseResult1.getVideoItems().size() > 2);

        return browseResult1.getVideoItems();
    }

    private List<VideoItem> getRecommended() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseParams.getHomeQuery(), AUTHORIZATION);

        TabbedBrowseResult browseResult1 = wrapper.execute().body();

        assertNotNull("Items not null", browseResult1);
        assertTrue("List > 2", browseResult1.getBrowseTabs().get(0).getSections().get(0).getVideoItems().size() > 2);

        return browseResult1.getBrowseTabs().get(0).getSections().get(0).getVideoItems();
    }

    private void testFields(VideoItem videoItem) {
        assertNotNull("Field not null", videoItem.getTitle());
        assertNotNull("Field not null", videoItem.getUserName());
        assertNotNull("Field not null", videoItem.getThumbnails());
    }

    @Test
    public void testThatTabbedResultNotEmpty() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseParams.getHomeQuery(), AUTHORIZATION);

        TabbedBrowseResult browseResult1 = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult1);

        String nextPageKey = browseResult1.getBrowseTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        Call<NextBrowseResult> next = mService.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), AUTHORIZATION);
        Response<NextBrowseResult> execute = next.execute();
        NextBrowseResult browseResult2 = execute.body();

        tabbedNextResultNotEmpty(browseResult2);
    }

    private void tabbedNextResultNotEmpty(NextBrowseResult browseResult2) {
        assertNotNull("Tabbed result not empty", browseResult2);
        assertNotNull("Video list not empty", browseResult2.getVideoItems());
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        assertTrue("Video list > 2", browseResult2.getVideoItems().size() > 2);
    }

    private void tabbedResultNotEmpty(TabbedBrowseResult browseResult1) {
        assertNotNull("Tabbed result not empty", browseResult1);
        assertNotNull("Tabs list not empty", browseResult1.getBrowseTabs());
        assertTrue("Tabs list > 2", browseResult1.getBrowseTabs().size() > 2);
        assertTrue("Tabs sections > 2", browseResult1.getBrowseTabs().get(0).getSections().size() > 2);
    }
}
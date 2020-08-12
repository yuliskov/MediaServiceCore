package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.auth.AuthManager;
import com.liskovsoft.youtubeapi.auth.models.RefreshTokenResult;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
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
public class BrowseManagerSignedTest {
    /**
     * Authorization should be updated each hour
     */
    private String mAuthorization; // type: Bearer
    private BrowseManagerSigned mService;
    private boolean mRunOnce;
    private static final String RAW_POST_DATA = "client_id=861556708454-d6dlm3lh05idd8npek18k6be8ba3oc68.apps.googleusercontent.com&client_secret=SboVhoG9s0rNafixCSGGKXAT&refresh_token=1%2F%2F0ca0zVzDYAcWCCgYIARAAGAwSNwF-L9IrCkqjDqPyup8sXFA40LiTGh-8yW2jM4lLBOXyhcRa07fDM35jM-dU80PUemu1u1F8-AY&grant_type=refresh_token";

    @Before
    public void setUp() throws IOException {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(BrowseManagerSigned.class);

        if (!mRunOnce) {
            mRunOnce = true;
            initToken();
        }
    }

    private void initToken() throws IOException {
        AuthManager authService = RetrofitHelper.withGson(AuthManager.class);
        Call<RefreshTokenResult> wrapper = authService.getRefreshToken(RequestBody.create(null, RAW_POST_DATA.getBytes()));
        RefreshTokenResult token = wrapper.execute().body();
        mAuthorization = String.format("%s %s", token.getTokenType(), token.getAccessToken());
    }

    @Test
    public void testThatSubscriptionsNotEmpty() throws IOException {
        Call<BrowseResult> wrapper = mService.getBrowseResult(BrowseManagerParams.getSubscriptionsQuery(), mAuthorization);

        BrowseResult browseResult1 = wrapper.execute().body();

        assertNotNull("Items not null", browseResult1);
        assertTrue("List > 2", browseResult1.getVideoItems().size() > 2);

        String nextPageKey = browseResult1.getNextPageKey();

        assertNotNull("Item not null", nextPageKey);

        Call<NextBrowseResult> browseResult2 = mService.getNextBrowseResult(BrowseManagerParams.getNextBrowseQuery(nextPageKey), mAuthorization);
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
        Call<BrowseResult> wrapper = mService.getBrowseResult(BrowseManagerParams.getSubscriptionsQuery(), mAuthorization);

        BrowseResult browseResult1 = wrapper.execute().body();

        assertNotNull("Items not null", browseResult1);
        assertTrue("List > 2", browseResult1.getVideoItems().size() > 2);

        return browseResult1.getVideoItems();
    }

    private List<VideoItem> getRecommended() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseManagerParams.getHomeQuery(), mAuthorization);

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
    public void testThatHomeNotEmpty() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseManagerParams.getHomeQuery(), mAuthorization);

        TabbedBrowseResult browseResult1 = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult1);

        String nextPageKey = browseResult1.getBrowseTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        Call<NextBrowseResult> next = mService.getNextBrowseResult(BrowseManagerParams.getNextBrowseQuery(nextPageKey), mAuthorization);
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
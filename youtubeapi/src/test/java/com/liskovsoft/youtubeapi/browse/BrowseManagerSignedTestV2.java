package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.ver2.BrowseManagerParams;
import com.liskovsoft.youtubeapi.browse.ver2.BrowseManagerSigned;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabResult;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
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
public class BrowseManagerSignedTestV2 {
    /**
     * Authorization should be updated each hour
     */
    private BrowseManagerSigned mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.withJsonPath(BrowseManagerSigned.class);
    }

    @Test
    public void testThatSubscriptionsNotEmpty() throws IOException {
        Call<GridTabResult> wrapper = mService.getGridTabResult(BrowseManagerParams.getSubscriptionsQuery(), TestHelpers.getAuthorization());

        GridTabResult browseResult = wrapper.execute().body();

        assertNotNull("Contains tabs", browseResult.getTabs());

        GridTab firstTab = browseResult.getTabs().get(0);

        assertNotNull("Items not null", browseResult);
        assertTrue("List > 2", firstTab.getVideoItems().size() > 2);

        String nextPageKey = firstTab.getNextPageKey();

        assertNotNull("Item not null", nextPageKey);

        Call<GridTabContinuation> browseResult2 = mService.continueGridTab(BrowseManagerParams.getContinuationQuery(nextPageKey), TestHelpers.getAuthorization());
        GridTabContinuation body = browseResult2.execute().body();

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
        Call<GridTabResult> wrapper = mService.getGridTabResult(BrowseManagerParams.getSubscriptionsQuery(), TestHelpers.getAuthorization());

        GridTabResult browseResult = wrapper.execute().body();

        assertNotNull("Contains tabs", browseResult.getTabs());

        GridTab firstTab = browseResult.getTabs().get(0);

        assertNotNull("Items not null", firstTab);
        assertTrue("List > 2", firstTab.getVideoItems().size() > 2);

        return firstTab.getVideoItems();
    }

    private List<VideoItem> getRecommended() throws IOException {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getHomeQuery(), TestHelpers.getAuthorization());

        SectionTabResult browseResult = wrapper.execute().body();

        assertNotNull("Items not null", browseResult);
        assertTrue("List > 2", browseResult.getTabs().get(0).getSections().get(0).getVideoItems().size() > 2);

        return browseResult.getTabs().get(0).getSections().get(0).getVideoItems();
    }

    private void testFields(VideoItem videoItem) {
        assertNotNull("Field not null", videoItem.getTitle());
        assertNotNull("Field not null", videoItem.getUserName());
        assertNotNull("Field not null", videoItem.getThumbnails());
    }

    @Test
    public void testThatHomeNotEmpty() throws IOException {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getHomeQuery(), TestHelpers.getAuthorization());

        SectionTabResult browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        assertNotNull("Contains tabs", browseResult.getTabs());

        String nextPageKey = browseResult.getTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), TestHelpers.getAuthorization());
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        tabbedNextResultNotEmpty(browseResult2);
    }

    private void tabbedNextResultNotEmpty(SectionContinuation browseResult) {
        assertNotNull("Tabbed result not empty", browseResult);
        assertNotNull("Video list not empty", browseResult.getVideoItems());
        //assertNotNull("Next key not empty", browseResult.getNextPageKey());
        assertTrue("Video list > 2", browseResult.getVideoItems().size() > 2);
    }

    private void tabbedResultNotEmpty(SectionTabResult browseResult) {
        assertNotNull("Tabbed result not empty", browseResult);
        assertNotNull("Tabs list not empty", browseResult.getTabs());
        assertTrue("Tabs list > 2", browseResult.getTabs().size() > 2);
        assertTrue("Tabs sections > 2", browseResult.getTabs().get(0).getSections().size() > 2);
    }
}
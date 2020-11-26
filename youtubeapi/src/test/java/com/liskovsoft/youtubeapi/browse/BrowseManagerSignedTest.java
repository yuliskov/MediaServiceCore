package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.guide.Guide;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabList;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
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
public class BrowseManagerSignedTest {
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
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseManagerParams.getSubscriptionsQuery(), TestHelpersV2.getAuthorization());

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        assertNotNull("Contains tabs", browseResult.getTabs());

        GridTab firstTab = browseResult.getTabs().get(0);

        assertNotNull("Items not null", browseResult);
        assertTrue("List > 2", firstTab.getItemWrappers().size() > 2);

        String nextPageKey = firstTab.getNextPageKey();

        assertNotNull("Item not null", nextPageKey);

        Call<GridTabContinuation> browseResult2 = mService.continueGridTab(BrowseManagerParams.getContinuationQuery(nextPageKey), TestHelpersV2.getAuthorization());
        GridTabContinuation body = browseResult2.execute().body();

        assertNotNull("Items not null", body);
        assertTrue("List > 2", body.getItemWrappers().size() > 2);
    }

    @Test
    public void testThatSubscribedChannelsContainsRequiredFields() {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseManagerParams.getSubscriptionsQuery(), TestHelpersV2.getAuthorization());

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        assertNotNull("Contains tabs", browseResult.getTabs());

        // First tab is subscriptions. Others are channels.
        for (GridTab tab : browseResult.getTabs().subList(1, browseResult.getTabs().size())) {
            checkChannelTab(tab);
        }
    }

    @Test
    public void testThatChannelHasContent() {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseManagerParams.getSubscriptionsQuery(), TestHelpersV2.getAuthorization());

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        assertTrue("Contains tabs", browseResult.getTabs() != null && browseResult.getTabs().size() >= 10);

        GridTab channel = browseResult.getTabs().get(10);

        Call<GridTabContinuation> wrapper2 =
                mService.continueGridTab(BrowseManagerParams.getContinuationQuery(channel.getReloadPageKey()), TestHelpersV2.getAuthorization());

        GridTabContinuation continuation = RetrofitHelper.get(wrapper2);

        assertTrue("Channel hash content", continuation.getItemWrappers().size() > 10);
    }

    @Test
    public void testThatRequiredFieldsExist() throws IOException {
        ItemWrapper videoItem = getSubscriptions().get(0);

        testFields(videoItem);

        videoItem = getRecommended().get(0);

        testFields(videoItem);
    }

    private List<ItemWrapper> getSubscriptions() throws IOException {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseManagerParams.getSubscriptionsQuery(), TestHelpersV2.getAuthorization());

        GridTabList browseResult = wrapper.execute().body();

        assertNotNull("Contains tabs", browseResult.getTabs());

        GridTab firstTab = browseResult.getTabs().get(0);

        assertNotNull("Items not null", firstTab);
        assertTrue("List > 2", firstTab.getItemWrappers().size() > 2);

        return firstTab.getItemWrappers();
    }

    private List<ItemWrapper> getRecommended() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery(), TestHelpersV2.getAuthorization());

        SectionTabList browseResult = wrapper.execute().body();

        assertNotNull("Items not null", browseResult);
        assertTrue("List > 2", browseResult.getTabs().get(0).getSections().get(0).getItemWrappers().size() > 2);

        return browseResult.getTabs().get(0).getSections().get(0).getItemWrappers();
    }

    private void testFields(ItemWrapper itemWrapper) {
        testFields(itemWrapper.getVideoItem());
    }

    private void testFields(VideoItem videoItem) {
        assertNotNull("Field not null", videoItem.getTitle());
        assertNotNull("Field not null", videoItem.getUserName());
        assertNotNull("Field not null", videoItem.getThumbnails());
    }

    @Test
    public void testThatHomeNotEmpty() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery(), TestHelpersV2.getAuthorization());

        SectionTabList browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        assertNotNull("Contains tabs", browseResult.getTabs());

        String nextPageKey = browseResult.getTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), TestHelpersV2.getAuthorization());
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        tabbedNextResultNotEmpty(browseResult2);
    }

    @Test
    public void testThatPlaylistsNotEmpty() {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseManagerParams.getMyLibraryQuery(), TestHelpersV2.getAuthorization());

        GridTabList gridTabResult = RetrofitHelper.get(wrapper);

        assertTrue("Contains playlists", gridTabResult.getTabs().size() > 3);

        GridTab lastGridTab = gridTabResult.getTabs().get(5);

        assertNotNull("Contains reload key", lastGridTab.getReloadPageKey());

          Call<GridTabContinuation> wrapper2 =
                mService.continueGridTab(BrowseManagerParams.getContinuationQuery(lastGridTab.getReloadPageKey()), TestHelpersV2.getAuthorization());

        GridTabContinuation gridTabContinuation = RetrofitHelper.get(wrapper2);

        assertTrue("Grid tab not empty", gridTabContinuation.getItemWrappers().size() > 3);

        assertNotNull("Contains next key", gridTabContinuation.getNextPageKey());
    }

    @Test
    public void testThatGuideNotEmpty() {
        Call<Guide> wrapper = mService.getGuide(BrowseManagerParams.getGuideQuery(), TestHelpersV2.getAuthorization());
        Guide guide = RetrofitHelper.get(wrapper);

        assertNotNull("Guide not null", guide);
        assertTrue("Guide contains items", guide.getItems().size() > 5);

        assertNotNull("Guide contains suggest token", guide.getSuggestToken());
    }

    private void tabbedNextResultNotEmpty(SectionContinuation browseResult) {
        assertNotNull("Tabbed result not empty", browseResult);
        assertNotNull("Video list not empty", browseResult.getItemWrappers());
        //assertNotNull("Next key not empty", browseResult.getNextPageKey());
        assertTrue("Video list > 2", browseResult.getItemWrappers().size() > 2);
    }

    private void tabbedResultNotEmpty(SectionTabList browseResult) {
        assertNotNull("Tabbed result not empty", browseResult);
        assertNotNull("Tabs list not empty", browseResult.getTabs());
        assertTrue("Tabs list > 2", browseResult.getTabs().size() >= 1);
        assertTrue("Tabs sections > 2", browseResult.getTabs().get(0).getSections().size() > 2);
    }

    private void checkChannelTab(GridTab tab) {
        if (tab.isUnselectable()) {
            return;
        }

        assertNotNull("Contains title", tab.getTitle());
        assertNotNull("Contains alt title", tab.getTitleAlt());
        assertNotNull("Contains reload key", tab.getReloadPageKey());
        assertNotNull("Contains thumbs", tab.getThumbnails());
    }
}
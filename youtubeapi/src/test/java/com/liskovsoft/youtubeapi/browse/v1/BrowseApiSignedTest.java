package com.liskovsoft.youtubeapi.browse.v1;

import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.guide.Guide;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.Chip;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTabList;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.v1.models.sections.SectionTabList;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import org.junit.Before;
import org.junit.Ignore;
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

@Ignore("Old api")
@RunWith(RobolectricTestRunner.class)
public class BrowseApiSignedTest extends BrowseApiTestBase {
    /**
     * Authorization should be updated each hour
     */
    private BrowseApi mService;

    @Before
    public void setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        mService = RetrofitHelper.create(BrowseApi.class);

        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());
    }

    @Test
    public void testThatSubscriptionsNotEmpty() throws IOException {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseApiHelper.getSubscriptionsQuery());

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        assertNotNull("Contains tabs", browseResult.getTabs());

        GridTab firstTab = browseResult.getTabs().get(0);

        assertNotNull("Items not null", browseResult);
        assertTrue("List > 2", firstTab.getItemWrappers().size() > 2);

        String nextPageKey = firstTab.getNextPageKey();

        assertNotNull("Item not null", nextPageKey);

        Call<GridTabContinuation> browseResult2 = mService.continueGridTab(BrowseApiHelper.getContinuationQuery(nextPageKey));
        GridTabContinuation body = browseResult2.execute().body();

        assertNotNull("Items not null", body);
        assertTrue("List > 2", body.getItemWrappers().size() > 2);
    }

    @Test
    public void testThatSubscribedChannelsContainsRequiredFields() {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseApiHelper.getSubscriptionsQuery());

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        assertNotNull("Contains tabs", browseResult.getTabs());

        // First tab is subscriptions. Others are channels.
        for (GridTab tab : browseResult.getTabs().subList(1, browseResult.getTabs().size())) {
            checkChannelTab(tab);
        }
    }

    @Test
    public void testThatChannelHasContent() {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseApiHelper.getSubscriptionsQuery());

        GridTabList browseResult = RetrofitHelper.get(wrapper);

        assertTrue("Contains tabs", browseResult.getTabs() != null && browseResult.getTabs().size() >= 10);

        GridTab channel = browseResult.getTabs().get(10);

        Call<GridTabContinuation> wrapper2 =
                mService.continueGridTab(BrowseApiHelper.getContinuationQuery(channel.getReloadPageKey()));

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
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseApiHelper.getSubscriptionsQuery());

        GridTabList browseResult = wrapper.execute().body();

        assertNotNull("Contains tabs", browseResult.getTabs());

        GridTab firstTab = browseResult.getTabs().get(0);

        assertNotNull("Items not null", firstTab);
        assertTrue("List > 2", firstTab.getItemWrappers().size() > 2);

        return firstTab.getItemWrappers();
    }

    private List<ItemWrapper> getRecommended() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseApiHelper.getHomeQuery());

        SectionTabList browseResult = wrapper.execute().body();

        assertNotNull("Items not null", browseResult);
        assertTrue("List > 2", browseResult.getTabs().get(0).getSections().get(0).getItemWrappers().size() > 2);

        return browseResult.getTabs().get(0).getSections().get(0).getItemWrappers();
    }

    @Test
    public void testThatHomeNotEmpty() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseApiHelper.getHomeQuery());

        SectionTabList browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        assertNotNull("Contains tabs", browseResult.getTabs());

        String nextPageKey = browseResult.getTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        Call<SectionContinuation> next = mService.continueSection(BrowseApiHelper.getContinuationQuery(nextPageKey));
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        tabbedNextResultNotEmpty(browseResult2);
    }

    @Test
    public void testThatGamesNotEmpty() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseApiHelper.getGamingQuery());

        SectionTabList browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        assertNotNull("Contains tabs", browseResult.getTabs());

        List<ItemWrapper> itemWrappers = browseResult.getTabs().get(0).getSections().get(0).getItemWrappers();
        assertNotNull("Items not null", itemWrappers);
    }

    @Test
    public void testThatPlaylistsNotEmpty() {
        Call<GridTabList> wrapper = mService.getGridTabList(BrowseApiHelper.getMyLibraryQuery());

        GridTabList gridTabResult = RetrofitHelper.get(wrapper);

        assertTrue("Contains playlists", gridTabResult.getTabs().size() > 3);

        GridTabContinuation lastContinuation = null;

        for (int i = 5; i < gridTabResult.getTabs().size(); i++) { // starting from the users playlists
            GridTab lastGridTab = gridTabResult.getTabs().get(i);
            Call<GridTabContinuation> wrapper2 =  mService.continueGridTab(BrowseApiHelper.getContinuationQuery(lastGridTab.getReloadPageKey()));
            GridTabContinuation gridTabContinuation = RetrofitHelper.get(wrapper2);

            if (gridTabContinuation != null && gridTabContinuation.getItemWrappers().size() > 3 && gridTabContinuation.getNextPageKey() != null) {
                lastContinuation = gridTabContinuation;
                break;
            }
        }

        assertTrue("Grid tab not empty", lastContinuation != null && lastContinuation.getItemWrappers().size() > 3);
        assertNotNull("Contains next key", lastContinuation.getNextPageKey());
    }

    @Test
    public void testThatGuideNotEmpty() {
        Call<Guide> wrapper = mService.getGuide(BrowseApiHelper.getGuideQuery());
        Guide guide = RetrofitHelper.get(wrapper);

        assertNotNull("Guide not null", guide);
        assertTrue("Guide contains items", guide.getItems().size() > 5);

        // No SUGGEST param in the result (probably a bug!!!)
        //assertNotNull("Guide contains suggest token", guide.getSuggestToken());
    }

    @Ignore("Old api")
    @Test
    public void testThatHomeChipsNotEmpty() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseApiHelper.getHomeQuery());

        SectionTabList browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        assertNotNull("Contains tabs", browseResult.getTabs());

        Section section = getChipsSection(browseResult.getTabs().get(0));

        assertNotNull("Contains chips", section.getChips());

        Chip firstChip = section.getChips().get(0);

        assertNotNull("Chip contains title", firstChip.getTitle());
        
        assertNotNull("Chip contains reload key", firstChip.getReloadPageKey());

        Call<SectionContinuation> next = mService.continueSection(BrowseApiHelper.getContinuationQuery(firstChip.getReloadPageKey()));
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        tabbedNextResultNotEmpty(browseResult2);
    }

    private Section getChipsSection(SectionTab sectionTab) throws IOException {
        for (Section section : sectionTab.getSections()) {
            if (section.getChips() != null) {
                return section;
            }
        }

        Call<SectionTabContinuation> continuation = mService.continueSectionTab(BrowseApiHelper.getContinuationQuery(sectionTab.getNextPageKey()));


        return getChipsSection(continuation.execute().body());
    }

    private Section getChipsSection(SectionTabContinuation continuationTab) throws IOException {
        for (Section section : continuationTab.getSections()) {
            if (section.getChips() != null) {
                return section;
            }
        }

        Call<SectionTabContinuation> continuation = mService.continueSectionTab(continuationTab.getNextPageKey());


        return getChipsSection(continuation.execute().body());
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
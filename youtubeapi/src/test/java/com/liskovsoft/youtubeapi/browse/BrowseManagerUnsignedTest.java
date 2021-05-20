package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabList;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
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

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BrowseManagerUnsignedTest extends BrowseManagerTestBase {
    private BrowseManagerUnsigned mService;

    @Before
    public void setUp() throws IOException {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        RetrofitHelper.sForceEnableProfiler = true;

        mService = RetrofitHelper.withJsonPath(BrowseManagerUnsigned.class);
    }

    @Test
    public void testThatRequiredFieldsExist() throws IOException {
        Section recommended = getRecommended();

        List<ItemWrapper> itemWrappers = recommended.getItemWrappers();

        VideoItem firstVideo = null;
        MusicItem firstMusic = null;

        for (ItemWrapper itemWrapper : itemWrappers) {
            if (firstVideo != null && firstMusic != null) {
                break;
            }

            if (firstVideo == null) {
                firstVideo = itemWrapper.getVideoItem();
            }

            if (firstMusic == null) {
                firstMusic = itemWrapper.getMusicItem();
            }
        }

        if (firstVideo != null) {
            testFields(firstVideo);
        }

        if (firstMusic != null) {
            testFields(firstMusic);
        }
    }

    private Section getRecommended() {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery());

        SectionTabList browseResult = RetrofitHelper.get(wrapper);

        assertNotNull("Items not null", browseResult);
        Section section = firstNotEmptyTab(browseResult).getSections().get(0);
        assertTrue("List > 2",
                section.getItemWrappers().size() > 2);

        return section;
    }

    @Test
    public void testEnsureNextResultIsUnique() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery());

        SectionTabList browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        Section firstSection = firstNotEmptyTab(browseResult).getSections().get(0);

        String nextPageKey = firstSection.getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), visitorId);
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation nextBrowseResult = execute.body();

        List<ItemWrapper> videoItems = firstSection.getItemWrappers();
        List<ItemWrapper> nextVideoItems = nextBrowseResult.getItemWrappers();

        for (int i = 0; i < videoItems.size(); i++) {
            ItemWrapper first = videoItems.get(i);

            for (int j = 0; j < nextVideoItems.size(); j++) {
                ItemWrapper second = nextVideoItems.get(j);
                assertNotNull("Item not null " + nextVideoItems, second);
                testThatItemsIsUnique(first, second);
            }
        }
    }

    @Test
    public void testThatTabbedResultNotEmpty() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery());

        SectionTabList browseResult1 = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult1);

        String nextPageKey = firstNotEmptyTab(browseResult1).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult1.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), visitorId);
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        nextSectionResultNotEmpty(browseResult2);

        String nextTabbedPageKey = firstNotEmptyTab(browseResult1).getNextPageKey();
        Call<SectionTabContinuation> nextTabbed = mService.continueSectionTab(BrowseManagerParams.getContinuationQuery(nextTabbedPageKey), visitorId);
        Response<SectionTabContinuation> executeTabbed = nextTabbed.execute();
        SectionTabContinuation browseTabbedResult2 = executeTabbed.body();

        nextTabbedResultNotEmpty(browseTabbedResult2);
    }

    @Test
    public void testThatAllTabsSectionHaveTitles() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery());

        Response<SectionTabList> execute = wrapper.execute();
        SectionTabList browseResult = execute.body();

        tabbedResultNotEmpty(browseResult);

        List<Section> sections = firstNotEmptyTab(browseResult).getSections();

        for (Section section : sections) {
            assertNotNull("All sections should have names", section.getTitle());
        }
    }

    @Test
    public void testThatMusicTabNotEmpty() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getMusicQuery());

        Response<SectionTabList> execute = wrapper.execute();
        SectionTabList browseResult = execute.body();

        tabbedResultNotEmpty(browseResult);
    }

    @Test
    public void testThatChannelResultNotEmpty() throws IOException {
        Call<SectionList> wrapper = mService.getSectionList(BrowseManagerParams.getChannelQuery(TestHelpersV1.CHANNEL_ID_UNSUBSCRIBED));

        Response<SectionList> execute = wrapper.execute();
        SectionList browseResult = execute.body();

        sectionListResultNotEmpty(browseResult);
    }

    @Test
    public void testThatMusicTabContinuationIsUnique() {
        SectionTabList browseResult = getMusicTab();

        String visitorData = browseResult.getVisitorData();

        Section firstSection = firstNotEmptyTab(browseResult).getSections().get(0);

        Call<SectionContinuation> wrapper2 =
                mService.continueSection(BrowseManagerParams.getContinuationQuery(firstSection.getNextPageKey()), visitorData);

        SectionContinuation musicContinuation = RetrofitHelper.get(wrapper2);

        ItemWrapper itemWrapper = firstSection.getItemWrappers().get(0);

        ItemWrapper continuationItemWrapper = musicContinuation.getItemWrappers().get(0);

        testThatItemsIsUnique(itemWrapper, continuationItemWrapper);
    }

    @Test
    public void testThatPlaylistItemContainsRequiredFields() {
        SectionTabList browseResult = getMusicTab();

        Section playlistSection = findPlaylistSection(firstNotEmptyTab(browseResult));

        if (playlistSection != null) {
            testFields(playlistSection.getItemWrappers().get(0));
        }
    }

    @Test
    public void testThatVideoContainsAnimatedPreview() {
        Section recommended = getRecommended();

        VideoItem videoItem = null;

        for (ItemWrapper itemWrapper : recommended.getItemWrappers()) {
            if (itemWrapper.getVideoItem() != null && !itemWrapper.getVideoItem().isLive()) {
                videoItem = itemWrapper.getVideoItem();
                break;
            }
        }

        assertNotNull("Video contains animated previews", videoItem.getRichThumbnailUrl());
    }

    private Section findPlaylistSection(SectionTab tab) {
        if (tab == null) {
            return null;
        }

        Section result = null;

        for (Section section : tab.getSections()) {
            if (section.getItemWrappers() != null && section.getItemWrappers().get(0).getPlaylistItem() != null) {
                result = section;
                break;
            }
        }

        return result;
    }

    private SectionTabList getMusicTab() {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getMusicQuery());

        SectionTabList browseResult = RetrofitHelper.get(wrapper);

        tabbedResultNotEmpty(browseResult);

        return browseResult;
    }

    private SectionTab firstNotEmptyTab(SectionTabList browseResult) {
        if (browseResult == null) {
            return null;
        }

        SectionTab result = null;

        for (SectionTab tab : browseResult.getTabs()) {
            if (tab.getSections() != null) {
                result = tab;
                break;
            }
        }

        return result;
    }

    private void nextSectionResultNotEmpty(SectionContinuation browseResult) {
        assertNotNull("Next section result: not empty", browseResult);
        assertTrue("Next section result: item list not empty", browseResult.getItemWrappers() != null);
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        assertTrue("Next section result: item list > 2", browseResult.getItemWrappers().size() > 2);
    }

    private void nextTabbedResultNotEmpty(SectionTabContinuation browseResult) {
        assertNotNull("Tabbed result: not empty", browseResult);
        assertNotNull("Tabbed result: media item list not empty", browseResult.getSections());
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        Section section = browseResult.getSections().get(0);
        assertNotNull("Tabbed result: media item list not empty", section.getItemWrappers());
    }

    private void tabbedResultNotEmpty(SectionTabList browseResult) {
        assertNotNull("Tabbed result not empty", browseResult);
        assertNotNull("Tabs list not empty", browseResult.getTabs());
        assertTrue("Tabs list > 2", browseResult.getTabs().size() >= 1);

        SectionTab browseTab = null;

        // get first not empty
        for (SectionTab tab : browseResult.getTabs()) {
            if (tab.getSections() != null) {
                browseTab = tab;
                break;
            }
        }

        assertNotNull("Tab is not null", browseTab);
        assertTrue("Tabs sections > 2", browseTab.getSections().size() > 2);
    }

    private void sectionListResultNotEmpty(SectionList sectionList) {
        assertNotNull("Section result not empty", sectionList);
        assertNotNull("Section list not empty", sectionList.getSections());
        assertTrue("Section list > 2", sectionList.getSections().size() > 2);
    }
}
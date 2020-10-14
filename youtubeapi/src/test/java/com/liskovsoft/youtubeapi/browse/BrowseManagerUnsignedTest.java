package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabList;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
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
public class BrowseManagerUnsignedTest {
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

    private Section getRecommended() throws IOException {
        Call<SectionTabList> wrapper = mService.getSectionTabList(BrowseManagerParams.getHomeQuery());

        SectionTabList browseResult = wrapper.execute().body();

        assertNotNull("Items not null", browseResult);
        Section section = firstNotEmptyTab(browseResult).getSections().get(0);
        assertTrue("List > 2",
                section.getItemWrappers().size() > 2);

        return section;
    }

    private void testFields(ItemWrapper itemWrapper) {
        if (itemWrapper.getPlaylistItem() != null) {
            testFields(itemWrapper.getPlaylistItem());
        } else if (itemWrapper.getVideoItem() != null) {
            testFields(itemWrapper.getVideoItem());
        } else if (itemWrapper.getMusicItem() != null) {
            testFields(itemWrapper.getMusicItem());
        }
    }

    private void testFields(PlaylistItem playlistItem) {
        assertNotNull("Title not null", playlistItem.getTitle());
        assertNotNull("Description not null", playlistItem.getDescription());
        String playlistId = playlistItem.getPlaylistId();
        assertNotNull("Playlist Id not null", playlistId);
        assertNotNull("Video count not null: " + playlistId, playlistItem.getVideoCountText());
        assertNotNull("Channel not null: " + playlistId, playlistItem.getChannelId());
        assertNotNull("Thumbs not null: " + playlistId, playlistItem.getThumbnails());
    }

    private void testFields(VideoItem videoItem) {
        assertNotNull("Title not null", videoItem.getTitle());
        String videoId = videoItem.getVideoId();
        assertNotNull("Id not null", videoId);
        assertTrue("Time not null or live: " + videoId, videoItem.getPublishedTime() != null || videoItem.isLive());
        assertNotNull("Views not null: " + videoId, videoItem.getViewCountText());
        assertTrue("Length not null or live: " + videoId, videoItem.getLengthText() != null || videoItem.isLive());
        //assertNotNull("Channel not null: " + videoId, videoItem.getChannelId());
        assertNotNull("User not null: " + videoId, videoItem.getUserName());
        assertNotNull("Thumbs not null: " + videoId, videoItem.getThumbnails());
    }

    private void testFields(MusicItem musicItem) {
        assertNotNull("Title not null", musicItem.getTitle());
        String videoId = musicItem.getVideoId();
        assertNotNull("Id not null", videoId);
        assertTrue("Time not null: " + videoId, musicItem.getPublishedText() != null);
        assertNotNull("Views not null: " + videoId, musicItem.getViewCountText());
        assertNotNull("Length not null: " + videoId, musicItem.getLengthText());
        assertNotNull("Channel not null: " + videoId, musicItem.getChannelId());
        assertNotNull("User not null: " + videoId, musicItem.getUserName());
        assertNotNull("Thumbs not null: " + videoId, musicItem.getThumbnails());
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
                assertNotEquals("All items are unique", first.getVideoItem().getTitle(), second.getVideoItem().getTitle());
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
        Call<SectionList> wrapper = mService.getSectionList(BrowseManagerParams.getChannelQuery(TestHelpers.CHANNEL_ID_UNSUBSCRIBED));

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

        String video1;
        String video2;

        ItemWrapper itemWrapper = firstSection.getItemWrappers().get(0);

        if (itemWrapper.getMusicItem() != null) {
            video1 = itemWrapper.getMusicItem().getVideoId();
            video2 = musicContinuation.getItemWrappers().get(0).getMusicItem().getVideoId();
        } else {
            video1 = itemWrapper.getRadioItem().getVideoId();
            video2 = musicContinuation.getItemWrappers().get(0).getRadioItem().getVideoId();
        }

        assertNotNull("Video1 not null", video1);
        assertNotNull("Video2 not null", video2);

        assertNotEquals("Music tab continuation is unique", video1, video2);
    }

    @Test
    public void testThatPlaylistItemContainsRequiredFields() {
        SectionTabList browseResult = getMusicTab();

        Section playlistSection = findPlaylistSection(firstNotEmptyTab(browseResult));

        testFields(playlistSection.getItemWrappers().get(0));
    }

    private Section findPlaylistSection(SectionTab tab) {
        if (tab == null) {
            return null;
        }

        Section result = null;

        for (Section section : tab.getSections()) {
            if (section.getItemWrappers().get(0).getPlaylistItem() != null) {
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
        assertTrue("Tabs list > 2", browseResult.getTabs().size() > 2);

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
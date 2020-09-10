package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.ver2.BrowseManagerParams;
import com.liskovsoft.youtubeapi.browse.ver2.BrowseManagerUnsigned;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
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
public class BrowseManagerUnsignedTestV2 {
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
        List<VideoItem> videoItems = recommended.getVideoItems();

        if (videoItems != null) {
            testFields(videoItems.get(0));
        }

        List<MusicItem> musicItems = recommended.getMusicItems();

        if (musicItems != null) {
            testFields(musicItems.get(0));
        }
    }

    private Section getRecommended() throws IOException {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getHomeQuery());

        SectionTabResult browseResult = wrapper.execute().body();

        assertNotNull("Items not null", browseResult);
        Section section = browseResult.getTabs().get(0).getSections().get(0);
        assertTrue("List > 2",
                section.getVideoItems().size() > 2 || section.getMusicItems().size() > 2 || section.getChannelItems().size() > 2);

        return section;
    }

    private void testFields(VideoItem videoItem) {
        assertNotNull("Title not null", videoItem.getTitle());
        String videoId = videoItem.getVideoId();
        assertNotNull("Id not null", videoId);
        assertTrue("Time not null or live: " + videoId, videoItem.getPublishedTime() != null || videoItem.isLive());
        assertNotNull("Views not null: " + videoId, videoItem.getViewCountText());
        assertTrue("Length not null or live: " + videoId, videoItem.getLengthText() != null || videoItem.isLive());
        assertNotNull("Channel not null: " + videoId, videoItem.getChannelId());
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
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getHomeQuery());

        SectionTabResult browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        Section firstSection = browseResult.getTabs().get(0).getSections().get(0);

        String nextPageKey = firstSection.getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), visitorId);
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation nextBrowseResult = execute.body();

        List<VideoItem> videoItems = firstSection.getVideoItems();
        List<VideoItem> nextVideoItems = nextBrowseResult.getVideoItems();

        for (int i = 0; i < videoItems.size(); i++) {
            VideoItem first = videoItems.get(i);

            for (int j = 0; j < nextVideoItems.size(); j++) {
                VideoItem second = nextVideoItems.get(j);
                assertNotEquals("All items are unique", first.getTitle(), second.getTitle());
            }
        }
    }

    @Test
    public void testThatTabbedResultNotEmpty() throws IOException {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getHomeQuery());

        SectionTabResult browseResult1 = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult1);

        String nextPageKey = browseResult1.getTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult1.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), visitorId);
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        nextSectionResultNotEmpty(browseResult2);

        String nextTabbedPageKey = browseResult1.getTabs().get(0).getNextPageKey();
        Call<SectionTabContinuation> nextTabbed = mService.continueSectionTab(BrowseManagerParams.getContinuationQuery(nextTabbedPageKey), visitorId);
        Response<SectionTabContinuation> executeTabbed = nextTabbed.execute();
        SectionTabContinuation browseTabbedResult2 = executeTabbed.body();

        nextTabbedResultNotEmpty(browseTabbedResult2);
    }

    @Test
    public void testThatAllTabsSectionHaveTitles() throws IOException {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getHomeQuery());

        Response<SectionTabResult> execute = wrapper.execute();
        SectionTabResult browseResult = execute.body();

        tabbedResultNotEmpty(browseResult);

        List<Section> sections = browseResult.getTabs().get(0).getSections();

        for (Section section : sections) {
            assertNotNull("All sections should have names", section.getTitle());
        }
    }

    @Test
    public void testThatMusicTabNotEmpty() throws IOException {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getMusicQuery());

        Response<SectionTabResult> execute = wrapper.execute();
        SectionTabResult browseResult = execute.body();

        tabbedResultNotEmpty(browseResult);
    }

    private void nextSectionResultNotEmpty(SectionContinuation browseResult) {
        assertNotNull("Next section result: not empty", browseResult);
        assertTrue("Next section result: item list not empty", browseResult.getVideoItems() != null || browseResult.getPlaylistItems() != null);
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        assertTrue("Next section result: item list > 2", browseResult.getVideoItems().size() > 2 || browseResult.getPlaylistItems().size() > 2);
    }

    private void nextTabbedResultNotEmpty(SectionTabContinuation browseResult) {
        assertNotNull("Tabbed result: not empty", browseResult);
        assertNotNull("Tabbed result: media item list not empty", browseResult.getSections());
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        Section section = browseResult.getSections().get(0);
        assertTrue("Tabbed result: media item list not empty",
                section.getVideoItems() != null || section.getMusicItems() != null || section.getChannelItems() != null);
    }

    private void tabbedResultNotEmpty(SectionTabResult browseResult) {
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
}
package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabResult;
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
        Section section = firstNotEmpty(browseResult).getSections().get(0);
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

        Section firstSection = firstNotEmpty(browseResult).getSections().get(0);

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

        String nextPageKey = firstNotEmpty(browseResult1).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult1.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<SectionContinuation> next = mService.continueSection(BrowseManagerParams.getContinuationQuery(nextPageKey), visitorId);
        Response<SectionContinuation> execute = next.execute();
        SectionContinuation browseResult2 = execute.body();

        nextSectionResultNotEmpty(browseResult2);

        String nextTabbedPageKey = firstNotEmpty(browseResult1).getNextPageKey();
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

        List<Section> sections = firstNotEmpty(browseResult).getSections();

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

    @Test
    public void testThatMusicTabContinuationIsUnique() {
        Call<SectionTabResult> wrapper = mService.getSectionTabResult(BrowseManagerParams.getMusicQuery());
        
        SectionTabResult browseResult = RetrofitHelper.get(wrapper);

        tabbedResultNotEmpty(browseResult);

        String visitorData = browseResult.getVisitorData();

        Section firstSection = firstNotEmpty(browseResult).getSections().get(0);

        Call<SectionContinuation> wrapper2 =
                mService.continueSection(BrowseManagerParams.getContinuationQuery(firstSection.getNextPageKey()), visitorData);

        SectionContinuation musicContinuation = RetrofitHelper.get(wrapper2);

        String video1;
        String video2;

        if (firstSection.getMusicItems() != null) {
            video1 = firstSection.getMusicItems().get(0).getVideoId();
            video2 = musicContinuation.getMusicItems().get(0).getVideoId();
        } else {
            video1 = firstSection.getRadioItems().get(0).getVideoId();
            video2 = musicContinuation.getRadioItems().get(0).getVideoId();
        }

        assertNotNull("Video1 not null", video1);
        assertNotNull("Video2 not null", video2);

        assertNotEquals("Music tab continuation is unique", video1, video2);
    }

    private SectionTab firstNotEmpty(SectionTabResult browseResult) {
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
        assertTrue("Next section result: item list not empty", browseResult.getVideoItems() != null || browseResult.getRadioItems() != null);
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        assertTrue("Next section result: item list > 2", browseResult.getVideoItems().size() > 2 || browseResult.getRadioItems().size() > 2);
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
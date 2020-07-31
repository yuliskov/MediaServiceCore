package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.NextBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.NextTabbedBrowseResult;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResult;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.RetrofitHelper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowLog;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
public class BrowseManagerTest {
    private BrowseManager mService;

    @Before
    public void setUp() throws IOException {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS");

        ShadowLog.stream = System.out; // catch Log class output

        RetrofitHelper.sForceEnableProfiler = true;

        mService = RetrofitHelper.withJsonPath(BrowseManager.class);
    }

    @Test
    public void testThatRequiredFieldsExist() throws IOException {
        VideoItem videoItem = getRecommended().get(0);

        testFields(videoItem);

        videoItem = getRecommended().get(0);

        testFields(videoItem);
    }

    private List<VideoItem> getRecommended() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseParams.getHomeQuery());

        TabbedBrowseResult browseResult1 = wrapper.execute().body();

        assertNotNull("Items not null", browseResult1);
        assertTrue("List > 2", browseResult1.getBrowseTabs().get(0).getSections().get(0).getVideoItems().size() > 2);

        return browseResult1.getBrowseTabs().get(0).getSections().get(0).getVideoItems();
    }

    private void testFields(VideoItem videoItem) {
        assertNotNull("Title not null", videoItem.getTitle());
        String videoId = videoItem.getVideoId();
        assertNotNull("Id not null", videoId);
        assertNotNull("Time not null: " + videoId, videoItem.getPublishedTime());
        assertNotNull("Views not null: " + videoId, videoItem.getViewCount());
        assertNotNull("Length not null: " + videoId, videoItem.getLengthText());
        assertNotNull("Channel not null: " + videoId, videoItem.getChannelId());
        assertNotNull("User not null: " + videoId, videoItem.getUserName());
        assertNotNull("Thumbs not null: " + videoId, videoItem.getThumbnails());
    }

    @Test
    public void testEnsureNextResultIsUnique() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseParams.getHomeQuery());

        TabbedBrowseResult browseResult = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult);

        BrowseSection firstSection = browseResult.getBrowseTabs().get(0).getSections().get(0);

        String nextPageKey = firstSection.getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<NextBrowseResult> next = mService.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), visitorId);
        Response<NextBrowseResult> execute = next.execute();
        NextBrowseResult nextBrowseResult = execute.body();

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
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseParams.getHomeQuery());

        TabbedBrowseResult browseResult1 = wrapper.execute().body();

        tabbedResultNotEmpty(browseResult1);

        String nextPageKey = browseResult1.getBrowseTabs().get(0).getSections().get(0).getNextPageKey();
        assertNotNull("Next page key not null", nextPageKey);

        String visitorId = browseResult1.getVisitorData();
        assertNotNull("Next page key not null", visitorId);

        Call<NextBrowseResult> next = mService.getNextBrowseResult(BrowseParams.getNextBrowseQuery(nextPageKey), visitorId);
        Response<NextBrowseResult> execute = next.execute();
        NextBrowseResult browseResult2 = execute.body();

        nextSectionResultNotEmpty(browseResult2);

        String nextTabbedPageKey = browseResult1.getBrowseTabs().get(0).getNextPageKey();
        Call<NextTabbedBrowseResult> nextTabbed = mService.getNextTabbedBrowseResult(BrowseParams.getNextBrowseQuery(nextTabbedPageKey), visitorId);
        Response<NextTabbedBrowseResult> executeTabbed = nextTabbed.execute();
        NextTabbedBrowseResult browseTabbedResult2 = executeTabbed.body();

        nextTabbedResultNotEmpty(browseTabbedResult2);
    }

    @Test
    public void testThatAllTabsSectionHaveTitles() throws IOException {
        Call<TabbedBrowseResult> wrapper = mService.getTabbedBrowseResult(BrowseParams.getHomeQuery());

        Response<TabbedBrowseResult> execute = wrapper.execute();
        TabbedBrowseResult browseResult = execute.body();

        tabbedResultNotEmpty(browseResult);

        List<BrowseSection> sections = browseResult.getBrowseTabs().get(0).getSections();

        for (BrowseSection section : sections) {
            assertNotNull("All sections should have names", section.getTitle());
        }
    }

    private void nextSectionResultNotEmpty(NextBrowseResult browseResult) {
        assertNotNull("Section result: not empty", browseResult);
        assertNotNull("Section result: video list not empty", browseResult.getVideoItems());
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        assertTrue("Section result: video list > 2", browseResult.getVideoItems().size() > 2);
    }

    private void nextTabbedResultNotEmpty(NextTabbedBrowseResult browseResult) {
        assertNotNull("Tabbed result: not empty", browseResult);
        assertNotNull("Tabbed result: video list not empty", browseResult.getSections());
        //assertNotNull("Next key not empty", browseResult2.getNextPageKey());
        assertTrue("Tabbed result: video list > 2", browseResult.getSections().get(0).getVideoItems().size() > 2);
    }

    private void tabbedResultNotEmpty(TabbedBrowseResult browseResult1) {
        assertNotNull("Tabbed result not empty", browseResult1);
        assertNotNull("Tabs list not empty", browseResult1.getBrowseTabs());
        assertTrue("Tabs list > 2", browseResult1.getBrowseTabs().size() > 2);
        assertTrue("Tabs sections > 2", browseResult1.getBrowseTabs().get(0).getSections().size() > 2);
    }
}
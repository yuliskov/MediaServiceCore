package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.browse.v1.BrowseService;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.common.models.V2.TileItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

public class TrackingApiTest {
    private AppService mAppService;
    private TrackingApi mTrackingApi;
    private VideoInfoService mVideoInfoService;
    private static String sAuthorization;
    private BrowseService mBrowseService;

    @Before
    public void setUp() throws InterruptedException {
        // Fix temp video url ban
        Thread.sleep(3_000);

        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mTrackingApi = RetrofitHelper.create(TrackingApi.class);
        mAppService = AppService.instance();
        mVideoInfoService = VideoInfoService.instance();
        if (sAuthorization == null) {
            sAuthorization = TestHelpers.getAuthorization();
        }
        mBrowseService = BrowseService.instance();
        RetrofitOkHttpHelper.getAuthHeaders().put("Authorization", TestHelpers.getAuthorization());
    }

    //@Test
    //public void testCreateWatchRecord() throws IOException {
    //    Response<WatchTimeEmptyResult> response = createWatchRecord(TestHelpers.VIDEO_ID_SIMPLE);
    //
    //    assertTrue("Create watch record response successful", response.isSuccessful());
    //
    //    BrowseResult history = mBrowseServiceSigned.getHistory(sAuthorization);
    //    String newVideoId = history.getVideoItems().get(0).getVideoId();
    //
    //    assertEquals("History updated", TestHelpers.VIDEO_ID_SIMPLE, newVideoId);
    //}

    @Test
    public void testUpdateWatchTime() throws IOException {
        String playbackNonce = mAppService.getClientPlaybackNonce();
        String videoIdSimple = TestHelpers.VIDEO_ID_3;

        Response<WatchTimeEmptyResult> response;

        response = createWatchRecord(videoIdSimple, 300.4f, playbackNonce);

        assertTrue("Create watch record response successful", response.isSuccessful());

        response = updateWatchTime(videoIdSimple, 300.4f, playbackNonce);

        assertTrue("Update watch time response successful", response.isSuccessful());

        GridTab history = mBrowseService.getHistory();

        TileItem historyItem = null;

        for (ItemWrapper itemWrapper : history.getItemWrappers()) {
            if (itemWrapper.getTileItem() != null) {
                historyItem = itemWrapper.getTileItem();
                break;
            }
        }

        assertEquals("History updated", videoIdSimple, historyItem.getVideoId());

        //WatchNextResult watchNextResult = mWatchNextServiceSigned.getWatchNextResult(videoIdSimple, sAuthorization);
        //
        //int percentWatched = watchNextResult.getVideoMetadata().getPercentWatched();

        int percentWatched = historyItem.getPercentWatched();

        assertTrue("Watch time position update successful. Percent watched: " + percentWatched, percentWatched > 0);
    }

    private Response<WatchTimeEmptyResult> createWatchRecord(String videoId, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoService.getVideoInfo(videoId, null);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecordShort(
                videoId, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), videoInfo.getOfParam());

        return wrapper.execute();
    }

    //private Response<WatchTimeEmptyResult> createWatchRecord(String videoId, float positionSec) throws IOException {
    //    String playbackNonce = mAppService.getClientPlaybackNonce();
    //    VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);
    //
    //    Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecord(
    //            videoId, Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds()), positionSec, positionSec, positionSec,
    //            playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), sAuthorization
    //    );
    //
    //    return wrapper.execute();
    //}

    private Response<WatchTimeEmptyResult> createWatchRecord(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoService.getAuthVideoInfo(videoId, null);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecord(
                videoId, Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds()), positionSec,
                playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), videoInfo.getOfParam());

        return wrapper.execute();
    }

    private Response<WatchTimeEmptyResult> updateWatchTime(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoService.getAuthVideoInfo(videoId, null);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.updateWatchTime(
                videoId, Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds()), positionSec,
                positionSec, positionSec, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), videoInfo.getOfParam());

        return wrapper.execute();
    }

    private Response<WatchTimeEmptyResult> updateWatchTimeAlt(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoService.getVideoInfo(videoId, null);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.updateWatchTimeShort(
                videoId, positionSec, positionSec, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), videoInfo.getOfParam());

        return wrapper.execute();
    }
}
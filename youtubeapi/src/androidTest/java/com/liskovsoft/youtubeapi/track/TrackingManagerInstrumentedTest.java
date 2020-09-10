package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.browse.ver1.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.WatchNextServiceSigned;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TrackingManagerInstrumentedTest {
    private AppService mAppService;
    private TrackingManager mTrackingManager;
    private VideoInfoServiceSigned mVideoInfoServiceSigned;
    private AuthService mAuthService;
    private static String sAuthorization;
    private BrowseServiceSigned mBrowseServiceSigned;
    private WatchNextServiceSigned mWatchNextServiceSigned;

    @Before
    public void setUp() {
        mTrackingManager = RetrofitHelper.withJsonPath(TrackingManager.class);
        mAppService = AppService.instance();
        mVideoInfoServiceSigned = VideoInfoServiceSigned.instance();
        mAuthService = AuthService.instance();
        if (sAuthorization == null) {
            sAuthorization = TestHelpers.getAuthorization();
        }
        mBrowseServiceSigned = BrowseServiceSigned.instance();
        mWatchNextServiceSigned = WatchNextServiceSigned.instance();
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
    public void testUpdateWatchTime() throws IOException, InterruptedException {
        String playbackNonce = mAppService.getClientPlaybackNonce();
        String videoIdSimple = TestHelpers.VIDEO_ID_SIMPLE_3;

        Response<WatchTimeEmptyResult> response;

        response = createWatchRecord(videoIdSimple, playbackNonce);

        assertTrue("Create watch record response successful", response.isSuccessful());

        response = updateWatchTimeAlt(videoIdSimple, 100.4f, playbackNonce);

        assertTrue("Update watch time response successful", response.isSuccessful());

        BrowseResult history = mBrowseServiceSigned.getHistory(sAuthorization);

        VideoItem historyItem = history.getVideoItems().get(0);

        assertEquals("History updated", videoIdSimple, historyItem.getVideoId());

        //WatchNextResult watchNextResult = mWatchNextServiceSigned.getWatchNextResult(videoIdSimple, sAuthorization);
        //
        //int percentWatched = watchNextResult.getVideoMetadata().getPercentWatched();

        int percentWatched = historyItem.getPercentWatched();

        assertTrue("Watch time position update successful. Percent watched: " + percentWatched, percentWatched > 0);
    }

    private Response<WatchTimeEmptyResult> createWatchRecord(String videoId, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecord(
                videoId, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), sAuthorization
        );

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

    private Response<WatchTimeEmptyResult> updateWatchTime(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.updateWatchTime(
                videoId, Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds()), positionSec, positionSec,
                positionSec, playbackNonce, videoInfo.getEventId(), sAuthorization
        );

        return wrapper.execute();
    }

    private Response<WatchTimeEmptyResult> updateWatchTimeAlt(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.updateWatchTime(
                videoId, positionSec, positionSec, playbackNonce, videoInfo.getEventId(), sAuthorization
        );

        return wrapper.execute();
    }
}
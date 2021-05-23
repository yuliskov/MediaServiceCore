package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.V2.AuthService;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.WatchNextServiceSigned;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import com.liskovsoft.youtubeapi.videoinfo.V1.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
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
            sAuthorization = TestHelpersV2.getAuthorization();
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
    public void testUpdateWatchTime() throws IOException {
        String playbackNonce = mAppService.getClientPlaybackNonce();
        String videoIdSimple = TestHelpersV2.VIDEO_ID_3;

        Response<WatchTimeEmptyResult> response;

        response = createWatchRecord(videoIdSimple, 300.4f, playbackNonce);

        assertTrue("Create watch record response successful", response.isSuccessful());

        response = updateWatchTime(videoIdSimple, 300.4f, playbackNonce);

        assertTrue("Update watch time response successful", response.isSuccessful());

        GridTab history = mBrowseServiceSigned.getHistory(sAuthorization);

        VideoItem historyItem = null;

        for (ItemWrapper itemWrapper : history.getItemWrappers()) {
            if (itemWrapper.getVideoItem() != null) {
                historyItem = itemWrapper.getVideoItem();
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
        VideoInfo videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecordShort(
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

    private Response<WatchTimeEmptyResult> createWatchRecord(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecord(
                videoId, Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds()), positionSec,
                positionSec, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), sAuthorization
        );

        return wrapper.execute();
    }

    private Response<WatchTimeEmptyResult> updateWatchTime(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.updateWatchTime(
                videoId, Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds()), positionSec,
                positionSec, playbackNonce, videoInfo.getEventId(), sAuthorization
        );

        return wrapper.execute();
    }

    private Response<WatchTimeEmptyResult> updateWatchTimeAlt(String videoId, float positionSec, String playbackNonce) throws IOException {
        //String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfo videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, sAuthorization);

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.updateWatchTimeShort(
                videoId, positionSec, positionSec, playbackNonce, videoInfo.getEventId(), sAuthorization
        );

        return wrapper.execute();
    }
}
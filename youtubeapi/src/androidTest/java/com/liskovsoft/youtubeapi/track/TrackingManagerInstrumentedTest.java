package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.track.models.WatchTimeResult;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TrackingManagerInstrumentedTest {
    private AppService mAppService;
    private TrackingManager mTrackingManager;
    private VideoInfoService mVideoInfoService;

    @Before
    public void setUp() {
        mAppService = AppService.instance();
        mVideoInfoService = VideoInfoService.instance();
        mTrackingManager = RetrofitHelper.withJsonPath(TrackingManager.class);
    }

    @Test
    public void testUpdateWatchTime() throws IOException {
        String playbackNonce = mAppService.generateClientPlaybackNonce();
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(TestHelpers.VIDEO_ID_SIMPLE);

        String videoId = videoInfo.getVideoDetails().getVideoId();
        float lengthSec = Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds());
        float positionSec = 10;
        String vm = "CAEQARgEKiB1bnk3SDdPbWtzNlMtcXhfMFlWdjc2Sk5sTm13V19JWjoyQUdiNlo4UDVweWd4ZjMxY1F0S3dmVTdHM2ZYUjJ1Yk02ZjQ3Z3B1c3A4U1lzX0lMbXc";
        String authorization = TestHelpers.getAuthorization();

        Call<WatchTimeResult> wrapper = mTrackingManager.createWatchRecord(
                videoId, lengthSec, positionSec, playbackNonce, videoInfo.getEventId(), vm, authorization
        );

        Response<WatchTimeResult> response = wrapper.execute();

        assertTrue("Watch time update success", response.isSuccessful());
    }
}
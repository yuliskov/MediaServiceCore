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
        float lengthSeconds = Float.parseFloat(videoInfo.getVideoDetails().getLengthSeconds());
        float currentTimeSeconds = 10;
        String authorization = TestHelpers.getAuthorization();

        Call<WatchTimeResult> wrapper = mTrackingManager.initWatchTime(
                videoId, lengthSeconds, currentTimeSeconds, currentTimeSeconds, currentTimeSeconds,
                playbackNonce, videoInfo.getEventId(), authorization);

        Response<WatchTimeResult> response = wrapper.execute();

        assertTrue("Watch time init success", response.isSuccessful());

        wrapper = mTrackingManager.updateWatchTime(
                videoId, lengthSeconds, currentTimeSeconds, currentTimeSeconds, currentTimeSeconds,
                playbackNonce, videoInfo.getEventId(), authorization);

        response = wrapper.execute();

        assertTrue("Watch time update success", response.isSuccessful());
    }
}
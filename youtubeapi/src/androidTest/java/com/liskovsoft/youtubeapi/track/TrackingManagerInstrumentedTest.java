package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.track.models.UpdateWatchTimeResult;
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

        Call<UpdateWatchTimeResult> wrapper = mTrackingManager.updateWatchTime(
                videoInfo.getVideoDetails().getVideoId(),
                videoInfo.getVideoDetails().getLengthSeconds(),
                10, 10, 10,
                playbackNonce, videoInfo.getEventId(), TestHelpers.getAuthorization());

        Response<UpdateWatchTimeResult> response = wrapper.execute();

        assertTrue("Watch time success", response.isSuccessful());
    }
}
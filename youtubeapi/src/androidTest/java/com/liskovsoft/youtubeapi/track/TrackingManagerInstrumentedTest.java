package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
import com.liskovsoft.youtubeapi.track.models.WatchTimeResult;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceSigned;
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
    private VideoInfoServiceSigned mVideoInfoServiceSigned;
    private AuthService mAuthService;
    private static String mAuthorization;

    @Before
    public void setUp() {
        mTrackingManager = RetrofitHelper.withJsonPath(TrackingManager.class);
        mAppService = AppService.instance();
        mVideoInfoServiceSigned = VideoInfoServiceSigned.instance();
        mAuthService = AuthService.instance();
        if (mAuthorization == null) {
            mAuthorization = TestHelpers.getAuthorization();
        }
    }

    @Test
    public void testUpdateWatchTime() throws IOException {
        String playbackNonce = mAppService.generateClientPlaybackNonce();
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(TestHelpers.VIDEO_ID_SIMPLE, mAuthorization);

        String videoId = videoInfo.getVideoDetails().getVideoId();
        String authorization = TestHelpers.getAuthorization();

        Call<WatchTimeResult> wrapper = mTrackingManager.createWatchRecord(
                videoId, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), authorization
        );

        Response<WatchTimeResult> response = wrapper.execute();

        assertTrue("Watch time update success", response.isSuccessful());
    }
}
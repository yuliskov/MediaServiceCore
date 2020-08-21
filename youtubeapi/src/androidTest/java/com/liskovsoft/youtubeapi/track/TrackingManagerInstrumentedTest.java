package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.auth.AuthService;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.helpers.TestHelpers;
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
    }

    @Test
    public void testUpdateWatchTime() throws IOException {
        String playbackNonce = mAppService.getClientPlaybackNonce();
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(TestHelpers.VIDEO_ID_SIMPLE, sAuthorization);

        String authorization = TestHelpers.getAuthorization();

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecord(
                TestHelpers.VIDEO_ID_SIMPLE, playbackNonce, videoInfo.getEventId(), videoInfo.getVisitorMonitoringData(), authorization
        );

        Response<WatchTimeEmptyResult> response = wrapper.execute();

        assertTrue("Watch time response successful", response.isSuccessful());

        BrowseResult history = mBrowseServiceSigned.getHistory(sAuthorization);
        String newVideoId = history.getVideoItems().get(0).getVideoId();

        assertEquals("History updated", TestHelpers.VIDEO_ID_SIMPLE, newVideoId);
    }
}
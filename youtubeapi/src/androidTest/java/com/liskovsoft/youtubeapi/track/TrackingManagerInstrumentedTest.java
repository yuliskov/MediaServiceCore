package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.sharedutils.okhttp.OkHttpHelpers;
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
import java.util.HashMap;
import java.util.Map;

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
        String authorization = TestHelpers.getAuthorization();

        Call<WatchTimeResult> wrapper = mTrackingManager.updateWatchTime(
                videoId, lengthSec, positionSec, positionSec, positionSec,
                playbackNonce, videoInfo.getEventId(), authorization);

        Response<WatchTimeResult> response = wrapper.execute();

        assertTrue("Watch time update success", response.isSuccessful());

        //Map<String, String> headers = new HashMap<>();
        //headers.put("Authorization", "Bearer ya29.a0AfH6SMC3jB2_4za-2d3u32WKM2wDtsJreFoC2f3lvbWWz7IybkkohC3WpvcTvmGfNSXJi2rgBNd96TrykVj5mV4UsGQ163HGPwF-AkvER1NYzS2ze3UxXECU5tYs7ALfvFc9ZyoWNsUt7jXnCtmaTraPzs1Sj1Asf87RUC43oMAvuw");
        //
        //OkHttpHelpers.doGetOkHttpRequest(
        //        "https://www.youtube.com/api/stats/watchtime?ns=yt&ver=2&final=1" +
        //                "&docid=" + videoId + "&len=" + lengthSec + "&cmt=" + positionSec + "&st=" + positionSec +
        //                "&et=" + positionSec + "&cpn=" + playbackNonce + "&ei=" + videoInfo.getEventId(), headers);
    }
}
package com.liskovsoft.youtubeapi.videoinfo;

import static org.junit.Assert.assertTrue;

import com.liskovsoft.googlecommon.common.api.FileApi;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.videoinfo.V2.DashInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;

import okhttp3.Headers;
import retrofit2.Call;

public class DashInfoApiTest extends BaseVideoInfoApiTest {
    private static final String SEQ_NUM = "X-Sequence-Num";
    private static final String STREAM_DUR_MS = "X-Head-Time-Millis";
    private static final String LAST_SEG_TIME_MS = "X-Walltime-Ms";
    private DashInfoApi mDashService;
    private FileApi mFileService;
    // Make response smaller
    private final String SMALL_RANGE = "&range=0-200";

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        //Thread.sleep(3_000);

        initBase();

        mDashService = RetrofitHelper.create(DashInfoApi.class);
        mFileService = RetrofitHelper.create(FileApi.class);

        RetrofitOkHttpHelper.getAuthHeaders().clear();
    }

    //@Test
    //public void testDashInfoNotEmpty() throws IOException {
    //    VideoInfo videoInfo = getVideoInfo(TestHelpersV2.VIDEO_ID_LIVE);
    //
    //    VideoInfoService videoInfoService = VideoInfoService.instance();
    //    DashInfo dashInfo = videoInfoService.getDashInfo(videoInfo);
    //    assertTrue("Has start time", dashInfo.getStartTimeMs() != -1);
    //    assertTrue("Has start segment", dashInfo.getStartSegmentNum() != -1);
    //    assertTrue("Has segment duration", dashInfo.getSegmentDurationUs() > 0);
    //}

    @Test
    public void testDashInfoUrlNotEmpty() throws IOException {
        VideoInfo videoInfo = getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_LIVE);
        Call<DashInfoUrl> dashInfoWrapper = mDashService.getDashInfoUrl(videoInfo.getDashManifestUrl());

        DashInfoUrl dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
        assertTrue("segment duration not null", dashInfo.getSegmentDurationUs() > 0);
        assertTrue("start time not null", dashInfo.getStartTimeMs() > 0);
    }

    @Ignore("Don't work anymore. Why?")
    @Test
    public void testDashInfoContentNotEmpty() throws IOException {
        VideoInfo videoInfo = getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_LIVE);
        Call<DashInfoContent> dashInfoWrapper = mDashService.getDashInfoContent(getSmallestAudio(videoInfo).getUrl());

        DashInfoContent dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
        assertTrue("start segment time not null", dashInfo.getStartTimeMs() > 0);
    }

    @Test
    public void testDashInfoHeadersNotEmpty() throws IOException {
        VideoInfo videoInfo = getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_LIVE);
        Call<Void> headersWrapper = mFileService.getHeaders(getSmallestAudio(videoInfo).getUrl());

        Headers headers = headersWrapper.execute().headers();
        int lastSegmentNum = Integer.parseInt(headers.get(SEQ_NUM));
        long streamDurationMs = Long.parseLong(headers.get(STREAM_DUR_MS));
        long lastSegmentTimeMs = Long.parseLong(headers.get(LAST_SEG_TIME_MS));

        assertTrue("last segment num not null", lastSegmentNum > 0);
        assertTrue("stream duration not null", streamDurationMs > 0);
        assertTrue("last segment time not null", lastSegmentTimeMs > 0);
    }
}
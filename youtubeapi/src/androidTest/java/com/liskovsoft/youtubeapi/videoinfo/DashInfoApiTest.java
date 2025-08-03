package com.liskovsoft.youtubeapi.videoinfo;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.api.FileApi;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;
import com.liskovsoft.youtubeapi.videoinfo.V2.DashInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApiHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import okhttp3.Headers;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import retrofit2.Call;

import java.io.IOException;

import static org.junit.Assert.*;

public class DashInfoApiTest {
    private static final String SEQ_NUM = "X-Sequence-Num";
    private static final String STREAM_DUR_MS = "X-Head-Time-Millis";
    private static final String LAST_SEG_TIME_MS = "X-Walltime-Ms";
    private DashInfoApi mDashService;
    private VideoInfoApi mVideoInfoService;
    private AppService mAppService;
    private FileApi mFileService;
    // Make response smaller
    private final String SMALL_RANGE = "&range=0-200";

    @Before
    public void setUp() throws Exception {
        // Fix temp video url ban
        //Thread.sleep(3_000);

        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());

        mDashService = RetrofitHelper.create(DashInfoApi.class);

        mVideoInfoService = RetrofitHelper.create(VideoInfoApi.class);

        mFileService = RetrofitHelper.create(FileApi.class);
        
        mAppService = AppService.instance();

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
        VideoInfo videoInfo = getVideoInfo(TestHelpers.VIDEO_ID_LIVE);
        Call<DashInfoUrl> dashInfoWrapper = mDashService.getDashInfoUrl(videoInfo.getDashManifestUrl());

        DashInfoUrl dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
        assertTrue("segment duration not null", dashInfo.getSegmentDurationUs() > 0);
        assertTrue("start time not null", dashInfo.getStartTimeMs() > 0);
    }

    @Ignore("Don't work anymore. Why?")
    @Test
    public void testDashInfoContentNotEmpty() throws IOException {
        VideoInfo videoInfo = getVideoInfo(TestHelpers.VIDEO_ID_LIVE);
        Call<DashInfoContent> dashInfoWrapper = mDashService.getDashInfoContent(getSmallestAudio(videoInfo).getUrl());

        DashInfoContent dashInfo = dashInfoWrapper.execute().body();

        assertTrue("start segment not null", dashInfo.getStartSegmentNum() > 0);
        assertTrue("start segment time not null", dashInfo.getStartTimeMs() > 0);
    }

    @Test
    public void testDashInfoHeadersNotEmpty() throws IOException {
        VideoInfo videoInfo = getVideoInfo(TestHelpers.VIDEO_ID_LIVE);
        Call<Void> headersWrapper = mFileService.getHeaders(getSmallestAudio(videoInfo).getUrl());

        Headers headers = headersWrapper.execute().headers();
        int lastSegmentNum = Integer.parseInt(headers.get(SEQ_NUM));
        long streamDurationMs = Long.parseLong(headers.get(STREAM_DUR_MS));
        long lastSegmentTimeMs = Long.parseLong(headers.get(LAST_SEG_TIME_MS));

        assertTrue("last segment num not null", lastSegmentNum > 0);
        assertTrue("stream duration not null", streamDurationMs > 0);
        assertTrue("last segment time not null", lastSegmentTimeMs > 0);
    }

    private VideoInfo getVideoInfo(String videoId) throws IOException {
        Call<VideoInfo> wrapper = mVideoInfoService.getVideoInfo(VideoInfoApiHelper.getVideoInfoQuery(AppClient.WEB, videoId, null));
        return wrapper.execute().body();
    }

    @NonNull
    private AdaptiveVideoFormat getSmallestAudio(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isAudio(item.getMimeType())); // smallest format

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.fixThrottling(format.getThrottleCipher()));

        return format;
    }
}
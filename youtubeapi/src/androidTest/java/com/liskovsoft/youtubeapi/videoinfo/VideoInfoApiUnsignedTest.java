package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * NOTE: testing with Duktape (native libs)!!!
 */
public class VideoInfoApiUnsignedTest extends BaseVideoInfoApiTest {
    @Before
    public void setUp() throws Exception {
        initBase();
        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().clear();
    }

    @Ignore("Require sign-in")
    @Test
    public void testThatAgeRestrictedVideoContainsRequiredFields() {
        testThatNonLiveVideoInfoContainsRequiredFields(getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_AGE_RESTRICTED));
    }

    @Test
    public void testThatUnavailableVideoContainsRequiredFields() {
        testThatNonLiveVideoInfoContainsRequiredFields(getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_UNAVAILABLE));
    }

    @Test
    public void testThatLiveVideoContainsSpecificFields() {
        testThatLiveVideoContainsSpecificFields(getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_LIVE));
    }

    @Test
    public void testThatVideoWithCaptionsContainsRequiredFields() {
        testThatVideoWithCaptionsContainsRequiredFields(getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_CAPTIONS));
    }

    @Test
    public void testSabrFormats() {
        VideoInfo videoInfo = getVideoInfo(AppClient.WEB, TestHelpers.VIDEO_ID_MUSIC_2);
        decipherFormats(videoInfo);
        Assert.assertTrue("Sabr format exists", TestHelpers.urlExists(videoInfo.getServerAbrStreamingUrl()));
    }
}
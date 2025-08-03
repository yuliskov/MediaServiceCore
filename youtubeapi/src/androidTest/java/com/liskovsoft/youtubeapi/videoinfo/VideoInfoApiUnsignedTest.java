package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.googlecommon.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApiHelper;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.models.CaptionTrack;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;

/**
 * NOTE: testing with Duktape (native libs)!!!
 */
public class VideoInfoApiUnsignedTest {
    private VideoInfoApi mService;
    private LocaleManager mLocaleManager;

    @Before
    public void setUp() throws Exception {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mService = RetrofitHelper.create(VideoInfoApi.class);
        mLocaleManager = LocaleManager.instance();
        RetrofitOkHttpHelper.setDisableCompression(true);
        RetrofitOkHttpHelper.getAuthHeaders().clear();
    }

    @Ignore("Require sign-in")
    @Test
    public void testThatAgeRestrictedVideoContainsRequiredFields() throws IOException {
        testThatNonLiveVideoInfoContainsRequiredFields(getVideoInfoRestricted(TestHelpers.VIDEO_ID_AGE_RESTRICTED));
    }

    @Test
    public void testThatUnavailableVideoContainsRequiredFields() throws IOException {
        testThatNonLiveVideoInfoContainsRequiredFields(getVideoInfo(TestHelpers.VIDEO_ID_UNAVAILABLE));
    }

    @Test
    public void testThatLiveVideoContainsSpecificFields()  throws IOException {
        testThatLiveVideoContainsSpecificFields(getVideoInfo(TestHelpers.VIDEO_ID_LIVE));
    }

    @Test
    public void testThatVideoWithCaptionsContainsRequiredFields() throws IOException {
        testThatVideoWithCaptionsContainsRequiredFields(getVideoInfo(TestHelpers.VIDEO_ID_CAPTIONS));
    }

    private void testThatLiveVideoContainsSpecificFields(VideoInfo result) {
        assertNotNull("Result not null", result);
        assertNotNull("Contains dash url", result.getDashManifestUrl());
        // V2 doesn't contains legacy hls urls
        //assertNotNull("Contains hls url", result.getHlsManifestUrl());

        testThatVideoInfoContainsRequiredFields(result);
    }

    private void testThatVideoWithCaptionsContainsRequiredFields(VideoInfo result) {
        assertNotNull("Result not null", result);
        List<CaptionTrack> captionTracks = result.getCaptionTracks();
        assertNotNull("Contains captions", captionTracks);
        CaptionTrack track = captionTracks.get(0);

        assertNotNull("Contains name", track.getName());
        assertNotNull("Contains base url", track.getBaseUrl());
        assertNotNull("Contains vss id", track.getVssId());
        assertNotNull("Contains lang", track.getLanguageCode());
        assertNotNull("Contains mime type", track.getMimeType());
        assertNotNull("Contains codecs", track.getCodecs());

        assertTrue("Subtitle url exists", TestHelpers.urlExists(track.getBaseUrl()));

        testThatNonLiveVideoInfoContainsRequiredFields(result);
    }

    private void testThatNonLiveVideoInfoContainsRequiredFields(VideoInfo result) {
        List<AdaptiveVideoFormat> formats = result.getAdaptiveFormats();
        assertNotNull("Contains range", formats.get(0).getIndexRange());

        testThatVideoInfoContainsRequiredFields(result);
    }

    private void testThatVideoInfoContainsRequiredFields(VideoInfo result) {
        assertNotNull("Result not null", result);
        //assertFalse("Video available externally", result.isEmbedRestricted());
        List<AdaptiveVideoFormat> formats = result.getAdaptiveFormats();
        assertTrue("Formats not empty", formats.size() > 0);
        assertTrue("Contains fps", formats.get(0).getFps() != 0);
        assertTrue("Contains bitrate", formats.get(0).getBitrate() != 0);
        assertNotNull("Contains tracking url", result.getVideoStatsWatchTimeUrl());
        assertNotNull("Contains video details", result.getVideoDetails());
        assertNotNull("Contains event id", result.getEventId());
        assertNotNull("Contains vm tracking param", result.getVisitorMonitoringData());
    }

    private VideoInfo getVideoInfoRestricted(String videoId) throws IOException {
        Call<VideoInfo> wrapper = mService.getVideoInfo(VideoInfoApiHelper.getVideoInfoQuery(AppClient.WEB, videoId, null));
        return wrapper.execute().body();
    }

    private VideoInfo getVideoInfo(String videoId) throws IOException {
        Call<VideoInfo> wrapper = mService.getVideoInfo(VideoInfoApiHelper.getVideoInfoQuery(AppClient.WEB, videoId, null));
        return wrapper.execute().body();
    }
}
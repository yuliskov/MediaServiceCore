package com.liskovsoft.youtubeapi.videoinfo;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.annotation.NonNull;
import androidx.test.platform.app.InstrumentationRegistry;

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.prefs.GlobalPreferences;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoApiHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder;
import com.liskovsoft.youtubeapi.videoinfo.models.CaptionTrack;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoReel;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

abstract class BaseVideoInfoApiTest {
    private VideoInfoApi mService;
    private AppService mAppService;

    protected void initBase() {
        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().getContext());
        mService = RetrofitHelper.create(VideoInfoApi.class);
        mAppService = AppService.instance();
    }

    protected void testThatLiveVideoContainsSpecificFields(VideoInfo result) {
        assertNotNull("Result not null", result);
        assertNotNull("Contains dash url", result.getDashManifestUrl());
        // V2 doesn't contains legacy hls urls
        //assertNotNull("Contains hls url", result.getHlsManifestUrl());

        testThatVideoInfoContainsRequiredFields(result);
    }

    protected void testThatVideoWithCaptionsContainsRequiredFields(VideoInfo result) {
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

    protected void testThatNonLiveVideoInfoContainsRequiredFields(VideoInfo result) {
        List<AdaptiveVideoFormat> formats = result.getAdaptiveFormats();
        assertNotNull("Contains range", formats.get(0).getIndexRange());

        testThatVideoInfoContainsRequiredFields(result);
    }

    protected void testThatVideoInfoContainsRequiredFields(VideoInfo result) {
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

    protected VideoInfo getVideoInfo(AppClient client, String videoId) {
        if (client == AppClient.INITIAL) {
            return InitialResponse.getVideoInfo(videoId, client.isAuthSupported());
        }

        if (client == AppClient.ANDROID_REEL) {
            Call<VideoInfoReel> wrapper = mService.getVideoInfoReel(VideoInfoApiHelper.getVideoInfoQuery(client, videoId, null), mAppService.getVisitorData(), client.getUserAgent());
            return RetrofitHelper.get(wrapper, client.isAuthSupported()).getVideoInfo();
        }

        Call<VideoInfo> wrapper = mService.getVideoInfo(VideoInfoApiHelper.getVideoInfoQuery(client, videoId, null), mAppService.getVisitorData(), client.getUserAgent());
        return RetrofitHelper.get(wrapper, client.isAuthSupported());
    }

    @NonNull
    protected AdaptiveVideoFormat getSmallestAudio(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isAudio(item.getMimeType())); // smallest format

        format.getUrlHolder().setSignature(mAppService.extractSig(format.getUrlHolder().getSParam()));
        format.getUrlHolder().setNParam(mAppService.extractNSig(format.getUrlHolder().getNParam()));

        return format;
    }

    protected void decipherFormats(VideoInfo videoInfo) {
        List<? extends VideoFormat> adaptiveFormats = videoInfo.getAdaptiveFormats();
        List<? extends VideoFormat> regularFormats = videoInfo.getRegularFormats();

        List<VideoUrlHolder> urlHolders = new ArrayList<>();
        if (adaptiveFormats != null)
            for (VideoFormat videoFormat : adaptiveFormats) {
                urlHolders.add(videoFormat.getUrlHolder());
            }
        if (regularFormats != null)
            for (VideoFormat videoFormat : regularFormats) {
                urlHolders.add(videoFormat.getUrlHolder());
            }
        urlHolders.add(videoInfo.getUrlHolder());

        decipherFormats(urlHolders);
    }

    private void decipherFormats(List<VideoUrlHolder> urlHolders) {
        if (urlHolders == null) {
            return;
        }

        List<String> sParams = extractSParams(urlHolders);
        List<String> signatures = mAppService.extractSig(sParams);
        applySignatures(urlHolders, signatures);

        List<String> nParams = extractNParams(urlHolders);
        List<String> nSignatures = mAppService.extractNSig(nParams);
        applyNSignatures(urlHolders, nSignatures);
    }

    private static List<String> extractSParams(List<VideoUrlHolder> urlHolders) {
        List<String> result = new ArrayList<>();

        for (VideoUrlHolder urlHolder : urlHolders) {
            result.add(urlHolder.getSParam());
        }

        return result;
    }

    private static void applySignatures(List<VideoUrlHolder> urlHolders, List<String> signatures) {
        if (signatures == null) {
            return;
        }

        if (signatures.size() != urlHolders.size()) {
            throw new IllegalStateException("Sizes of urlHolders and signatures should match!");
        }

        for (int i = 0; i < urlHolders.size(); i++) {
            urlHolders.get(i).setSignature(signatures.get(i));
        }
    }

    private static List<String> extractNParams(List<VideoUrlHolder> urlHolders) {
        List<String> result = new ArrayList<>();

        for (VideoUrlHolder urlHolder : urlHolders) {
            result.add(urlHolder.getNParam());
            // All throttled strings has same values
        }

        return result;
    }

    private static void applyNSignatures(List<VideoUrlHolder> urlHolders, List<String> nSignatures) {
        if (nSignatures == null || nSignatures.isEmpty()) {
            return;
        }

        // All throttled strings has same values
        boolean sameSize = nSignatures.size() == urlHolders.size();

        for (int i = 0; i < urlHolders.size(); i++) {
            urlHolders.get(i).setNParam(nSignatures.get(sameSize ? i : 0));
        }
    }
}

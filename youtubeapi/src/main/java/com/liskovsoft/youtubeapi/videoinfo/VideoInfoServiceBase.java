package com.liskovsoft.youtubeapi.videoinfo;

import androidx.annotation.NonNull;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.api.FileApi;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;
import com.liskovsoft.youtubeapi.videoinfo.V2.DashInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoHeaders;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;
import okhttp3.Headers;

import java.util.ArrayList;
import java.util.List;

public abstract class VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceBase.class.getSimpleName();
    private final DashInfoApi mDashInfoApi;
    private final FileApi mFileApi;
    protected final AppService mAppService;
    // Make response smaller
    private final String SMALL_RANGE = "&range=0-200";

    protected VideoInfoServiceBase() {
        mAppService = AppService.instance();
        mDashInfoApi = RetrofitHelper.create(DashInfoApi.class);
        mFileApi = RetrofitHelper.create(FileApi.class);
    }

    protected boolean isPotSupported() {
        return false;
    }

    protected void decipherFormats(List<? extends VideoFormat> formats) {
        if (formats == null) {
            return;
        }

        List<String> ciphered = extractCipheredStrings(formats);
        List<String> deciphered = mAppService.decipher(ciphered);
        applyDecipheredStrings(formats, deciphered);

        List<String> throttled = extractThrottledStrings(formats);
        List<String> throttleFixed = mAppService.fixThrottling(throttled);
        applyThrottleFixedStrings(formats, throttleFixed);

        // What this for? Could this fix throttling or maybe the source error?
        //applyAdditionalStrings(formats);

        if (isPotSupported()) {
            applyPoToken(formats, mAppService.getSessionPoToken());
        }
    }

    private static List<String> extractCipheredStrings(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getSignatureCipher());
        }

        return result;
    }

    private static void applyDecipheredStrings(List<? extends VideoFormat> formats, List<String> deciphered) {
        if (deciphered == null) {
            return;
        }

        if (deciphered.size() != formats.size()) {
            throw new IllegalStateException("Sizes of formats and deciphered strings should match!");
        }

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setSignature(deciphered.get(i));
        }
    }

    private static List<String> extractThrottledStrings(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getThrottleCipher());
            // All throttled strings has same values
            break;
        }

        return result;
    }

    private static void applyThrottleFixedStrings(List<? extends VideoFormat> formats, List<String> throttleFixed) {
        if (throttleFixed == null || throttleFixed.isEmpty()) {
            return;
        }

        // All throttled strings has same values
        boolean sameSize = throttleFixed.size() == formats.size();

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setThrottleCipher(throttleFixed.get(sameSize ? i : 0));
        }
    }

    private static void applyPoToken(List<? extends VideoFormat> formats, String poToken) {
        if (poToken == null) {
            return;
        }

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setParam("pot", poToken);
        }
    }

    /**
     * What this for? Could this fix throttling?
     */
    private static void applyAdditionalStrings(List<? extends VideoFormat> formats) {
        String cpn = AppService.instance().getClientPlaybackNonce();
        //String poTokenResult = AppService.instance().getPoTokenResult();

        for (VideoFormat format : formats) {
            format.setCpn(cpn);
            format.setClientVersion(AppClient.WEB.getClientVersion());

            // Buffering fix? ptk=youtube_host&ptchn=youtube_host&pltype=adhost
            //format.setParam("ptk", "youtube_host");
            //format.setParam("ptchn", "youtube_host");
            //format.setParam("pltype", "adhost");

            //format.setParam("pot", poTokenResult);
        }
    }

    private DashInfoUrl getDashInfoUrl(String url) {
        if (url == null) {
            return null;
        }

        return RetrofitHelper.get(mDashInfoApi.getDashInfoUrl(url));
    }

    private DashInfoContent getDashInfoContent(String url) {
        if (url == null) {
            return null;
        }

        return RetrofitHelper.get(mDashInfoApi.getDashInfoContent(url));
    }

    private DashInfoHeaders getDashInfoHeaders(String url) {
        if (url == null) {
            return null;
        }

        // Range doesn't work???
        //return RetrofitHelper.getHeaders(mFileApi.getHeaders(url + SMALL_RANGE));
        return new DashInfoHeaders(RetrofitHelper.getHeaders(mFileApi.getHeaders(url)));
    }

    protected DashInfo getDashInfo(VideoInfo videoInfo) {
        if (videoInfo == null || videoInfo.getAdaptiveFormats() == null || videoInfo.getAdaptiveFormats().isEmpty()) {
            return null;
        }

        DashInfo info = getCumulativeDashInfo(videoInfo);

        // Do retry. Sometimes the previous try failed?
        if (info == null || info.getSegmentDurationUs() <= 0 || info.getStartTimeMs() <= 0 || info.getStartSegmentNum() < 0) {
            info = getCumulativeDashInfo(videoInfo);
        }

        return info;
    }

    private DashInfo getCumulativeDashInfo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = getSmallestAudio(videoInfo);

        try {
            return getDashInfoHeaders(format.getUrl());
        } catch (ArithmeticException | NumberFormatException | IllegalStateException ex) {
            try {
                return getDashInfoUrl(format.getUrl());
            } catch (ArithmeticException | NumberFormatException exc) {
                // Empty results received. Url isn't available or something like that
                return getDashInfoContent(format.getUrl());
            }
        }
    }

    @NonNull
    private AdaptiveVideoFormat getSmallestAudio(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isAudio(item.getMimeType())); // smallest format

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.fixThrottling(format.getThrottleCipher()));
        return format;
    }

    @NonNull
    private AdaptiveVideoFormat getSmallestVideo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findLast(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isVideo(item.getMimeType())); // smallest format

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.fixThrottling(format.getThrottleCipher()));
        return format;
    }

    @NonNull
    private AdaptiveVideoFormat getLargestVideo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isVideo(item.getMimeType())); // first is largest

        format.setSignature(mAppService.decipher(format.getSignatureCipher()));
        format.setThrottleCipher(mAppService.fixThrottling(format.getThrottleCipher()));
        return format;
    }
}

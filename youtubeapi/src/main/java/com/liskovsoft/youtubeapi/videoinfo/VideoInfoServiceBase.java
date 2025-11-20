package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.api.FileApi;
import com.liskovsoft.youtubeapi.app.PoTokenGate;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils;
import com.liskovsoft.youtubeapi.videoinfo.V2.DashInfoApi;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoHeaders;
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.AdaptiveVideoFormat;
import com.liskovsoft.youtubeapi.videoinfo.models.formats.VideoFormat;

import java.util.ArrayList;
import java.util.List;

import kotlin.Pair;

public abstract class VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceBase.class.getSimpleName();
    private final DashInfoApi mDashInfoApi;
    private final FileApi mFileApi;
    protected final AppService mAppService;

    protected VideoInfoServiceBase() {
        mAppService = AppService.instance();
        mDashInfoApi = RetrofitHelper.create(DashInfoApi.class);
        mFileApi = RetrofitHelper.create(FileApi.class);
    }

    // Will be overridden in descendants
    protected AppClient getClient() {
        return null;
    }

    protected void transformFormats(VideoInfo videoInfo) {
        if (videoInfo == null || videoInfo.isUnplayable()) {
            return;
        }

        decipherFormats(videoInfo);

        if (videoInfo.isLive()) {
            Log.d(TAG, "Enable seeking support on live streams...");
            videoInfo.sync(getDashInfo(videoInfo));
        }

        videoInfo.setClient(getClient());
    }

    private void decipherFormats(VideoInfo videoInfo) {
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

        Pair<List<String>, List<String>> result = mAppService.bulkSigExtract(extractNParams(urlHolders), extractSParams(urlHolders));

        if (result != null) {
            List<String> nParams = result.getFirst();
            List<String> signatures = result.getSecond();

            applyNParams(urlHolders, nParams);
            applySignatures(urlHolders, signatures);
        }

        String poToken = PoTokenGate.getPoToken(getClient());
        videoInfo.setPoToken(poToken);
        applySessionPoToken(urlHolders, poToken);
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

    private static void applyNParams(List<VideoUrlHolder> urlHolders, List<String> nParams) {
        if (nParams == null || nParams.isEmpty()) {
            return;
        }

        // All throttled strings has same values
        boolean sameSize = nParams.size() == urlHolders.size();

        for (int i = 0; i < urlHolders.size(); i++) {
            urlHolders.get(i).setNParam(nParams.get(sameSize ? i : 0));
        }
    }

    private static void applySessionPoToken(List<VideoUrlHolder> urlHolders, String poToken) {
        if (poToken == null) {
            return;
        }

        for (int i = 0; i < urlHolders.size(); i++) {
            urlHolders.get(i).setPoToken(poToken);
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

    private DashInfo getDashInfo(VideoInfo videoInfo) {
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

        if (format == null) {
            return null;
        }

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

    private AdaptiveVideoFormat getSmallestAudio(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isAudio(item.getMimeType())); // smallest format
        return format;
    }

    private AdaptiveVideoFormat getSmallestVideo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findLast(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isVideo(item.getMimeType())); // smallest format
        return format;
    }
    
    private AdaptiveVideoFormat getLargestVideo(VideoInfo videoInfo) {
        AdaptiveVideoFormat format = Helpers.findFirst(videoInfo.getAdaptiveFormats(),
                item -> MediaFormatUtils.isVideo(item.getMimeType())); // first is largest
        return format;
    }
}

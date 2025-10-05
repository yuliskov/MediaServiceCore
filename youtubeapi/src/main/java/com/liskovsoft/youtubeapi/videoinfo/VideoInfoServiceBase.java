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
    // Make response smaller
    private final String SMALL_RANGE = "&range=0-200";

    protected VideoInfoServiceBase() {
        mAppService = AppService.instance();
        mDashInfoApi = RetrofitHelper.create(DashInfoApi.class);
        mFileApi = RetrofitHelper.create(FileApi.class);
    }

    // Will be overridden in descendants
    protected boolean isWebPotRequired() {
        return false;
    }

    protected void transformFormats(VideoInfo videoInfo) {
        applySabrFixes(videoInfo.getAdaptiveFormats(), videoInfo.getServerAbrStreamingUrl());

        decipherFormats(videoInfo.getAdaptiveFormats(), videoInfo.getRegularFormats());

        if (videoInfo.isLive()) {
            Log.d(TAG, "Enable seeking support on live streams...");
            videoInfo.sync(getDashInfo(videoInfo));
        }
    }

    private void applySabrFixes(List<? extends VideoFormat> formats, String serverAbrStreamingUrl) {
        if (serverAbrStreamingUrl != null) {
            for (VideoFormat format : formats) {
                if (!format.isEmpty()) {
                    break;
                }
                format.setSabrUrl(serverAbrStreamingUrl);
            }
        }
    }

    private void decipherFormats(List<? extends VideoFormat> adaptiveFormats, List<? extends VideoFormat> regularFormats) {
        List<VideoFormat> formats = new ArrayList<>();
        if (adaptiveFormats != null)
            formats.addAll(adaptiveFormats);
        if (regularFormats != null)
            formats.addAll(regularFormats);

        if (formats.isEmpty()) {
            return;
        }

        Pair<List<String>, List<String>> result = mAppService.bulkSigExtract(extractNParams(formats), extractSParams(formats));

        if (result != null) {
            List<String> nSignatures = result.getFirst();
            List<String> signatures = result.getSecond();

            applyNSignatures(formats, nSignatures);
            applySignatures(formats, signatures);
        }

        // What this for? Could this fix throttling or maybe the source error?
        //applyAdditionalStrings(formats);

        if (isWebPotRequired()) {
            applySessionPoToken(formats, PoTokenGate.getWebSessionPoToken());
        }
    }

    private static List<String> extractSParams(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getSParam());
        }

        return result;
    }

    private static void applySignatures(List<? extends VideoFormat> formats, List<String> signatures) {
        if (signatures == null) {
            return;
        }

        if (signatures.size() != formats.size()) {
            throw new IllegalStateException("Sizes of formats and signatures should match!");
        }

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setSignature(signatures.get(i));
        }
    }

    private static List<String> extractNParams(List<? extends VideoFormat> formats) {
        List<String> result = new ArrayList<>();

        for (VideoFormat format : formats) {
            result.add(format.getNParam());
            // All throttled strings has same values
            //break;
        }

        return result;
    }

    private static void applyNSignatures(List<? extends VideoFormat> formats, List<String> nSignatures) {
        if (nSignatures == null || nSignatures.isEmpty()) {
            return;
        }

        // All throttled strings has same values
        boolean sameSize = nSignatures.size() == formats.size();

        for (int i = 0; i < formats.size(); i++) {
            formats.get(i).setNSignature(nSignatures.get(sameSize ? i : 0));
        }
    }

    private static void applySessionPoToken(List<? extends VideoFormat> formats, String poToken) {
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

package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import retrofit2.Call;

public class VideoInfoServiceSigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceSigned.class.getSimpleName();
    private static VideoInfoServiceSigned sInstance;
    private final VideoInfoManagerSigned mVideoInfoManagerSigned;
    private final LocaleManager mLocaleManager;

    private VideoInfoServiceSigned() {
        mVideoInfoManagerSigned = RetrofitHelper.withQueryString(VideoInfoManagerSigned.class);
        mLocaleManager = LocaleManager.instance();
    }

    public static VideoInfoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceSigned();
        }

        return sInstance;
    }

    public VideoInfoResult getVideoInfo(String videoId, String authorization) {
        VideoInfoResult result = getVideoInfoRegular(videoId, authorization);

        if (result != null && result.isLoginRequired()) {
            Log.e(TAG, "Seems that video age restricted. Retrying with different query method...");
            result = getVideoInfoRestricted(videoId, authorization);
        }

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info for videoId " + videoId);
        }

        return result;
    }

    private VideoInfoResult getVideoInfoRegular(String videoId, String authorization) {
        Call<VideoInfoResult> wrapper = mVideoInfoManagerSigned.getVideoInfoLocalized(videoId, mLocaleManager.getLanguage(), authorization);

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfoResult getVideoInfoRestricted(String videoId, String authorization) {
        Call<VideoInfoResult> wrapper = mVideoInfoManagerSigned.getVideoInfoRestricted(videoId, authorization);

        return RetrofitHelper.get(wrapper);
    }
}

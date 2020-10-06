package com.liskovsoft.youtubeapi.videoinfo;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
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

    public VideoInfo getVideoInfo(String videoId, String authorization) {
        VideoInfo result = getVideoInfoRegular(videoId, authorization);

        if (result != null && result.isLoginRequired()) {
            Log.e(TAG, "Seems that video age restricted. Retrying with different query method...");
            result = getVideoInfoRestricted(videoId, authorization);
        }

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s, authorization: %s", videoId, authorization);
        }

        return result;
    }

    private VideoInfo getVideoInfoRegular(String videoId, String authorization) {
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfoLocalized(videoId, mLocaleManager.getLanguage(), authorization);

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(String videoId, String authorization) {
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfoRestricted(videoId, authorization);

        return RetrofitHelper.get(wrapper);
    }
}

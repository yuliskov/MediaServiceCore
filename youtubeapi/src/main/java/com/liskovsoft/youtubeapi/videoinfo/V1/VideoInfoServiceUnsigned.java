package com.liskovsoft.youtubeapi.videoinfo.V1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceUnsigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceUnsigned.class.getSimpleName();
    private static VideoInfoServiceUnsigned sInstance;
    private final VideoInfoApiUnsigned mVideoInfoApiUnsigned;
    private final LocaleManager mLocaleManager;

    private VideoInfoServiceUnsigned() {
        mVideoInfoApiUnsigned = RetrofitHelper.create(VideoInfoApiUnsigned.class);
        mLocaleManager = LocaleManager.instance();
    }

    public static VideoInfoServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceUnsigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        return getVideoInfo(videoId);
    }

    public VideoInfo getVideoInfo(String videoId) {
        VideoInfo result = getVideoInfoHls(videoId);

        if (result != null && result.isAgeRestricted()) {
            Log.e(TAG, "Seems that video age restricted. Retrying with different query method...");
            result = getVideoInfoRestricted(videoId);
        }

        if (result != null) {
            transformFormats(result);
        } else {
            Log.e(TAG, "Can't get video info for videoId " + videoId);
        }

        return result;
    }
    
    private VideoInfo getVideoInfoHls(String videoId) {
        Call<VideoInfo> wrapper = mVideoInfoApiUnsigned.getVideoInfoHls(videoId, mLocaleManager.getLanguage(), AppClient.TV.getClientVersion());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(String videoId) {
        Call<VideoInfo> wrapper = mVideoInfoApiUnsigned.getVideoInfoRestricted(videoId, mLocaleManager.getLanguage(), AppClient.TV.getClientVersion());

        return RetrofitHelper.get(wrapper);
    }
}

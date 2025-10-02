package com.liskovsoft.youtubeapi.videoinfo.V1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.AppClient;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import com.liskovsoft.googlecommon.common.locale.LocaleManager;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceSigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceSigned.class.getSimpleName();
    private static VideoInfoServiceSigned sInstance;
    private final VideoInfoApiSigned mVideoInfoApiSigned;
    private final LocaleManager mLocaleManager;

    private VideoInfoServiceSigned() {
        mVideoInfoApiSigned = RetrofitHelper.create(VideoInfoApiSigned.class);
        mLocaleManager = LocaleManager.instance();
    }

    public static VideoInfoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceSigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams, String authorization) {
        return getVideoInfo(videoId, authorization);
    }

    public VideoInfo getVideoInfo(String videoId, String authorization) {
        VideoInfo result = getVideoInfoHls(videoId, authorization);

        if (result != null && result.isAgeRestricted()) {
            Log.e(TAG, "Seems that video age restricted. Retrying with different query method...");
            result = getVideoInfoRestricted(videoId, authorization);
        } else if (result != null && result.getVideoDetails() != null && result.getVideoDetails().isOwnerViewing()) {
            Log.e(TAG, "Seems that this is user video. Retrying with different query method...");
            result = getVideoInfoRegular(videoId, authorization);
        }

        if (result != null) {
            transformFormats(result);
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s, authorization: %s", videoId, authorization);
        }

        return result;
    }

    private VideoInfo getVideoInfoHls(String videoId, String authorization) {
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfoHls(videoId, ServiceHelper.getToken(authorization), mLocaleManager.getLanguage(), AppClient.TV.getClientVersion());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRegular(String videoId, String authorization) {
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfoRegular(videoId, ServiceHelper.getToken(authorization), mLocaleManager.getLanguage(), AppClient.TV.getClientVersion());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoRestricted(String videoId, String authorization) {
        Call<VideoInfo> wrapper = mVideoInfoApiSigned.getVideoInfoRestricted(videoId, ServiceHelper.getToken(authorization), mLocaleManager.getLanguage(), AppClient.TV.getClientVersion());

        return RetrofitHelper.get(wrapper);
    }
}

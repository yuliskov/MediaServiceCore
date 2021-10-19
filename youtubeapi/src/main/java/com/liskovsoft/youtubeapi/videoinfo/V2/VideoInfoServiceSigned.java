package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceSigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceSigned.class.getSimpleName();
    private static VideoInfoServiceSigned sInstance;
    private final VideoInfoManagerSigned mVideoInfoManagerSigned;

    private VideoInfoServiceSigned() {
        mVideoInfoManagerSigned = RetrofitHelper.withJsonPath(VideoInfoManagerSigned.class);
    }

    public static VideoInfoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceSigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams, String authorization) {
        VideoInfo result = getVideoInfoRegular(videoId, clickTrackingParams, authorization);

        //if (result != null && result.getVideoDetails() != null && result.getVideoDetails().isOwnerViewing()) {
        //    Log.e(TAG, "Seems that this is user video. Retrying with different query method...");
        //    result = getVideoInfoPrivate(videoId, clickTrackingParams, authorization);
        //}

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s, authorization: %s", videoId, authorization);
        }

        return result;
    }

    private VideoInfo getVideoInfoRegular(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQuery(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper);
    }

    private VideoInfo getVideoInfoPrivate(String videoId, String clickTrackingParams, String authorization) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQueryPrivate(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerSigned.getVideoInfo(videoInfoQuery, authorization, mAppService.getVisitorData());

        return RetrofitHelper.get(wrapper);
    }
}

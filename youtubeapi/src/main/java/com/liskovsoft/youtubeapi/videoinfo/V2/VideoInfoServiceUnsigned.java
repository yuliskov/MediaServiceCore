package com.liskovsoft.youtubeapi.videoinfo.V2;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceBase;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class VideoInfoServiceUnsigned extends VideoInfoServiceBase {
    private static final String TAG = VideoInfoServiceUnsigned.class.getSimpleName();
    private static VideoInfoServiceUnsigned sInstance;
    private final VideoInfoManagerUnsigned mVideoInfoManagerUnsigned;

    private VideoInfoServiceUnsigned() {
        mVideoInfoManagerUnsigned = RetrofitHelper.withJsonPath(VideoInfoManagerUnsigned.class);
    }

    public static VideoInfoServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new VideoInfoServiceUnsigned();
        }

        return sInstance;
    }

    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        VideoInfo result = getVideoInfoRegular(videoId, clickTrackingParams);

        if (result != null) {
            decipherFormats(result.getAdaptiveFormats());
            decipherFormats(result.getRegularFormats());
        } else {
            Log.e(TAG, "Can't get video info. videoId: %s", videoId);
        }

        return result;
    }

    private VideoInfo getVideoInfoRegular(String videoId, String clickTrackingParams) {
        String videoInfoQuery = VideoInfoManagerParams.getVideoInfoQuery(videoId, clickTrackingParams);
        Call<VideoInfo> wrapper = mVideoInfoManagerUnsigned.getVideoInfo(videoInfoQuery);

        return RetrofitHelper.get(wrapper);
    }
}

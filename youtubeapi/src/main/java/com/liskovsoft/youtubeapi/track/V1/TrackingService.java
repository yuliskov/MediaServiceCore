package com.liskovsoft.youtubeapi.track.V1;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import retrofit2.Call;

public class TrackingService {
    private static final String TAG = TrackingService.class.getSimpleName();
    private static TrackingService sInstance;
    private final TrackingManager mTrackingManager;
    private final AppService mAppService;
    private final VideoInfoServiceSigned mVideoInfoServiceSigned;

    private TrackingService() {
        mTrackingManager = RetrofitHelper.withJsonPath(TrackingManager.class);
        mAppService = AppService.instance();
        mVideoInfoServiceSigned = VideoInfoServiceSigned.instance();
    }

    public static TrackingService instance() {
        if (sInstance == null) {
            sInstance = new TrackingService();
        }

        return sInstance;
    }

    public void updateWatchTime(String videoId, float positionSec, float lengthSeconds,
                                String eventId, String visitorMonitoringData, String authorization) {
        updateWatchTime(
                videoId,
                lengthSeconds,
                positionSec,
                mAppService.getClientPlaybackNonce(),
                eventId,
                visitorMonitoringData,
                authorization
        );
    }

    public void updateWatchTime(String videoId, float positionSec, String authorization) {
        VideoInfo videoInfoResult = mVideoInfoServiceSigned.getVideoInfo(videoId, authorization);

        String lengthSeconds = videoInfoResult.getVideoDetails().getLengthSeconds();
        updateWatchTime(
                videoId,
                Float.parseFloat(lengthSeconds),
                positionSec,
                mAppService.getClientPlaybackNonce(),
                videoInfoResult.getEventId(),
                videoInfoResult.getVisitorMonitoringData(),
                authorization
        );
    }

    public void updateWatchTime(VideoInfo videoInfoResult, float positionSec, String authorization) {
        String lengthSeconds = videoInfoResult.getVideoDetails().getLengthSeconds();
        updateWatchTime(
                videoInfoResult.getVideoDetails().getVideoId(),
                Float.parseFloat(lengthSeconds),
                positionSec,
                mAppService.getClientPlaybackNonce(),
                videoInfoResult.getEventId(),
                videoInfoResult.getVisitorMonitoringData(),
                authorization
        );
    }

    private void updateWatchTime(String videoId, float lengthSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String authorization) {

        Log.d(TAG, String.format("Updating watch time... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec));

        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecord(
                videoId,
                lengthSec, 0, positionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                authorization
        );

        RetrofitHelper.get(wrapper); // execute

        wrapper = mTrackingManager.updateWatchTime(
                videoId, lengthSec, 0, positionSec, positionSec,
                clientPlaybackNonce, eventId, authorization
        );

        RetrofitHelper.get(wrapper); // execute
    }
}

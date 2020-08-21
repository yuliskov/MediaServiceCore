package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;
import retrofit2.Call;

public class TrackingService {
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

    public void updateWatchTime(String videoId, float positionSec, String authorization) {
        VideoInfoResult videoInfoResult = mVideoInfoServiceSigned.getVideoInfo(videoId, authorization);

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

    public void updateWatchTime(VideoInfoResult videoInfoResult, float positionSec, String authorization) {
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
        Call<WatchTimeEmptyResult> wrapper = mTrackingManager.createWatchRecord(
                videoId,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                authorization
        );

        RetrofitHelper.get(wrapper); // execute 'create record'

        wrapper = mTrackingManager.updateWatchTime(
                videoId, lengthSec, positionSec, positionSec, positionSec,
                clientPlaybackNonce, eventId, authorization
        );

        RetrofitHelper.get(wrapper); // execute 'update record time'
    }
}

package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.service.YouTubeSignInService;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import retrofit2.Call;

public class TrackingService {
    private static final String TAG = TrackingService.class.getSimpleName();
    private static TrackingService sInstance;
    private final TrackingApi mTrackingApi;
    private final AppService mAppService;
    private final YouTubeSignInService mSignInService;

    private TrackingService() {
        mTrackingApi = RetrofitHelper.withJsonPath(TrackingApi.class);
        mAppService = AppService.instance();
        mSignInService = YouTubeSignInService.instance();
    }

    public static TrackingService instance() {
        if (sInstance == null) {
            sInstance = new TrackingService();
        }

        return sInstance;
    }

    public void pauseWatchHistory() {
        String authorization = mSignInService.getAuthorizationHeader();

        if (authorization == null) {
            return;
        }

        Call<Void> wrapper = mTrackingApi.pauseWatchHistory(TrackingApiParams.getHistoryQuery(), authorization);
        RetrofitHelper.get(wrapper);
    }

    public void resumeWatchHistory() {
        String authorization = mSignInService.getAuthorizationHeader();

        if (authorization == null) {
            return;
        }

        Call<Void> wrapper = mTrackingApi.resumeWatchHistory(TrackingApiParams.getHistoryQuery(), authorization);
        RetrofitHelper.get(wrapper);
    }

    public void clearWatchHistory() {
        String authorization = mSignInService.getAuthorizationHeader();

        if (authorization == null) {
            return;
        }

        Call<Void> wrapper = mTrackingApi.clearWatchHistory(TrackingApiParams.getHistoryQuery(), authorization);
        RetrofitHelper.get(wrapper);
    }

    public void updateWatchTime(String videoId, float positionSec, float lengthSeconds,
                                String eventId, String visitorMonitoringData, String ofParam, String authorization) {
        updateWatchTimeFull(
                videoId,
                lengthSeconds,
                positionSec,
                mAppService.getClientPlaybackNonce(),
                eventId,
                visitorMonitoringData,
                ofParam,
                authorization
        );
    }

    private void updateWatchTimeFull(String videoId, float lengthSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam, String authorization) {

        Log.d(TAG, String.format("Updating watch time... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec));

        // Mark video as full watched if less than couple minutes remains
        boolean isVideoAlmostWatched = lengthSec - positionSec < 3 * 60;
        if (isVideoAlmostWatched) {
            updateWatchTimeShort(videoId, lengthSec, positionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam, authorization);
            return;
        }

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecord(
                videoId,
                lengthSec, positionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam,
                authorization
        );

        RetrofitHelper.get(wrapper); // execute

        wrapper = mTrackingApi.updateWatchTime(
                videoId, lengthSec, positionSec, positionSec,
                clientPlaybackNonce, eventId, authorization
        );

        RetrofitHelper.get(wrapper); // execute
    }

    private void updateWatchTimeShort(String videoId, float lengthSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam, String authorization) {

        Log.d(TAG, String.format("Updating watch time... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec));

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecordShort(
                videoId,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam,
                authorization
        );

        RetrofitHelper.get(wrapper); // execute

        wrapper = mTrackingApi.updateWatchTimeShort(
                videoId, positionSec, positionSec,
                clientPlaybackNonce, eventId, authorization
        );

        RetrofitHelper.get(wrapper); // execute
    }
}

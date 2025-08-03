package com.liskovsoft.youtubeapi.track;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import retrofit2.Call;

public class TrackingService {
    private static final String TAG = TrackingService.class.getSimpleName();
    private static TrackingService sInstance;
    private final TrackingApi mTrackingApi;

    private TrackingService() {
        mTrackingApi = RetrofitHelper.create(TrackingApi.class);
    }

    public static TrackingService instance() {
        if (sInstance == null) {
            sInstance = new TrackingService();
        }

        return sInstance;
    }

    public void updateWatchTime(String videoId, float positionSec, float lengthSeconds,
                                String eventId, String visitorMonitoringData, String ofParam) {
        if (Helpers.anyNull(videoId, eventId, visitorMonitoringData, ofParam)) {
            return;
        }

        updateWatchTimeFull(
                videoId,
                lengthSeconds,
                positionSec,
                getAppService().getClientPlaybackNonce(),
                eventId,
                visitorMonitoringData,
                ofParam
        );
    }

    private void updateWatchTimeFull(String videoId, float lengthSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam) {

        Log.d(TAG, String.format("Updating watch time... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec));

        // Mark video as full watched if less than couple minutes remains
        boolean isVideoAlmostWatched = lengthSec - positionSec < 3 * 60;
        if (isVideoAlmostWatched) {
            updateWatchTimeShort(videoId, lengthSec, positionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam);
            return;
        }

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecord(
                videoId,
                lengthSec, positionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        RetrofitHelper.get(wrapper); // execute

        wrapper = mTrackingApi.updateWatchTime(
                videoId, lengthSec, positionSec, positionSec,
                clientPlaybackNonce, eventId
        );

        RetrofitHelper.get(wrapper); // execute
    }

    private void updateWatchTimeShort(String videoId, float lengthSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam) {

        Log.d(TAG, String.format("Updating watch time... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec));

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecordShort(
                videoId,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        RetrofitHelper.get(wrapper); // execute

        wrapper = mTrackingApi.updateWatchTimeShort(
                videoId, positionSec, positionSec,
                clientPlaybackNonce, eventId
        );

        RetrofitHelper.get(wrapper); // execute
    }

    @NonNull
    private static AppService getAppService() {
        return AppService.instance();
    }
}

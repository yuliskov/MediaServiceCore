package com.liskovsoft.youtubeapi.track;

import android.util.Pair;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;

import retrofit2.Call;

public class TrackingService {
    private static final String TAG = TrackingService.class.getSimpleName();
    private static final int START_THRESHOLD_SEC = 3 * 60;
    private static TrackingService sInstance;
    private final TrackingApi mTrackingApi;
    private Pair<String, Float> mPosition;

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
                getOldPositionSec(videoId, positionSec),
                positionSec,
                getAppService().getClientPlaybackNonce(),
                eventId,
                visitorMonitoringData,
                ofParam
        );

        mPosition = new Pair<>(videoId, positionSec);
    }

    private void updateWatchTimeFull(String videoId, float lengthSec, float oldPositionSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam) {
        // Mark video as full watched if less than couple minutes remains
        boolean isVideoAlmostWatched = lengthSec - positionSec < getEndThresholdSec(lengthSec);
        //boolean previouslyAlmostWatched = previouslyAlmostWatched(videoId, lengthSec);
        //
        //if (isVideoAlmostWatched && previouslyAlmostWatched) {
        //    return;
        //}

        //if (isVideoAlmostWatched) {
        //    createWatchRecordShort(videoId, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam);
        //    updateWatchTimeShort(videoId, lengthSec, lengthSec, lengthSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam);
        //    return;
        //}

        if (isVideoAlmostWatched) {
            positionSec = lengthSec;
        }

        if (needNewRecord(videoId)) {
            createWatchRecordLong(videoId, lengthSec, oldPositionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam);
        }
        updateWatchTimeLong(videoId, lengthSec, oldPositionSec, positionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam);
    }

    private void createWatchRecordLong(String videoId, float lengthSec, float watchStartSec, String clientPlaybackNonce,
                                     String eventId, String visitorMonitoringData, String ofParam) {
        Log.d(TAG, "Creating watch record long... Video Id: %s, length: %s, start position: %s", videoId, lengthSec, watchStartSec);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecord(
                videoId, lengthSec, watchStartSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        RetrofitHelper.get(wrapper); // execute
    }

    private void updateWatchTimeLong(String videoId, float lengthSec, float oldPositionSec, float positionSec, String clientPlaybackNonce,
                                     String eventId, String visitorMonitoringData, String ofParam) {
        Log.d(TAG, "Updating watch time long... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.updateWatchTime(
                videoId, lengthSec, oldPositionSec, positionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        RetrofitHelper.get(wrapper); // execute
    }

    private void createWatchRecordShort(String videoId, String clientPlaybackNonce,
                                      String eventId, String visitorMonitoringData, String ofParam) {
        Log.d(TAG, "Creating watch record short... Video Id: %s", videoId);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecordShort(
                videoId,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        RetrofitHelper.get(wrapper); // execute
    }

    private void updateWatchTimeShort(String videoId, float lengthSec, float oldPositionSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam) {
        Log.d(TAG, "Updating watch time short... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.updateWatchTimeShort(
                videoId, oldPositionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        RetrofitHelper.get(wrapper); // execute
    }

    @NonNull
    private static AppService getAppService() {
        return AppService.instance();
    }

    private boolean needNewRecord(String videoId) {
        return !containsRecord(videoId);
    }

    private float getOldPositionSec(String videoId, float positionSec) {
        return containsRecord(videoId) ? mPosition.second : positionSec < START_THRESHOLD_SEC ? 0 : positionSec;
    }

    private boolean previouslyAlmostWatched(String videoId, float lengthSec) {
        return containsRecord(videoId) && lengthSec - mPosition.second < getEndThresholdSec(lengthSec);
    }

    private boolean containsRecord(String videoId) {
        return mPosition != null && Helpers.equals(mPosition.first, videoId);
    }

    private float getEndThresholdSec(float lengthSec) {
        return lengthSec * 0.05f;
    }
}

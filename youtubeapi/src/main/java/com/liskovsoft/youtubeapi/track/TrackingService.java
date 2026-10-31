package com.liskovsoft.youtubeapi.track;

import androidx.annotation.NonNull;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.track.models.WatchTimeEmptyResult;
import com.liskovsoft.youtubeapi.service.internal.AccountSession;

import kotlin.Triple;
import retrofit2.Call;

public class TrackingService {
    private static final String TAG = TrackingService.class.getSimpleName();
    private static final int START_THRESHOLD_SEC = 3 * 60;
    private static final long HISTORY_RENEW_MS = 30 * 60_000;
    private static TrackingService sInstance;
    private final TrackingApi mTrackingApi;
    private volatile AccountSession.Value<Triple<String, Float, Long>> mPosition;

    private TrackingService() {
        this(RetrofitHelper.create(TrackingApi.class));
    }

    TrackingService(TrackingApi trackingApi) {
        mTrackingApi = trackingApi;
    }

    public static TrackingService instance() {
        if (sInstance == null) {
            sInstance = new TrackingService();
        }

        return sInstance;
    }

    public void updateWatchTime(String videoId, float positionSec, float lengthSeconds,
                                String eventId, String visitorMonitoringData, String ofParam) {
        updateWatchTime(videoId, positionSec, lengthSeconds, eventId, visitorMonitoringData, ofParam,
                AccountSession.getGeneration());
    }

    public void updateWatchTime(String videoId, float positionSec, float lengthSeconds,
                                String eventId, String visitorMonitoringData, String ofParam, long generation) {
        AccountSession.checkCurrent(generation);
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
                ofParam,
                generation
        );

        mPosition = new AccountSession.Value<>(generation, new Triple<>(videoId, positionSec, System.currentTimeMillis()));
    }

    private void updateWatchTimeFull(String videoId, float lengthSec, float oldPositionSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam, long generation) {
        // Mark video as full watched if less than couple minutes remains
        boolean isVideoAlmostWatched = lengthSec - positionSec < getEndThresholdSec(lengthSec);

        if (isVideoAlmostWatched) {
            //positionSec = lengthSec;
            createWatchRecordShort(videoId, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam, generation);
            updateWatchTimeShort(videoId, lengthSec, lengthSec, lengthSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam, generation);
            return;
        }

        if (needNewRecord(videoId)) {
            createWatchRecordLong(videoId, lengthSec, oldPositionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam, generation);
        }
        //createWatchRecordLong(videoId, lengthSec, oldPositionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam);
        updateWatchTimeLong(videoId, lengthSec, oldPositionSec, positionSec, clientPlaybackNonce, eventId, visitorMonitoringData, ofParam, generation);
    }

    private void createWatchRecordLong(String videoId, float lengthSec, float watchStartSec, String clientPlaybackNonce,
                                     String eventId, String visitorMonitoringData, String ofParam, long generation) {
        Log.d(TAG, "Creating watch record long... Video Id: %s, length: %s, start position: %s", videoId, lengthSec, watchStartSec);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecord(
                videoId, lengthSec, watchStartSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        AccountSession.checkCurrent(generation);
        RetrofitHelper.get(wrapper); // execute
    }

    private void updateWatchTimeLong(String videoId, float lengthSec, float oldPositionSec, float positionSec, String clientPlaybackNonce,
                                     String eventId, String visitorMonitoringData, String ofParam, long generation) {
        Log.d(TAG, "Updating watch time long... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.updateWatchTime(
                videoId, lengthSec, oldPositionSec, positionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        AccountSession.checkCurrent(generation);
        RetrofitHelper.get(wrapper); // execute
    }

    private void createWatchRecordShort(String videoId, String clientPlaybackNonce,
                                      String eventId, String visitorMonitoringData, String ofParam, long generation) {
        Log.d(TAG, "Creating watch record short... Video Id: %s", videoId);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.createWatchRecordShort(
                videoId,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        AccountSession.checkCurrent(generation);
        RetrofitHelper.get(wrapper); // execute
    }

    private void updateWatchTimeShort(String videoId, float lengthSec, float oldPositionSec, float positionSec, String clientPlaybackNonce,
                                 String eventId, String visitorMonitoringData, String ofParam, long generation) {
        Log.d(TAG, "Updating watch time short... Video Id: %s, length: %s, position: %s", videoId, lengthSec, positionSec);

        Call<WatchTimeEmptyResult> wrapper = mTrackingApi.updateWatchTimeShort(
                videoId, oldPositionSec, positionSec,
                clientPlaybackNonce,
                eventId,
                visitorMonitoringData,
                ofParam
        );

        AccountSession.checkCurrent(generation);
        RetrofitHelper.get(wrapper); // execute
    }

    @NonNull
    private static AppService getAppService() {
        return AppService.instance();
    }

    private boolean needNewRecord(String videoId) {
        return !containsRecord(videoId) || isRecordOutdated();
    }

    private float getOldPositionSec(String videoId, float positionSec) {
        Triple<String, Float, Long> position = getPosition();
        return containsRecord(position, videoId) ? position.getSecond() : positionSec < START_THRESHOLD_SEC ? 0 : positionSec;
    }

    private boolean previouslyAlmostWatched(String videoId, float lengthSec) {
        Triple<String, Float, Long> position = getPosition();
        return containsRecord(position, videoId) && lengthSec - position.getSecond() < getEndThresholdSec(lengthSec);
    }

    private boolean containsRecord(String videoId) {
        return containsRecord(getPosition(), videoId);
    }

    private boolean containsRecord(Triple<String, Float, Long> position, String videoId) {
        return position != null && Helpers.equals(position.getFirst(), videoId);
    }

    private Triple<String, Float, Long> getPosition() {
        AccountSession.Value<Triple<String, Float, Long>> position = mPosition;
        return position != null ? position.get() : null;
    }

    private boolean isRecordOutdated() {
        Triple<String, Float, Long> position = getPosition();
        return position == null || System.currentTimeMillis() - position.getThird() > HISTORY_RENEW_MS;
    }

    private float getEndThresholdSec(float lengthSec) {
        return lengthSec * 0.05f;
    }

    public void clearCache() {
        mPosition = null;
    }
}

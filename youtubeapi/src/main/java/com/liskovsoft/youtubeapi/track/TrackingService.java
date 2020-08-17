package com.liskovsoft.youtubeapi.track;

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

public class TrackingService {
    private static TrackingService sInstance;
    private final TrackingManager mTrackingManager;

    private TrackingService() {
        mTrackingManager = RetrofitHelper.withJsonPath(TrackingManager.class);
    }

    public static TrackingService instance() {
        if (sInstance == null) {
            sInstance = new TrackingService();
        }

        return sInstance;
    }

    public void updateWatchTime(VideoInfoResult videoInfoResult) {
        
    }
}

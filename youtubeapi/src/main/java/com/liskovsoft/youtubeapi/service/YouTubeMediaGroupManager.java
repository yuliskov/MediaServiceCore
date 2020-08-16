package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaGroupManager implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManager.class.getSimpleName();
    private static YouTubeMediaGroupManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaGroupManager mMediaGroupManagerReal;

    private YouTubeMediaGroupManager() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
        checkSigned();
    }

    public static MediaGroupManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManager();
        }

        return sInstance;
    }

    @Override
    public MediaGroup getSearch(String searchText) {
        checkSigned();

        return mMediaGroupManagerReal.getSearch(searchText);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        checkSigned();

        return mMediaGroupManagerReal.getSearchObserve(searchText);
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mMediaGroupManagerReal.getSubscriptions();
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getSubscriptionsObserve();
    }

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        return mMediaGroupManagerReal.getRecommended();
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getRecommendedObserve();
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mMediaGroupManagerReal.getHistory();
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getHistoryObserve();
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        return mMediaGroupManagerReal.getHome();
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getHomeObserve();
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaTab) {
        checkSigned();

        return mMediaGroupManagerReal.continueGroup(mediaTab);
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab) {
        checkSigned();

        return mMediaGroupManagerReal.continueGroupObserve(mediaTab);
    }

    private void checkSigned() {
        if (mSignInManager.isSigned()) {
            Log.d(TAG, "User signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupManagerSigned.instance();
            YouTubeMediaGroupManagerUnsigned.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupManagerUnsigned.instance();
            YouTubeMediaGroupManagerSigned.unhold();
        }
    }
}

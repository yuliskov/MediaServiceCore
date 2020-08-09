package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;

public class YouTubeMediaGroupManager implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManager.class.getSimpleName();
    private static YouTubeMediaGroupManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaGroupManager mMediaTabManagerReal;

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
    public MediaGroup getSearchGroup(String searchText) {
        checkSigned();

        return mMediaTabManagerReal.getSearchGroup(searchText);
    }

    @Override
    public Observable<MediaGroup> getSearchGroupObserve(String searchText) {
        checkSigned();

        return mMediaTabManagerReal.getSearchGroupObserve(searchText);
    }

    @Override
    public MediaGroup getSubscriptionsGroup() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mMediaTabManagerReal.getSubscriptionsGroup();
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsGroupObserve() {
        checkSigned();

        return mMediaTabManagerReal.getSubscriptionsGroupObserve();
    }

    @Override
    public MediaGroup getRecommendedGroup() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        return mMediaTabManagerReal.getRecommendedGroup();
    }

    @Override
    public Observable<MediaGroup> getRecommendedGroupObserve() {
        checkSigned();

        return mMediaTabManagerReal.getRecommendedGroupObserve();
    }

    @Override
    public MediaGroup getHistoryGroup() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mMediaTabManagerReal.getHistoryGroup();
    }

    @Override
    public Observable<MediaGroup> getHistoryGroupObserve() {
        checkSigned();

        return mMediaTabManagerReal.getHistoryGroupObserve();
    }

    @Override
    public MediaGroup getHomeGroup() {
        checkSigned();

        return mMediaTabManagerReal.getHomeGroup();
    }

    @Override
    public Observable<MediaGroup> getHomeGroupObserve() {
        checkSigned();

        return mMediaTabManagerReal.getHomeGroupObserve();
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaTab) {
        checkSigned();

        return mMediaTabManagerReal.continueGroup(mediaTab);
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab) {
        checkSigned();

        return mMediaTabManagerReal.continueGroupObserve(mediaTab);
    }

    private void checkSigned() {
        if (mSignInManager.isSigned()) {
            Log.d(TAG, "User signed.");

            mMediaTabManagerReal = YouTubeMediaGroupManagerSigned.instance();
            YouTubeMediaGroupManagerSimple.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaTabManagerReal = YouTubeMediaGroupManagerSimple.instance();
            YouTubeMediaGroupManagerSigned.unhold();
        }
    }
}

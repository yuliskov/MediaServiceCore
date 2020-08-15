package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;

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
    public MediaGroup getSearchGroup(String searchText) {
        checkSigned();

        return mMediaGroupManagerReal.getSearchGroup(searchText);
    }

    @Override
    public Observable<MediaGroup> getSearchGroupObserve(String searchText) {
        checkSigned();

        return mMediaGroupManagerReal.getSearchGroupObserve(searchText);
    }

    @Override
    public MediaGroup getSubscriptionsGroup() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mMediaGroupManagerReal.getSubscriptionsGroup();
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsGroupObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getSubscriptionsGroupObserve();
    }

    @Override
    public MediaGroup getRecommendedGroup() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        return mMediaGroupManagerReal.getRecommendedGroup();
    }

    @Override
    public Observable<MediaGroup> getRecommendedGroupObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getRecommendedGroupObserve();
    }

    @Override
    public MediaGroup getHistoryGroup() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mMediaGroupManagerReal.getHistoryGroup();
    }

    @Override
    public Observable<MediaGroup> getHistoryGroupObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getHistoryGroupObserve();
    }

    @Override
    public MediaGroup getHomeGroup() {
        checkSigned();

        return mMediaGroupManagerReal.getHomeGroup();
    }

    @Override
    public Observable<MediaGroup> getHomeGroupObserve() {
        checkSigned();

        return mMediaGroupManagerReal.getHomeGroupObserve();
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

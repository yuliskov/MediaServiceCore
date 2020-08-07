package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaTabManager implements MediaTabManager {
    private static final String TAG = YouTubeMediaTabManager.class.getSimpleName();
    private static YouTubeMediaTabManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaTabManager mMediaTabManagerReal;

    private YouTubeMediaTabManager() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
        checkSigned();
    }

    public static MediaTabManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaTabManager();
        }

        return sInstance;
    }

    @Override
    public MediaTab getSearchTab(String searchText) {
        checkSigned();

        return mMediaTabManagerReal.getSearchTab(searchText);
    }

    @Override
    public Observable<MediaTab> getSearchTabObserve(String searchText) {
        checkSigned();

        return mMediaTabManagerReal.getSearchTabObserve(searchText);
    }

    @Override
    public MediaTab getSubscriptionsTab() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mMediaTabManagerReal.getSubscriptionsTab();
    }

    @Override
    public Observable<MediaTab> getSubscriptionsTabObserve() {
        checkSigned();

        return mMediaTabManagerReal.getSubscriptionsTabObserve();
    }

    @Override
    public MediaTab getRecommendedTab() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        return mMediaTabManagerReal.getRecommendedTab();
    }

    @Override
    public Observable<MediaTab> getRecommendedTabObserve() {
        checkSigned();

        return mMediaTabManagerReal.getRecommendedTabObserve();
    }

    @Override
    public MediaTab getHistoryTab() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mMediaTabManagerReal.getHistoryTab();
    }

    @Override
    public Observable<MediaTab> getHistoryTabObserve() {
        checkSigned();

        return mMediaTabManagerReal.getHistoryTabObserve();
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        checkSigned();

        return mMediaTabManagerReal.getHomeTabs();
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        checkSigned();

        return mMediaTabManagerReal.getHomeTabsObserve();
    }

    @Override
    public MediaTab continueTab(MediaTab mediaTab) {
        checkSigned();

        return mMediaTabManagerReal.continueTab(mediaTab);
    }

    @Override
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        checkSigned();

        return mMediaTabManagerReal.continueTabObserve(mediaTab);
    }

    private void checkSigned() {
        if (mSignInManager.isSigned()) {
            Log.d(TAG, "User signed.");

            mMediaTabManagerReal = YouTubeMediaTabManagerSigned.instance();
            YouTubeMediaTabManagerSimple.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaTabManagerReal = YouTubeMediaTabManagerSimple.instance();
            YouTubeMediaTabManagerSigned.unhold();
        }
    }
}

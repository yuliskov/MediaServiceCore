package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaService implements MediaService {
    private static final String TAG = YouTubeMediaService.class.getSimpleName();
    private static YouTubeMediaService sInstance;
    private MediaService mMediaService;
    private YouTubeSignInManager mAuthManager;

    private YouTubeMediaService() {
        Log.d(TAG, "Starting...");

        checkSigned();
    }

    public static MediaService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaService();
        }

        return sInstance;
    }

    @Override
    public MediaTab getSearch(String searchText) {
        checkSigned();

        return mMediaService.getSearch(searchText);
    }

    @Override
    public Observable<MediaTab> getSearchObserve(String searchText) {
        checkSigned();

        return mMediaService.getSearchObserve(searchText);
    }

    @Override
    public MediaTab getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mMediaService.getSubscriptions();
    }

    @Override
    public Observable<MediaTab> getSubscriptionsObserve() {
        checkSigned();

        return mMediaService.getSubscriptionsObserve();
    }

    @Override
    public MediaTab getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        return mMediaService.getRecommended();
    }

    @Override
    public Observable<MediaTab> getRecommendedObserve() {
        checkSigned();

        return mMediaService.getRecommendedObserve();
    }

    @Override
    public MediaTab getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mMediaService.getHistory();
    }

    @Override
    public Observable<MediaTab> getHistoryObserve() {
        checkSigned();

        return mMediaService.getHistoryObserve();
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        checkSigned();

        return mMediaService.getHomeTabs();
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        checkSigned();

        return mMediaService.getHomeTabsObserve();
    }

    @Override
    public MediaTab continueTab(MediaTab mediaTab) {
        checkSigned();

        return mMediaService.continueTab(mediaTab);
    }

    @Override
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        checkSigned();

        return mMediaService.continueTabObserve(mediaTab);
    }

    @Override
    public SignInManager getSignInManager() {
        checkSigned();

        return mMediaService.getSignInManager();
    }

    @Override
    public MediaTabManager getMediaTabManager() {
        return null;
    }

    private void checkSigned() {
        if (mAuthManager == null) {
            mAuthManager = YouTubeSignInManager.instance();
        }

        if (mAuthManager.isSigned()) {
            Log.d(TAG, "User signed.");

            mMediaService = YouTubeMediaServiceSigned.instance();
            YouTubeMediaServiceSimple.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaService = YouTubeMediaServiceSimple.instance();
            YouTubeMediaServiceSigned.unhold();
        }
    }
}

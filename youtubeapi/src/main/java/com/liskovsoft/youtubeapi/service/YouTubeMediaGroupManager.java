package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.service.internal.MediaGroupManagerInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupManagerSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupManagerUnsigned;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroupManager implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManager.class.getSimpleName();
    private static YouTubeMediaGroupManager sInstance;
    private final YouTubeSignInManager mSignInManager;
    private MediaGroupManagerInt mMediaGroupManagerReal;

    private YouTubeMediaGroupManager() {
        Log.d(TAG, "Starting...");

        mSignInManager = YouTubeSignInManager.instance();
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
        return Observable.fromCallable(() -> getSearch(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        return mMediaGroupManagerReal.getSubscriptions();
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return Observable.fromCallable(this::getSubscriptions);
    }

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        return mMediaGroupManagerReal.getRecommended();
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return Observable.fromCallable(this::getRecommended);
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        return mMediaGroupManagerReal.getHistory();
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return Observable.fromCallable(this::getHistory);
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        List<MediaGroup> result = new ArrayList<>();

        List<MediaGroup> groups = mMediaGroupManagerReal.getFirstHomeGroups();

        while (!groups.isEmpty()) {
            result.addAll(groups);
            groups = mMediaGroupManagerReal.getNextHomeGroups();
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            List<MediaGroup> groups = mMediaGroupManagerReal.getFirstHomeGroups();

            while (!groups.isEmpty()) {
                emitter.onNext(groups);
                groups = mMediaGroupManagerReal.getNextHomeGroups();
            }

            emitter.onComplete();
        });
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        checkSigned();

        return mMediaGroupManagerReal.continueGroup(mediaGroup);
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return Observable.fromCallable(() -> this.continueGroup(mediaGroup));
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

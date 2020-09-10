package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.TabbedBrowseResultContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import com.liskovsoft.youtubeapi.service.internal.MediaGroupManagerInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupManagerSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaGroupManagerUnsigned;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;

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

        SearchResult search = mMediaGroupManagerReal.getSearch(searchText);
        return YouTubeMediaGroup.from(search, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        return Observable.fromCallable(() -> getSearch(searchText));
    }

    @Override
    public MediaGroup getSubscriptions() {
        Log.d(TAG, "Getting subscriptions...");

        checkSigned();

        BrowseResult subscriptions = mMediaGroupManagerReal.getSubscriptions();
        return YouTubeMediaGroup.from(subscriptions, MediaGroup.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return Observable.create(emitter -> {
            MediaGroup subscriptions = getSubscriptions();

            if (subscriptions != null) {
                emitter.onNext(subscriptions);
            }

            emitter.onComplete();
        });
    }

    @Override
    public MediaGroup getRecommended() {
        Log.d(TAG, "Getting recommended...");

        checkSigned();

        BrowseTab homeTab = mMediaGroupManagerReal.getHomeTab();

        BrowseSection recommended = null;

        if (homeTab.getSections() != null) {
            recommended = homeTab.getSections().get(0); // first one is recommended
        }

        return YouTubeMediaGroup.from(recommended);
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return Observable.create(emitter -> {
            MediaGroup recommended = getRecommended();

            if (recommended != null) {
                emitter.onNext(recommended);
            }

            emitter.onComplete();
        });
    }

    @Override
    public MediaGroup getHistory() {
        Log.d(TAG, "Getting history...");

        checkSigned();

        BrowseResult history = mMediaGroupManagerReal.getHistory();
        return YouTubeMediaGroup.from(history, MediaGroup.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return Observable.create(emitter -> {
            MediaGroup history = getHistory();

            if (history != null) {
                emitter.onNext(history);
            }

            emitter.onComplete();
        });
    }

    @Override
    public List<MediaGroup> getHome() {
        checkSigned();

        BrowseTab tab = mMediaGroupManagerReal.getHomeTab();

        List<MediaGroup> result = new ArrayList<>();

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections());

        if (groups.isEmpty()) {
            Log.e(TAG, "Home group is empty");
        }

        while (!groups.isEmpty()) {
            result.addAll(groups);
            TabbedBrowseResultContinuation continuation = mMediaGroupManagerReal.continueTab(nextPageKey);

            if (continuation == null) {
                break;
            }

            nextPageKey = continuation.getNextPageKey();
            groups = YouTubeMediaGroup.from(continuation.getSections());
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            BrowseTab tab = mMediaGroupManagerReal.getHomeTab();

            emitGroups(emitter, tab);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getMusicObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            BrowseTab tab = mMediaGroupManagerReal.getMusicTab();

            emitGroups(emitter, tab);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getNewsObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            BrowseTab tab = mMediaGroupManagerReal.getNewsTab();

            emitGroups(emitter, tab);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getGamingObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            BrowseTab tab = mMediaGroupManagerReal.getGamingTab();

            emitGroups(emitter, tab);
        });
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, BrowseTab tab) {
        if (tab == null) {
            Log.e(TAG, "BrowseTab is null");
            emitter.onComplete();
            return;
        }

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections());

        if (groups.isEmpty()) {
            Log.e(TAG, "Music group is empty");
        }

        while (!groups.isEmpty()) {
            emitter.onNext(groups);
            TabbedBrowseResultContinuation continuation = mMediaGroupManagerReal.continueTab(nextPageKey);

            if (continuation == null) {
                break;
            }

            nextPageKey = continuation.getNextPageKey();
            groups = YouTubeMediaGroup.from(continuation.getSections());
        }

        emitter.onComplete();
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        checkSigned();

        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        if (mediaGroup.getType() == MediaGroup.TYPE_SEARCH) {
            return YouTubeMediaGroup.from(
                    mMediaGroupManagerReal.continueSearchGroup(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                    mediaGroup);
        }

        return YouTubeMediaGroup.from(
                mMediaGroupManagerReal.continueBrowseGroup(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                mediaGroup
        );
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return Observable.create(emitter -> {
            MediaGroup result = continueGroup(mediaGroup);

            if (result != null) {
                emitter.onNext(result);
            }

            emitter.onComplete();
        });
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

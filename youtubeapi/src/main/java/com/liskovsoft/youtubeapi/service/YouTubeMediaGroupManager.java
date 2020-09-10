package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.Section;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTab;
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

        GridTab subscriptions = mMediaGroupManagerReal.getSubscriptions();
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

        SectionTab homeTab = mMediaGroupManagerReal.getHomeTab();

        Section recommended = null;

        List<Section> rows = homeTab.getSections();

        if (rows != null) {
            recommended = rows.get(0); // first one is recommended
        }

        return YouTubeMediaGroup.from(recommended, MediaGroup.TYPE_RECOMMENDED);
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

        GridTab history = mMediaGroupManagerReal.getHistory();
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

        SectionTab tab = mMediaGroupManagerReal.getHomeTab();

        List<MediaGroup> result = new ArrayList<>();

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), MediaGroup.TYPE_HOME);

        if (groups.isEmpty()) {
            Log.e(TAG, "Home group is empty");
        }

        while (!groups.isEmpty()) {
            result.addAll(groups);
            SectionTabContinuation continuation = mMediaGroupManagerReal.continueSectionTab(nextPageKey);

            if (continuation == null) {
                break;
            }

            nextPageKey = continuation.getNextPageKey();
            groups = YouTubeMediaGroup.from(continuation.getSections(), MediaGroup.TYPE_HOME);
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getHomeTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_HOME);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getMusicObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getMusicTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_MUSIC);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getNewsObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getNewsTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_NEWS);
        });
    }

    @Override
    public Observable<List<MediaGroup>> getGamingObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            SectionTab tab = mMediaGroupManagerReal.getGamingTab();

            emitGroups(emitter, tab, MediaGroup.TYPE_GAMING);
        });
    }

    private void emitGroups(ObservableEmitter<List<MediaGroup>> emitter, SectionTab tab, int type) {
        if (tab == null) {
            Log.e(TAG, "BrowseTab is null");
            emitter.onComplete();
            return;
        }

        String nextPageKey = tab.getNextPageKey();
        List<MediaGroup> groups = YouTubeMediaGroup.from(tab.getSections(), type);

        if (groups.isEmpty()) {
            Log.e(TAG, "Music group is empty");
        }

        while (!groups.isEmpty()) {
            emitter.onNext(groups);
            SectionTabContinuation continuation = mMediaGroupManagerReal.continueSectionTab(nextPageKey);

            if (continuation != null) {
                nextPageKey = continuation.getNextPageKey();
                groups = YouTubeMediaGroup.from(continuation.getSections(), type);
            } else {
                break;
            }
        }

        emitter.onComplete();
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        checkSigned();

        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        switch (mediaGroup.getType()) {
            case MediaGroup.TYPE_SEARCH:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueSearch(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                        mediaGroup);
            case MediaGroup.TYPE_HISTORY:
            case MediaGroup.TYPE_SUBSCRIPTIONS:
            case MediaGroup.TYPE_PLAYLISTS:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueGridTab(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                        mediaGroup
                );
            default:
                return YouTubeMediaGroup.from(
                        mMediaGroupManagerReal.continueSection(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                        mediaGroup
                );
        }
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
            com.liskovsoft.youtubeapi.service.internal.old.YouTubeMediaGroupManagerUnsigned.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaGroupManagerReal = YouTubeMediaGroupManagerUnsigned.instance();
            com.liskovsoft.youtubeapi.service.internal.old.YouTubeMediaGroupManagerSigned.unhold();
        }
    }

    @Override
    public Observable<List<MediaGroup>> getPlaylistsObserve() {
        return Observable.create(emitter -> {
            checkSigned();

            List<GridTab> tabs = mMediaGroupManagerReal.getPlaylists();

            for (GridTab tab : tabs) {
                GridTabContinuation tabContinuation = mMediaGroupManagerReal.continueGridTab(tab.getReloadPageKey());

                if (tabContinuation != null) {
                    ArrayList<MediaGroup> list = new ArrayList<>();
                    YouTubeMediaGroup mediaGroup = new YouTubeMediaGroup(MediaGroup.TYPE_PLAYLISTS);
                    mediaGroup.setTitle(tab.getTitle());
                    list.add(YouTubeMediaGroup.from(tabContinuation, mediaGroup));
                    emitter.onNext(list);
                }
            }

            emitter.onComplete();
        });
    }
}

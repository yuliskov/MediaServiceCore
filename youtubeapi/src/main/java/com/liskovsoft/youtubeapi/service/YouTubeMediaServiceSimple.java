package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaServiceSimple implements MediaService {
    private static final String TAG = YouTubeMediaServiceSimple.class.getSimpleName();
    private static YouTubeMediaServiceSimple sInstance;
    private final BrowseService mBrowseService;
    private final SearchService mSearchService;

    private YouTubeMediaServiceSimple() {
        mSearchService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeMediaServiceSimple instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaServiceSimple();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    @Override
    public MediaTab getSearch(String searchText) {
        SearchResult searchResult = mSearchService.getSearch(searchText);
        return VideoServiceHelper.convertSearchResult(searchResult, MediaTab.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaTab> getSearchObserve(String searchText) {
        return Observable.fromCallable(() -> VideoServiceHelper.convertSearchResult(mSearchService.getSearch(searchText), MediaTab.TYPE_SEARCH));
    }

    @Override
    public MediaTab getRecommended() {
        List<MediaTab> tabs = getFirstHomeTabs();

        return tabs.get(0); // first one is Recommended tab
    }

    @Override
    public Observable<MediaTab> getRecommendedObserve() {
        return Observable.fromCallable(this::getRecommended);
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        List<MediaTab> result = new ArrayList<>();

        List<MediaTab> tabs = getFirstHomeTabs();

        while (!tabs.isEmpty()) {
            result.addAll(tabs);
            tabs = getNextHomeTabs();
        }

        return result;
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        return Observable.create(emitter -> {
            List<MediaTab> tabs = getFirstHomeTabs();

            while (!tabs.isEmpty()) {
                emitter.onNext(tabs);
                tabs = getNextHomeTabs();
            }

            emitter.onComplete();
        });
    }

    private List<MediaTab> getFirstHomeTabs() {
        Log.d(TAG, "Emitting first home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    private List<MediaTab> getNextHomeTabs() {
        Log.d(TAG, "Emitting next home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getNextHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public MediaTab continueTab(MediaTab mediaTab) {
        Log.d(TAG, "Continue tab " + mediaTab.getTitle() + "...");
        return VideoServiceHelper.convertNextBrowseResult(
                mBrowseService.continueSection(VideoServiceHelper.extractNextKey(mediaTab)),
                mediaTab
        );
    }

    @Override
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        return Observable.create(emitter -> {
            MediaTab newMediaTab = continueTab(mediaTab);

            if (newMediaTab != null) {
                emitter.onNext(newMediaTab);
            }

            emitter.onComplete();
        });
    }

    // SHOULD BE EMPTY FOR UNSIGNED

    @Override
    public MediaTab getSubscriptions() {
        return YouTubeMediaTab.EMPTY_TAB;
    }

    @Override
    public MediaTab getHistory() {
        return YouTubeMediaTab.EMPTY_TAB;
    }

    @Override
    public Observable<MediaTab> getSubscriptionsObserve() {
        return Observable.fromCallable(this::getSubscriptions);
    }

    @Override
    public Observable<MediaTab> getHistoryObserve() {
        return Observable.fromCallable(this::getHistory);
    }

    @Override
    public SignInManager getSignInManager() {
        return null;
    }

    // END SHOULD BE EMPTY FOR UNSIGNED

    @Override
    public MediaTabManager getMediaTabManager() {
        return null;
    }
}

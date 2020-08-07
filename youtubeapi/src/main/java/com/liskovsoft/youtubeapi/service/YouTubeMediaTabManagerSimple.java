package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.common.helpers.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaTabManagerSimple implements MediaTabManager {
    private static final String TAG = YouTubeMediaTabManagerSimple.class.getSimpleName();
    private static YouTubeMediaTabManagerSimple sInstance;
    private final BrowseService mBrowseService;
    private final SearchService mSearchService;

    private YouTubeMediaTabManagerSimple() {
        mSearchService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeMediaTabManagerSimple instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaTabManagerSimple();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    @Override
    public MediaTab getSearchTab(String searchText) {
        SearchResult searchResult = mSearchService.getSearch(searchText);
        return VideoServiceHelper.convertSearchResult(searchResult, MediaTab.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaTab> getSearchTabObserve(String searchText) {
        return Observable.fromCallable(() -> VideoServiceHelper.convertSearchResult(mSearchService.getSearch(searchText), MediaTab.TYPE_SEARCH));
    }

    @Override
    public MediaTab getRecommendedTab() {
        List<MediaTab> tabs = getFirstHomeTabs();

        return tabs.get(0); // first one is Recommended tab
    }

    @Override
    public Observable<MediaTab> getRecommendedTabObserve() {
        return Observable.fromCallable(this::getRecommendedTab);
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
    public MediaTab getSubscriptionsTab() {
        return YouTubeMediaTab.EMPTY_TAB;
    }

    @Override
    public MediaTab getHistoryTab() {
        return YouTubeMediaTab.EMPTY_TAB;
    }

    @Override
    public Observable<MediaTab> getSubscriptionsTabObserve() {
        return Observable.fromCallable(this::getSubscriptionsTab);
    }

    @Override
    public Observable<MediaTab> getHistoryTabObserve() {
        return Observable.fromCallable(this::getHistoryTab);
    }
}

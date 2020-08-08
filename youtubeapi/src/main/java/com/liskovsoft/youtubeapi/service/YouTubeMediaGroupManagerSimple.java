package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroupManagerSimple implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManagerSimple.class.getSimpleName();
    private static YouTubeMediaGroupManagerSimple sInstance;
    private final BrowseService mBrowseService;
    private final SearchService mSearchService;

    private YouTubeMediaGroupManagerSimple() {
        mSearchService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeMediaGroupManagerSimple instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerSimple();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    @Override
    public MediaGroup getSearchGroup(String searchText) {
        SearchResult searchResult = mSearchService.getSearch(searchText);
        return YouTubeMediaServiceHelper.convertSearchResult(searchResult, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchGroupObserve(String searchText) {
        return Observable.fromCallable(() -> YouTubeMediaServiceHelper.convertSearchResult(mSearchService.getSearch(searchText), MediaGroup.TYPE_SEARCH));
    }

    @Override
    public MediaGroup getRecommendedGroup() {
        List<MediaGroup> tabs = getFirstHomeTabs();

        return tabs.get(0); // first one is Recommended tab
    }

    @Override
    public Observable<MediaGroup> getRecommendedGroupObserve() {
        return Observable.fromCallable(this::getRecommendedGroup);
    }

    @Override
    public List<MediaGroup> getHomeGroups() {
        List<MediaGroup> result = new ArrayList<>();

        List<MediaGroup> tabs = getFirstHomeTabs();

        while (!tabs.isEmpty()) {
            result.addAll(tabs);
            tabs = getNextHomeTabs();
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeGroupsObserve() {
        return Observable.create(emitter -> {
            List<MediaGroup> tabs = getFirstHomeTabs();

            while (!tabs.isEmpty()) {
                emitter.onNext(tabs);
                tabs = getNextHomeTabs();
            }

            emitter.onComplete();
        });
    }

    private List<MediaGroup> getFirstHomeTabs() {
        Log.d(TAG, "Emitting first home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getHomeSections();
        return YouTubeMediaServiceHelper.convertBrowseSections(browseTabs);
    }

    private List<MediaGroup> getNextHomeTabs() {
        Log.d(TAG, "Emitting next home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getNextHomeSections();
        return YouTubeMediaServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaTab) {
        Log.d(TAG, "Continue tab " + mediaTab.getTitle() + "...");
        return YouTubeMediaServiceHelper.convertNextBrowseResult(
                mBrowseService.continueSection(YouTubeMediaServiceHelper.extractNextKey(mediaTab)),
                mediaTab
        );
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab) {
        return Observable.create(emitter -> {
            MediaGroup newMediaTab = continueGroup(mediaTab);

            if (newMediaTab != null) {
                emitter.onNext(newMediaTab);
            }

            emitter.onComplete();
        });
    }

    // SHOULD BE EMPTY FOR UNSIGNED

    @Override
    public MediaGroup getSubscriptionsGroup() {
        return YouTubeMediaGroup.EMPTY_TAB;
    }

    @Override
    public MediaGroup getHistoryGroup() {
        return YouTubeMediaGroup.EMPTY_TAB;
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsGroupObserve() {
        return Observable.fromCallable(this::getSubscriptionsGroup);
    }

    @Override
    public Observable<MediaGroup> getHistoryGroupObserve() {
        return Observable.fromCallable(this::getHistoryGroup);
    }
}

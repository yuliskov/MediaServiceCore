package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceUnsigned;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroupManagerUnsigned implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManagerUnsigned.class.getSimpleName();
    private static YouTubeMediaGroupManagerUnsigned sInstance;
    private final BrowseServiceUnsigned mBrowseService;
    private final SearchServiceUnsigned mSearchService;

    private YouTubeMediaGroupManagerUnsigned() {
        mSearchService = SearchServiceUnsigned.instance();
        mBrowseService = BrowseServiceUnsigned.instance();
    }

    public static YouTubeMediaGroupManagerUnsigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerUnsigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    @Override
    public MediaGroup getSearchGroup(String searchText) {
        SearchResult searchResult = mSearchService.getSearch(searchText);
        return YouTubeMediaGroup.from(searchResult, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchGroupObserve(String searchText) {
        return Observable.fromCallable(() -> YouTubeMediaGroup.from(mSearchService.getSearch(searchText), MediaGroup.TYPE_SEARCH));
    }

    @Override
    public MediaGroup getRecommendedGroup() {
        List<MediaGroup> tabs = getFirstHomeGroups();

        return tabs.get(0); // first one is Recommended tab
    }

    @Override
    public Observable<MediaGroup> getRecommendedGroupObserve() {
        return Observable.fromCallable(this::getRecommendedGroup);
    }

    @Override
    public MediaGroup getHomeGroup() {
        List<MediaGroup> result = new ArrayList<>();

        List<MediaGroup> groups = getFirstHomeGroups();

        while (!groups.isEmpty()) {
            result.addAll(groups);
            groups = getNextHomeGroups();
        }

        return YouTubeMediaGroup.from(result, MediaGroup.TYPE_HOME);
    }

    @Override
    public Observable<MediaGroup> getHomeGroupObserve() {
        return Observable.create(emitter -> {
            List<MediaGroup> groups = getFirstHomeGroups();

            while (!groups.isEmpty()) {
                emitter.onNext(YouTubeMediaGroup.from(groups, MediaGroup.TYPE_HOME));
                groups = getNextHomeGroups();
            }

            emitter.onComplete();
        });
    }

    private List<MediaGroup> getFirstHomeGroups() {
        Log.d(TAG, "Emitting first home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getHomeSections();
        return YouTubeMediaGroup.from(browseTabs);
    }

    private List<MediaGroup> getNextHomeGroups() {
        Log.d(TAG, "Emitting next home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getNextHomeSections();
        return YouTubeMediaGroup.from(browseTabs);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaTab) {
        Log.d(TAG, "Continue tab " + mediaTab.getTitle() + "...");
        return YouTubeMediaGroup.from(
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
        return YouTubeMediaGroup.EMPTY_GROUP;
    }

    @Override
    public MediaGroup getHistoryGroup() {
        return YouTubeMediaGroup.EMPTY_GROUP;
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

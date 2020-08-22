package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceUnsigned;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaGroupManagerUnsigned implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManagerUnsigned.class.getSimpleName();
    private static YouTubeMediaGroupManagerUnsigned sInstance;
    private final BrowseServiceUnsigned mBrowseServiceUnsigned;
    private final SearchServiceUnsigned mSearchServiceUnsigned;

    private YouTubeMediaGroupManagerUnsigned() {
        mSearchServiceUnsigned = SearchServiceUnsigned.instance();
        mBrowseServiceUnsigned = BrowseServiceUnsigned.instance();
    }

    public static YouTubeMediaGroupManagerUnsigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerUnsigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        BrowseServiceUnsigned.unhold();
        SearchServiceUnsigned.unhold();
    }

    @Override
    public MediaGroup getSearch(String searchText) {
        SearchResult searchResult = mSearchServiceUnsigned.getSearch(searchText);
        return YouTubeMediaGroup.from(searchResult, MediaGroup.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaGroup> getSearchObserve(String searchText) {
        return Observable.fromCallable(() -> getSearch(searchText));
    }

    @Override
    public MediaGroup getRecommended() {
        List<MediaGroup> tabs = getFirstHomeGroups();

        return tabs.get(0); // first one is Recommended tab
    }

    @Override
    public Observable<MediaGroup> getRecommendedObserve() {
        return Observable.fromCallable(this::getRecommended);
    }

    @Override
    public List<MediaGroup> getHome() {
        List<MediaGroup> result = new ArrayList<>();

        List<MediaGroup> groups = getFirstHomeGroups();

        while (!groups.isEmpty()) {
            result.addAll(groups);
            groups = getNextHomeGroups();
        }

        return result;
    }

    @Override
    public Observable<List<MediaGroup>> getHomeObserve() {
        return Observable.create(emitter -> {
            List<MediaGroup> groups = getFirstHomeGroups();

            while (!groups.isEmpty()) {
                emitter.onNext(groups);
                groups = getNextHomeGroups();
            }

            emitter.onComplete();
        });
    }

    private List<MediaGroup> getFirstHomeGroups() {
        Log.d(TAG, "Emitting first home groups...");
        List<BrowseSection> browseTabs = mBrowseServiceUnsigned.getHomeSections();
        return YouTubeMediaGroup.from(browseTabs);
    }

    private List<MediaGroup> getNextHomeGroups() {
        Log.d(TAG, "Emitting next home groups...");
        List<BrowseSection> browseTabs = mBrowseServiceUnsigned.getNextHomeSections();
        return YouTubeMediaGroup.from(browseTabs);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        Log.d(TAG, "Continue group " + mediaGroup.getTitle() + "...");

        if (mediaGroup.getType() == MediaGroup.TYPE_SEARCH) {
            return YouTubeMediaGroup.from(
                    mSearchServiceUnsigned.continueSearch(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                    mediaGroup);
        }

        return YouTubeMediaGroup.from(
                mBrowseServiceUnsigned.continueSection(YouTubeMediaServiceHelper.extractNextKey(mediaGroup)),
                mediaGroup
        );
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return Observable.fromCallable(() -> continueGroup(mediaGroup));
    }

    // SHOULD BE EMPTY FOR UNSIGNED

    @Override
    public MediaGroup getSubscriptions() {
        return YouTubeMediaGroup.EMPTY_GROUP;
    }

    @Override
    public MediaGroup getHistory() {
        return YouTubeMediaGroup.EMPTY_GROUP;
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsObserve() {
        return Observable.fromCallable(this::getSubscriptions);
    }

    @Override
    public Observable<MediaGroup> getHistoryObserve() {
        return Observable.fromCallable(this::getHistory);
    }
}

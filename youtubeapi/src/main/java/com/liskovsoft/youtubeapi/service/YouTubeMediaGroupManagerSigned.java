package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.MediaGroupManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

public class YouTubeMediaGroupManagerSigned implements MediaGroupManager {
    private static final String TAG = YouTubeMediaGroupManagerSigned.class.getSimpleName();
    private final SearchService mSearchService;
    private final BrowseServiceSigned mBrowseServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private static YouTubeMediaGroupManagerSigned sInstance;

    private YouTubeMediaGroupManagerSigned() {
        mSearchService = SearchService.instance();
        mBrowseServiceSigned = new BrowseServiceSigned();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static YouTubeMediaGroupManagerSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerSigned();
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
    public MediaGroup getSubscriptionsGroup() {
        return YouTubeMediaServiceHelper.convertBrowseResult(mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorization()), MediaGroup.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaGroup getRecommendedGroup() {
        return YouTubeMediaServiceHelper.convertBrowseSection(mBrowseServiceSigned.getRecommended(mSignInManager.getAuthorization()));
    }

    @Override
    public Observable<MediaGroup> getRecommendedGroupObserve() {
        return Observable.fromCallable(this::getRecommendedGroup);
    }

    @Override
    public MediaGroup getHistoryGroup() {
        return YouTubeMediaServiceHelper.convertBrowseResult(mBrowseServiceSigned.getHistory(mSignInManager.getAuthorization()), MediaGroup.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaGroup> getHistoryGroupObserve() {
        return Observable.fromCallable(this::getHistoryGroup);
    }

    @Override
    public Observable<MediaGroup> getSubscriptionsGroupObserve() {
        return Observable.fromCallable(this::getSubscriptionsGroup);
    }

    @Override
    public MediaGroup getHomeGroup() {
        // TODO: not implemented
        return null;
    }

    @Override
    public Observable<MediaGroup> getHomeGroupObserve() {
        // TODO: not implemented
        return null;
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaTab) {
        Log.d(TAG, "Continue tab " + mediaTab.getTitle() + "...");

        if (mediaTab.getType() == MediaGroup.TYPE_SEARCH) {
            return YouTubeMediaServiceHelper.convertNextSearchResult(
                    mSearchService.continueSearch(YouTubeMediaServiceHelper.extractNextKey(mediaTab)),
                    mediaTab);
        }

        return YouTubeMediaServiceHelper.convertNextBrowseResult(
                mBrowseServiceSigned.continueSection(YouTubeMediaServiceHelper.extractNextKey(mediaTab), mSignInManager.getAuthorization()),
                mediaTab
        );
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab) {
        return Observable.fromCallable(() -> continueGroup(mediaTab));
    }
}

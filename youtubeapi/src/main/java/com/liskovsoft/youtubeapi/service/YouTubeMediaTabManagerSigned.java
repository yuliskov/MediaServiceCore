package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaTabManagerSigned implements MediaTabManager {
    private static final String TAG = YouTubeMediaTabManagerSigned.class.getSimpleName();
    private final SearchService mSearchService;
    private final BrowseServiceSigned mBrowseServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private static YouTubeMediaTabManagerSigned sInstance;

    private YouTubeMediaTabManagerSigned() {
        mSearchService = new SearchService();
        mBrowseServiceSigned = new BrowseServiceSigned();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static YouTubeMediaTabManagerSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaTabManagerSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
    }

    @Override
    public MediaTab getSearchTab(String searchText) {
        SearchResult searchResult = mSearchService.getSearch(searchText);
        return YouTubeMediaServiceHelper.convertSearchResult(searchResult, MediaTab.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaTab> getSearchTabObserve(String searchText) {
        return Observable.fromCallable(() -> YouTubeMediaServiceHelper.convertSearchResult(mSearchService.getSearch(searchText), MediaTab.TYPE_SEARCH));
    }

    @Override
    public MediaTab getSubscriptionsTab() {
        return YouTubeMediaServiceHelper.convertBrowseResult(mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorization()), MediaTab.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaTab getRecommendedTab() {
        return YouTubeMediaServiceHelper.convertBrowseSection(mBrowseServiceSigned.getRecommended(mSignInManager.getAuthorization()));
    }

    @Override
    public Observable<MediaTab> getRecommendedTabObserve() {
        return Observable.fromCallable(this::getRecommendedTab);
    }

    @Override
    public MediaTab getHistoryTab() {
        return YouTubeMediaServiceHelper.convertBrowseResult(mBrowseServiceSigned.getHistory(mSignInManager.getAuthorization()), MediaTab.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaTab> getHistoryTabObserve() {
        return Observable.fromCallable(this::getHistoryTab);
    }

    @Override
    public Observable<MediaTab> getSubscriptionsTabObserve() {
        return Observable.fromCallable(this::getSubscriptionsTab);
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        // TODO: not implemented
        return null;
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        // TODO: not implemented
        return null;
    }

    @Override
    public MediaTab continueTab(MediaTab mediaTab) {
        Log.d(TAG, "Continue tab " + mediaTab.getTitle() + "...");

        if (mediaTab.getType() == MediaTab.TYPE_SEARCH) {
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
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        return Observable.fromCallable(() -> continueTab(mediaTab));
    }
}

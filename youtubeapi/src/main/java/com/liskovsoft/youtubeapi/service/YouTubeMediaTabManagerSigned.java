package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.common.helpers.VideoServiceHelper;
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
        return VideoServiceHelper.convertSearchResult(searchResult, MediaTab.TYPE_SEARCH);
    }

    @Override
    public Observable<MediaTab> getSearchTabObserve(String searchText) {
        return Observable.fromCallable(() -> VideoServiceHelper.convertSearchResult(mSearchService.getSearch(searchText), MediaTab.TYPE_SEARCH));
    }

    @Override
    public MediaTab getSubscriptionsTab() {
        return VideoServiceHelper.convertBrowseResult(mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorization()), MediaTab.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaTab getRecommendedTab() {
        return VideoServiceHelper.convertBrowseSection(mBrowseServiceSigned.getRecommended(mSignInManager.getAuthorization()));
    }

    @Override
    public Observable<MediaTab> getRecommendedTabObserve() {
        return Observable.fromCallable(this::getRecommendedTab);
    }

    @Override
    public MediaTab getHistoryTab() {
        return VideoServiceHelper.convertBrowseResult(mBrowseServiceSigned.getHistory(mSignInManager.getAuthorization()), MediaTab.TYPE_HISTORY);
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
            return VideoServiceHelper.convertNextSearchResult(
                    mSearchService.continueSearch(VideoServiceHelper.extractNextKey(mediaTab)),
                    mediaTab);
        }

        return VideoServiceHelper.convertNextBrowseResult(
                mBrowseServiceSigned.continueSection(VideoServiceHelper.extractNextKey(mediaTab), mSignInManager.getAuthorization()),
                mediaTab
        );
    }

    @Override
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        return Observable.fromCallable(() -> continueTab(mediaTab));
    }
}

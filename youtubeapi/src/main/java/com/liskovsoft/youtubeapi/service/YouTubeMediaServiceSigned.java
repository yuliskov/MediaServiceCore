package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaTabManager;
import com.liskovsoft.mediaserviceinterfaces.SignInManager;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaServiceSigned implements MediaService {
    private static final String TAG = YouTubeMediaServiceSigned.class.getSimpleName();
    private final SearchService mSearchService;
    private final BrowseServiceSigned mBrowseServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private static YouTubeMediaServiceSigned sInstance;

    private YouTubeMediaServiceSigned() {
        mSearchService = new SearchService();
        mBrowseServiceSigned = new BrowseServiceSigned();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static YouTubeMediaServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaServiceSigned();
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
    public MediaTab getSubscriptions() {
        return VideoServiceHelper.convertBrowseResult(mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorization()), MediaTab.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaTab getRecommended() {
        return VideoServiceHelper.convertBrowseSection(mBrowseServiceSigned.getRecommended(mSignInManager.getAuthorization()));
    }

    @Override
    public Observable<MediaTab> getRecommendedObserve() {
        return Observable.fromCallable(this::getRecommended);
    }

    @Override
    public MediaTab getHistory() {
        return VideoServiceHelper.convertBrowseResult(mBrowseServiceSigned.getHistory(mSignInManager.getAuthorization()), MediaTab.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaTab> getHistoryObserve() {
        return Observable.fromCallable(this::getHistory);
    }

    @Override
    public Observable<MediaTab> getSubscriptionsObserve() {
        return Observable.fromCallable(this::getSubscriptions);
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

    @Override
    public SignInManager getSignInManager() {
        return mSignInManager;
    }

    @Override
    public MediaTabManager getMediaTabManager() {
        return null;
    }
}

package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.Callable;

public class YouTubeMediaServiceSigned implements MediaService {
    private static final String TAG = YouTubeMediaServiceSigned.class.getSimpleName();
    private final SearchService mSearchService;
    private final BrowseServiceSigned mBrowseService;
    private static YouTubeMediaServiceSigned sInstance;

    public YouTubeMediaServiceSigned() {
        mSearchService = new SearchService();
        mBrowseService = new BrowseServiceSigned();
    }

    public static YouTubeMediaServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaServiceSigned();
        }

        return sInstance;
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
        return VideoServiceHelper.convertBrowseResult(mBrowseService.getSubscriptions(), MediaTab.TYPE_SUBSCRIPTIONS);
    }

    @Override
    public MediaTab getRecommended() {
        return VideoServiceHelper.convertBrowseSection(mBrowseService.getRecommended());
    }

    @Override
    public MediaTab getHistory() {
        return VideoServiceHelper.convertBrowseResult(mBrowseService.getHistory(), MediaTab.TYPE_HISTORY);
    }

    @Override
    public Observable<MediaTab> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<MediaTab> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<MediaTab> getRecommendedObserve() {
        return null;
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        return null;
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
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
                mBrowseService.continueSection(VideoServiceHelper.extractNextKey(mediaTab)),
                mediaTab
        );
    }

    @Override
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        return Observable.fromCallable(() -> continueTab(mediaTab));
    }
}

package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaService implements MediaService {
    private static YouTubeMediaService sInstance;
    private final BrowseService mBrowseService;
    private final SearchService mService;

    public YouTubeMediaService() {
        mService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeMediaService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaService();
        }

        return sInstance;
    }

    @Override
    public List<MediaItem> getSearch(String searchText) {
        return null;
    }

    @Override
    public List<MediaItem> getNextSearch() {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> getSearchObserve(String searchText) {
        return null;
    }

    @Override
    public List<MediaItem> getSubscriptions() {
        return null;
    }

    @Override
    public List<MediaItem> getNextSubscriptions() {
        return null;
    }

    @Override
    public List<MediaItem> getRecommended() {
        return null;
    }

    @Override
    public List<MediaItem> getNextRecommended() {
        return null;
    }

    @Override
    public List<MediaItem> getHistory() {
        return null;
    }

    @Override
    public List<MediaItem> getNextHistory() {
        return null;
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        List<BrowseSection> browseTabs = mBrowseService.getHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public List<MediaTab> getNextHomeTabs() {
        List<BrowseSection> browseTabs = mBrowseService.getNextHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        return Observable.fromCallable(this::getHomeTabs);
    }

    @Override
    public List<MediaItem> continueHomeTab(int tabIndex) {
        return VideoServiceHelper.convertVideoItems(mBrowseService.continueHomeSection(tabIndex));
    }

    @Override
    public Observable<List<MediaItem>> continueHomeTabObserve(int tabIndex) {
        return Observable.fromCallable(() -> continueHomeTab(tabIndex));
    }

    @Override
    public Observable<List<MediaItem>> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> getRecommendedObserve() {
        return null;
    }
}

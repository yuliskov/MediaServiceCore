package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.Callable;

public class YouTubeMediaServiceSigned implements MediaService {
    private final SearchService mService;
    private final BrowseServiceSigned mBrowseService;
    private static YouTubeMediaServiceSigned sInstance;

    public YouTubeMediaServiceSigned() {
        mService = new SearchService();
        mBrowseService = new BrowseServiceSigned();
    }

    public static YouTubeMediaServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaServiceSigned();
        }

        return sInstance;
    }

    @Override
    public List<MediaItem> getSearch(String searchText) {
        List<com.liskovsoft.youtubeapi.common.models.videos.VideoItem> videoItems = mService.getSearch(searchText);
        return VideoServiceHelper.convertVideoItems(videoItems);
    }

    @Override
    public List<MediaItem> getNextSearch() {
        List<com.liskovsoft.youtubeapi.common.models.videos.VideoItem> videoItems = mService.getNextSearch();
        return VideoServiceHelper.convertVideoItems(videoItems);
    }

    @Override
    public Observable<List<MediaItem>> getSearchObserve(String searchText) {
        return Observable.fromCallable(new Callable<List<MediaItem>>() {
            private boolean mSecondSearch;

            @Override
            public List<MediaItem> call() {
                List<com.liskovsoft.youtubeapi.common.models.videos.VideoItem> videoItems;

                if (!mSecondSearch) {
                    videoItems = mService.getSearch(searchText);
                    mSecondSearch = true;
                } else {
                    videoItems = mService.getNextSearch();
                }

                return VideoServiceHelper.convertVideoItems(videoItems);
            }
        });
    }

    @Override
    public List<MediaItem> getSubscriptions() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getSubscriptions());
    }

    @Override
    public List<MediaItem> getNextSubscriptions() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextSubscriptions());
    }

    @Override
    public List<MediaItem> getRecommended() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getRecommended());
    }

    @Override
    public List<MediaItem> getNextRecommended() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextRecommended());
    }

    @Override
    public List<MediaItem> getHistory() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getHistory());
    }

    @Override
    public List<MediaItem> getNextHistory() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextHistory());
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

    @Override
    public List<MediaTab> getHomeTabs() {
        return null;
    }

    @Override
    public List<MediaTab> getNextHomeTabs() {
        return null;
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        return null;
    }

    @Override
    public List<MediaItem> continueHomeTab(MediaTab mediaTab) {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> continueHomeTabObserve(MediaTab mediaTab) {
        return null;
    }
}

package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.VideoItem;
import com.liskovsoft.mediaserviceinterfaces.MediaSection;
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
    public List<VideoItem> getSearch(String searchText) {
        List<com.liskovsoft.youtubeapi.common.models.videos.VideoItem> videoItems = mService.getSearch(searchText);
        return VideoServiceHelper.convertVideoItems(videoItems);
    }

    @Override
    public List<VideoItem> getNextSearch() {
        List<com.liskovsoft.youtubeapi.common.models.videos.VideoItem> videoItems = mService.getNextSearch();
        return VideoServiceHelper.convertVideoItems(videoItems);
    }

    @Override
    public Observable<List<VideoItem>> getSearchObserve(String searchText) {
        return Observable.fromCallable(new Callable<List<VideoItem>>() {
            private boolean mSecondSearch;

            @Override
            public List<VideoItem> call() {
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
    public List<VideoItem> getSubscriptions() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getSubscriptions());
    }

    @Override
    public List<VideoItem> getNextSubscriptions() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextSubscriptions());
    }

    @Override
    public List<VideoItem> getRecommended() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getRecommended());
    }

    @Override
    public List<VideoItem> getNextRecommended() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextRecommended());
    }

    @Override
    public List<VideoItem> getHistory() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getHistory());
    }

    @Override
    public List<VideoItem> getNextHistory() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextHistory());
    }

    @Override
    public Observable<List<VideoItem>> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<List<VideoItem>> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<List<VideoItem>> getRecommendedObserve() {
        return null;
    }

    @Override
    public List<MediaSection> getHomeSections() {
        return null;
    }

    @Override
    public Observable<List<MediaSection>> getHomeSectionsObserve() {
        return null;
    }

    @Override
    public List<VideoItem> continueHomeSection(int sectionIndex) {
        return null;
    }

    @Override
    public Observable<List<VideoItem>> continueHomeSectionObserve(int sectionIndex) {
        return null;
    }
}

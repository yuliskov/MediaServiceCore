package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.videoserviceinterfaces.Video;
import com.liskovsoft.videoserviceinterfaces.VideoSection;
import com.liskovsoft.videoserviceinterfaces.VideoService;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import io.reactivex.Observable;

import java.util.List;
import java.util.concurrent.Callable;

public class YouTubeVideoServiceSigned implements VideoService {
    private final SearchService mService;
    private final BrowseServiceSigned mBrowseService;
    private static YouTubeVideoServiceSigned sInstance;

    public YouTubeVideoServiceSigned() {
        mService = new SearchService();
        mBrowseService = new BrowseServiceSigned();
    }

    public static YouTubeVideoServiceSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeVideoServiceSigned();
        }

        return sInstance;
    }

    @Override
    public List<Video> getSearch(String searchText) {
        List<VideoItem> videoItems = mService.getSearch(searchText);
        return VideoServiceHelper.convertVideoItems(videoItems);
    }

    @Override
    public List<Video> getNextSearch() {
        List<VideoItem> videoItems = mService.getNextSearch();
        return VideoServiceHelper.convertVideoItems(videoItems);
    }

    @Override
    public Observable<List<Video>> getSearchObserve(String searchText) {
        return Observable.fromCallable(new Callable<List<Video>>() {
            private boolean mSecondSearch;

            @Override
            public List<Video> call() {
                List<VideoItem> videoItems;

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
    public List<Video> getSubscriptions() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getSubscriptions());
    }

    @Override
    public List<Video> getNextSubscriptions() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextSubscriptions());
    }

    @Override
    public List<Video> getRecommended() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getRecommended());
    }

    @Override
    public List<Video> getNextRecommended() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextRecommended());
    }

    @Override
    public List<Video> getHistory() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getHistory());
    }

    @Override
    public List<Video> getNextHistory() {
        return VideoServiceHelper.convertVideoItems(mBrowseService.getNextHistory());
    }

    @Override
    public Observable<List<Video>> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<List<Video>> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<List<Video>> getRecommendedObserve() {
        return null;
    }

    @Override
    public List<VideoSection> getHomeSections() {
        return null;
    }

    @Override
    public Observable<List<VideoSection>> getHomeSectionsObserve() {
        return null;
    }

    @Override
    public List<Video> continueHomeSection(int sectionIndex) {
        return null;
    }

    @Override
    public Observable<List<Video>> continueHomeSectionObserve(int sectionIndex) {
        return null;
    }
}

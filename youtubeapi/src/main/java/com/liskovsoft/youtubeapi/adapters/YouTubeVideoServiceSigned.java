package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoSection;
import com.liskovsoft.myvideotubeapi.VideoService;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
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
        return YouTubeAdapterHelper.convertVideoItems(videoItems);
    }

    @Override
    public List<Video> getNextSearch() {
        List<VideoItem> videoItems = mService.getNextSearch();
        return YouTubeAdapterHelper.convertVideoItems(videoItems);
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

                return YouTubeAdapterHelper.convertVideoItems(videoItems);
            }
        });
    }

    @Override
    public List<Video> getSubscriptions() {
        return YouTubeAdapterHelper.convertVideoItems(mBrowseService.getSubscriptions());
    }

    @Override
    public List<Video> getNextSubscriptions() {
        return YouTubeAdapterHelper.convertVideoItems(mBrowseService.getNextSubscriptions());
    }

    @Override
    public List<Video> getRecommended() {
        return YouTubeAdapterHelper.convertVideoItems(mBrowseService.getRecommended());
    }

    @Override
    public List<Video> getNextRecommended() {
        return YouTubeAdapterHelper.convertVideoItems(mBrowseService.getNextRecommended());
    }

    @Override
    public List<Video> getHistory() {
        return YouTubeAdapterHelper.convertVideoItems(mBrowseService.getHistory());
    }

    @Override
    public List<Video> getNextHistory() {
        return YouTubeAdapterHelper.convertVideoItems(mBrowseService.getNextHistory());
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
    public List<VideoSection> getHome() {
        return null;
    }

    @Override
    public Observable<List<VideoSection>> getHomeObserve() {
        return null;
    }
}

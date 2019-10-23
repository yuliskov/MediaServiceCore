package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoService;
import com.liskovsoft.youtubeapi.common.BrowseService;
import com.liskovsoft.youtubeapi.common.SearchService;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class YouTubeVideoService implements VideoService {
    private final SearchService mService;
    private final BrowseService mBrowseService;
    private static YouTubeVideoService sInstance;

    public YouTubeVideoService() {
        mService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeVideoService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeVideoService();
        }

        return sInstance;
    }

    @Override
    public List<Video> getSearch(String searchText) {
        List<VideoItem> videoItems = mService.getSearch(searchText);
        return convertVideoItems(videoItems);
    }

    @Override
    public List<Video> getNextSearch() {
        List<VideoItem> videoItems = mService.getNextSearch();
        return convertVideoItems(videoItems);
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

                return convertVideoItems(videoItems);
            }
        });
    }

    private List<Video> convertVideoItems(List<VideoItem> items) {
        ArrayList<Video> result = new ArrayList<>();

        if (items == null) {
            return result;
        }

        for (VideoItem item : items) {
            result.add(YouTubeVideo.from(item));
        }

        return result;
    }

    @Override
    public List<Video> getSubscriptions() {
        return convertVideoItems(mBrowseService.getSubscriptions());
    }

    @Override
    public List<Video> getNextSubscriptions() {
        return convertVideoItems(mBrowseService.getNextSubscriptions());
    }

    @Override
    public List<Video> getRecommended() {
        return convertVideoItems(mBrowseService.getRecommended());
    }

    @Override
    public List<Video> getNextRecommended() {
        return convertVideoItems(mBrowseService.getNextRecommended());
    }

    @Override
    public List<Video> getHistory() {
        return convertVideoItems(mBrowseService.getHistory());
    }

    @Override
    public List<Video> getNextHistory() {
        return convertVideoItems(mBrowseService.getNextHistory());
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
}

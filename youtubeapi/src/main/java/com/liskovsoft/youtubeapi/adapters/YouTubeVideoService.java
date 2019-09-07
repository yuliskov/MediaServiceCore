package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoService;
import com.liskovsoft.youtubeapi.common.FrontendService;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class YouTubeVideoService implements VideoService {
    private final FrontendService mService;

    public YouTubeVideoService() {
        mService = new FrontendService();
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

        for (VideoItem item : items) {
            result.add(YouTubeVideo.from(item));
        }

        return result;
    }

    @Override
    public Observable<List<Video>> getSubscriptions() {
        return null;
    }

    @Override
    public Observable<List<Video>> getHistory() {
        return null;
    }

    @Override
    public Observable<List<Video>> getRecommended() {
        return null;
    }
}

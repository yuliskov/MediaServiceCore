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
    public List<Video> findVideos2(String searchText) {
        List<VideoItem> videoItems = mService.startSearch(searchText);
        return convertVideoItems(videoItems);
    }

    @Override
    public Observable<List<Video>> findVideos(String searchText) {
        return Observable.fromCallable(new Callable<List<Video>>() {
            private boolean mSecondSearch;

            @Override
            public List<Video> call() {
                List<VideoItem> videoItems;

                if (!mSecondSearch) {
                    videoItems = mService.startSearch(searchText);
                    mSecondSearch = true;
                } else {
                    videoItems = mService.getNextSearchPage();
                }

                return convertVideoItems(videoItems);
            }
        });
    }

    private List<Video> convertVideoItems(List<VideoItem> items) {
        ArrayList<Video> result = new ArrayList<>();

        for (VideoItem item : items) {
            result.add(convertVideo(item));
        }

        return result;
    }

    private Video convertVideo(VideoItem item) {
        return YouTubeVideo.from(item);
    }
}

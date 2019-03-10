package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.myvideotubeapi.VideoService;
import com.liskovsoft.youtubeapi.common.FrontendService;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import io.reactivex.Observable;

import java.util.ArrayList;
import java.util.List;

public class YouTubeVideoService implements VideoService {
    private final FrontendService mService;

    public YouTubeVideoService() {
        mService = new FrontendService();
    }

    @Override
    public Observable<List<Video>> findVideos(String searchText) {
        return Observable.fromCallable(() -> {
            List<VideoItem> videoItems = findVideoItems(searchText);
            return convertVideoItems(videoItems);
        });
    }

    private List<VideoItem> findVideoItems(String searchText) {
        return mService.startSearch(searchText);
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

package com.liskovsoft.myvideotubeapi;

import io.reactivex.Observable;

import java.util.List;

public interface VideoService {
    Observable<List<VideoItem>> findVideos(String searchText);
}

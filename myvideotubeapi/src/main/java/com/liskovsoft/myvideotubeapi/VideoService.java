package com.liskovsoft.myvideotubeapi;

import io.reactivex.Observable;
import java.util.List;

public interface VideoService {
    List<Video> findVideos2(String searchText);
    Observable<List<Video>> findVideos(String searchText);
}

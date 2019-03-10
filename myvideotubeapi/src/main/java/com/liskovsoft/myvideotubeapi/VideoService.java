package com.liskovsoft.myvideotubeapi;

import io.reactivex.Observable;
import java.util.List;

public interface VideoService {
    Observable<List<Video>> findVideos(String searchText);
}

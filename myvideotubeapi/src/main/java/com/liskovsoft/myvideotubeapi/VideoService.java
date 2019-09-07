package com.liskovsoft.myvideotubeapi;

import io.reactivex.Observable;
import java.util.List;

public interface VideoService {
    List<Video> getSearch(String searchText);
    List<Video> getNextSearch();
    Observable<List<Video>> getSearchObserve(String searchText);

    Observable<List<Video>> getSubscriptions();
    Observable<List<Video>> getHistory();
    Observable<List<Video>> getRecommended();
}

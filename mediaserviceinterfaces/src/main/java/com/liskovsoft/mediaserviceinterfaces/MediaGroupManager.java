package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import io.reactivex.Observable;

import java.util.List;

public interface MediaGroupManager {
    MediaGroup getSearch(String searchText);
    MediaGroup getSubscriptions();
    MediaGroup getRecommended();
    MediaGroup getHistory();
    List<MediaGroup> getHome();
    MediaGroup continueGroup(MediaGroup mediaTab);

    // RxJava interfaces
    Observable<MediaGroup> getSearchObserve(String searchText);
    Observable<MediaGroup> getSubscriptionsObserve();
    Observable<MediaGroup> getRecommendedObserve();
    Observable<MediaGroup> getHistoryObserve();
    Observable<List<MediaGroup>> getHomeObserve();
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}

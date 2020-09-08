package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import io.reactivex.Observable;

import java.util.List;

public interface MediaGroupManager {
    MediaGroup getSearch(String searchText);
    MediaGroup getSubscriptions();
    MediaGroup getRecommended(); // Note, that recommended is a part of Home
    MediaGroup getHistory();
    List<MediaGroup> getHome();
    MediaGroup continueGroup(MediaGroup mediaTab);

    // RxJava interfaces
    Observable<MediaGroup> getSearchObserve(String searchText);
    Observable<MediaGroup> getSubscriptionsObserve();
    Observable<MediaGroup> getRecommendedObserve(); // Note, that recommended is a part of Home
    Observable<MediaGroup> getHistoryObserve();
    Observable<List<MediaGroup>> getHomeObserve();
    Observable<List<MediaGroup>> getMusicObserve();
    Observable<List<MediaGroup>> getNewsObserve();
    Observable<List<MediaGroup>> getGamingObserve();
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}

package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import io.reactivex.Observable;

import java.util.List;

public interface MediaGroupManager {
    MediaGroup getSearch(String searchText);
    Observable<MediaGroup> getSearchObserve(String searchText);

    MediaGroup getSubscriptions();
    Observable<MediaGroup> getSubscriptionsObserve();

    MediaGroup getRecommended();
    Observable<MediaGroup> getRecommendedObserve();

    MediaGroup getHistory();
    Observable<MediaGroup> getHistoryObserve();

    List<MediaGroup> getHome();
    Observable<List<MediaGroup>> getHomeObserve();

    MediaGroup continueGroup(MediaGroup mediaTab);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}

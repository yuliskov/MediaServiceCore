package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

public interface MediaGroupManager {
    MediaGroup getSearchGroup(String searchText);
    Observable<MediaGroup> getSearchGroupObserve(String searchText);

    MediaGroup getSubscriptionsGroup();
    Observable<MediaGroup> getSubscriptionsGroupObserve();

    MediaGroup getRecommendedGroup();
    Observable<MediaGroup> getRecommendedGroupObserve();

    MediaGroup getHistoryGroup();
    Observable<MediaGroup> getHistoryGroupObserve();

    MediaGroup getHomeGroup();
    Observable<MediaGroup> getHomeGroupObserve();

    MediaGroup continueGroup(MediaGroup mediaTab);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}

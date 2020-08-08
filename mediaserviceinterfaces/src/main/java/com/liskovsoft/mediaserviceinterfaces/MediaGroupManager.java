package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

import java.util.List;

public interface MediaGroupManager {
    MediaGroup getSearchGroup(String searchText);
    Observable<MediaGroup> getSearchGroupObserve(String searchText);

    MediaGroup getSubscriptionsGroup();
    Observable<MediaGroup> getSubscriptionsGroupObserve();

    MediaGroup getRecommendedGroup();
    Observable<MediaGroup> getRecommendedGroupObserve();

    MediaGroup getHistoryGroup();
    Observable<MediaGroup> getHistoryGroupObserve();

    List<MediaGroup> getHomeGroups();
    Observable<List<MediaGroup>> getHomeGroupsObserve();

    MediaGroup continueGroup(MediaGroup mediaTab);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}

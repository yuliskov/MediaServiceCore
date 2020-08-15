package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
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

    List<MediaGroup> getHomeGroup();
    Observable<List<MediaGroup>> getHomeGroupObserve();

    MediaGroup continueGroup(MediaGroup mediaTab);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
}

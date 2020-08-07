package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

import java.util.List;

public interface MediaTabManager {
    MediaTab getSearchTab(String searchText);
    Observable<MediaTab> getSearchTabObserve(String searchText);

    MediaTab getSubscriptionsTab();
    Observable<MediaTab> getSubscriptionsTabObserve();

    MediaTab getRecommendedTab();
    Observable<MediaTab> getRecommendedTabObserve();

    MediaTab getHistoryTab();
    Observable<MediaTab> getHistoryTabObserve();

    List<MediaTab> getHomeTabs();
    Observable<List<MediaTab>> getHomeTabsObserve();

    MediaTab continueTab(MediaTab mediaTab);
    Observable<MediaTab> continueTabObserve(MediaTab mediaTab);
}

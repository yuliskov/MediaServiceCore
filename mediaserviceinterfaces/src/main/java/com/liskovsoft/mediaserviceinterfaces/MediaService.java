package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;
import java.util.List;

public interface MediaService {
    List<MediaItem> getSearch(String searchText);
    List<MediaItem> getNextSearch();
    Observable<List<MediaItem>> getSearchObserve(String searchText);

    List<MediaItem> getSubscriptions();
    List<MediaItem> getNextSubscriptions();

    List<MediaItem> getRecommended();
    List<MediaItem> getNextRecommended();

    List<MediaItem> getHistory();
    List<MediaItem> getNextHistory();

    List<MediaTab> getHomeTabs();
    Observable<List<MediaTab>> getHomeTabsObserve();

    List<MediaItem> continueHomeTab(int tabIndex);
    Observable<List<MediaItem>> continueHomeTabObserve(int tabIndex);

    Observable<List<MediaItem>> getSubscriptionsObserve();
    Observable<List<MediaItem>> getHistoryObserve();
    Observable<List<MediaItem>> getRecommendedObserve();
}

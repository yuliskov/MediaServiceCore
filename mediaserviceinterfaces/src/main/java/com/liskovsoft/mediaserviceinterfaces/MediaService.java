package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;

import java.util.List;

public interface MediaService {
    MediaTab getSearch(String searchText);
    Observable<MediaTab> getSearchObserve(String searchText);

    MediaTab getSubscriptions();
    Observable<MediaTab> getSubscriptionsObserve();

    MediaTab getRecommended();
    Observable<MediaTab> getRecommendedObserve();

    MediaTab getHistory();
    Observable<MediaTab> getHistoryObserve();

    List<MediaTab> getHomeTabs();
    Observable<List<MediaTab>> getHomeTabsObserve();

    MediaTab continueTab(MediaTab mediaTab);
    Observable<MediaTab> continueTabObserve(MediaTab mediaTab);

    SignInManager getSignInManager();
}

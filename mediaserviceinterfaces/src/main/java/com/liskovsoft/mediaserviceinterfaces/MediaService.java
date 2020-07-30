package com.liskovsoft.mediaserviceinterfaces;

import io.reactivex.Observable;
import java.util.List;

public interface MediaService {
    List<VideoItem> getSearch(String searchText);
    List<VideoItem> getNextSearch();
    Observable<List<VideoItem>> getSearchObserve(String searchText);

    List<VideoItem> getSubscriptions();
    List<VideoItem> getNextSubscriptions();

    List<VideoItem> getRecommended();
    List<VideoItem> getNextRecommended();

    List<VideoItem> getHistory();
    List<VideoItem> getNextHistory();

    List<MediaSection> getHomeSections();
    Observable<List<MediaSection>> getHomeSectionsObserve();

    List<VideoItem> continueHomeSection(int sectionIndex);
    Observable<List<VideoItem>> continueHomeSectionObserve(int sectionIndex);

    Observable<List<VideoItem>> getSubscriptionsObserve();
    Observable<List<VideoItem>> getHistoryObserve();
    Observable<List<VideoItem>> getRecommendedObserve();
}

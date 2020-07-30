package com.liskovsoft.videoserviceinterfaces;

import io.reactivex.Observable;
import java.util.List;

public interface VideoService {
    List<Video> getSearch(String searchText);
    List<Video> getNextSearch();
    Observable<List<Video>> getSearchObserve(String searchText);

    List<Video> getSubscriptions();
    List<Video> getNextSubscriptions();

    List<Video> getRecommended();
    List<Video> getNextRecommended();

    List<Video> getHistory();
    List<Video> getNextHistory();

    List<VideoSection> getHomeSections();
    Observable<List<VideoSection>> getHomeSectionsObserve();

    List<Video> continueHomeSection(int sectionIndex);
    Observable<List<Video>> continueHomeSectionObserve(int sectionIndex);

    Observable<List<Video>> getSubscriptionsObserve();
    Observable<List<Video>> getHistoryObserve();
    Observable<List<Video>> getRecommendedObserve();
}

package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.videoserviceinterfaces.Video;
import com.liskovsoft.videoserviceinterfaces.VideoSection;
import com.liskovsoft.videoserviceinterfaces.VideoService;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeVideoService implements VideoService {
    private static YouTubeVideoService sInstance;
    private final BrowseService mBrowseService;
    private final SearchService mService;

    public YouTubeVideoService() {
        mService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeVideoService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeVideoService();
        }

        return sInstance;
    }

    @Override
    public List<Video> getSearch(String searchText) {
        return null;
    }

    @Override
    public List<Video> getNextSearch() {
        return null;
    }

    @Override
    public Observable<List<Video>> getSearchObserve(String searchText) {
        return null;
    }

    @Override
    public List<Video> getSubscriptions() {
        return null;
    }

    @Override
    public List<Video> getNextSubscriptions() {
        return null;
    }

    @Override
    public List<Video> getRecommended() {
        return null;
    }

    @Override
    public List<Video> getNextRecommended() {
        return null;
    }

    @Override
    public List<Video> getHistory() {
        return null;
    }

    @Override
    public List<Video> getNextHistory() {
        return null;
    }

    @Override
    public List<VideoSection> getHomeSections() {
        List<BrowseTab> browseTabs = mBrowseService.getHomeTabs();
        return VideoServiceHelper.convertBrowseTabs(browseTabs);
    }

    @Override
    public Observable<List<VideoSection>> getHomeSectionsObserve() {
        return Observable.fromCallable(this::getHomeSections);
    }

    @Override
    public List<Video> continueHomeSection(int sectionIndex) {
        return VideoServiceHelper.convertVideoItems(mBrowseService.continueHomeSection(sectionIndex));
    }

    @Override
    public Observable<List<Video>> continueHomeSectionObserve(int sectionIndex) {
        return Observable.fromCallable(() -> continueHomeSection(sectionIndex));
    }

    @Override
    public Observable<List<Video>> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<List<Video>> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<List<Video>> getRecommendedObserve() {
        return null;
    }
}

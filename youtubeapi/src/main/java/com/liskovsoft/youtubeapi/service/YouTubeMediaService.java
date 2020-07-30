package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.VideoItem;
import com.liskovsoft.mediaserviceinterfaces.MediaSection;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaService implements MediaService {
    private static YouTubeMediaService sInstance;
    private final BrowseService mBrowseService;
    private final SearchService mService;

    public YouTubeMediaService() {
        mService = new SearchService();
        mBrowseService = new BrowseService();
    }

    public static YouTubeMediaService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaService();
        }

        return sInstance;
    }

    @Override
    public List<VideoItem> getSearch(String searchText) {
        return null;
    }

    @Override
    public List<VideoItem> getNextSearch() {
        return null;
    }

    @Override
    public Observable<List<VideoItem>> getSearchObserve(String searchText) {
        return null;
    }

    @Override
    public List<VideoItem> getSubscriptions() {
        return null;
    }

    @Override
    public List<VideoItem> getNextSubscriptions() {
        return null;
    }

    @Override
    public List<VideoItem> getRecommended() {
        return null;
    }

    @Override
    public List<VideoItem> getNextRecommended() {
        return null;
    }

    @Override
    public List<VideoItem> getHistory() {
        return null;
    }

    @Override
    public List<VideoItem> getNextHistory() {
        return null;
    }

    @Override
    public List<MediaSection> getHomeSections() {
        List<BrowseTab> browseTabs = mBrowseService.getHomeTabs();
        return VideoServiceHelper.convertBrowseTabs(browseTabs);
    }

    @Override
    public Observable<List<MediaSection>> getHomeSectionsObserve() {
        return Observable.fromCallable(this::getHomeSections);
    }

    @Override
    public List<VideoItem> continueHomeSection(int sectionIndex) {
        return VideoServiceHelper.convertVideoItems(mBrowseService.continueHomeSection(sectionIndex));
    }

    @Override
    public Observable<List<VideoItem>> continueHomeSectionObserve(int sectionIndex) {
        return Observable.fromCallable(() -> continueHomeSection(sectionIndex));
    }

    @Override
    public Observable<List<VideoItem>> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<List<VideoItem>> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<List<VideoItem>> getRecommendedObserve() {
        return null;
    }
}

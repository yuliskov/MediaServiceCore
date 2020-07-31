package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaService;
import com.liskovsoft.mediaserviceinterfaces.MediaTab;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseService;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.search.SearchService;
import io.reactivex.Observable;

import java.util.List;

public class YouTubeMediaService implements MediaService {
    private static final String TAG = YouTubeMediaService.class.getSimpleName();
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
    public List<MediaItem> getSearch(String searchText) {
        return null;
    }

    @Override
    public List<MediaItem> getNextSearch() {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> getSearchObserve(String searchText) {
        return null;
    }

    @Override
    public List<MediaItem> getSubscriptions() {
        return null;
    }

    @Override
    public List<MediaItem> getNextSubscriptions() {
        return null;
    }

    @Override
    public List<MediaItem> getRecommended() {
        return null;
    }

    @Override
    public List<MediaItem> getNextRecommended() {
        return null;
    }

    @Override
    public List<MediaItem> getHistory() {
        return null;
    }

    @Override
    public List<MediaItem> getNextHistory() {
        return null;
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        Log.d(TAG, "Emitting home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public List<MediaTab> getNextHomeTabs() {
        Log.d(TAG, "Emitting next home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getNextHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        return Observable.create(emitter -> {
            List<MediaTab> tabs = getHomeTabs();

            while (tabs != null && !tabs.isEmpty()) {
                emitter.onNext(tabs);
                tabs = getNextHomeTabs();
            }

            emitter.onComplete();
        });
    }

    @Override
    public List<MediaItem> continueHomeTab(MediaTab mediaTab) {
        Log.d(TAG, "Continue home tab at index " + mediaTab.getPosition() + "...");
        return VideoServiceHelper.convertVideoItems(mBrowseService.continueHomeSection(mediaTab.getPosition()));
    }

    @Override
    public Observable<List<MediaItem>> continueHomeTabObserve(MediaTab mediaTab) {
        return Observable.create(emitter -> {
            List<MediaItem> mediaItems = continueHomeTab(mediaTab);

            while (mediaItems != null && !mediaItems.isEmpty()) {
                emitter.onNext(mediaItems);
                mediaItems = continueHomeTab(mediaTab);
            }

            emitter.onComplete();
        });
    }

    @Override
    public Observable<List<MediaItem>> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<List<MediaItem>> getRecommendedObserve() {
        return null;
    }
}

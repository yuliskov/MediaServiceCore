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

import java.util.ArrayList;
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
    public MediaTab getSearch(String searchText) {
        return null;
    }

    @Override
    public Observable<MediaTab> getSearchObserve(String searchText) {
        return null;
    }

    @Override
    public MediaTab getSubscriptions() {
        return null;
    }

    @Override
    public MediaTab getRecommended() {
        return null;
    }

    @Override
    public MediaTab getHistory() {
        return null;
    }

    @Override
    public List<MediaTab> getHomeTabs() {
        List<MediaTab> result = new ArrayList<>();

        List<MediaTab> tabs = getFirstHomeTabs();

        while (!tabs.isEmpty()) {
            result.addAll(tabs);
            tabs = getNextHomeTabs();
        }

        return result;
    }

    @Override
    public Observable<List<MediaTab>> getHomeTabsObserve() {
        return Observable.create(emitter -> {
            List<MediaTab> tabs = getFirstHomeTabs();

            while (!tabs.isEmpty()) {
                emitter.onNext(tabs);
                tabs = getNextHomeTabs();
            }

            emitter.onComplete();
        });
    }

    private List<MediaTab> getFirstHomeTabs() {
        Log.d(TAG, "Emitting first home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    private List<MediaTab> getNextHomeTabs() {
        Log.d(TAG, "Emitting next home tabs...");
        List<BrowseSection> browseTabs = mBrowseService.getNextHomeSections();
        return VideoServiceHelper.convertBrowseSections(browseTabs);
    }

    @Override
    public MediaTab continueTab(MediaTab mediaTab) {
        Log.d(TAG, "Continue tab " + mediaTab.getTitle() + "...");
        return VideoServiceHelper.convertNextBrowseResult(
                mBrowseService.continueSection(VideoServiceHelper.extractNextKey(mediaTab)),
                mediaTab
        );
    }

    @Override
    public Observable<MediaTab> continueTabObserve(MediaTab mediaTab) {
        return Observable.create(emitter -> {
            MediaTab newMediaTab = continueTab(mediaTab);

            if (newMediaTab != null) {
                emitter.onNext(newMediaTab);
            }

            emitter.onComplete();
        });
    }

    @Override
    public Observable<MediaTab> getSubscriptionsObserve() {
        return null;
    }

    @Override
    public Observable<MediaTab> getHistoryObserve() {
        return null;
    }

    @Override
    public Observable<MediaTab> getRecommendedObserve() {
        return null;
    }
}

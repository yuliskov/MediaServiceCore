package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.ver2.BrowseServiceUnsigned;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.rows.SectionTabContinuation;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

import java.util.List;

public class YouTubeMediaGroupManagerUnsignedV2 implements MediaGroupManagerIntV2 {
    private static final String TAG = YouTubeMediaGroupManagerUnsignedV2.class.getSimpleName();
    private static YouTubeMediaGroupManagerUnsignedV2 sInstance;
    private final BrowseServiceUnsigned mBrowseServiceUnsigned;
    private final SearchServiceUnsigned mSearchServiceUnsigned;

    private YouTubeMediaGroupManagerUnsignedV2() {
        mSearchServiceUnsigned = SearchServiceUnsigned.instance();
        mBrowseServiceUnsigned = BrowseServiceUnsigned.instance();
    }

    public static YouTubeMediaGroupManagerUnsignedV2 instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerUnsignedV2();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        BrowseServiceUnsigned.unhold();
        SearchServiceUnsigned.unhold();
    }

    @Override
    public SearchResult getSearch(String searchText) {
        return mSearchServiceUnsigned.getSearch(searchText);
    }

    @Override
    public SearchResultContinuation continueSearch(String nextKey) {
        Log.d(TAG, "Continue search group...");

        return mSearchServiceUnsigned.continueSearch(nextKey);
    }

    @Override
    public GridTabContinuation continueGridTab(String nextKey) {
        Log.d(TAG, "Continue browse group...");

        return mBrowseServiceUnsigned.continueGridTab(nextKey);
    }

    @Override
    public SectionTab getHomeTab() {
        Log.d(TAG, "Emitting home group...");
        return mBrowseServiceUnsigned.getHome();
    }

    @Override
    public GridTab getSubscriptions() {
        // NOP
        return null;
    }

    @Override
    public GridTab getHistory() {
        // NOP
        return null;
    }

    @Override
    public List<GridTab> getPlaylists() {
        // NOP
        return null;
    }

    @Override
    public SectionTab getMusicTab() {
        Log.d(TAG, "Emitting music group...");
        return mBrowseServiceUnsigned.getMusic();
    }

    @Override
    public SectionTab getNewsTab() {
        Log.d(TAG, "Emitting news group...");
        return mBrowseServiceUnsigned.getNews();
    }

    @Override
    public SectionTab getGamingTab() {
        Log.d(TAG, "Emitting gaming group...");
        return mBrowseServiceUnsigned.getGaming();
    }

    @Override
    public SectionTabContinuation continueSectionTab(String nextPageKey) {
        Log.d(TAG, "Continue tab...");
        return mBrowseServiceUnsigned.continueRowsTab(nextPageKey);
    }
}

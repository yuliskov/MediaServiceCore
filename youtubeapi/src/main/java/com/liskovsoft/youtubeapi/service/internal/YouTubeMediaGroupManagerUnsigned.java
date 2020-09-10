package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.ver1.BrowseServiceUnsigned;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.TabbedBrowseResultContinuation;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

public class YouTubeMediaGroupManagerUnsigned implements MediaGroupManagerInt {
    private static final String TAG = YouTubeMediaGroupManagerUnsigned.class.getSimpleName();
    private static YouTubeMediaGroupManagerUnsigned sInstance;
    private final BrowseServiceUnsigned mBrowseServiceUnsigned;
    private final SearchServiceUnsigned mSearchServiceUnsigned;

    private YouTubeMediaGroupManagerUnsigned() {
        mSearchServiceUnsigned = SearchServiceUnsigned.instance();
        mBrowseServiceUnsigned = BrowseServiceUnsigned.instance();
    }

    public static YouTubeMediaGroupManagerUnsigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerUnsigned();
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
    public SearchResultContinuation continueSearchGroup(String nextKey) {
        Log.d(TAG, "Continue search group...");

        return mSearchServiceUnsigned.continueSearch(nextKey);
    }

    @Override
    public BrowseResultContinuation continueBrowseGroup(String nextKey) {
        Log.d(TAG, "Continue browse group...");

        return mBrowseServiceUnsigned.continueSection(nextKey);
    }

    @Override
    public BrowseTab getHomeTab() {
        Log.d(TAG, "Emitting home group...");
        return mBrowseServiceUnsigned.getHome();
    }

    @Override
    public BrowseResult getSubscriptions() {
        // NOP
        return null;
    }

    @Override
    public BrowseResult getHistory() {
        // NOP
        return null;
    }

    @Override
    public BrowseTab getMusicTab() {
        Log.d(TAG, "Emitting music group...");
        return mBrowseServiceUnsigned.getMusic();
    }

    @Override
    public BrowseTab getNewsTab() {
        Log.d(TAG, "Emitting news group...");
        return mBrowseServiceUnsigned.getNews();
    }

    @Override
    public BrowseTab getGamingTab() {
        Log.d(TAG, "Emitting gaming group...");
        return mBrowseServiceUnsigned.getGaming();
    }

    @Override
    public TabbedBrowseResultContinuation continueTab(String nextPageKey) {
        Log.d(TAG, "Continue tab...");
        return mBrowseServiceUnsigned.continueTab(nextPageKey);
    }
}

package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceSigned;
import com.liskovsoft.youtubeapi.browse.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseSection;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResultContinuation;
import com.liskovsoft.youtubeapi.search.SearchServiceSigned;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;

import java.util.List;

public class YouTubeMediaGroupManagerSigned implements MediaGroupManagerInt {
    private static final String TAG = YouTubeMediaGroupManagerSigned.class.getSimpleName();
    private final SearchServiceSigned mSearchServiceSigned;
    private final BrowseServiceSigned mBrowseServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private static YouTubeMediaGroupManagerSigned sInstance;

    private YouTubeMediaGroupManagerSigned() {
        mSearchServiceSigned = SearchServiceSigned.instance();
        mBrowseServiceSigned = BrowseServiceSigned.instance();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static YouTubeMediaGroupManagerSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaGroupManagerSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        BrowseServiceSigned.unhold();
        SearchServiceUnsigned.unhold();
    }

    @Override
    public SearchResult getSearch(String searchText) {
        return mSearchServiceSigned.getSearch(searchText, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseResult getSubscriptions() {
        return mBrowseServiceSigned.getSubscriptions(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseResult getHistory() {
        return mBrowseServiceSigned.getHistory(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public SearchResultContinuation continueSearchGroup(String nextKey) {
        Log.d(TAG, "Continue search group...");

        return mSearchServiceSigned.continueSearch(nextKey, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseResultContinuation continueBrowseGroup(String nextKey) {
        Log.d(TAG, "Continue browse group...");

        return mBrowseServiceSigned.continueSection(nextKey, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseTab getHomeTab() {
        Log.d(TAG, "Emitting home group...");
        return mBrowseServiceSigned.getHome(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseTab getMusicTab() {
        Log.d(TAG, "Emitting music group...");
        return mBrowseServiceSigned.getMusic(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseTab getNewsTab() {
        Log.d(TAG, "Emitting news group...");
        return mBrowseServiceSigned.getNews(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public BrowseTab getGamingTab() {
        Log.d(TAG, "Emitting gaming group...");
        return mBrowseServiceSigned.getGaming(mSignInManager.getAuthorizationHeader());
    }

    @Override
    public TabbedBrowseResultContinuation continueTab(String nextPageKey) {
        Log.d(TAG, "Continue tab...");
        return mBrowseServiceSigned.continueTab(nextPageKey, mSignInManager.getAuthorizationHeader());
    }
}

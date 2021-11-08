package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.BrowseServiceUnsigned;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.search.SearchServiceUnsigned;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

import java.util.List;

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
    public SearchResult getSearch(String searchText, int options) {
        return mSearchServiceUnsigned.getSearch(searchText, options);
    }

    @Override
    public List<String> getSearchTags(String searchText) {
        return mSearchServiceUnsigned.getSearchTags(searchText);
    }

    @Override
    public SearchResultContinuation continueSearch(String nextKey) {
        Log.d(TAG, "Continue search group...");

        return mSearchServiceUnsigned.continueSearch(nextKey);
    }

    @Override
    public SectionContinuation continueSection(String nextKey) {
        Log.d(TAG, "Continue section...");

        return mBrowseServiceUnsigned.continueSection(nextKey);
    }

    @Override
    public GridTabContinuation continueGridTab(String nextKey) {
        Log.d(TAG, "Continue grid tab...");

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
    public List<GridTab> getSubscribedChannelsUpdate() {
        // NOP
        return null;
    }

    @Override
    public List<GridTab> getSubscribedChannelsAZ() {
        // NOP
        return null;
    }

    @Override
    public List<GridTab> getSubscribedChannelsLastViewed() {
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
    public SectionList getChannel(String channelId) {
        Log.d(TAG, "Emitting channel sections...");
        return mBrowseServiceUnsigned.getChannel(channelId);
    }

    @Override
    public SectionTabContinuation continueSectionTab(String nextPageKey) {
        Log.d(TAG, "Continue tab...");
        return mBrowseServiceUnsigned.continueSectionTab(nextPageKey);
    }
}

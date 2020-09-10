package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.ver1.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.ver1.models.sections.RowsTabContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

public interface MediaGroupManagerInt {
    SearchResult getSearch(String searchText);
    BrowseResult getSubscriptions();
    BrowseResult getHistory();
    BrowseTab getHomeTab();
    BrowseTab getMusicTab();
    BrowseTab getNewsTab();
    BrowseTab getGamingTab();
    SearchResultContinuation continueSearchGroup(String nextKey);
    BrowseResultContinuation continueBrowseGroup(String nextKey);
    RowsTabContinuation continueTab(String nextPageKey);
}

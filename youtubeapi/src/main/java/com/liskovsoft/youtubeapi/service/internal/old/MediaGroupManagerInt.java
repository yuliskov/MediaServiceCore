package com.liskovsoft.youtubeapi.service.internal.old;

import com.liskovsoft.youtubeapi.browse.old.models.BrowseResult;
import com.liskovsoft.youtubeapi.browse.old.models.BrowseResultContinuation;
import com.liskovsoft.youtubeapi.browse.old.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.old.models.sections.RowsTabContinuation;
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

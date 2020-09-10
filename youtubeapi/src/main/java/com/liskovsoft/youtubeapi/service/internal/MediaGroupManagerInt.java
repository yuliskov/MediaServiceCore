package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.ver2.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

import java.util.List;

public interface MediaGroupManagerInt {
    SearchResult getSearch(String searchText);
    GridTab getSubscriptions();
    GridTab getHistory();
    SectionTab getHomeTab();
    SectionTab getMusicTab();
    SectionTab getNewsTab();
    SectionTab getGamingTab();
    List<GridTab> getPlaylists();
    SearchResultContinuation continueSearch(String nextKey);
    SectionContinuation continueSection(String nextKey);
    GridTabContinuation continueGridTab(String nextKey);
    SectionTabContinuation continueSectionTab(String nextPageKey);
}

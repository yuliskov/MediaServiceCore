package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTabContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionContinuation;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionList;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTab;
import com.liskovsoft.youtubeapi.browse.models.sections.SectionTabContinuation;
import com.liskovsoft.youtubeapi.search.models.SearchResult;
import com.liskovsoft.youtubeapi.search.models.SearchResultContinuation;

import java.util.List;

public interface MediaGroupManagerInt {
    SearchResult getSearch(String searchText);
    SearchResult getSearch(String searchText, int options);
    List<String> getSearchTags(String searchText);
    GridTab getSubscriptions();
    List<GridTab> getSubscribedChannelsUpdate();
    List<GridTab> getSubscribedChannelsAZ();
    List<GridTab> getSubscribedChannelsLastViewed();
    GridTab getHistory();
    SectionTab getHomeTab();
    SectionTab getMusicTab();
    SectionTab getNewsTab();
    SectionTab getGamingTab();
    SectionList getChannel(String channelId, String params);
    GridTab getGridChannel(String channelId);
    List<GridTab> getPlaylists();
    SearchResultContinuation continueSearch(String nextKey);
    SectionContinuation continueSection(String nextKey);
    GridTabContinuation continueGridTab(String nextKey);
    SectionTabContinuation continueSectionTab(String nextPageKey);
}

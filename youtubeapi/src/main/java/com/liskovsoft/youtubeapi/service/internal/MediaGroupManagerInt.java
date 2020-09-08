package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.youtubeapi.browse.models.sections.BrowseTab;
import com.liskovsoft.youtubeapi.browse.models.sections.TabbedBrowseResultContinuation;

import java.util.List;

public interface MediaGroupManagerInt {
    MediaGroup getSearch(String searchText);
    MediaGroup getSubscriptions();
    MediaGroup getRecommended();
    MediaGroup getHistory();
    List<MediaGroup> getFirstHomeGroups();
    List<MediaGroup> getNextHomeGroups();
    MediaGroup continueGroup(MediaGroup mediaGroup);
    BrowseTab getMusicTab();
    BrowseTab getNewsTab();
    BrowseTab getGamingTab();
    TabbedBrowseResultContinuation continueTab(String nextPageKey);
}

package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;

import java.util.List;

public interface MediaGroupManagerInt {
    MediaGroup getSearch(String searchText);
    MediaGroup getSubscriptions();
    MediaGroup getRecommended();
    MediaGroup getHistory();
    List<MediaGroup> getFirstHomeGroups();
    List<MediaGroup> getNextHomeGroups();
    MediaGroup continueGroup(MediaGroup mediaGroup);
}

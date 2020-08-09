package com.liskovsoft.mediaserviceinterfaces;

import java.util.List;

public interface MediaGroup {
    int TYPE_UNDEFINED = -1;
    int TYPE_HOME = 0;
    int TYPE_SEARCH = 1;
    int TYPE_RECOMMENDED = 2;
    int TYPE_HISTORY = 3;
    int TYPE_SUBSCRIPTIONS = 4;
    int getType();
    void setType(int type);
    List<MediaItem> getMediaItems();
    void setMediaItems(List<MediaItem> tabs);
    String getTitle();
    void setTitle(String title);
    List<MediaGroup> getNestedGroups();
}

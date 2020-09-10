package com.liskovsoft.mediaserviceinterfaces.data;

import java.util.List;

public interface MediaGroup {
    int TYPE_UNDEFINED = -1;
    int TYPE_HOME = 0;
    int TYPE_SEARCH = 1;
    int TYPE_RECOMMENDED = 2;
    int TYPE_HISTORY = 3;
    int TYPE_SUBSCRIPTIONS = 4;
    int TYPE_MUSIC = 5;
    int TYPE_NEWS = 6;
    int TYPE_GAMING = 7;
    int TYPE_PLAYLISTS = 8;
    int getType();
    void setType(int type);
    List<MediaItem> getMediaItems();
    void setMediaItems(List<MediaItem> tabs);
    String getTitle();
    void setTitle(String title);
}

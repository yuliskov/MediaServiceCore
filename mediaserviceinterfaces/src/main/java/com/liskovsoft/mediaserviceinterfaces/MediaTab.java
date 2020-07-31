package com.liskovsoft.mediaserviceinterfaces;

import java.util.List;

public interface MediaTab {
    List<MediaItem> getMediaItems();
    void setMediaItems(List<MediaItem> items);
    String getTitle();
    void setTitle(String title);
}

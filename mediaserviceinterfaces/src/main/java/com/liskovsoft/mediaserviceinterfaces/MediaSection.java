package com.liskovsoft.mediaserviceinterfaces;

import java.util.List;

public interface MediaSection {
    List<MediaItem> getVideoItems();
    void setVideoItems(List<MediaItem> items);
    String getTitle();
    void setTitle(String title);
}

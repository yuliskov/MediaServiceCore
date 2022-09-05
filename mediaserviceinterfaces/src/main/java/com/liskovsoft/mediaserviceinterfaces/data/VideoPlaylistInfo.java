package com.liskovsoft.mediaserviceinterfaces.data;

public interface VideoPlaylistInfo {
    String getTitle();
    String getPlaylistId();
    boolean isSelected();
    int getSize();
    int getCurrentIndex();
}

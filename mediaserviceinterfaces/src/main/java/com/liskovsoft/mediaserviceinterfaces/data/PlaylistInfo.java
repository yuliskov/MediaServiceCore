package com.liskovsoft.mediaserviceinterfaces.data;

public interface PlaylistInfo {
    String getTitle();
    String getPlaylistId();
    boolean isSelected();
    int getSize();
    int getCurrentIndex();
}

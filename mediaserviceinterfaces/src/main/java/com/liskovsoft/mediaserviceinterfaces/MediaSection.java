package com.liskovsoft.mediaserviceinterfaces;

import java.util.List;

public interface MediaSection {
    List<VideoItem> getVideoItems();
    void setVideoItems(List<VideoItem> items);
    List<MusicItem> getMusicItems();
    void setMusicItems(List<MusicItem> items);
    String getTitle();
    void setTitle(String title);
}

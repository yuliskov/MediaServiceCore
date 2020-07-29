package com.liskovsoft.myvideotubeapi;

import java.util.List;

public interface VideoSection {
    List<Video> getVideos();
    void setVideos(List<Video> videos);
    String getTitle();
    void setTitle(String title);
}

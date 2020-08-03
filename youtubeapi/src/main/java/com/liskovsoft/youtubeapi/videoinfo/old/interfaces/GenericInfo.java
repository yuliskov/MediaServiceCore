package com.liskovsoft.youtubeapi.videoinfo.old.interfaces;

public interface GenericInfo {
    String LENGTH_SECONDS = "length_seconds";
    String TITLE = "title";
    String AUTHOR = "author";
    String VIEW_COUNT = "view_count";
    String TIMESTAMP = "timestamp";
    String getLengthSeconds();
    void setLengthSeconds(String lengthSeconds);
    String getTitle();
    void setTitle(String title);
    String getAuthor();
    void setAuthor(String author);
    String getViewCount();
    void setViewCount(String viewCount);
    String getTimestamp();
    void setTimestamp(String timestamp);
    String getDescription();
    void setDescription(String description);
    String getVideoId();
    void setVideoId(String videoId);
    String getChannelId();
    void setChannelId(String channelId);
}

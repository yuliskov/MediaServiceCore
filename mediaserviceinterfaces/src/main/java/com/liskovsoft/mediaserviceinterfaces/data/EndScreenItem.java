package com.liskovsoft.mediaserviceinterfaces.data;

public interface EndScreenItem {
    int TYPE_VIDEO = 0;
    int TYPE_CHANNEL = 1;
    int TYPE_PLAYLIST = 2;
    int TYPE_UNKNOWN = 3;

    int getType();
    String getTitle();
    String getMetadata();
    String getImageUrl();
    String getId(); // videoId, channelId, or playlistId
    long getStartTimeMs();
    long getEndTimeMs();
    float getLeft();
    float getTop();
    float getWidth();
    float getAspectRatio();
}
package com.liskovsoft.mediaserviceinterfaces;

public interface VideoItem extends GenericItem {
    String getDescription();
    void setDescription(String description);
    boolean isLive();
    void setLive(boolean isLive);
}

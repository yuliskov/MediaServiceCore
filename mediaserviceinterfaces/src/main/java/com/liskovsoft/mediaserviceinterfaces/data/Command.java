package com.liskovsoft.mediaserviceinterfaces.data;

public interface Command {
    int TYPE_OPEN = 0;
    int getType();
    String getVideoId();
}

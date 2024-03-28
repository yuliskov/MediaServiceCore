package com.liskovsoft.mediaserviceinterfaces.yt.data;

public interface MediaItemStoryboard {
    int getGroupDurationMS();
    Size getGroupSize();
    String getGroupUrl(int imgNum);
    interface Size {
        int getDurationEachMS();
        int getWidth();
        int getHeight();
        int getRowCount();
        int getColCount();
    }
}

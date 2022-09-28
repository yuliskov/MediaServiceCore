package com.liskovsoft.youtubeapi.videoinfo.models;

public interface DashInfo {
    long getStartTimeMs();
    int getStartSegmentNum();
    boolean isSeekable();
}

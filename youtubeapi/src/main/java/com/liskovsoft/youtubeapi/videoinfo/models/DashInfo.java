package com.liskovsoft.youtubeapi.videoinfo.models;

public interface DashInfo {
    int getSegmentDurationUs();
    long getStartTimeMs();
    int getStartSegmentNum();
    boolean isSeekable();
}

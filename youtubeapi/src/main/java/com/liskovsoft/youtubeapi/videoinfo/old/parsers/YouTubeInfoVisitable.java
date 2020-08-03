package com.liskovsoft.youtubeapi.videoinfo.old.parsers;

public interface YouTubeInfoVisitable {
    void accept(YouTubeInfoVisitor visitor);
}

package com.liskovsoft.youtubeapi.videoinfo.parsers;

public interface YouTubeInfoVisitable {
    void accept(YouTubeInfoVisitor visitor);
}

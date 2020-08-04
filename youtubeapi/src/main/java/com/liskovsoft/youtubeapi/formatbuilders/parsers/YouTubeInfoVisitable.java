package com.liskovsoft.youtubeapi.formatbuilders.parsers;

public interface YouTubeInfoVisitable {
    void accept(YouTubeInfoVisitor visitor);
}

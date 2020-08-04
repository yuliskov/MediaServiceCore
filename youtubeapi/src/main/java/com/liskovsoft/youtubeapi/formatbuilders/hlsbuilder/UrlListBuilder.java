package com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder;

import com.liskovsoft.youtubeapi.formatbuilders.interfaces.MediaItem;

import java.util.List;

public interface UrlListBuilder {
    void append(MediaItem mediaItem);
    boolean isEmpty();
    List<String> buildUriList();
}

package com.liskovsoft.youtubeapi.videoinfo.hlsbuilder;

import com.liskovsoft.youtubeapi.videoinfo.interfaces.MediaItem;

import java.util.List;

public interface UrlListBuilder {
    void append(MediaItem mediaItem);
    boolean isEmpty();
    List<String> buildUriList();
}

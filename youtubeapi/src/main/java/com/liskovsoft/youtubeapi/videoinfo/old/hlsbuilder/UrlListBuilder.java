package com.liskovsoft.youtubeapi.videoinfo.old.hlsbuilder;

import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.MediaItem;

import java.util.List;

public interface UrlListBuilder {
    void append(MediaItem mediaItem);
    boolean isEmpty();
    List<String> buildUriList();
}

package com.liskovsoft.youtubeapi.videoinfo.mpdbuilder;

import com.liskovsoft.youtubeapi.videoinfo.interfaces.MediaItem;

import java.util.List;

public interface MPDParser {
    List<MediaItem> parse();
}

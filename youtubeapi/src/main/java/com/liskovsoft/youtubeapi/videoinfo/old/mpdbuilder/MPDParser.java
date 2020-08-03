package com.liskovsoft.youtubeapi.videoinfo.old.mpdbuilder;

import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.MediaItem;

import java.util.List;

public interface MPDParser {
    List<MediaItem> parse();
}

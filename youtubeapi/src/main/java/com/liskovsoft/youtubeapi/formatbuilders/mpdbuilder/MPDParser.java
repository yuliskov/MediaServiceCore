package com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder;

import com.liskovsoft.youtubeapi.formatbuilders.interfaces.MediaItem;

import java.util.List;

public interface MPDParser {
    List<MediaItem> parse();
}

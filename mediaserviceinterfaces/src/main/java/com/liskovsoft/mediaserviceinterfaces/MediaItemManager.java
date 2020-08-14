package com.liskovsoft.mediaserviceinterfaces;

import java.io.InputStream;
import java.util.List;

public interface MediaItemManager {
    MediaItemFormatDetails getFormatDetails(MediaItem item);
    MediaItemFormatDetails getFormatDetails(String videoId);
    InputStream getMpdStream(MediaItemFormatDetails formatDetails);
    List<String> getUrlList(MediaItemFormatDetails formatDetails);
    MediaItemSuggestions getSuggestions(MediaItem item);
    MediaItemSuggestions getSuggestions(String videoId);
}

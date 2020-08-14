package com.liskovsoft.mediaserviceinterfaces;

import java.io.InputStream;
import java.util.List;

public interface MediaItemManager {
    MediaItemDetails getMediaItemDetails(MediaItem item);
    MediaItemDetails getMediaItemDetails(String videoId);
    InputStream getMpdStream(MediaItemDetails mediaItemDetails);
    List<String> getUrlList(MediaItemDetails mediaItemDetails);
}

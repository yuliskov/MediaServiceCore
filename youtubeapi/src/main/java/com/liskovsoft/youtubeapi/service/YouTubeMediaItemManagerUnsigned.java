package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.youtubeapi.next.WatchNextServiceUnsigned;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;

public class YouTubeMediaItemManagerUnsigned extends YouTubeMediaItemManagerBase {
    private static MediaItemManager sInstance;
    private final WatchNextServiceUnsigned mWatchNextServiceUnsigned;

    private YouTubeMediaItemManagerUnsigned() {
       mWatchNextServiceUnsigned = WatchNextServiceUnsigned.instance();
    }

    public static MediaItemManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemManagerUnsigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        WatchNextServiceUnsigned.unhold();
    }

    @Override
    public MediaItemMetadata getMetadata(MediaItem item) {
        return getMetadata(item.getMediaId());
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId) {
        WatchNextResult watchNextResult = mWatchNextServiceUnsigned.getWatchNextResult(videoId);

        return YouTubeMediaItemMetadata.from(watchNextResult);
    }
}

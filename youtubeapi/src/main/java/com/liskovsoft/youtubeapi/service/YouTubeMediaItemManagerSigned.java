package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.youtubeapi.next.WatchNextServiceSigned;

public class YouTubeMediaItemManagerSigned extends YouTubeMediaItemManagerBase {
    private static MediaItemManager sInstance;
    private final WatchNextServiceSigned mWatchNextServiceSigned;

    private YouTubeMediaItemManagerSigned() {
        mWatchNextServiceSigned = WatchNextServiceSigned.instance();
    }

    public static MediaItemManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemManagerSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        WatchNextServiceSigned.unhold();
    }

    @Override
    public MediaItemMetadata getMetadata(MediaItem item) {
        // TODO: not implemented

        return null;
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId) {
        // TODO: not implemented

        return null;
    }
}

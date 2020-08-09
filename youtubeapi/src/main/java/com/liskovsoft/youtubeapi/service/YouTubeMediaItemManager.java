package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.FormatInfo;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoService;

public class YouTubeMediaItemManager implements MediaItemManager {
    private static MediaItemManager sInstance;
    private final VideoInfoService mVideoInfoService;

    private YouTubeMediaItemManager() {
        mVideoInfoService = VideoInfoService.instance();
    }

    public static MediaItemManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemManager();
        }

        return sInstance;
    }

    @Override
    public FormatInfo getFormatInfo(MediaItem item) {
        // TODO: not implemented

        //mVideoInfoService.getVideoInfo(item.getVideoUrl());

        return null;
    }
}

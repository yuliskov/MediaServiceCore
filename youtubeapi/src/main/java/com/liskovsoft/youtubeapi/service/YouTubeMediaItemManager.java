package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemDetails;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

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
    public MediaItemDetails getMediaItemDetails(MediaItem item) {
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(item.getVideoUrl());

        return YouTubeMediaItemDetails.from(videoInfo);
    }
}

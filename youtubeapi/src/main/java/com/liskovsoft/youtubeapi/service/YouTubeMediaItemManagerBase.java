package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

public abstract class YouTubeMediaItemManagerBase implements MediaItemManager {
    private final VideoInfoService mVideoInfoService;

    protected YouTubeMediaItemManagerBase() {
        mVideoInfoService = VideoInfoService.instance();
    }

    @Override
    public MediaItemFormatInfo getFormatInfo(MediaItem item) {
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(item.getMediaId());

        return YouTubeMediaItemFormatInfo.from(videoInfo);
    }

    @Override
    public MediaItemFormatInfo getFormatInfo(String videoId) {
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(videoId);

        return YouTubeMediaItemFormatInfo.from(videoInfo);
    }
}

package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemDetails;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder.SimpleUrlListBuilder;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.SimpleMPDBuilder;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

import java.io.InputStream;
import java.util.List;

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
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(item.getVideoId());

        return YouTubeMediaItemDetails.from(videoInfo);
    }

    @Override
    public MediaItemDetails getMediaItemDetails(String videoId) {
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(videoId);

        return YouTubeMediaItemDetails.from(videoInfo);
    }

    @Override
    public InputStream getMpdStream(MediaItemDetails mediaItemDetails) {
        return SimpleMPDBuilder.from(mediaItemDetails).build();
    }

    @Override
    public List<String> getUrlList(MediaItemDetails mediaItemDetails) {
        return SimpleUrlListBuilder.from(mediaItemDetails).buildUriList();
    }
}

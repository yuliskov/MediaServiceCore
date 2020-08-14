package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemFormatDetails;
import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.MediaItemSuggestions;
import com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder.SimpleUrlListBuilder;
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.SimpleMPDBuilder;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

import java.io.InputStream;
import java.util.List;

public class YouTubeMediaItemManager implements MediaItemManager {
    private static MediaItemManager sInstance;
    private final VideoInfoService mVideoInfoService;
    private final YouTubeSignInManager mSignInManager;

    private YouTubeMediaItemManager() {
        mVideoInfoService = VideoInfoService.instance();
        mSignInManager = YouTubeSignInManager.instance();
    }

    public static MediaItemManager instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemManager();
        }

        return sInstance;
    }

    @Override
    public MediaItemFormatDetails getFormatDetails(MediaItem item) {
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(item.getVideoId());

        return YouTubeMediaItemFormatDetails.from(videoInfo);
    }

    @Override
    public MediaItemFormatDetails getFormatDetails(String videoId) {
        VideoInfoResult videoInfo = mVideoInfoService.getVideoInfo(videoId);

        return YouTubeMediaItemFormatDetails.from(videoInfo);
    }

    @Override
    public InputStream getMpdStream(MediaItemFormatDetails formatDetails) {
        return SimpleMPDBuilder.from(formatDetails).build();
    }

    @Override
    public List<String> getUrlList(MediaItemFormatDetails formatDetails) {
        return SimpleUrlListBuilder.from(formatDetails).buildUriList();
    }

    @Override
    public MediaItemSuggestions getSuggestions(MediaItem item) {
        // TODO: not implemented

        return null;
    }

    @Override
    public MediaItemSuggestions getSuggestions(String videoId) {
        // TODO: not implemented

        return null;
    }
}

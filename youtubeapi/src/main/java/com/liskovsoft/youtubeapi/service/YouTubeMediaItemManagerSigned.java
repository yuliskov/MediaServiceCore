package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemManager;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.youtubeapi.next.WatchNextServiceSigned;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;
import com.liskovsoft.youtubeapi.track.TrackingService;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceUnsigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

public class YouTubeMediaItemManagerSigned implements MediaItemManager {
    private static MediaItemManager sInstance;
    private final WatchNextServiceSigned mWatchNextServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private final TrackingService mTrackingService;
    private final VideoInfoServiceSigned mVideoInfoServiceSigned;

    private YouTubeMediaItemManagerSigned() {
        mWatchNextServiceSigned = WatchNextServiceSigned.instance();
        mSignInManager = YouTubeSignInManager.instance();
        mTrackingService = TrackingService.instance();
        mVideoInfoServiceSigned = VideoInfoServiceSigned.instance();
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
    public YouTubeMediaItemMetadata getMetadata(MediaItem item) {
        YouTubeMediaItem ytMediaItem = (YouTubeMediaItem) item;

        YouTubeMediaItemMetadata metadata = ytMediaItem.getMetadata();

        if (metadata == null) {
            metadata = getMetadata(item.getMediaId());

            ytMediaItem.setMetadata(metadata);
        }

        return metadata;
    }

    @Override
    public YouTubeMediaItemMetadata getMetadata(String videoId) {
        WatchNextResult watchNextResult = mWatchNextServiceSigned.getWatchNextResult(videoId, mSignInManager.getAuthorization());

        return YouTubeMediaItemMetadata.from(watchNextResult);
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(MediaItem item) {
        YouTubeMediaItem ytMediaItem = (YouTubeMediaItem) item;

        YouTubeMediaItemFormatInfo formatInfo = ytMediaItem.getFormatInfo();

        if (formatInfo == null) {
            VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(item.getMediaId(), mSignInManager.getAuthorization());

            formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo);

            ytMediaItem.setFormatInfo(formatInfo);
        }

        return formatInfo;
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(String videoId) {
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, mSignInManager.getAuthorization());

        return YouTubeMediaItemFormatInfo.from(videoInfo);
    }

    @Override
    public void updateHistoryPosition(MediaItem item, float positionSec) {
        YouTubeMediaItemFormatInfo formatInfo = getFormatInfo(item);

        mTrackingService.updateWatchTime(
                formatInfo.getVideoId(), positionSec, Float.parseFloat(formatInfo.getLengthSeconds()), formatInfo.getEventId(),
                formatInfo.getVisitorMonitoringData(), mSignInManager.getAuthorization());
    }

    @Override
    public void updateHistoryPosition(String videoId, float positionSec) {
        YouTubeMediaItemFormatInfo formatInfo = getFormatInfo(videoId);

        mTrackingService.updateWatchTime(
                formatInfo.getVideoId(), positionSec, Float.parseFloat(formatInfo.getLengthSeconds()), formatInfo.getEventId(),
                formatInfo.getVisitorMonitoringData(), mSignInManager.getAuthorization());
    }
}

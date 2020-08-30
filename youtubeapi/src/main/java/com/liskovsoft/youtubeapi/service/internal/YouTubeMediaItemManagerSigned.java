package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.actions.ActionsService;
import com.liskovsoft.youtubeapi.next.WatchNextServiceSigned;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;
import com.liskovsoft.youtubeapi.track.TrackingService;
import com.liskovsoft.youtubeapi.videoinfo.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

public class YouTubeMediaItemManagerSigned implements MediaItemManagerInt {
    private static YouTubeMediaItemManagerSigned sInstance;
    private final WatchNextServiceSigned mWatchNextServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private final TrackingService mTrackingService;
    private final VideoInfoServiceSigned mVideoInfoServiceSigned;
    private final ActionsService mActionsService;

    private YouTubeMediaItemManagerSigned() {
        mWatchNextServiceSigned = WatchNextServiceSigned.instance();
        mSignInManager = YouTubeSignInManager.instance();
        mTrackingService = TrackingService.instance();
        mVideoInfoServiceSigned = VideoInfoServiceSigned.instance();
        mActionsService = ActionsService.instance();
    }

    public static YouTubeMediaItemManagerSigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemManagerSigned();
        }

        return sInstance;
    }

    public static void unhold() {
        sInstance = null;
        WatchNextServiceSigned.unhold();
    }

    //@Override
    //public YouTubeMediaItemMetadata getMetadata(MediaItem item) {
    //    YouTubeMediaItem ytMediaItem = (YouTubeMediaItem) item;
    //
    //    YouTubeMediaItemMetadata metadata = ytMediaItem.getMetadata();
    //
    //    if (metadata == null) {
    //        metadata = getMetadata(item.getMediaId());
    //
    //        ytMediaItem.setMetadata(metadata);
    //    }
    //
    //    return metadata;
    //}

    @Override
    public YouTubeMediaItemMetadata getMetadata(String videoId) {
        WatchNextResult watchNextResult = mWatchNextServiceSigned.getWatchNextResult(videoId, mSignInManager.getAuthorizationHeader());

        return YouTubeMediaItemMetadata.from(watchNextResult);
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(MediaItem item) {
        YouTubeMediaItem ytMediaItem = (YouTubeMediaItem) item;

        YouTubeMediaItemFormatInfo formatInfo = ytMediaItem.getFormatInfo();

        if (formatInfo == null) {
            VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(item.getMediaId(), mSignInManager.getAuthorizationHeader());

            formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo);

            ytMediaItem.setFormatInfo(formatInfo);
        }

        return formatInfo;
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(String videoId) {
        VideoInfoResult videoInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, mSignInManager.getAuthorizationHeader());

        return YouTubeMediaItemFormatInfo.from(videoInfo);
    }

    @Override
    public void updateHistoryPosition(MediaItem item, float positionSec) {
        YouTubeMediaItemFormatInfo formatInfo = getFormatInfo(item);

        mTrackingService.updateWatchTime(
                formatInfo.getVideoId(), positionSec, Float.parseFloat(formatInfo.getLengthSeconds()), formatInfo.getEventId(),
                formatInfo.getVisitorMonitoringData(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void updateHistoryPosition(String videoId, float positionSec) {
        YouTubeMediaItemFormatInfo formatInfo = getFormatInfo(videoId);

        mTrackingService.updateWatchTime(
                formatInfo.getVideoId(), positionSec, Float.parseFloat(formatInfo.getLengthSeconds()), formatInfo.getEventId(),
                formatInfo.getVisitorMonitoringData(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void setLike(MediaItem item) {
        mActionsService.setLike(item.getMediaId(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void removeLike(MediaItem item) {
        mActionsService.removeLike(item.getMediaId(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void setDislike(MediaItem item) {
        mActionsService.setDislike(item.getMediaId(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void removeDislike(MediaItem item) {
        mActionsService.removeDislike(item.getMediaId(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void subscribe(MediaItem item) {
        mActionsService.subscribe(item.getChannelId(), mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void unsubscribe(MediaItem item) {
        mActionsService.unsubscribe(item.getChannelId(), mSignInManager.getAuthorizationHeader());
    }
}

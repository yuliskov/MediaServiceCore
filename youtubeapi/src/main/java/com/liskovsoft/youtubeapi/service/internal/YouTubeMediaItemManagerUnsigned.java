package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.next.v1.WatchNextServiceUnsigned;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoServiceUnsigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

public class YouTubeMediaItemManagerUnsigned implements MediaItemManagerInt {
    private static YouTubeMediaItemManagerUnsigned sInstance;
    private final WatchNextServiceUnsigned mWatchNextServiceUnsigned;
    private final VideoInfoServiceUnsigned mVideoInfoServiceUnsigned;

    private YouTubeMediaItemManagerUnsigned() {
       mWatchNextServiceUnsigned = WatchNextServiceUnsigned.instance();
       mVideoInfoServiceUnsigned = VideoInfoServiceUnsigned.instance();
    }

    public static YouTubeMediaItemManagerUnsigned instance() {
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
    public WatchNextResult getWatchNextResult(String videoId) {
        return mWatchNextServiceUnsigned.getWatchNextResult(videoId);
    }

    @Override
    public WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return mWatchNextServiceUnsigned.getWatchNextResult(videoId, playlistId, playlistIndex);
    }

    @Override
    public WatchNextResultContinuation continueWatchNext(String nextKey) {
        return mWatchNextServiceUnsigned.continueWatchNext(nextKey);
    }

    @Override
    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        return mVideoInfoServiceUnsigned.getVideoInfo(videoId, clickTrackingParams);
    }

    @Override
    public void updateHistoryPosition(String videoId, String lengthSec,
                                      String eventId, String vmData, float positionSec) {
        // Do nothing, user is unsigned
    }

    @Override
    public void setLike(String videoId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void removeLike(String videoId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void setDislike(String videoId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void removeDislike(String videoId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void subscribe(String channelId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void unsubscribe(String channelId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void markAsNotInterested(String feedbackToken) {
        // Do nothing, user is unsigned
    }

    @Override
    public PlaylistsResult getVideoPlaylistsInfos(String videoId) {
        // Do nothing, user is unsigned
        return null;
    }

    @Override
    public void addToPlaylist(String playlistId, String videoId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void removeFromPlaylist(String playlistId, String videoId) {
        // Do nothing, user is unsigned
    }
}

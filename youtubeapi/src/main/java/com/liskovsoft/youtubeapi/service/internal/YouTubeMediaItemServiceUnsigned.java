package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.next.v1.WatchNextServiceUnsigned;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

public class YouTubeMediaItemServiceUnsigned implements MediaItemServiceInt {
    private static YouTubeMediaItemServiceUnsigned sInstance;
    private final WatchNextServiceUnsigned mWatchNextServiceUnsigned;
    private final VideoInfoService mVideoInfoService;

    private YouTubeMediaItemServiceUnsigned() {
       mWatchNextServiceUnsigned = WatchNextServiceUnsigned.instance();
       mVideoInfoService = VideoInfoService.instance();
    }

    public static YouTubeMediaItemServiceUnsigned instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemServiceUnsigned();
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
        return mVideoInfoService.getVideoInfo(videoId, clickTrackingParams, null);
    }

    @Override
    public void updateHistoryPosition(String videoId, String lengthSec,
                                      String eventId, String vmData, String ofParam, float positionSec) {
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
    public void subscribe(String channelId, String params) {
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
    public PlaylistsResult getVideoPlaylistsInfo(String videoId) {
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

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        // Do nothing, user is unsigned
    }

    @Override
    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        // Do nothing, user is unsigned
    }

    @Override
    public void savePlaylist(String playlistId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void removePlaylist(String playlistId) {
        // Do nothing, user is unsigned
    }

    @Override
    public void createPlaylist(String playlistName, String videoId) {
        // Do nothing, user is unsigned
    }
}

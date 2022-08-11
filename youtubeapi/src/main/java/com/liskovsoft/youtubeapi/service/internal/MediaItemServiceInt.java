package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.pageinfo.models.PageInfo;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

public interface MediaItemServiceInt {
    WatchNextResult getWatchNextResult(String videoId);
    WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex, String playlistParams);
    WatchNextResultContinuation continueWatchNext(String nextKey);
    VideoInfo getVideoInfo(String videoId, String clickTrackingParams);
    PageInfo getPageInfo(String videoId);
    void updateHistoryPosition(String videoId, String lengthSec,
                               String eventId, String vmData, float positionSec);
    void setLike(String videoId);
    void removeLike(String videoId);
    void setDislike(String videoId);
    void removeDislike(String videoId);
    void subscribe(String channelId);
    void unsubscribe(String channelId);
    PlaylistsResult getVideoPlaylistsInfo(String videoId);
    void addToPlaylist(String playlistId, String videoId);
    void removeFromPlaylist(String playlistId, String videoId);
    void renamePlaylist(String playlistId, String newName);
    void setPlaylistOrder(String playlistId, int playlistOrder);
    void savePlaylist(String playlistId);
    void removePlaylist(String playlistId);
    void createPlaylist(String playlistName, String videoId);
    void markAsNotInterested(String feedbackToken);
}

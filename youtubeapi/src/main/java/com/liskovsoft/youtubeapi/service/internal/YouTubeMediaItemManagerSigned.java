package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.actions.ActionsService;
import com.liskovsoft.youtubeapi.feedback.FeedbackService;
import com.liskovsoft.youtubeapi.next.v1.WatchNextServiceSigned;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResultContinuation;
import com.liskovsoft.youtubeapi.playlist.PlaylistService;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.service.YouTubeSignInManager;
import com.liskovsoft.youtubeapi.track.TrackingService;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoServiceSigned;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoServiceUnsigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;

public class YouTubeMediaItemManagerSigned implements MediaItemManagerInt {
    private static final String TAG = YouTubeMediaItemManagerSigned.class.getSimpleName();
    private static YouTubeMediaItemManagerSigned sInstance;
    private final WatchNextServiceSigned mWatchNextServiceSigned;
    private final YouTubeSignInManager mSignInManager;
    private final TrackingService mTrackingService;
    private final VideoInfoServiceSigned mVideoInfoServiceSigned;
    private final ActionsService mActionsService;
    private final PlaylistService mPlaylistService;
    private final FeedbackService mFeedbackService;

    private YouTubeMediaItemManagerSigned() {
        mWatchNextServiceSigned = WatchNextServiceSigned.instance();
        mSignInManager = YouTubeSignInManager.instance();
        mTrackingService = TrackingService.instance();
        mVideoInfoServiceSigned = VideoInfoServiceSigned.instance();
        mActionsService = ActionsService.instance();
        mPlaylistService = PlaylistService.instance();
        mFeedbackService = FeedbackService.instance();
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

    @Override
    public WatchNextResult getWatchNextResult(String videoId) {
        return mWatchNextServiceSigned.getWatchNextResult(videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public WatchNextResult getWatchNextResult(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return mWatchNextServiceSigned.getWatchNextResult(videoId, playlistId, playlistIndex, playlistParams, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public WatchNextResultContinuation continueWatchNext(String nextKey) {
        return mWatchNextServiceSigned.continueWatchNext(nextKey, mSignInManager.getAuthorizationHeader());
    }

    //@Override
    //public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
    //    // Enable history (temporally, until frequent buffering will be fixed)
    //    VideoInfo signedInfo = mVideoInfoServiceSigned.getVideoInfo(videoId, clickTrackingParams, mSignInManager.getAuthorizationHeader());
    //    VideoInfo unsignedInfo = VideoInfoServiceUnsigned.instance().getVideoInfo(videoId, clickTrackingParams); // Fix no playback bug (temporal fix)
    //    if (signedInfo != null && unsignedInfo != null) { // Merge signed info with unsigned (otherwise history won't work)
    //        unsignedInfo.setEventId(signedInfo.getEventId());
    //        unsignedInfo.setVisitorMonitoringData(signedInfo.getVisitorMonitoringData());
    //    }
    //
    //    return unsignedInfo;
    //}

    @Override
    public VideoInfo getVideoInfo(String videoId, String clickTrackingParams) {
        // Enable history (temporally, until no playback be fixed)
        VideoInfo result = mVideoInfoServiceSigned.getVideoInfo(videoId, clickTrackingParams, mSignInManager.getAuthorizationHeader());

        // Fix no playback bug (temporal fix)
        if (result == null || !result.isValid()) {
            result = VideoInfoServiceUnsigned.instance().getVideoInfo(videoId, clickTrackingParams);
        }

        return result;
    }

    @Override
    public void updateHistoryPosition(String videoId, String lengthSec, String eventId, String vmData, float positionSec) {
        if (lengthSec == null) {
            Log.e(TAG, "Can't update history: lengthSec is null");
            return;
        }

        mTrackingService.updateWatchTime(
                videoId, positionSec, Float.parseFloat(lengthSec), eventId,
                vmData, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void setLike(String videoId) {
        mActionsService.setLike(videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void removeLike(String videoId) {
        mActionsService.removeLike(videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void setDislike(String videoId) {
        mActionsService.setDislike(videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void removeDislike(String videoId) {
        mActionsService.removeDislike(videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void subscribe(String channelId) {
        mActionsService.subscribe(channelId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void unsubscribe(String channelId) {
        mActionsService.unsubscribe(channelId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void markAsNotInterested(String feedbackToken) {
        mFeedbackService.markAsNotInterested(feedbackToken, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public PlaylistsResult getVideoPlaylistsInfo(String videoId) {
        return mPlaylistService.getPlaylistsInfo(videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void addToPlaylist(String playlistId, String videoId) {
        mPlaylistService.addToPlaylist(playlistId, videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void removeFromPlaylist(String playlistId, String videoId) {
        mPlaylistService.removeFromPlaylist(playlistId, videoId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        mPlaylistService.renamePlaylist(playlistId, newName, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        mPlaylistService.setPlaylistOrder(playlistId, playlistOrder, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void savePlaylist(String playlistId) {
        mPlaylistService.savePlaylist(playlistId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void removePlaylist(String playlistId) {
        mPlaylistService.removePlaylist(playlistId, mSignInManager.getAuthorizationHeader());
    }

    @Override
    public void createPlaylist(String playlistName, String videoId) {
        mPlaylistService.createPlaylist(playlistName, videoId, mSignInManager.getAuthorizationHeader());
    }
}

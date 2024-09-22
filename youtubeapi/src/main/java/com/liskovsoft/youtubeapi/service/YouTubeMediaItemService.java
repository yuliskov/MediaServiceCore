package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.yt.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.yt.data.DeArrowData;
import com.liskovsoft.mediaserviceinterfaces.yt.data.DislikeData;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.yt.data.PlaylistInfo;
import com.liskovsoft.mediaserviceinterfaces.yt.data.SponsorSegment;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.actions.ActionsService;
import com.liskovsoft.youtubeapi.block.SponsorBlockService;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.dearrow.DeArrowService;
import com.liskovsoft.youtubeapi.feedback.FeedbackService;
import com.liskovsoft.youtubeapi.next.v2.WatchNextService;
import com.liskovsoft.youtubeapi.playlist.PlaylistService;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubePlaylistInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeSponsorSegment;
import com.liskovsoft.youtubeapi.track.TrackingService;
import com.liskovsoft.youtubeapi.videoinfo.V2.VideoInfoService;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

public class YouTubeMediaItemService implements MediaItemService {
    private static final String TAG = YouTubeMediaItemService.class.getSimpleName();
    private static YouTubeMediaItemService sInstance;
    private final YouTubeSignInService mSignInService;
    private final SponsorBlockService mSponsorBlockService;
    private final TrackingService mTrackingService;
    private final VideoInfoService mVideoInfoService;
    private final ActionsService mActionsService;
    private final PlaylistService mPlaylistService;
    private final FeedbackService mFeedbackService;
    private YouTubeMediaItemFormatInfo mCachedFormatInfo;

    private YouTubeMediaItemService() {
        mSignInService = YouTubeSignInService.instance();
        mSponsorBlockService = SponsorBlockService.instance();
        mTrackingService = TrackingService.instance();
        mVideoInfoService = VideoInfoService.instance();
        mActionsService = ActionsService.instance();
        mPlaylistService = PlaylistService.instance();
        mFeedbackService = FeedbackService.instance();
    }

    public static YouTubeMediaItemService instance() {
        if (sInstance == null) {
            sInstance = new YouTubeMediaItemService();
        }

        return sInstance;
    }

    /**
     * Format info is cached because it's supposed to run in multiple methods
     */
    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(MediaItem item) {
        return getFormatInfo(item.getVideoId(), item.getClickTrackingParams());
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(String videoId) {
        return getFormatInfo(videoId, null);
    }

    @Override
    public YouTubeMediaItemFormatInfo getFormatInfo(String videoId, String clickTrackingParams) {
        if (isCacheActual(videoId)) {
            return mCachedFormatInfo;
        }

        checkSigned();

        VideoInfo videoInfo = mVideoInfoService.getVideoInfo(videoId, clickTrackingParams);

        YouTubeMediaItemFormatInfo formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo);

        saveInCache(formatInfo);

        return formatInfo;
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item) {
        return RxHelper.fromNullable(() -> getFormatInfo(item));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId) {
        return RxHelper.fromNullable(() -> getFormatInfo(videoId));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId, String clickTrackingParams) {
        return RxHelper.fromNullable(() -> getFormatInfo(videoId, clickTrackingParams));
    }

    @Override
    public MediaItemStoryboard getStoryboard(MediaItem item) {
        return getStoryboard(item.getVideoId());
    }

    @Override
    public MediaItemStoryboard getStoryboard(String videoId) {
        YouTubeMediaItemFormatInfo format = getFormatInfo(videoId);
        return format != null ? format.createStoryboard() : null;
    }

    @Override
    public Observable<MediaItemStoryboard> getStoryboardObserve(MediaItem item) {
        return RxHelper.fromNullable(() -> getStoryboard(item));
    }

    @Override
    public Observable<MediaItemStoryboard> getStoryboardObserve(String videoId) {
        return RxHelper.fromNullable(() -> getStoryboard(videoId));
    }

    @Override
    public MediaItemMetadata getMetadata(MediaItem item) {
        return getMetadataV2(item.getVideoId(), item.getPlaylistId(), item.getPlaylistIndex(), item.getParams());
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return getMetadataV2(videoId, playlistId, playlistIndex, playlistParams);
    }

    private MediaItemMetadata getMetadataV2(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return WatchNextService.getMetadata(videoId, playlistId, playlistIndex, playlistParams);
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId) {
        return getMetadataIntV2(videoId);
    }

    private MediaItemMetadata getMetadataIntV2(String videoId) {
        return WatchNextService.getMetadata(videoId);
    }

    //@Override
    //public MediaGroup continueGroup(MediaGroup mediaGroup) {
    //    checkSigned();
    //
    //    String nextKey = YouTubeHelper.extractNextKey(mediaGroup);
    //
    //    if (mediaGroup instanceof YouTubeMediaGroup) {
    //        return YouTubeMediaGroup.from(
    //                mWatchNextServiceOld.continueWatchNext(nextKey),
    //                mediaGroup
    //        );
    //    } else if (mediaGroup instanceof MediaGroupImpl) {
    //        return mWatchNextService.continueGroup(mediaGroup);
    //    }
    //
    //    return null;
    //}

    //@Override
    //public MediaGroup continueGroup(MediaGroup mediaGroup) {
    //    checkSigned();
    //
    //    if (mediaGroup instanceof SuggestionsGroup) {
    //        return WatchNextService.continueGroup(mediaGroup);
    //    }
    //
    //    // Continue special embedded section group
    //    return YouTubeContentService.instance().continueGroup(mediaGroup);
    //}

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(MediaItem item) {
        return RxHelper.create(emitter -> {
            MediaItemMetadata metadata = getMetadata(item);

            if (metadata != null) {
                item.sync(metadata);
                emitter.onNext(metadata);
                emitter.onComplete();
            } else {
                RxHelper.onError(emitter, "getMetadataObserve result is null");
            }
        });
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId) {
        return RxHelper.fromNullable(() -> getMetadata(videoId));
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return RxHelper.fromNullable(() -> getMetadata(videoId, playlistId, playlistIndex, playlistParams));
    }

    //@Override
    //public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
    //    return RxHelper.fromNullable(() -> continueGroup(mediaGroup));
    //}

    @Override
    public void updateHistoryPosition(MediaItem item, float positionSec) {
        checkSigned();

        updateHistoryPosition(item.getVideoId(), positionSec);
    }

    @Override
    public void updateHistoryPosition(String videoId, float positionSec) {
        checkSigned();

        YouTubeMediaItemFormatInfo formatInfo = getFormatInfo(videoId);

        if (formatInfo == null) {
            Log.e(TAG, "Can't update history for video id %s. formatInfo == null", videoId);
            return;
        }

        mTrackingService.updateWatchTime(
                formatInfo.getVideoId(), positionSec, Helpers.parseFloat(formatInfo.getLengthSeconds()), formatInfo.getEventId(),
                formatInfo.getVisitorMonitoringData(), formatInfo.getOfParam());
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec) {
        return RxHelper.fromVoidable(() -> updateHistoryPosition(item, positionSec));
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec) {
        return RxHelper.fromVoidable(() -> updateHistoryPosition(videoId, positionSec));
    }

    @Override
    public Observable<Void> subscribeObserve(MediaItem item) {
        return RxHelper.fromVoidable(() -> subscribe(item));
    }

    @Override
    public Observable<Void> subscribeObserve(String channelId) {
        return RxHelper.fromVoidable(() -> subscribe(channelId));
    }

    @Override
    public Observable<Void> unsubscribeObserve(MediaItem item) {
        return RxHelper.fromVoidable(() -> unsubscribe(item));
    }

    @Override
    public Observable<Void> unsubscribeObserve(String channelId) {
        return RxHelper.fromVoidable(() -> unsubscribe(channelId));
    }

    @Override
    public Observable<Void> setLikeObserve(MediaItem item) {
        return RxHelper.fromVoidable(() -> setLike(item));
    }

    @Override
    public Observable<Void> removeLikeObserve(MediaItem item) {
        return RxHelper.fromVoidable(() -> removeLike(item));
    }

    @Override
    public Observable<Void> setDislikeObserve(MediaItem item) {
        return RxHelper.fromVoidable(() -> setDislike(item));
    }

    @Override
    public Observable<Void> removeDislikeObserve(MediaItem item) {
        return RxHelper.fromVoidable(() -> removeDislike(item));
    }

    @Override
    public void setLike(MediaItem item) {
        checkSigned();

        mActionsService.setLike(item.getVideoId());
    }

    @Override
    public void removeLike(MediaItem item) {
        checkSigned();

        mActionsService.removeLike(item.getVideoId());
    }

    @Override
    public void setDislike(MediaItem item) {
        checkSigned();

        mActionsService.setDislike(item.getVideoId());
    }

    @Override
    public void removeDislike(MediaItem item) {
        checkSigned();

        mActionsService.removeDislike(item.getVideoId());
    }

    @Override
    public void subscribe(MediaItem item) {
        subscribe(item.getChannelId(), item.getParams());
    }

    @Override
    public void subscribe(String channelId) {
        subscribe(channelId, null);
    }

    private void subscribe(String channelId, String params) {
        checkSigned();

        mActionsService.subscribe(channelId, params);
    }

    @Override
    public void unsubscribe(MediaItem item) {
        unsubscribe(item.getChannelId());
    }

    @Override
    public void unsubscribe(String channelId) {
        checkSigned();

        mActionsService.unsubscribe(channelId);
    }

    @Override
    public void markAsNotInterested(String feedbackToken) {
        checkSigned();

        mFeedbackService.markAsNotInterested(feedbackToken);
    }

    @Override
    public Observable<Void> markAsNotInterestedObserve(String feedbackToken) {
        return RxHelper.fromVoidable(() -> markAsNotInterested(feedbackToken));
    }

    @Override
    public List<PlaylistInfo> getPlaylistsInfo(String videoId) {
        checkSigned();

        PlaylistsResult playlistsInfo = mPlaylistService.getPlaylistsInfo(videoId);

        return YouTubePlaylistInfo.from(playlistsInfo);
    }

    @Override
    public void addToPlaylist(String playlistId, String videoId) {
        checkSigned();

        mPlaylistService.addToPlaylist(playlistId, videoId);
    }

    @Override
    public void removeFromPlaylist(String playlistId, String videoId) {
        checkSigned();

        mPlaylistService.removeFromPlaylist(playlistId, videoId);
    }

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        checkSigned();

        mPlaylistService.renamePlaylist(playlistId, newName);
    }

    @Override
    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        checkSigned();

        mPlaylistService.setPlaylistOrder(playlistId, playlistOrder);
    }

    @Override
    public void savePlaylist(String playlistId) {
        checkSigned();

        mPlaylistService.savePlaylist(playlistId);
    }

    @Override
    public void removePlaylist(String playlistId) {
        checkSigned();

        mPlaylistService.removePlaylist(playlistId);
    }

    @Override
    public void createPlaylist(String playlistName, String videoId) {
        checkSigned();

        mPlaylistService.createPlaylist(playlistName, videoId);
    }

    @Override
    public List<SponsorSegment> getSponsorSegments(String videoId) {
        SegmentList segmentList = mSponsorBlockService.getSegmentList(videoId);

        return YouTubeSponsorSegment.from(segmentList);
    }

    @Override
    public List<SponsorSegment> getSponsorSegments(String videoId, Set<String> categories) {
        SegmentList segmentList = mSponsorBlockService.getSegmentList(videoId, categories);

        return YouTubeSponsorSegment.from(segmentList);
    }

    @Override
    public Observable<List<PlaylistInfo>> getPlaylistsInfoObserve(String videoId) {
        return RxHelper.fromCallable(() -> getPlaylistsInfo(videoId));
    }

    @Override
    public Observable<Void> addToPlaylistObserve(String playlistId, String videoId) {
        return RxHelper.fromVoidable(() -> addToPlaylist(playlistId, videoId));
    }

    @Override
    public Observable<Void> removeFromPlaylistObserve(String playlistId, String videoId) {
        return RxHelper.fromVoidable(() -> removeFromPlaylist(playlistId, videoId));
    }

    @Override
    public Observable<Void> renamePlaylistObserve(String playlistId, String newName) {
        return RxHelper.fromVoidable(() -> renamePlaylist(playlistId, newName));
    }

    @Override
    public Observable<Void> setPlaylistOrderObserve(String playlistId, int playlistOrder) {
        return RxHelper.fromVoidable(() -> setPlaylistOrder(playlistId, playlistOrder));
    }

    @Override
    public Observable<Void> savePlaylistObserve(String playlistId) {
        return RxHelper.fromVoidable(() -> savePlaylist(playlistId));
    }

    @Override
    public Observable<Void> removePlaylistObserve(String playlistId) {
        return RxHelper.fromVoidable(() -> removePlaylist(playlistId));
    }

    @Override
    public Observable<Void> createPlaylistObserve(String playlistName, String videoId) {
        return RxHelper.fromVoidable(() -> createPlaylist(playlistName, videoId));
    }

    @Override
    public Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId) {
        return RxHelper.fromNullable(() -> getSponsorSegments(videoId));
    }

    @Override
    public Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId, Set<String> categories) {
        return RxHelper.fromNullable(() -> getSponsorSegments(videoId, categories));
    }

    @Override
    public Observable<DeArrowData> getDeArrowDataObserve(String videoId) {
        return RxHelper.fromNullable(() -> getDeArrowData(videoId));
    }

    @Override
    public Observable<DeArrowData> getDeArrowDataObserve(List<String> videoIds) {
        return RxHelper.create(emitter -> {
            for (String videoId : videoIds) {
                DeArrowData result = getDeArrowData(videoId);
                if (result != null) {
                    emitter.onNext(result);
                }
            }
            emitter.onComplete();
        });
    }

    private DeArrowData getDeArrowData(String videoId) {
        return DeArrowService.getData(videoId);
    }

    @Override
    public Observable<DislikeData> getDislikeDataObserve(String videoId) {
        return RxHelper.fromNullable(() -> WatchNextService.getDislikeData(videoId));
    }

    public void invalidateCache() {
        mCachedFormatInfo = null;
    }

    private boolean isCacheActual(String videoId) {
        return  mCachedFormatInfo != null &&
                mCachedFormatInfo.getVideoId() != null &&
                mCachedFormatInfo.getVideoId().equals(videoId) &&
                mCachedFormatInfo.isCacheActual();
    }

    private void saveInCache(YouTubeMediaItemFormatInfo formatInfo) {
        mCachedFormatInfo = formatInfo;
    }

    private void checkSigned() {
        mSignInService.checkAuth();
    }
}

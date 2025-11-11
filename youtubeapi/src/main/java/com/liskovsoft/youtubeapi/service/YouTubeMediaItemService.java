package com.liskovsoft.youtubeapi.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.mediaserviceinterfaces.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.data.DeArrowData;
import com.liskovsoft.mediaserviceinterfaces.data.DislikeData;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo;
import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.sharedutils.rx.RxHelper;
import com.liskovsoft.youtubeapi.actions.ActionsService;
import com.liskovsoft.youtubeapi.actions.ActionsServiceWrapper;
import com.liskovsoft.youtubeapi.block.SponsorBlockService;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.BaseMediaItem;
import com.liskovsoft.youtubeapi.dearrow.DeArrowService;
import com.liskovsoft.youtubeapi.feedback.FeedbackService;
import com.liskovsoft.youtubeapi.next.v2.WatchNextService;
import com.liskovsoft.youtubeapi.next.v2.WatchNextServiceWrapper;
import com.liskovsoft.youtubeapi.playlist.PlaylistService;
import com.liskovsoft.youtubeapi.playlist.PlaylistServiceWrapper;
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
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
    private YouTubeMediaItemFormatInfo mCachedFormatInfo;

    private YouTubeMediaItemService() {
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
        YouTubeMediaItemFormatInfo cachedFormatInfo = getCachedFormatInfo(videoId);

        if (cachedFormatInfo != null) {
            // Improve the performance by fetching the history data on the second run
            //syncWithAuthFormatIfNeeded(cachedFormatInfo);
            return cachedFormatInfo;
        }

        checkSigned();

        VideoInfo videoInfo = getVideoInfoService().getVideoInfo(videoId, clickTrackingParams);

        YouTubeMediaItemFormatInfo formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo);

        setCachedFormatInfo(formatInfo, clickTrackingParams);

        return formatInfo;
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item) {
        return RxHelper.fromCallable(() -> getFormatInfo(item));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId) {
        return RxHelper.fromCallable(() -> getFormatInfo(videoId));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId, String clickTrackingParams) {
        return RxHelper.fromCallable(() -> getFormatInfo(videoId, clickTrackingParams));
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
        return RxHelper.fromCallable(() -> getStoryboard(item));
    }

    @Override
    public Observable<MediaItemStoryboard> getStoryboardObserve(String videoId) {
        return RxHelper.fromCallable(() -> getStoryboard(videoId));
    }

    @Override
    public MediaItemMetadata getMetadata(MediaItem item) {
        return getMetadata(item.getVideoId(), item.getPlaylistId(), item.getPlaylistIndex(), item.getParams());
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return getWatchNextService().getMetadata(videoId, playlistId, playlistIndex, playlistParams);
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId) {
        return getWatchNextService().getMetadata(videoId);
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(MediaItem item) {
        return RxHelper.create(emitter -> {
            MediaItemMetadata metadata = getMetadata(item);

            if (metadata != null) {
                syncItem(item, metadata);
                emitter.onNext(metadata);
                emitter.onComplete();
            } else {
                RxHelper.onError(emitter, "getMetadataObserve result is null");
            }
        });
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId) {
        return RxHelper.fromCallable(() -> getMetadata(videoId));
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return RxHelper.fromCallable(() -> getMetadata(videoId, playlistId, playlistIndex, playlistParams));
    }

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

        // Improve the performance by fetching the history data on the second run
        syncWithAuthFormatIfNeeded(formatInfo);

        if (shouldBeSynced(formatInfo)) {
            throw new IllegalStateException("Update history error: the format should be synced first");
        }

        getTrackingService().updateWatchTime(
                formatInfo.getVideoId(), positionSec, Helpers.parseFloat(formatInfo.getLengthSeconds()), formatInfo.getEventId(),
                formatInfo.getVisitorMonitoringData(), formatInfo.getOfParam());
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec) {
        return RxHelper.fromRunnable(() -> updateHistoryPosition(item, positionSec));
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec) {
        return RxHelper.fromRunnable(() -> updateHistoryPosition(videoId, positionSec));
    }

    @Override
    public Observable<Void> subscribeObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> subscribe(item));
    }

    @Override
    public Observable<Void> subscribeObserve(String channelId) {
        return RxHelper.fromRunnable(() -> subscribe(channelId));
    }

    @Override
    public Observable<Void> unsubscribeObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> unsubscribe(item));
    }

    @Override
    public Observable<Void> unsubscribeObserve(String channelId) {
        return RxHelper.fromRunnable(() -> unsubscribe(channelId));
    }

    @Override
    public Observable<Void> setLikeObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> setLike(item));
    }

    @Override
    public Observable<Void> removeLikeObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> removeLike(item));
    }

    @Override
    public Observable<Void> setDislikeObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> setDislike(item));
    }

    @Override
    public Observable<Void> removeDislikeObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> removeDislike(item));
    }

    @Override
    public void setLike(MediaItem item) {
        checkSigned();

        getActionsService().setLike(item.getVideoId());
    }

    @Override
    public void removeLike(MediaItem item) {
        checkSigned();

        getActionsService().removeLike(item.getVideoId());
    }

    @Override
    public void setDislike(MediaItem item) {
        checkSigned();

        getActionsService().setDislike(item.getVideoId());
    }

    @Override
    public void removeDislike(MediaItem item) {
        checkSigned();

        getActionsService().removeDislike(item.getVideoId());
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

        getActionsService().subscribe(channelId, params);
    }

    @Override
    public void unsubscribe(MediaItem item) {
        unsubscribe(item.getChannelId());
    }

    @Override
    public void unsubscribe(String channelId) {
        checkSigned();

        getActionsService().unsubscribe(channelId);
    }

    @Override
    public void markAsNotInterested(String feedbackToken) {
        checkSigned();

        getFeedbackService().markAsNotInterested(feedbackToken);
    }

    @Override
    public Observable<Void> markAsNotInterestedObserve(String feedbackToken) {
        return RxHelper.fromRunnable(() -> markAsNotInterested(feedbackToken));
    }

    @Override
    public List<PlaylistInfo> getPlaylistsInfo(String videoId) {
        checkSigned();

        return getPlaylistService().getPlaylistsInfo(videoId);
    }

    private void addToPlaylist(String playlistId, String videoId) {
        checkSigned();

        getPlaylistService().addToPlaylist(playlistId, videoId);
    }

    private void addToPlaylist(String playlistId, MediaItem item) {
        checkSigned();

        PlaylistGroupServiceImpl.cachedItem = item;
        getPlaylistService().addToPlaylist(playlistId, item.getVideoId());
    }

    @Override
    public void removeFromPlaylist(String playlistId, String videoId) {
        checkSigned();

        getPlaylistService().removeFromPlaylist(playlistId, videoId);
    }

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        checkSigned();

        getPlaylistService().renamePlaylist(playlistId, newName);
    }

    @Override
    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        checkSigned();

        getPlaylistService().setPlaylistOrder(playlistId, playlistOrder);
    }

    private void savePlaylist(String playlistId) {
        checkSigned();

        getPlaylistService().savePlaylist(playlistId);
    }

    private void savePlaylist(MediaItem item) {
        checkSigned();

        PlaylistGroupServiceImpl.cachedItem = item;
        getPlaylistService().savePlaylist(item.getPlaylistId());
    }

    @Override
    public void removePlaylist(String playlistId) {
        checkSigned();

        getPlaylistService().removePlaylist(playlistId);
    }

    private void createPlaylist(String playlistName, String videoId) {
        checkSigned();

        getPlaylistService().createPlaylist(playlistName, videoId);
    }

    private void createPlaylist(String playlistName, @Nullable MediaItem item) {
        checkSigned();

        PlaylistGroupServiceImpl.cachedItem = item;
        getPlaylistService().createPlaylist(playlistName, item != null ? item.getVideoId() : null);
    }

    @Override
    public List<SponsorSegment> getSponsorSegments(String videoId) {
        SegmentList segmentList = getSponsorBlockService().getSegmentList(videoId);

        return YouTubeSponsorSegment.from(segmentList);
    }

    @Override
    public List<SponsorSegment> getSponsorSegments(String videoId, Set<String> categories) {
        SegmentList segmentList = getSponsorBlockService().getSegmentList(videoId, categories);

        return YouTubeSponsorSegment.from(segmentList);
    }

    @Override
    public Observable<List<PlaylistInfo>> getPlaylistsInfoObserve(String videoId) {
        return RxHelper.fromCallable(() -> getPlaylistsInfo(videoId));
    }

    @Override
    public Observable<Void> addToPlaylistObserve(String playlistId, String videoId) {
        return RxHelper.fromRunnable(() -> addToPlaylist(playlistId, videoId));
    }

    @Override
    public Observable<Void> addToPlaylistObserve(String playlistId, MediaItem item) {
        return RxHelper.fromRunnable(() -> addToPlaylist(playlistId, item));
    }

    @Override
    public Observable<Void> removeFromPlaylistObserve(String playlistId, String videoId) {
        return RxHelper.fromRunnable(() -> removeFromPlaylist(playlistId, videoId));
    }

    @Override
    public Observable<Void> renamePlaylistObserve(String playlistId, String newName) {
        return RxHelper.fromRunnable(() -> renamePlaylist(playlistId, newName));
    }

    @Override
    public Observable<Void> setPlaylistOrderObserve(String playlistId, int playlistOrder) {
        return RxHelper.fromRunnable(() -> setPlaylistOrder(playlistId, playlistOrder));
    }

    @Override
    public Observable<Void> savePlaylistObserve(String playlistId) {
        return RxHelper.fromRunnable(() -> savePlaylist(playlistId));
    }

    @Override
    public Observable<Void> savePlaylistObserve(MediaItem item) {
        return RxHelper.fromRunnable(() -> savePlaylist(item));
    }

    @Override
    public Observable<Void> removePlaylistObserve(String playlistId) {
        return RxHelper.fromRunnable(() -> removePlaylist(playlistId));
    }

    @Override
    public Observable<Void> createPlaylistObserve(String playlistName, String videoId) {
        return RxHelper.fromRunnable(() -> createPlaylist(playlistName, videoId));
    }

    @Override
    public Observable<Void> createPlaylistObserve(String playlistName, MediaItem item) {
        return RxHelper.fromRunnable(() -> createPlaylist(playlistName, item));
    }

    @Override
    public Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId) {
        return RxHelper.fromCallable(() -> getSponsorSegments(videoId));
    }

    @Override
    public Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId, Set<String> categories) {
        return RxHelper.fromCallable(() -> getSponsorSegments(videoId, categories));
    }

    @Override
    public Observable<DeArrowData> getDeArrowDataObserve(String videoId) {
        return RxHelper.fromCallable(() -> getDeArrowData(videoId));
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
        return RxHelper.fromCallable(() -> getWatchNextService().getDislikeData(videoId));
    }

    @Override
    public Observable<String> getUnlocalizedTitleObserve(String videoId) {
        return RxHelper.fromCallable(() -> getWatchNextService().getUnlocalizedTitle(videoId));
    }

    public void invalidateCache() {
        mCachedFormatInfo = null;
    }

    private YouTubeMediaItemFormatInfo getCachedFormatInfo(String videoId) {
        return  mCachedFormatInfo != null &&
                mCachedFormatInfo.getVideoId() != null &&
                mCachedFormatInfo.getVideoId().equals(videoId) &&
                mCachedFormatInfo.isCacheActual() ? mCachedFormatInfo : null;
    }

    private void setCachedFormatInfo(YouTubeMediaItemFormatInfo formatInfo, String clickTrackingParams) {
        mCachedFormatInfo = formatInfo;

        if (formatInfo != null) {
            formatInfo.setClickTrackingParams(clickTrackingParams);
        }
    }

    private void checkSigned() {
        getSignInService().checkAuth();
    }

    @NonNull
    private static YouTubeSignInService getSignInService() {
        return YouTubeSignInService.instance();
    }

    @NonNull
    private static SponsorBlockService getSponsorBlockService() {
        return SponsorBlockService.instance();
    }

    @NonNull
    private static TrackingService getTrackingService() {
        return TrackingService.instance();
    }

    @NonNull
    private static VideoInfoService getVideoInfoService() {
        return VideoInfoService.instance();
    }

    @NonNull
    private static ActionsService getActionsService() {
        return ActionsServiceWrapper.instance();
    }

    @NonNull
    private static PlaylistService getPlaylistService() {
        return PlaylistServiceWrapper.instance();
    }

    @NonNull
    private static FeedbackService getFeedbackService() {
        return FeedbackService.instance();
    }

    @NonNull
    private static WatchNextService getWatchNextService() {
        return WatchNextServiceWrapper.INSTANCE;
    }

    private static void syncWithAuthFormatIfNeeded(YouTubeMediaItemFormatInfo formatInfo) {
        if (formatInfo == null) {
            return;
        }

        if (shouldBeSynced(formatInfo) && !formatInfo.isSynced()) {
            VideoInfo videoInfo = getVideoInfoService().getAuthVideoInfo(formatInfo.getVideoId(), formatInfo.getClickTrackingParams());
            formatInfo.sync(YouTubeMediaItemFormatInfo.from(videoInfo));
        }
    }

    private static boolean shouldBeSynced(YouTubeMediaItemFormatInfo formatInfo) {
        return !formatInfo.isAuth() && !formatInfo.isUnplayable() && getSignInService().isSigned();
    }

    private static void syncItem(MediaItem item, MediaItemMetadata metadata) {
        if (item instanceof BaseMediaItem) {
            ((BaseMediaItem) item).sync(metadata);
        } else if (item instanceof YouTubeMediaItem) {
            ((YouTubeMediaItem) item).sync(metadata);
        }
    }
}

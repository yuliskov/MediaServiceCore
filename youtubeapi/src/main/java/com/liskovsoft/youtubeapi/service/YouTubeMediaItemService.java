package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItemService;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.mediaserviceinterfaces.data.VideoPlaylistInfo;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.app.AppService;
import com.liskovsoft.youtubeapi.block.SponsorBlockService;
import com.liskovsoft.youtubeapi.block.data.SegmentList;
import com.liskovsoft.youtubeapi.common.helpers.ObservableHelper;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.next.v2.WatchNextServiceV2;
import com.liskovsoft.youtubeapi.next.v2.impl.mediagroup.MediaGroupImpl;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;
import com.liskovsoft.youtubeapi.service.data.YouTubeSponsorSegment;
import com.liskovsoft.youtubeapi.service.data.YouTubeVideoPlaylistInfo;
import com.liskovsoft.youtubeapi.service.internal.MediaItemServiceInt;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaItemServiceSigned;
import com.liskovsoft.youtubeapi.service.internal.YouTubeMediaItemServiceUnsigned;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfo;
import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

public class YouTubeMediaItemService implements MediaItemService {
    private static final String TAG = YouTubeMediaItemService.class.getSimpleName();
    private static YouTubeMediaItemService sInstance;
    private final YouTubeSignInService mSignInManager;
    private final SponsorBlockService mSponsorBlockService;
    private final WatchNextServiceV2 mWatchNextServiceV2;
    private MediaItemServiceInt mMediaItemManagerReal;
    private YouTubeMediaItemFormatInfo mCachedFormatInfo;

    private YouTubeMediaItemService() {
        mSignInManager = YouTubeSignInService.instance();
        mSponsorBlockService = SponsorBlockService.instance();
        mWatchNextServiceV2 = WatchNextServiceV2.instance();
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

        VideoInfo videoInfo = mMediaItemManagerReal.getVideoInfo(videoId, clickTrackingParams);

        YouTubeMediaItemFormatInfo formatInfo = YouTubeMediaItemFormatInfo.from(videoInfo);

        saveInCache(formatInfo);

        return formatInfo;
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item) {
        return ObservableHelper.fromNullable(() -> getFormatInfo(item));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId) {
        return ObservableHelper.fromNullable(() -> getFormatInfo(videoId));
    }

    @Override
    public Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId, String clickTrackingParams) {
        return ObservableHelper.fromNullable(() -> getFormatInfo(videoId, clickTrackingParams));
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
        return ObservableHelper.fromNullable(() -> getStoryboard(item));
    }

    @Override
    public Observable<MediaItemStoryboard> getStoryboardObserve(String videoId) {
        return ObservableHelper.fromNullable(() -> getStoryboard(videoId));
    }

    @Override
    public MediaItemMetadata getMetadata(MediaItem item) {
        return getMetadataV2(item.getVideoId(), item.getPlaylistId(), item.getPlaylistIndex(), item.getPlaylistParams());
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return getMetadataV2(videoId, playlistId, playlistIndex, playlistParams);
    }

    private MediaItemMetadata getMetadataV1(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        checkSigned();

        WatchNextResult watchNextResult = mMediaItemManagerReal.getWatchNextResult(videoId, playlistId, playlistIndex, playlistParams);

        return YouTubeMediaItemMetadata.from(watchNextResult);
    }

    private MediaItemMetadata getMetadataV2(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return mWatchNextServiceV2.getMetadata(videoId, playlistId, playlistIndex, playlistParams);
    }

    @Override
    public MediaItemMetadata getMetadata(String videoId) {
        return getMetadataIntV2(videoId);
    }

    private YouTubeMediaItemMetadata getMetadataIntV1(String videoId) {
        checkSigned();

        WatchNextResult watchNextResult = mMediaItemManagerReal.getWatchNextResult(videoId);

        return YouTubeMediaItemMetadata.from(watchNextResult);
    }

    private MediaItemMetadata getMetadataIntV2(String videoId) {
        return mWatchNextServiceV2.getMetadata(videoId);
    }

    @Override
    public MediaGroup continueGroup(MediaGroup mediaGroup) {
        checkSigned();

        String nextKey = YouTubeMediaServiceHelper.extractNextKey(mediaGroup);

        if (mediaGroup instanceof YouTubeMediaGroup) {
            return YouTubeMediaGroup.from(
                    mMediaItemManagerReal.continueWatchNext(nextKey),
                    mediaGroup
            );
        } else if (mediaGroup instanceof MediaGroupImpl) {
            return mWatchNextServiceV2.continueGroup(mediaGroup);
        }

        return null;
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(MediaItem item) {
        return Observable.create(emitter -> {
            MediaItemMetadata metadata = getMetadata(item);

            if (metadata != null) {
                item.sync(metadata);
                emitter.onNext(metadata);
                emitter.onComplete();
            } else {
                ObservableHelper.onError(emitter, "getMetadataObserve result is null");
            }
        });
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId) {
        return ObservableHelper.fromNullable(() -> getMetadata(videoId));
    }

    @Override
    public Observable<MediaItemMetadata> getMetadataObserve(String videoId, String playlistId, int playlistIndex, String playlistParams) {
        return ObservableHelper.fromNullable(() -> getMetadata(videoId, playlistId, playlistIndex, playlistParams));
    }

    @Override
    public Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup) {
        return ObservableHelper.fromNullable(() -> continueGroup(mediaGroup));
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

        mMediaItemManagerReal.updateHistoryPosition(formatInfo.getVideoId(), formatInfo.getLengthSeconds(),
                formatInfo.getEventId(), formatInfo.getVisitorMonitoringData(), positionSec);
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec) {
        return ObservableHelper.fromVoidable(() -> updateHistoryPosition(item, positionSec));
    }

    @Override
    public Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec) {
        return ObservableHelper.fromVoidable(() -> updateHistoryPosition(videoId, positionSec));
    }

    @Override
    public Observable<Void> subscribeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> subscribe(item));
    }

    @Override
    public Observable<Void> subscribeObserve(String channelId) {
        return ObservableHelper.fromVoidable(() -> subscribe(channelId));
    }

    @Override
    public Observable<Void> unsubscribeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> unsubscribe(item));
    }

    @Override
    public Observable<Void> unsubscribeObserve(String channelId) {
        return ObservableHelper.fromVoidable(() -> unsubscribe(channelId));
    }

    @Override
    public Observable<Void> setLikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> setLike(item));
    }

    @Override
    public Observable<Void> removeLikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> removeLike(item));
    }

    @Override
    public Observable<Void> setDislikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> setDislike(item));
    }

    @Override
    public Observable<Void> removeDislikeObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> removeDislike(item));
    }

    @Override
    public void setLike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.setLike(item.getVideoId());
    }

    @Override
    public void removeLike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.removeLike(item.getVideoId());
    }

    @Override
    public void setDislike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.setDislike(item.getVideoId());
    }

    @Override
    public void removeDislike(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.removeDislike(item.getVideoId());
    }

    @Override
    public void subscribe(MediaItem item) {
        subscribe(item.getChannelId());
    }

    @Override
    public void subscribe(String channelId) {
        checkSigned();

        mMediaItemManagerReal.subscribe(channelId);
    }

    @Override
    public void unsubscribe(MediaItem item) {
        unsubscribe(item.getChannelId());
    }

    @Override
    public void unsubscribe(String channelId) {
        checkSigned();

        mMediaItemManagerReal.unsubscribe(channelId);
    }

    @Override
    public void markAsNotInterested(MediaItem item) {
        checkSigned();

        mMediaItemManagerReal.markAsNotInterested(item.getFeedbackToken());
    }

    @Override
    public Observable<Void> markAsNotInterestedObserve(MediaItem item) {
        return ObservableHelper.fromVoidable(() -> markAsNotInterested(item));
    }

    @Override
    public List<VideoPlaylistInfo> getVideoPlaylistsInfo(String videoId) {
        checkSigned();

        PlaylistsResult playlistsInfo = mMediaItemManagerReal.getVideoPlaylistsInfo(videoId);

        return YouTubeVideoPlaylistInfo.from(playlistsInfo);
    }

    @Override
    public void addToPlaylist(String playlistId, String videoId) {
        checkSigned();

        mMediaItemManagerReal.addToPlaylist(playlistId, videoId);
    }

    @Override
    public void removeFromPlaylist(String playlistId, String videoId) {
        checkSigned();

        mMediaItemManagerReal.removeFromPlaylist(playlistId, videoId);
    }

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        checkSigned();

        mMediaItemManagerReal.renamePlaylist(playlistId, newName);
    }

    @Override
    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        checkSigned();

        mMediaItemManagerReal.setPlaylistOrder(playlistId, playlistOrder);
    }

    @Override
    public void savePlaylist(String playlistId) {
        checkSigned();

        mMediaItemManagerReal.savePlaylist(playlistId);
    }

    @Override
    public void removePlaylist(String playlistId) {
        checkSigned();

        mMediaItemManagerReal.removePlaylist(playlistId);
    }

    @Override
    public void createPlaylist(String playlistName, String videoId) {
        checkSigned();

        mMediaItemManagerReal.createPlaylist(playlistName, videoId);
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
    public Observable<List<VideoPlaylistInfo>> getVideoPlaylistsInfoObserve(String videoId) {
        return Observable.fromCallable(() -> getVideoPlaylistsInfo(videoId));
    }

    @Override
    public Observable<Void> addToPlaylistObserve(String playlistId, String videoId) {
        return ObservableHelper.fromVoidable(() -> addToPlaylist(playlistId, videoId));
    }

    @Override
    public Observable<Void> removeFromPlaylistObserve(String playlistId, String videoId) {
        return ObservableHelper.fromVoidable(() -> removeFromPlaylist(playlistId, videoId));
    }

    @Override
    public Observable<Void> renamePlaylistObserve(String playlistId, String newName) {
        return ObservableHelper.fromVoidable(() -> renamePlaylist(playlistId, newName));
    }

    @Override
    public Observable<Void> setPlaylistOrderObserve(String playlistId, int playlistOrder) {
        return ObservableHelper.fromVoidable(() -> setPlaylistOrder(playlistId, playlistOrder));
    }

    @Override
    public Observable<Void> savePlaylistObserve(String playlistId) {
        return ObservableHelper.fromVoidable(() -> savePlaylist(playlistId));
    }

    @Override
    public Observable<Void> removePlaylistObserve(String playlistId) {
        return ObservableHelper.fromVoidable(() -> removePlaylist(playlistId));
    }

    @Override
    public Observable<Void> createPlaylistObserve(String playlistName, String videoId) {
        return ObservableHelper.fromVoidable(() -> createPlaylist(playlistName, videoId));
    }

    @Override
    public Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId) {
        return ObservableHelper.fromNullable(() -> getSponsorSegments(videoId));
    }

    @Override
    public Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId, Set<String> categories) {
        return ObservableHelper.fromNullable(() -> getSponsorSegments(videoId, categories));
    }

    public void invalidateCache() {
        mCachedFormatInfo = null;
    }

    private boolean isCacheActual(String videoId) {
        return  mCachedFormatInfo != null &&
                mCachedFormatInfo.getVideoId() != null &&
                mCachedFormatInfo.getVideoId().equals(videoId) &&
                (AppService.instance().isCacheActual() || mCachedFormatInfo.isLive()); // Cipher may be outdated. Also fix finished live streams.
    }

    private void saveInCache(YouTubeMediaItemFormatInfo formatInfo) {
        if (formatInfo == null || !formatInfo.containsMedia()) {
            return;
        }

        mCachedFormatInfo = formatInfo;
    }

    private void checkSigned() {
        if (mSignInManager.checkAuthHeader()) {
            Log.d(TAG, "User signed.");

            mMediaItemManagerReal = YouTubeMediaItemServiceSigned.instance();
            YouTubeMediaItemServiceUnsigned.unhold();
        } else {
            Log.d(TAG, "User doesn't signed.");

            mMediaItemManagerReal = YouTubeMediaItemServiceUnsigned.instance();
            YouTubeMediaItemServiceSigned.unhold();
        }
    }
}

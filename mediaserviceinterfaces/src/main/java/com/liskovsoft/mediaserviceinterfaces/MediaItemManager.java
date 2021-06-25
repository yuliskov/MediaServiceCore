package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.mediaserviceinterfaces.data.VideoPlaylistInfo;
import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

public interface MediaItemManager {
    // Base interfaces
    MediaItemFormatInfo getFormatInfo(MediaItem item);
    MediaItemFormatInfo getFormatInfo(String videoId);
    MediaItemFormatInfo getFormatInfo(String videoId, String clickTrackingParams);
    MediaItemStoryboard getStoryboard(MediaItem item);
    MediaItemStoryboard getStoryboard(String videoId);
    MediaItemMetadata getMetadata(MediaItem item);
    MediaItemMetadata getMetadata(String videoId);
    MediaItemMetadata getMetadata(String videoId, String playlistId, int playlistIndex);
    MediaGroup continueGroup(MediaGroup mediaGroup);
    void updateHistoryPosition(MediaItem item, float positionSec);
    void updateHistoryPosition(String videoId, float positionSec);
    void setLike(MediaItem item);
    void removeLike(MediaItem item);
    void setDislike(MediaItem item);
    void removeDislike(MediaItem item);
    void subscribe(MediaItem item);
    void subscribe(String channelId);
    void unsubscribe(MediaItem item);
    void unsubscribe(String channelId);
    void markAsNotInterested(MediaItem item);
    List<VideoPlaylistInfo> getVideoPlaylistsInfos(String videoId);
    void addToPlaylist(String playlistId, String videoId);
    void removeFromPlaylist(String playlistId, String videoId);
    List<SponsorSegment> getSponsorSegments(String videoId);
    List<SponsorSegment> getSponsorSegments(String videoId, Set<String> categories);

    // RxJava interfaces
    Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item);
    Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId);
    Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId, String clickTrackingParams);
    Observable<MediaItemStoryboard> getStoryboardObserve(MediaItem item);
    Observable<MediaItemStoryboard> getStoryboardObserve(String videoId);
    Observable<MediaItemMetadata> getMetadataObserve(MediaItem item);
    Observable<MediaItemMetadata> getMetadataObserve(String videoId);
    Observable<MediaItemMetadata> getMetadataObserve(String videoId, String playlistId, int playlistIndex);
    Observable<MediaGroup> continueGroupObserve(MediaGroup mediaTab);
    Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec);
    Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec);
    Observable<Void> subscribeObserve(MediaItem item);
    Observable<Void> subscribeObserve(String channelId);
    Observable<Void> unsubscribeObserve(MediaItem item);
    Observable<Void> unsubscribeObserve(String channelId);
    Observable<Void> markAsNotInterestedObserve(MediaItem item);
    Observable<Void> setLikeObserve(MediaItem item);
    Observable<Void> removeLikeObserve(MediaItem item);
    Observable<Void> setDislikeObserve(MediaItem item);
    Observable<Void> removeDislikeObserve(MediaItem item);
    Observable<List<VideoPlaylistInfo>> getVideoPlaylistsInfosObserve(String videoId);
    Observable<Void> addToPlaylistObserve(String playlistId, String videoId);
    Observable<Void> removeFromPlaylistObserve(String playlistId, String videoId);
    Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId);
    Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId, Set<String> categories);
}

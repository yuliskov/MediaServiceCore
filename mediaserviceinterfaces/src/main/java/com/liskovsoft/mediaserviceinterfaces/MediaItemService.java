package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.DeArrowData;
import com.liskovsoft.mediaserviceinterfaces.data.DislikeData;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard;
import com.liskovsoft.mediaserviceinterfaces.data.SponsorSegment;
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo;
import io.reactivex.Observable;

import java.util.List;
import java.util.Set;

public interface MediaItemService {
    int PLAYLIST_ORDER_ADDED_DATE_NEWER_FIRST = 1;
    int PLAYLIST_ORDER_ADDED_DATE_OLDER_FIRST = 2;
    int PLAYLIST_ORDER_POPULARITY = 3;
    int PLAYLIST_ORDER_PUBLISHED_DATE_NEWER_FIRST = 4;
    int PLAYLIST_ORDER_PUBLISHED_DATE_OLDER_FIRST = 5;

    // Blocking interfaces
    MediaItemFormatInfo getFormatInfo(MediaItem item);
    MediaItemFormatInfo getFormatInfo(String videoId);
    MediaItemFormatInfo getFormatInfo(String videoId, String clickTrackingParams);
    MediaItemStoryboard getStoryboard(MediaItem item);
    MediaItemStoryboard getStoryboard(String videoId);
    MediaItemMetadata getMetadata(MediaItem item);
    MediaItemMetadata getMetadata(String videoId);
    MediaItemMetadata getMetadata(String videoId, String playlistId, int playlistIndex, String playlistParams);
    //MediaGroup continueGroup(MediaGroup mediaGroup);
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
    void markAsNotInterested(String feedbackToken);
    List<PlaylistInfo> getPlaylistsInfo(String videoId);
    void removeFromPlaylist(String playlistId, String videoId);
    void renamePlaylist(String playlistId, String newName);
    void setPlaylistOrder(String playlistId, int playlistOrder);
    void removePlaylist(String playlistId);
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
    Observable<MediaItemMetadata> getMetadataObserve(String videoId, String playlistId, int playlistIndex, String playlistParams);
    //Observable<MediaGroup> continueGroupObserve(MediaGroup mediaGroup);
    Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec);
    Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec);
    Observable<Void> subscribeObserve(MediaItem item);
    Observable<Void> subscribeObserve(String channelId);
    Observable<Void> unsubscribeObserve(MediaItem item);
    Observable<Void> unsubscribeObserve(String channelId);
    Observable<Void> markAsNotInterestedObserve(String feedbackToken);
    Observable<Void> setLikeObserve(MediaItem item);
    Observable<Void> removeLikeObserve(MediaItem item);
    Observable<Void> setDislikeObserve(MediaItem item);
    Observable<Void> removeDislikeObserve(MediaItem item);
    Observable<List<PlaylistInfo>> getPlaylistsInfoObserve(String videoId);
    Observable<Void> addToPlaylistObserve(String playlistId, String videoId);
    Observable<Void> addToPlaylistObserve(String playlistId, MediaItem item);
    Observable<Void> removeFromPlaylistObserve(String playlistId, String videoId);
    Observable<Void> renamePlaylistObserve(String playlistId, String newName);
    Observable<Void> setPlaylistOrderObserve(String playlistId, int playlistOrder);
    Observable<Void> savePlaylistObserve(String playlistId);
    Observable<Void> savePlaylistObserve(MediaItem item);
    Observable<Void> removePlaylistObserve(String playlistId);
    Observable<Void> createPlaylistObserve(String playlistName, String videoId);
    Observable<Void> createPlaylistObserve(String playlistName, MediaItem item);
    Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId);
    Observable<List<SponsorSegment>> getSponsorSegmentsObserve(String videoId, Set<String> categories);
    Observable<DeArrowData> getDeArrowDataObserve(String videoId);
    Observable<DeArrowData> getDeArrowDataObserve(List<String> videoIds);
    Observable<DislikeData> getDislikeDataObserve(String videoId);
    Observable<String> getUnlocalizedTitleObserve(String videoId);
}

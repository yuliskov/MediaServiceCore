package com.liskovsoft.mediaserviceinterfaces;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import io.reactivex.Observable;

public interface MediaItemManager {
    MediaItemFormatInfo getFormatInfo(MediaItem item);
    MediaItemFormatInfo getFormatInfo(String videoId);
    MediaItemMetadata getMetadata(MediaItem item);
    MediaItemMetadata getMetadata(String videoId);
    void updateHistoryPosition(MediaItem item, float positionSec);
    void updateHistoryPosition(String videoId, float positionSec);
    void setLike(MediaItem item);
    void removeLike(MediaItem item);
    void setDislike(MediaItem item);
    void removeDislike(MediaItem item);
    void subscribe(MediaItem item);
    void unsubscribe(MediaItem item);

    // RxJava interfaces
    Observable<MediaItemFormatInfo> getFormatInfoObserve(MediaItem item);
    Observable<MediaItemFormatInfo> getFormatInfoObserve(String videoId);
    Observable<MediaItemMetadata> getMetadataObserve(MediaItem item);
    Observable<MediaItemMetadata> getMetadataObserve(String videoId);
    Observable<Void> updateHistoryPositionObserve(MediaItem item, float positionSec);
    Observable<Void> updateHistoryPositionObserve(String videoId, float positionSec);
    Observable<Void> subscribeObserve(MediaItem item);
    Observable<Void> unsubscribeObserve(MediaItem item);
    Observable<Void> setLikeObserve(MediaItem item);
    Observable<Void> removeLikeObserve(MediaItem item);
    Observable<Void> setDislikeObserve(MediaItem item);
    Observable<Void> removeDislikeObserve(MediaItem item);
}

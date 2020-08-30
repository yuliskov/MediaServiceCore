package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemFormatInfo;
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata;

public interface MediaItemManagerInt {
    YouTubeMediaItemMetadata getMetadata(String videoId);
    YouTubeMediaItemFormatInfo getFormatInfo(MediaItem item);
    YouTubeMediaItemFormatInfo getFormatInfo(String videoId);
    void updateHistoryPosition(MediaItem item, float positionSec);
    void updateHistoryPosition(String videoId, float positionSec);
    void setLike(MediaItem item);
    void removeLike(MediaItem item);
    void setDislike(MediaItem item);
    void removeDislike(MediaItem item);
    void subscribe(MediaItem item);
    void unsubscribe(MediaItem item);
}

package com.liskovsoft.youtubeapi.service.internal;

import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoInfoResult;

public interface MediaItemManagerInt {
    WatchNextResult getWatchNextResult(String videoId);
    VideoInfoResult getVideoInfo(String videoId);
    void updateHistoryPosition(String videoId, String lengthSec,
                               String eventId, String vmData, float positionSec);
    void setLike(String videoId);
    void removeLike(String videoId);
    void setDislike(String videoId);
    void removeDislike(String videoId);
    void subscribe(String channelId);
    void unsubscribe(String channelId);
}

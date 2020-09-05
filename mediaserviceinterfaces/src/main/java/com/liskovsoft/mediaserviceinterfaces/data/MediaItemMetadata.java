package com.liskovsoft.mediaserviceinterfaces.data;

import java.util.List;

public interface MediaItemMetadata {
    int LIKE_STATUS_INDIFFERENT = 0;
    int LIKE_STATUS_LIKE = 1;
    int LIKE_STATUS_DISLIKE = 2;
    String getTitle();
    String getAuthor();
    String getViewCount();
    String getLikesCount();
    String getDislikesCount();
    String getDescription();
    String getPublishedDate();
    String getMediaId();
    MediaItem getNextVideo();
    Boolean isSubscribed();
    String getChannelId();
    int getPercentWatched();
    int getLikeStatus();
    List<MediaGroup> getSuggestions();
}

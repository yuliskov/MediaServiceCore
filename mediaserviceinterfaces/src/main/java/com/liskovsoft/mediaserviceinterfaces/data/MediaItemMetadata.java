package com.liskovsoft.mediaserviceinterfaces.data;

import java.util.List;

public interface MediaItemMetadata {
    int LIKE_STATUS_INDIFFERENT = 0;
    int LIKE_STATUS_LIKE = 1;
    int LIKE_STATUS_DISLIKE = 2;
    String getTitle();
    String getDescription();
    String getFullDescription();
    String getAuthor();
    String getViewCount();
    String getLikesCount();
    String getDislikesCount();
    String getPublishedDate();
    String getVideoId();
    MediaItem getNextVideo();
    boolean isSubscribed();
    boolean isLive();
    boolean isUpcoming();
    String getChannelId();
    int getPercentWatched();
    int getLikeStatus();
    List<MediaGroup> getSuggestions();
    String getDescriptionAlt();
}

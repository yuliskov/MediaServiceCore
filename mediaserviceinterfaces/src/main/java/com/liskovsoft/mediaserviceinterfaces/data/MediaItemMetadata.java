package com.liskovsoft.mediaserviceinterfaces.data;

import java.util.List;

public interface MediaItemMetadata {
    int LIKE_STATUS_INDIFFERENT = 0;
    int LIKE_STATUS_LIKE = 1;
    int LIKE_STATUS_DISLIKE = 2;
    String getTitle();
    void setTitle(String title);
    String getAuthor();
    void setAuthor(String author);
    String getViewCount();
    void setViewCount(String viewCount);
    String getLikesCount();
    void setLikesCount(String likesCount);
    String getDislikesCount();
    void setDislikesCount(String dislikesCount);
    String getDescription();
    void setDescription(String description);
    String getPublishedDate();
    void setPublishedDate(String publishedDate);
    String getMediaId();
    void setMediaId(String mediaId);
    MediaItemMetadata getNextVideo();
    void setNextVideo(MediaItemMetadata autoplayMetadata);
    Boolean isSubscribed();
    void setSubscribed(Boolean subscribed);
    String getChannelId();
    void setChannelId(String channelId);
    Integer getPercentWatched();
    void setPercentWatched(Integer percentWatched);
    int getLikeStatus();
    void setLikeStatus(int likeStatus);
    List<MediaItemSuggestions> getSuggestions();
}

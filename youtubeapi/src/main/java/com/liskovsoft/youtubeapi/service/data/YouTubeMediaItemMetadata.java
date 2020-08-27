package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemSuggestions;
import com.liskovsoft.youtubeapi.next.models.VideoMetadata;
import com.liskovsoft.youtubeapi.next.models.VideoOwner;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.next.models.WatchNextSection;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaItemMetadata implements MediaItemMetadata {
    private String mTitle;
    private String mAuthor;
    private String mViewCount;
    private String mLikesCount;
    private String mDislikesCount;
    private String mDescription;
    private String mPublishedDate;
    private boolean mSubscribed;
    private int mLikeStatus;
    private String mMediaId;
    private String mChannelId;
    private int mPercentWatched;
    private MediaItemMetadata mNextVideo;
    private List<MediaGroup> mSuggestions;

    public static YouTubeMediaItemMetadata from(WatchNextResult watchNextResult) {
        YouTubeMediaItemMetadata mediaItemMetadata = new YouTubeMediaItemMetadata();

        VideoMetadata videoMetadata = watchNextResult.getVideoMetadata();
        VideoOwner videoOwner = watchNextResult.getVideoOwner();

        mediaItemMetadata.mTitle = videoMetadata.getTitle();
        mediaItemMetadata.mMediaId = videoMetadata.getVideoId();
        mediaItemMetadata.mAuthor = videoOwner.getVideoAuthor();
        mediaItemMetadata.mChannelId = videoOwner.getChannelId();
        mediaItemMetadata.mDescription = videoMetadata.getDescription();
        mediaItemMetadata.mDislikesCount = videoMetadata.getDislikesCount();
        mediaItemMetadata.mLikesCount = videoMetadata.getLikesCount();
        mediaItemMetadata.mPercentWatched = videoMetadata.getPercentWatched();
        mediaItemMetadata.mPublishedDate = videoMetadata.getPublishedDate();
        mediaItemMetadata.mSubscribed = videoOwner.isSubscribed();

        switch (videoMetadata.getLikeStatus()) {
            case VideoMetadata.LIKE_STATUS_LIKE:
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_LIKE;
                break;
            case VideoMetadata.LIKE_STATUS_DISLIKE:
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_DISLIKE;
                break;
        }

        List<WatchNextSection> watchNextSections = watchNextResult.getWatchNextSections();

        if (watchNextSections != null) {
            mediaItemMetadata.mSuggestions = new ArrayList<>();

            for (WatchNextSection section : watchNextSections) {
                mediaItemMetadata.mSuggestions.add(YouTubeMediaGroup.from(section));
            }
        }

        return mediaItemMetadata;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public void setAuthor(String author) {
        mAuthor = author;
    }

    @Override
    public String getViewCount() {
        return mViewCount;
    }

    @Override
    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

    @Override
    public String getLikesCount() {
        return mLikesCount;
    }

    @Override
    public void setLikesCount(String likesCount) {
        mLikesCount = likesCount;
    }

    @Override
    public String getDislikesCount() {
        return mDislikesCount;
    }

    @Override
    public void setDislikesCount(String dislikesCount) {
        mDislikesCount = dislikesCount;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getPublishedDate() {
        return mPublishedDate;
    }

    @Override
    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    @Override
    public String getMediaId() {
        return mMediaId;
    }

    @Override
    public void setMediaId(String mediaId) {
        mMediaId = mediaId;
    }

    @Override
    public MediaItemMetadata getNextVideo() {
        return mNextVideo;
    }

    @Override
    public void setNextVideo(MediaItemMetadata autoplayMetadata) {
        mNextVideo = autoplayMetadata;
    }

    @Override
    public Boolean isSubscribed() {
        return mSubscribed;
    }

    @Override
    public void setSubscribed(Boolean subscribed) {
        mSubscribed = subscribed;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    @Override
    public int getPercentWatched() {
        return mPercentWatched;
    }

    @Override
    public void setPercentWatched(int percentWatched) {
        mPercentWatched = percentWatched;
    }

    @Override
    public int getLikeStatus() {
        return mLikeStatus;
    }

    @Override
    public void setLikeStatus(int likeStatus) {
        mLikeStatus = likeStatus;
    }

    @Override
    public List<MediaGroup> getSuggestions() {
        return mSuggestions;
    }

    @Override
    public void setSuggestions(List<MediaGroup> suggestions) {
        mSuggestions = suggestions;
    }
}

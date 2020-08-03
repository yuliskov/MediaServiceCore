package com.liskovsoft.youtubeapi.videoinfo.old.interfaces;

import android.content.Intent;

public class VideoMetadata {
    private String mTitle;
    private String mAuthor;
    private String mViewCount;
    private String mLikesCount;
    private String mDislikesCount;
    private String mDescription;
    private String mPublishedDate;
    private Boolean mSubscribed;
    private Boolean mLiked;
    private Boolean mDisliked;
    private String mVideoId;
    private String mChannelId;
    private String mPlaylistId;
    private Integer mPercentWatched;

    private VideoMetadata mNextVideo;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public void setViewCount(String viewCount) {
        mViewCount = viewCount;
    }

    public String getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(String likesCount) {
        mLikesCount = likesCount;
    }

    public String getDislikesCount() {
        return mDislikesCount;
    }

    public void setDislikesCount(String dislikesCount) {
        mDislikesCount = dislikesCount;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        mPublishedDate = publishedDate;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    public VideoMetadata getNextVideo() {
        return mNextVideo;
    }

    public void setNextVideo(VideoMetadata autoplayMetadata) {
        mNextVideo = autoplayMetadata;
    }

    public Boolean isSubscribed() {
        return mSubscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        mSubscribed = subscribed;
    }

    public Boolean isLiked() {
        return mLiked;
    }

    public void setLiked(Boolean liked) {
        mLiked = liked;
    }

    public Boolean isDisliked() {
        return mDisliked;
    }

    public void setDisliked(Boolean disliked) {
        mDisliked = disliked;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public void setPlaylistId(String playlistId) {
        mPlaylistId = playlistId;
    }

    public Integer getPercentWatched() {
        return mPercentWatched;
    }

    public void setPercentWatched(Integer percentWatched) {
        mPercentWatched = percentWatched;
    }

    public Intent toIntent() {
        Intent intent = new Intent();
        intent.putExtra(VideoConstants.VIDEO_TITLE, mTitle);
        intent.putExtra(VideoConstants.VIDEO_AUTHOR, mAuthor);
        intent.putExtra(VideoConstants.VIDEO_VIEW_COUNT, mViewCount);
        intent.putExtra(VideoConstants.VIDEO_DATE, mPublishedDate);
        intent.putExtra(VideoConstants.BUTTON_LIKE, mLiked);
        intent.putExtra(VideoConstants.BUTTON_DISLIKE, mDisliked);
        intent.putExtra(VideoConstants.BUTTON_SUBSCRIBE, mSubscribed);
        intent.putExtra(VideoConstants.PERCENT_WATCHED, mPercentWatched);

        return intent;
    }

    public static VideoMetadata fromIntent(Intent intent) {
        if (intent == null) {
            return null;
        }

        VideoMetadata metadata = new VideoMetadata();

        metadata.mTitle = intent.getStringExtra(VideoConstants.VIDEO_TITLE);
        metadata.mViewCount = intent.getStringExtra(VideoConstants.VIDEO_VIEW_COUNT);
        metadata.mPublishedDate = intent.getStringExtra(VideoConstants.VIDEO_DATE);
        metadata.mLiked =
                intent.hasExtra(VideoConstants.BUTTON_LIKE) ?
                        intent.getBooleanExtra(VideoConstants.BUTTON_LIKE, false) :
                        null;
        metadata.mDisliked = intent.hasExtra(VideoConstants.BUTTON_DISLIKE) ?
                intent.getBooleanExtra(VideoConstants.BUTTON_DISLIKE, false) :
                null;
        metadata.mSubscribed = intent.hasExtra(VideoConstants.BUTTON_SUBSCRIBE) ?
                intent.getBooleanExtra(VideoConstants.BUTTON_SUBSCRIBE, false) :
                null;

        return metadata;
    }
}

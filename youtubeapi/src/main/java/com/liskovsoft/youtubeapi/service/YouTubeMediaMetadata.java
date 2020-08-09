package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.FormatMetadata;
import com.liskovsoft.youtubeapi.videoinfo.models.VideoDetails;

public class YouTubeMediaMetadata implements FormatMetadata {
    private String mLengthSeconds;
    private String mTitle;
    private String mAuthor;
    private String mViewCount;
    private String mTimestamp;
    private String mDescription;
    private String mVideoId;
    private String mChannelId;
    private boolean mIsLive;

    public static FormatMetadata from(VideoDetails videoDetails) {
        YouTubeMediaMetadata metadata = new YouTubeMediaMetadata();

        metadata.mLengthSeconds = videoDetails.getLengthSeconds();
        metadata.mVideoId = videoDetails.getVideoId();
        metadata.mViewCount = videoDetails.getViewCount();
        metadata.mTitle = videoDetails.getTitle();
        metadata.mDescription = videoDetails.getShortDescription();
        metadata.mChannelId = videoDetails.getChannelId();
        metadata.mAuthor = videoDetails.getAuthor();
        metadata.mIsLive = videoDetails.isLiveContent();

        return metadata;
    }

    @Override
    public String getLengthSeconds() {
        return mLengthSeconds;
    }

    @Override
    public void setLengthSeconds(String lengthSeconds) {
        mLengthSeconds = lengthSeconds;
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
    public String getTimestamp() {
        return mTimestamp;
    }

    @Override
    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
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
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public void setChannelId(String channelId) {
        mChannelId = channelId;
    }

    public boolean isLive() {
        return mIsLive;
    }
}

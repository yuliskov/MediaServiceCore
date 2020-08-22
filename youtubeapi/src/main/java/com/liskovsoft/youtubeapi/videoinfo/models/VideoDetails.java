package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class VideoDetails {
    @JsonPath("$.lengthSeconds")
    private String mLengthSeconds;
    @JsonPath("$.title")
    private String mTitle;
    @JsonPath("$.channelId")
    private String mChannelId;
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.shortDescription")
    private String mShortDescription;
    @JsonPath("$.viewCount")
    private String mViewCount;
    @JsonPath("$.author")
    private String mAuthor;
    @JsonPath("$.isLiveContent")
    private boolean mIsLiveContent;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;

    public String getLengthSeconds() {
        return mLengthSeconds;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getChannelId() {
        return mChannelId;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public boolean isLiveContent() {
        return mIsLiveContent;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }
}

package com.liskovsoft.youtubeapi.videoinfo.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

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
    @JsonPath("$.isLive")
    private boolean mIsLive;
    @JsonPath("$.isLiveContent")
    private boolean mIsLiveContent;
    @JsonPath("$.isOwnerViewing")
    private boolean mIsOwnerViewing;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.isLowLatencyLiveStream")
    private boolean mIsLowLatencyLiveStream;

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

    /**
     * Live content != live translation
     */
    public boolean isLiveContent() {
        return mIsLiveContent;
    }

    /**
     * Live translation
     */
    public boolean isLive() {
        return mIsLive;
    }

    /**
     * Personal videos
     */
    public boolean isOwnerViewing() {
        return mIsOwnerViewing;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public boolean isLowLatencyLiveStream() {
        return mIsLowLatencyLiveStream;
    }
}

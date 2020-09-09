package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class CurrentVideo {
    private static final String BADGE_LIVE = "LIVE";
    public static final String LIKE_STATUS_LIKE = "LIKE";
    public static final String LIKE_STATUS_DISLIKE = "DISLIKE";
    public static final String LIKE_STATUS_INDIFFERENT = "INDIFFERENT";
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.likeStatus")
    private String mLikeStatus;
    @JsonPath("$.title.runs[0].text")
    private String mTitle;
    @JsonPath("$.shortViewCountText.runs[0].text")
    private String mShortViewCount;
    @JsonPath("$.viewCountText.runs[0].text")
    private String mViewCount;
    @JsonPath("$.likesCount.runs[0].text")
    private String mLikesCount;
    @JsonPath("$.dislikesCount.runs[0].text")
    private String mDislikesCount;
    @JsonPath("$.description.runs[0].text")
    private String mDescription;
    @JsonPath("$.publishedTimeText.runs[0].text")
    private String mPublishedDateAlt;
    @JsonPath("$.dateText.runs[0].text")
    private String mPublishedDate;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched;
    @JsonPath("$.badges[0].liveBadge.label.runs[0].text")
    private String mLiveBadge;

    public String getVideoId() {
        return mVideoId;
    }

    public String getLikeStatus() {
        return mLikeStatus;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getShortViewCount() {
        return mShortViewCount;
    }

    public String getViewCount() {
        return mViewCount;
    }

    public String getLikesCount() {
        return mLikesCount;
    }

    public String getDislikesCount() {
        return mDislikesCount;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getPublishedDateAlt() {
        return mPublishedDateAlt;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public String getLiveBadge() {
        return mLiveBadge;
    }

    public boolean isLive() {
        return BADGE_LIVE.equals(mLiveBadge);
    }
}

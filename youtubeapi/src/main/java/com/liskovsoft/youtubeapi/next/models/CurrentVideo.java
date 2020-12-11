package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class CurrentVideo {
    public static final String LIKE_STATUS_LIKE = "LIKE";
    public static final String LIKE_STATUS_DISLIKE = "DISLIKE";
    public static final String LIKE_STATUS_INDIFFERENT = "INDIFFERENT";
    @JsonPath("$.owner.videoOwnerRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.likeStatus")
    private String mLikeStatus;
    @JsonPath({"title.simpleText", "$.title.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.viewCount.videoViewCountRenderer.shortViewCount.simpleText", "$.shortViewCountText.runs[0].text"})
    private String mShortViewCount;
    @JsonPath({"$.viewCount.videoViewCountRenderer.viewCount.runs[0].text", "$.viewCount.videoViewCountRenderer.viewCount.simpleText", "$.viewCountText.runs[0].text"})
    private String mViewCount;
    @JsonPath({"$.likesCount.simpleText", "$.likesCount.runs[0].text"})
    private String mLikesCount;
    @JsonPath({"$.dislikesCount.simpleText", "$.dislikesCount.runs[0].text"})
    private String mDislikesCount;
    @JsonPath({"$.description.simpleText", "$.description.runs[0].text"})
    private String mDescription;
    @JsonPath({"$.publishedTimeText.simpleText", "$.publishedTimeText.runs[0].text"})
    private String mPublishedTime;
    @JsonPath({"$.dateText.simpleText", "$.dateText.runs[0].text"})
    private String mPublishedDate;
    @JsonPath("$.thumbnailOverlays[0].thumbnailOverlayResumePlaybackRenderer.percentDurationWatched")
    private int mPercentWatched;
    @JsonPath({"$.viewCount.videoViewCountRenderer.isLive"})
    private boolean mIsLive;

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
        // On live streams short view counter is absent
        return mShortViewCount != null ? mShortViewCount : mViewCount;
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

    public String getPublishedTime() {
        return mPublishedTime;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public int getPercentWatched() {
        return mPercentWatched;
    }

    public boolean isLive() {
        return mIsLive;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }
}

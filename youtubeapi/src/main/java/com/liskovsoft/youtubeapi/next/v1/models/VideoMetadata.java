package com.liskovsoft.youtubeapi.next.v1.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

public class VideoMetadata {
    public static final String LIKE_STATUS_LIKE = "LIKE";
    public static final String LIKE_STATUS_DISLIKE = "DISLIKE";
    public static final String LIKE_STATUS_INDIFFERENT = "INDIFFERENT";
    @JsonPath("$.owner.videoOwnerRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.videoId")
    private String mVideoId;
    @JsonPath("$.likeStatus")
    private String mLikeStatus;
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mTitle;
    @JsonPath({"$.viewCount.videoViewCountRenderer.shortViewCount.simpleText",
            "$.shortViewCountText.runs[0].text"})
    private String mShortViewCount1;
    @JsonPath("$.shortViewCountText.runs[1].text")
    private String mShortViewCount2;
    @JsonPath({"$.viewCount.videoViewCountRenderer.viewCount.simpleText",
            "$.viewCountText.simpleText", // YouTube Music
            "$.viewCount.videoViewCountRenderer.viewCount.runs[0].text",
            "$.viewCountText.runs[0].text"})
    private String mViewCount1;
    @JsonPath({"$.viewCount.videoViewCountRenderer.viewCount.runs[1].text", "$.viewCountText.runs[1].text"})
    private String mViewCount2;
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
    @JsonPath("$.viewCount.videoViewCountRenderer.isLive")
    private boolean mIsLive;
    @JsonPath("$.badges[0].upcomingEventBadge.label.simpleText")
    private String mUpcomingBadge;
    /**
     * Appeared in YouTube Music
     */
    @JsonPath("$.byline.runs[0].text")
    private String mByLine;
    /**
     * Appeared in YouTube Music
     */
    @JsonPath("$.albumName.simpleText")
    private String mAlbumName;

    public String getVideoId() {
        return mVideoId;
    }

    public String getLikeStatus() {
        return mLikeStatus;
    }

    public String getTitle() {
        return mTitle;
    }

    public CharSequence getShortViewCount() {
        // On live streams short view counter is absent
        return mShortViewCount1 != null ? ServiceHelper.combineText(mShortViewCount1, mShortViewCount2) : getViewCount();
    }

    public CharSequence getViewCount() {
        return ServiceHelper.combineText(mViewCount1, mViewCount2);
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

    public boolean isUpcoming() {
        return mUpcomingBadge != null;
    }

    /**
     * Appeared in YouTube Music
     */
    public String getByLine() {
        return mByLine;
    }

    /**
     * Appeared in YouTube Music
     */
    public String getAlbumName() {
        return mAlbumName;
    }
}

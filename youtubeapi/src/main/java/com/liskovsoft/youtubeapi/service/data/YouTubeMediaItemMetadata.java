package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.youtubeapi.next.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.models.CurrentVideo;
import com.liskovsoft.youtubeapi.next.models.VideoOwner;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaItemMetadata implements MediaItemMetadata {
    private String mTitle;
    private String mAuthor;
    private String mViewCount;
    private String mLikesCount;
    private String mDislikesCount;
    private String mFullDescription;
    private String mPublishedDate;
    private boolean mSubscribed;
    private int mLikeStatus;
    private String mMediaId;
    private String mChannelId;
    private int mPercentWatched;
    private MediaItem mNextVideo;
    private List<MediaGroup> mSuggestions;
    private String mDescription;
    private boolean mIsLive;

    public static YouTubeMediaItemMetadata from(WatchNextResult watchNextResult) {
        if (watchNextResult == null) {
            return null;
        }

        YouTubeMediaItemMetadata mediaItemMetadata = new YouTubeMediaItemMetadata();

        CurrentVideo videoMetadata = watchNextResult.getVideoMetadata();
        VideoOwner videoOwner = watchNextResult.getVideoOwner();

        mediaItemMetadata.mTitle = videoMetadata.getTitle();
        mediaItemMetadata.mDescription = YouTubeMediaServiceHelper.createDescription(
                videoOwner.getVideoAuthor(),
                videoMetadata.getPublishedTimeAlt(),
                videoMetadata.getShortViewCount(),
                videoMetadata.getLiveBadge());
        mediaItemMetadata.mMediaId = videoMetadata.getVideoId();
        mediaItemMetadata.mFullDescription = videoMetadata.getDescription();
        mediaItemMetadata.mDislikesCount = videoMetadata.getDislikesCount();
        mediaItemMetadata.mLikesCount = videoMetadata.getLikesCount();
        mediaItemMetadata.mViewCount = videoMetadata.getViewCount();
        mediaItemMetadata.mPercentWatched = videoMetadata.getPercentWatched();
        mediaItemMetadata.mPublishedDate = videoMetadata.getPublishedTime();
        mediaItemMetadata.mIsLive = videoMetadata.isLive();
        mediaItemMetadata.mAuthor = videoOwner.getVideoAuthor();
        mediaItemMetadata.mChannelId = videoOwner.getChannelId();
        mediaItemMetadata.mSubscribed = videoOwner.isSubscribed();

        mediaItemMetadata.mNextVideo = YouTubeMediaItem.from(watchNextResult.getNextVideo());

        switch (videoMetadata.getLikeStatus()) {
            case CurrentVideo.LIKE_STATUS_LIKE:
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_LIKE;
                break;
            case CurrentVideo.LIKE_STATUS_DISLIKE:
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_DISLIKE;
                break;
        }

        List<SuggestedSection> suggestedSections = watchNextResult.getSuggestedSections();

        if (suggestedSections != null) {
            mediaItemMetadata.mSuggestions = new ArrayList<>();

            for (SuggestedSection section : suggestedSections) {
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
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public String getViewCount() {
        return mViewCount;
    }

    @Override
    public String getLikesCount() {
        return mLikesCount;
    }

    @Override
    public String getDislikesCount() {
        return mDislikesCount;
    }

    @Override
    public String getFullDescription() {
        return mFullDescription;
    }

    @Override
    public String getPublishedDate() {
        return mPublishedDate;
    }

    @Override
    public String getMediaId() {
        return mMediaId;
    }

    @Override
    public MediaItem getNextVideo() {
        return mNextVideo;
    }

    @Override
    public Boolean isSubscribed() {
        return mSubscribed;
    }

    @Override
    public boolean isLive() {
        return mIsLive;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public int getPercentWatched() {
        return mPercentWatched;
    }

    @Override
    public int getLikeStatus() {
        return mLikeStatus;
    }

    @Override
    public List<MediaGroup> getSuggestions() {
        return mSuggestions;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }
}

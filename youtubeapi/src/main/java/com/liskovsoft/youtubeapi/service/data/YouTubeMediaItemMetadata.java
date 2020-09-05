package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.youtubeapi.next.models.VideoMetadata;
import com.liskovsoft.youtubeapi.next.models.VideoOwner;
import com.liskovsoft.youtubeapi.next.models.WatchNextResult;
import com.liskovsoft.youtubeapi.next.models.SuggestedSection;

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
    private MediaItem mNextVideo;
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
        mediaItemMetadata.mViewCount = videoMetadata.getViewCount();
        mediaItemMetadata.mPercentWatched = videoMetadata.getPercentWatched();
        mediaItemMetadata.mPublishedDate = videoMetadata.getPublishedDate();
        mediaItemMetadata.mSubscribed = videoOwner.isSubscribed();

        mediaItemMetadata.mNextVideo = YouTubeMediaItem.from(watchNextResult.getNextVideo());

        switch (videoMetadata.getLikeStatus()) {
            case VideoMetadata.LIKE_STATUS_LIKE:
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_LIKE;
                break;
            case VideoMetadata.LIKE_STATUS_DISLIKE:
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_DISLIKE;
                break;
        }

        List<SuggestedSection> watchNextSections = watchNextResult.getSuggestedSections();

        if (watchNextSections != null) {
            mediaItemMetadata.mSuggestions = new ArrayList<>();

            for (SuggestedSection section : watchNextSections) {
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
    public String getDescription() {
        return mDescription;
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
}

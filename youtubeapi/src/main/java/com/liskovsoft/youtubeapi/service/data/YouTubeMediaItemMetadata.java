package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.sharedutils.mylogger.Log;
import com.liskovsoft.youtubeapi.browse.models.sections.Chip;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.v1.models.ButtonStates;
import com.liskovsoft.youtubeapi.next.v1.models.SuggestedSection;
import com.liskovsoft.youtubeapi.next.v1.models.VideoMetadata;
import com.liskovsoft.youtubeapi.next.v1.models.VideoOwner;
import com.liskovsoft.youtubeapi.next.v1.result.WatchNextResult;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

import java.util.ArrayList;
import java.util.List;

public class YouTubeMediaItemMetadata implements MediaItemMetadata {
    private static final String TAG = YouTubeMediaItemMetadata.class.getSimpleName();
    private String mTitle;
    private String mSecondTitle;
    private String mSecondTitleAlt;
    private String mDescription;
    private String mAuthor;
    private String mAuthorImageUrl;
    private String mViewCount;
    private String mLikesCount;
    private String mDislikesCount;
    private String mPublishedDate;
    private boolean mIsSubscribed;
    private int mLikeStatus;
    private String mVideoId;
    private String mChannelId;
    private int mPercentWatched;
    private MediaItem mNextVideo;
    private List<MediaGroup> mSuggestions;
    private boolean mIsLive;
    private boolean mIsUpcoming;

    public static YouTubeMediaItemMetadata from(WatchNextResult watchNextResult) {
        if (watchNextResult == null) {
            return null;
        }

        YouTubeMediaItemMetadata mediaItemMetadata = new YouTubeMediaItemMetadata();

        VideoMetadata videoMetadata = watchNextResult.getVideoMetadata();
        VideoOwner videoOwner = watchNextResult.getVideoOwner();
        VideoItem videoDetails = watchNextResult.getVideoDetails();

        if (videoMetadata == null && videoOwner == null && videoDetails == null) {
            Log.e(TAG, "Oops. Next format has been changed. Please upgrade parser.");
        }

        if (videoDetails != null) {
            mediaItemMetadata.mAuthor = videoDetails.getUserName();
            mediaItemMetadata.mChannelId = videoDetails.getChannelId();
            mediaItemMetadata.mTitle = videoDetails.getTitle();
            mediaItemMetadata.mVideoId = videoDetails.getVideoId();
            mediaItemMetadata.mPublishedDate = videoDetails.getPublishedDate();

            mediaItemMetadata.mSecondTitle = YouTubeMediaServiceHelper.createInfo(
                    mediaItemMetadata.mAuthor,
                    videoDetails.getPublishedDate(),
                    videoDetails.getViewCountText());
            mediaItemMetadata.mSecondTitleAlt = YouTubeMediaServiceHelper.createInfo(
                    mediaItemMetadata.mAuthor,
                    videoDetails.getPublishedDate(),
                    videoDetails.getShortViewCountText());
        }

        if (videoOwner != null) {
            mediaItemMetadata.mAuthor = videoOwner.getVideoAuthor();
            mediaItemMetadata.mAuthorImageUrl = YouTubeMediaServiceHelper.findLowResThumbnailUrl(videoOwner.getThumbnails());
            mediaItemMetadata.mChannelId = videoOwner.getChannelId();
            Boolean subscribed = videoOwner.isSubscribed();
            mediaItemMetadata.mIsSubscribed = subscribed != null && subscribed;
        }

        if (videoMetadata != null) {
            String author = mediaItemMetadata.mAuthor != null ? mediaItemMetadata.mAuthor : videoMetadata.getByLine();
            String publishedTime = videoMetadata.getPublishedTime() != null ? videoMetadata.getPublishedTime() : videoMetadata.getAlbumName();
            mediaItemMetadata.mTitle = videoMetadata.getTitle();
            mediaItemMetadata.mSecondTitle = YouTubeMediaServiceHelper.createInfo(
                    author, publishedTime,
                    videoMetadata.getShortViewCount());
            mediaItemMetadata.mSecondTitleAlt = YouTubeMediaServiceHelper.createInfo(
                    author,
                    videoMetadata.getPublishedDate(),
                    videoMetadata.getShortViewCount());
            mediaItemMetadata.mVideoId = videoMetadata.getVideoId();
            mediaItemMetadata.mDescription = videoMetadata.getDescription();
            mediaItemMetadata.mDislikesCount = videoMetadata.getDislikesCount();
            mediaItemMetadata.mLikesCount = videoMetadata.getLikesCount();
            mediaItemMetadata.mViewCount = videoMetadata.getViewCount();
            mediaItemMetadata.mPercentWatched = videoMetadata.getPercentWatched();
            mediaItemMetadata.mPublishedDate = videoMetadata.getPublishedDate();
            mediaItemMetadata.mIsLive = videoMetadata.isLive();
            mediaItemMetadata.mIsUpcoming = videoMetadata.isUpcoming();

            String likeStatus = videoMetadata.getLikeStatus();

            if (likeStatus != null) {
                switch (likeStatus) {
                    case VideoMetadata.LIKE_STATUS_LIKE:
                        mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_LIKE;
                        break;
                    case VideoMetadata.LIKE_STATUS_DISLIKE:
                        mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_DISLIKE;
                        break;
                }
            }
        }

        mediaItemMetadata.mNextVideo = YouTubeMediaItem.from(watchNextResult.getNextVideo());

        List<SuggestedSection> suggestedSections = watchNextResult.getSuggestedSections();

        if (suggestedSections != null) {
            mediaItemMetadata.mSuggestions = new ArrayList<>();

            for (SuggestedSection section : suggestedSections) {
                if (section.getChips() != null) {
                    // Contains multiple nested sections
                    for (Chip chip : section.getChips()) {
                        mediaItemMetadata.mSuggestions.add(YouTubeMediaGroup.from(chip));
                    }
                }

                mediaItemMetadata.mSuggestions.add(YouTubeMediaGroup.from(section));
            }
        }

        ButtonStates buttonStates = watchNextResult.getButtonStates();

        // Alt path to get like/subscribe status (when no such info in metadata section, e.g. YouTube Music items)
        if (buttonStates != null) {
            if (buttonStates.isSubscribeToggled() != null) {
                mediaItemMetadata.mIsSubscribed = buttonStates.isSubscribeToggled();
            }

            if (buttonStates.isLikeToggled() != null && buttonStates.isLikeToggled()) {
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_LIKE;
            }

            if (buttonStates.isDislikeToggled() != null && buttonStates.isDislikeToggled()) {
                mediaItemMetadata.mLikeStatus = MediaItemMetadata.LIKE_STATUS_DISLIKE;
            }

            if (buttonStates.getChannelId() != null) {
                mediaItemMetadata.mChannelId = buttonStates.getChannelId();
            }
        }

        return mediaItemMetadata;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSecondTitle() {
        return mSecondTitle;
    }

    @Override
    public String getSecondTitleAlt() {
        return mSecondTitleAlt;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public String getAuthorImageUrl() {
        return mAuthorImageUrl;
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
    public String getPublishedDate() {
        return mPublishedDate;
    }

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public MediaItem getNextVideo() {
        return mNextVideo;
    }

    @Override
    public boolean isSubscribed() {
        return mIsSubscribed;
    }

    @Override
    public boolean isLive() {
        return mIsLive;
    }

    @Override
    public String getLiveChatKey() {
        return null;
    }

    @Override
    public boolean isUpcoming() {
        return mIsUpcoming;
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

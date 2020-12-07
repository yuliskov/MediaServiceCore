package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.common.models.items.MusicItem;
import com.liskovsoft.youtubeapi.common.models.items.PlaylistItem;
import com.liskovsoft.youtubeapi.common.models.items.RadioItem;
import com.liskovsoft.youtubeapi.common.models.items.VideoItem;
import com.liskovsoft.youtubeapi.next.models.NextVideo;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

public class YouTubeMediaItem implements MediaItem {
    private static int sId;
    private String mTitle;
    private int mId;
    private String mVideoId;
    private String mChannelId;
    private String mPlaylistId;
    private String mMediaUrl;
    private String mChannelUrl;
    private String mDescription;
    private String mCardImageUrl;
    private String mBackgroundImageUrl;
    private String mContentType;
    private boolean mIsLive;
    private boolean mIsUpcoming;
    private int mDurationMs;
    private String mBadgeText;
    private String mProductionDate;
    private int mWidth;
    private int mHeight;
    private String mAudioChannelConfig;
    private String mPurchasePrice;
    private String mRentalPrice;
    private int mRatingStyle;
    private double mRatingScore;
    private int mMediaItemType;
    private YouTubeMediaItemFormatInfo mFormatInfo;
    private int mPercentWatched;
    private String mAuthor;
    private String mVideoPreviewUrl;
    private int mPlaylistIndex;
    private String mReloadPageKey;
    private boolean mHasNewContent;
    private String mFeedbackToken;

    public static YouTubeMediaItem from(ItemWrapper item, int position) {
        YouTubeMediaItem mediaItem = from(item);

        // In case can't find a position of item inside browse playlist query. So using position inside group instead.
        if (mediaItem != null && mediaItem.mPlaylistIndex == -1 && mediaItem.mPlaylistId != null) {
            mediaItem.mPlaylistIndex = position;
        }

        return mediaItem;
    }

    public static YouTubeMediaItem from(ItemWrapper item) {
        if (item.getVideoItem() != null) {
            return from(item.getVideoItem());
        } else if (item.getMusicItem() != null) {
            return from(item.getMusicItem());
        } else if (item.getChannelItem() != null) {
            return from(item.getChannelItem());
        } else if (item.getPlaylistItem() != null) {
            return from(item.getPlaylistItem());
        } else if (item.getRadioItem() != null) {
            return from(item.getRadioItem());
        }

        return null;
    }

    public static YouTubeMediaItem from(VideoItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(
                item.getUserName(),
                item.getPublishedTime(),
                item.getShortViewCountText() != null ? item.getShortViewCountText() : item.getViewCountText(),
                item.getUpcomingEventText());
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mProductionDate = item.getPublishedTime();
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mPlaylistIndex = item.getPlaylistIndex();
        video.mChannelId = item.getChannelId();
        video.mMediaUrl = AppHelper.videoIdToFullUrl(item.getVideoId());
        video.mChannelUrl = AppHelper.channelIdToFullUrl(item.getChannelId());
        // TODO: time conversion doesn't take into account locale specific delimiters
        video.mDurationMs = AppHelper.timeTextToMillis(item.getLengthText());
        video.mBadgeText = item.getBadgeText() != null ? item.getBadgeText() : item.getLengthText();
        video.mPercentWatched = item.getPercentWatched();
        video.mAuthor = item.getUserName();
        video.mVideoPreviewUrl = item.getRichThumbnailUrl();
        video.mIsLive = item.isLive();
        video.mIsUpcoming = item.isUpcoming();
        video.mFeedbackToken = item.getFeedbackToken();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(MusicItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_MUSIC;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(
                item.getUserName(),
                item.getViewsAndPublished());
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mProductionDate = item.getViewsAndPublished();
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mPlaylistIndex = item.getPlaylistIndex();
        video.mChannelId = item.getChannelId();
        video.mMediaUrl = AppHelper.videoIdToFullUrl(item.getVideoId());
        video.mChannelUrl = AppHelper.channelIdToFullUrl(item.getChannelId());
        // TODO: time conversion doesn't take into account locale specific delimiters
        video.mDurationMs = AppHelper.timeTextToMillis(item.getLengthText());
        video.mBadgeText = item.getLengthText();
        video.mPercentWatched = item.getPercentWatched();
        video.mAuthor = item.getUserName();
        video.mVideoPreviewUrl = item.getRichThumbnailUrl();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(ChannelItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_CHANNEL;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(item.getSubscriberCountText());
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mChannelId = item.getChannelId();
        video.mChannelUrl = AppHelper.channelIdToFullUrl(item.getChannelId());

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(RadioItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_PLAYLIST;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(item.getVideoCountText());
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mPlaylistId = item.getPlaylistId();
        video.mVideoId = item.getVideoId();

        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(PlaylistItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_PLAYLIST;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(item.getVideoCountText());
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mPlaylistId = item.getPlaylistId();
        video.mVideoId = item.getVideoId();

        addCommonProps(video);

        return video;
    }

    private static void addCommonProps(YouTubeMediaItem video) {
        video.mId = createId(video);
        video.mContentType = "video/mp4";
        video.mWidth = 1280;
        video.mHeight = 720;
        video.mAudioChannelConfig = "2.0";
        video.mPurchasePrice = "$5.99";
        video.mRentalPrice = "$4.99";
        video.mRatingStyle = 5;
        video.mRatingScore = 4d;
    }

    public static MediaItem from(NextVideo item) {
        if (item == null) {
            return null;
        }

        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mTitle = item.getTitle();
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mVideoId = item.getVideoId();
        video.mPlaylistId = item.getPlaylistId();
        video.mPlaylistIndex = item.getPlaylistItemIndex();
        video.mMediaUrl = AppHelper.videoIdToFullUrl(item.getVideoId());
        addCommonProps(video);

        return video;
    }

    public static YouTubeMediaItem from(GridTab tab, int type) {
        YouTubeMediaItem item = new YouTubeMediaItem();
        
        item.mMediaItemType = type;
        item.mTitle = tab.getTitle();
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(tab.getThumbnails());
        item.mCardImageUrl = highResThumbnailUrl;
        item.mBackgroundImageUrl = highResThumbnailUrl;
        item.mReloadPageKey = tab.getReloadPageKey();
        item.mHasNewContent = tab.hasNewContent();
        addCommonProps(item);

        return item;
    }

    private static int createId(YouTubeMediaItem item) {
        int id;

        if (item.mVideoId != null) {
            id = item.mVideoId.hashCode();
        } else if (item.mChannelId != null) {
            id = item.mChannelId.hashCode();
        } else {
            id = sId++;
        }

        return id;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public int getType() {
        return mMediaItemType;
    }

    @Override
    public String getDescription() {
        return mDescription;
    }

    @Override
    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    @Override
    public String getBackgroundImageUrl() {
        return mBackgroundImageUrl;
    }

    @Override
    public String getVideoUrl() {
        return mMediaUrl;
    }

    @Override
    public String getVideoId() {
        return mVideoId;
    }

    @Override
    public String getContentType() {
        return mContentType;
    }

    @Override
    public boolean isLive() {
        return mIsLive;
    }

    @Override
    public boolean isUpcoming() {
        return mIsUpcoming;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public String getAudioChannelConfig() {
        return mAudioChannelConfig;
    }

    @Override
    public String getPurchasePrice() {
        return mPurchasePrice;
    }

    @Override
    public String getRentalPrice() {
        return mRentalPrice;
    }

    @Override
    public int getRatingStyle() {
        return mRatingStyle;
    }

    @Override
    public double getRatingScore() {
        return mRatingScore;
    }

    @Override
    public String getProductionDate() {
        return mProductionDate;
    }

    @Override
    public int getDurationMs() {
        return mDurationMs;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public String getChannelUrl() {
        return mChannelUrl;
    }

    @Override
    public String getPlaylistId() {
        return mPlaylistId;
    }

    @Override
    public int getPercentWatched() {
        return mPercentWatched;
    }

    @Override
    public String getAuthor() {
        return mAuthor;
    }

    @Override
    public String getBadgeText() {
        return mBadgeText;
    }

    @Override
    public boolean hasNewContent() {
        return mHasNewContent;
    }

    @Override
    public String getVideoPreviewUrl() {
        return mVideoPreviewUrl;
    }

    @Override
    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    @Override
    public String getFeedbackToken() {
        return mFeedbackToken;
    }

    public void sync(MediaItemMetadata metadata) {
        if (metadata == null) {
            return;
        }

        mTitle = metadata.getTitle();
        mDescription = metadata.getDescription();
        mChannelId = metadata.getChannelId();
    }

    public YouTubeMediaItemFormatInfo getFormatInfo() {
        return mFormatInfo;
    }

    public void setFormatInfo(YouTubeMediaItemFormatInfo formatInfo) {
        mFormatInfo = formatInfo;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }

    public boolean isEmpty() {
        return mTitle == null && mCardImageUrl == null;
    }
}

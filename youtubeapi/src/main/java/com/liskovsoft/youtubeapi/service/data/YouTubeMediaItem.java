package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.common.models.items.ChannelItem;
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
    private String mMediaId;
    private String mChannelId;
    private String mPlaylistId;
    private String mMediaUrl;
    private String mDescription;
    private String mCardImageUrl;
    private String mBackgroundImageUrl;
    private String mContentType;
    private boolean mIsLive;
    private int mDuration;
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
    private YouTubeMediaItemMetadata mMetadata;
    private int mPercentWatched;

    public static YouTubeMediaItem from(VideoItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(
                item.getUserName(),
                item.getPublishedTime(),
                item.getViewCountText());
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mProductionDate = item.getPublishedTime();
        video.mMediaId = item.getVideoId();
        video.mChannelId = item.getChannelId();
        video.mMediaUrl = YouTubeHelper.videoIdToFullUrl(item.getVideoId());
        video.mDuration = YouTubeHelper.timeTextToMillis(item.getLengthText());
        video.mPercentWatched = item.getPercentWatched();

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
        video.mMediaId = item.getVideoId();
        video.mChannelId = item.getChannelId();
        video.mMediaUrl = YouTubeHelper.videoIdToFullUrl(item.getVideoId());
        video.mDuration = YouTubeHelper.timeTextToMillis(item.getLengthText());
        video.mPercentWatched = item.getPercentWatched();

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
        video.mMediaId = item.getVideoId();

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
        video.mMediaId = item.getVideoId();

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
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mTitle = item.getTitle();
        String highResThumbnailUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item.getThumbnails());
        video.mCardImageUrl = highResThumbnailUrl;
        video.mBackgroundImageUrl = highResThumbnailUrl;
        video.mMediaId = item.getVideoId();
        video.mMediaUrl = YouTubeHelper.videoIdToFullUrl(item.getVideoId());
        addCommonProps(video);

        return video;
    }

    private static int createId(YouTubeMediaItem item) {
        int id;

        if (item.mMediaId != null) {
            id = item.mMediaId.hashCode();
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
    public String getMediaUrl() {
        return mMediaUrl;
    }

    @Override
    public String getMediaId() {
        return mMediaId;
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
        return mDuration;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public String getPlaylistId() {
        return mPlaylistId;
    }

    @Override
    public int getPercentWatched() {
        return mPercentWatched;
    }

    public YouTubeMediaItemFormatInfo getFormatInfo() {
        return mFormatInfo;
    }

    public void setFormatInfo(YouTubeMediaItemFormatInfo formatInfo) {
        mFormatInfo = formatInfo;
    }

    public YouTubeMediaItemMetadata getMetadata() {
        return mMetadata;
    }

    public void setMetadata(YouTubeMediaItemMetadata metadata) {
        mMetadata = metadata;
    }
}

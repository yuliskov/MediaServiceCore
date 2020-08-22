package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

public class YouTubeMediaItem implements MediaItem {
    private static int id;
    private String mTitle;
    private int mId;
    private String mMediaId;
    private String mChannelId;
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

    public static YouTubeMediaItem from(com.liskovsoft.youtubeapi.common.models.items.VideoItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mId = id++;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(item);
        video.mCardImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item);
        video.mBackgroundImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item);
        video.mProductionDate = item.getPublishedTime();
        video.mMediaId = item.getVideoId();
        video.mChannelId = item.getChannelId();
        video.mMediaUrl = YouTubeHelper.videoIdToFullUrl(item.getVideoId());
        video.mDuration = YouTubeHelper.timeTextToMillis(item.getLengthText());
        video.mContentType = "video/mp4";
        video.mWidth = 1280;
        video.mHeight = 720;
        video.mAudioChannelConfig = "2.0";
        video.mPurchasePrice = "$5.99";
        video.mRentalPrice = "$4.99";
        video.mRatingStyle = 5;
        video.mRatingScore = 4d;

        return video;
    }

    public static YouTubeMediaItem from(com.liskovsoft.youtubeapi.common.models.items.MusicItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_MUSIC;
        video.mId = id++;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(item);
        video.mCardImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item);
        video.mBackgroundImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item);
        video.mProductionDate = item.getViewsAndPublished();
        video.mMediaId = item.getVideoId();
        video.mChannelId = item.getChannelId();
        video.mMediaUrl = YouTubeHelper.videoIdToFullUrl(item.getVideoId());
        video.mDuration = YouTubeHelper.timeTextToMillis(item.getLengthText());
        video.mContentType = "video/mp4";
        video.mWidth = 1280;
        video.mHeight = 720;
        video.mAudioChannelConfig = "2.0";
        video.mPurchasePrice = "$5.99";
        video.mRentalPrice = "$4.99";
        video.mRatingStyle = 5;
        video.mRatingScore = 4d;

        return video;
    }

    public static YouTubeMediaItem from(com.liskovsoft.youtubeapi.common.models.items.ChannelItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();

        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.mId = id++;
        video.mTitle = item.getTitle();
        video.mDescription = YouTubeMediaServiceHelper.createDescription(item);
        video.mCardImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item);
        video.mBackgroundImageUrl = YouTubeMediaServiceHelper.findHighResThumbnailUrl(item);
        video.mChannelId = item.getChannelId();
        video.mContentType = "video/mp4";
        video.mWidth = 1280;
        video.mHeight = 720;
        video.mAudioChannelConfig = "2.0";
        video.mPurchasePrice = "$5.99";
        video.mRentalPrice = "$4.99";
        video.mRatingStyle = 5;
        video.mRatingScore = 4d;

        return video;
    }

    @Override
    public int getId() {
        return mId;
    }

    @Override
    public void setId(int id) {
        mId = id;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public void setTitle(String title) {
        mTitle = title;
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
    public void setDescription(String description) {
        mDescription = description;
    }

    @Override
    public String getCardImageUrl() {
        return mCardImageUrl;
    }

    @Override
    public void setCardImageUrl(String cardImageUrl) {
        mCardImageUrl = cardImageUrl;
    }

    @Override
    public String getBackgroundImageUrl() {
        return mBackgroundImageUrl;
    }

    @Override
    public void setBackgroundImageUrl(String backgroundImageUrl) {
        mBackgroundImageUrl = backgroundImageUrl;
    }

    @Override
    public String getMediaUrl() {
        return mMediaUrl;
    }

    @Override
    public void setMediaUrl(String mediaUrl) {
        mMediaUrl = mediaUrl;
    }

    @Override
    public String getMediaId() {
        return mMediaId;
    }

    @Override
    public void setMediaId(String mediaId) {
        mMediaId = mediaId;
    }

    @Override
    public String getContentType() {
        return mContentType;
    }

    @Override
    public void setContentType(String contentType) {
        mContentType = contentType;
    }

    @Override
    public boolean isLive() {
        return mIsLive;
    }

    @Override
    public void setLive(boolean isLive) {
        mIsLive = isLive;
    }

    @Override
    public int getWidth() {
        return mWidth;
    }

    @Override
    public void setWidth(int width) {
        mWidth = width;
    }

    @Override
    public int getHeight() {
        return mHeight;
    }

    @Override
    public void setHeight(int height) {
        mHeight = height;
    }

    @Override
    public String getAudioChannelConfig() {
        return mAudioChannelConfig;
    }

    @Override
    public void setAudioChannelConfig(String audioChannelConfig) {
        mAudioChannelConfig = audioChannelConfig;
    }

    @Override
    public String getPurchasePrice() {
        return mPurchasePrice;
    }

    @Override
    public void setPurchasePrice(String purchasePrice) {
        mPurchasePrice = purchasePrice;
    }

    @Override
    public String getRentalPrice() {
        return mRentalPrice;
    }

    @Override
    public void setRentalPrice(String rentalPrice) {
        mRentalPrice = rentalPrice;
    }

    @Override
    public int getRatingStyle() {
        return mRatingStyle;
    }

    @Override
    public void setRatingStyle(int ratingStyle) {
        mRatingStyle = ratingStyle;
    }

    @Override
    public double getRatingScore() {
        return mRatingScore;
    }

    @Override
    public void setRatingScore(double ratingScore) {
        mRatingScore = ratingScore;
    }

    @Override
    public String getProductionDate() {
        return mProductionDate;
    }

    @Override
    public void setProductionDate(String productionDate) {
        mProductionDate = productionDate;
    }

    @Override
    public int getDuration() {
        return mDuration;
    }

    @Override
    public void setDuration(int duration) {
        mDuration = duration;
    }

    @Override
    public String getChannelId() {
        return mChannelId;
    }

    @Override
    public void setChannelId(String channelId) {
        mChannelId = channelId;
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

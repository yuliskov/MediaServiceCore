package com.liskovsoft.youtubeapi.service;

import com.liskovsoft.mediaserviceinterfaces.MediaItem;
import com.liskovsoft.youtubeapi.common.VideoServiceHelper;
import com.liskovsoft.youtubeapi.support.utils.YouTubeHelper;

public class YouTubeMediaItem implements MediaItem {
    private static int id;
    private String mTitle;
    private int mId;
    private String mDescription;
    private String mCardImageUrl;
    private String mBackgroundImageUrl;
    private String mVideoUrl;
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

    public static MediaItem from(com.liskovsoft.youtubeapi.common.models.videos.VideoItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();
        video.mMediaItemType = MediaItem.TYPE_VIDEO;
        video.setId(id++);
        video.setTitle(item.getTitle());
        video.setDescription(VideoServiceHelper.obtainDescription(item));
        video.setCardImageUrl(VideoServiceHelper.obtainHighResThumbnailUrl(item));
        video.setBackgroundImageUrl(VideoServiceHelper.obtainHighResThumbnailUrl(item));
        video.setProductionDate(item.getPublishedTime());
        video.setVideoUrl(YouTubeHelper.videoIdToFullUrl(item.getVideoId()));
        video.setDuration(YouTubeHelper.timeTextToMillis(item.getLengthText()));
        video.setContentType("video/mp4");
        video.setWidth(1280);
        video.setHeight(720);
        video.setAudioChannelConfig("2.0");
        video.setPurchasePrice("$5.99");
        video.setRentalPrice("$4.99");
        video.setRatingStyle(5);
        video.setRatingScore(4d);
        return video;
    }

    public static MediaItem from(com.liskovsoft.youtubeapi.common.models.videos.MusicItem item) {
        YouTubeMediaItem video = new YouTubeMediaItem();
        video.mMediaItemType = MediaItem.TYPE_MUSIC;
        video.setId(id++);
        video.setTitle(item.getTitle());
        video.setCardImageUrl(VideoServiceHelper.obtainHighResThumbnailUrl(item));
        video.setBackgroundImageUrl(VideoServiceHelper.obtainHighResThumbnailUrl(item));
        video.setProductionDate(item.getViewsAndPublished());
        video.setVideoUrl(YouTubeHelper.videoIdToFullUrl(item.getVideoId()));
        video.setDuration(YouTubeHelper.timeTextToMillis(item.getLength()));
        video.setContentType("video/mp4");
        video.setWidth(1280);
        video.setHeight(720);
        video.setAudioChannelConfig("2.0");
        video.setPurchasePrice("$5.99");
        video.setRentalPrice("$4.99");
        video.setRatingStyle(5);
        video.setRatingScore(4d);
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
    public String getVideoUrl() {
        return mVideoUrl;
    }

    @Override
    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
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
}

package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.YouTubeHelper;

import java.util.List;

public class YouTubeVideo implements Video {
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

    public static Video from(VideoItem item) {
        YouTubeVideo video = new YouTubeVideo();
        video.setId(id++);
        video.setTitle(item.getTitle());
        video.setDescription(obtainDescription(item));
        video.setCardImageUrl(obtainHighResThumbnailUrl(item));
        video.setBackgroundImageUrl(obtainHighResThumbnailUrl(item));
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

    private static String obtainHighResThumbnailUrl(VideoItem item) {
        List<Thumbnail> thumbnails = item.getThumbnails();

        if (thumbnails.size() == 0) {
            return null;
        }

        return thumbnails.get(thumbnails.size() - 1).getUrl();
    }

    private static String obtainDescription(VideoItem item) {
        return YouTubeHelper.itemsToDescription(
                item.getUserName(),
                item.getPublishedTime(),
                item.getShortViewCount()
        );
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

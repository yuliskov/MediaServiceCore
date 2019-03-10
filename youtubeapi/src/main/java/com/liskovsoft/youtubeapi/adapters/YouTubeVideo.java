package com.liskovsoft.youtubeapi.adapters;

import com.liskovsoft.myvideotubeapi.Video;
import com.liskovsoft.youtubeapi.common.models.videos.VideoItem;
import com.liskovsoft.youtubeapi.support.utils.YouTubeHelper;

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

    public static Video from(VideoItem item) {
        YouTubeVideo video = new YouTubeVideo();
        video.setId(id++);
        video.setTitle(item.getTitle());
        video.setCardImageUrl(item.getThumbnails().get(0).getUrl());
        video.setBackgroundImageUrl(item.getThumbnails().get(0).getUrl());
        video.setProductionDate(item.getPublishedTime());
        video.setVideoUrl(YouTubeHelper.toFullUrl(item.getVideoId()));
        video.setDuration(YouTubeHelper.toMillis(item.getLengthText()));
        video.setContentType("video/*");
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
        return 0;
    }

    @Override
    public void setWidth(int width) {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setHeight(int height) {

    }

    @Override
    public String getAudioChannelConfig() {
        return null;
    }

    @Override
    public void setAudioChannelConfig(String audioChannelConfig) {

    }

    @Override
    public String getPurchasePrice() {
        return null;
    }

    @Override
    public void setPurchasePrice(String purchasePrice) {

    }

    @Override
    public String getRentalPrice() {
        return null;
    }

    @Override
    public void setRentalPrice(String rentalPrice) {

    }

    @Override
    public int getRatingStyle() {
        return 0;
    }

    @Override
    public void setRatingStyle(int ratingStyle) {

    }

    @Override
    public double getRatingScore() {
        return 0;
    }

    @Override
    public void setRatingScore(double ratingScore) {

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

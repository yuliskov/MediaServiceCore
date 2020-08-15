package com.liskovsoft.mediaserviceinterfaces.data;

import android.media.Rating;

public interface MediaItem {
    int TYPE_VIDEO = 0;
    int TYPE_MUSIC = 1;
    int TYPE_CHANNEL = 2;
    int getType();

    // Video specific props
    String getDescription();
    void setDescription(String description);
    boolean isLive();
    void setLive(boolean isLive);

    // Generic props
    int getId();
    void setId(int id);
    String getTitle();
    void setTitle(String title);
    String getMediaUrl();
    void setMediaUrl(String mediaUrl);
    String getMediaId();
    void setMediaId(String mediaId);
    String getContentType();
    /**
     * Set mime content type.<br/>
     * Example: <b>"video/mp4"</b>
     * @param contentType content type
     */
    void setContentType(String contentType);
    int getDuration();
    /**
     * Set video duration in millis.<br/>
     * @param duration duration millis
     */
    void setDuration(int duration);
    String getProductionDate();
    void setProductionDate(String productionDate);
    String getCardImageUrl();
    void setCardImageUrl(String cardImageUrl);
    String getBackgroundImageUrl();
    void setBackgroundImageUrl(String backgroundImageUrl);
    int getWidth();
    void setWidth(int width);
    int getHeight();
    void setHeight(int height);
    String getAudioChannelConfig();
    /**
     * Set num of audio channels.<br/>
     * Example: <b>"2.0"</b>
     * @param audioChannelConfig num of channels
     */
    void setAudioChannelConfig(String audioChannelConfig);
    String getPurchasePrice();
    /**
     * Price in local currency.<br/>
     * Example: <b>"$19.99"</b>
     * @param purchasePrice price
     */
    void setPurchasePrice(String purchasePrice);
    String getRentalPrice();
    void setRentalPrice(String rentalPrice);
    int getRatingStyle();
    /**
     * Set video rating max points.<br/>
     * Example: {@link Rating#RATING_5_STARS}
     * @param ratingStyle rating style
     */
    void setRatingStyle(int ratingStyle);
    double getRatingScore();
    /**
     * Set video rating.<br/>
     * Example: <b>4.05f</b>
     * @param ratingScore score
     */
    void setRatingScore(double ratingScore);
}

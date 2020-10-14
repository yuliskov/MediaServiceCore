package com.liskovsoft.mediaserviceinterfaces.data;

import android.media.Rating;

public interface MediaItem {
    int TYPE_VIDEO = 0;
    int TYPE_MUSIC = 1;
    int TYPE_CHANNEL = 2;
    int TYPE_PLAYLIST = 3;
    int getType();

    // Music/Video props
    boolean isLive();
    int getPercentWatched();
    String getAuthor();

    // Playlist props
    String getPlaylistId();

    // Generic props
    int getId();
    String getTitle();
    /**
     * Additional video info such as user, published etc.
     */
    String getDescription();
    String getMediaUrl();
    String getMediaId();
    /**
     * Mime content type.<br/>
     * Example: <b>"video/mp4"</b>
     */
    String getContentType();
    /**
     * Video duration in millis.<br/>
     */
    int getDurationMs();
    String getBadgeText();
    String getProductionDate();
    String getCardImageUrl();
    String getBackgroundImageUrl();
    int getWidth();
    int getHeight();
    /**
     * Num of audio channels.<br/>
     * Example: <b>"2.0"</b>
     */
    String getAudioChannelConfig();
    /**
     * Price in local currency.<br/>
     * Example: <b>"$19.99"</b>
     */
    String getPurchasePrice();
    String getRentalPrice();
    /**
     * Video rating max points.<br/>
     * Example: {@link Rating#RATING_5_STARS}
     */
    int getRatingStyle();
    /**
     * Video rating score.<br/>
     * Example: <b>4.05f</b>
     */
    double getRatingScore();
    String getChannelId();
    String getVideoPreviewUrl();
}

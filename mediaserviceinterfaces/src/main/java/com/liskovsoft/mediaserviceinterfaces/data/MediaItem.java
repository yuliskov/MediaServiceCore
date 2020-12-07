package com.liskovsoft.mediaserviceinterfaces.data;

import android.media.Rating;

public interface MediaItem {
    int TYPE_VIDEO = 0;
    int TYPE_MUSIC = 1;
    int TYPE_CHANNEL = 2;
    int TYPE_PLAYLIST = 3;

    // Special type of items derived from groups.
    // Such items should have same ids.
    int TYPE_PLAYLISTS_SECTION = 8;
    int TYPE_CHANNELS_SECTION = 12;
    int getType();

    // Music/Video props
    boolean isLive();
    boolean isUpcoming();
    int getPercentWatched();
    String getAuthor();
    String getFeedbackToken();

    // Playlist props
    String getPlaylistId();

    // Channel props
    boolean hasNewContent();

    // Generic props
    int getId();
    String getTitle();
    /**
     * Additional video info such as user, published etc.
     */
    String getDescription();
    String getVideoUrl();
    String getVideoId();
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
    String getChannelId();
    String getChannelUrl();
    String getVideoPreviewUrl();
    int getPlaylistIndex();
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
}

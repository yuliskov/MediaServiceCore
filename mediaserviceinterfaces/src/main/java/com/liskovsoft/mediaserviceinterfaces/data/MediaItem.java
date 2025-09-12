package com.liskovsoft.mediaserviceinterfaces.data;

import android.media.Rating;

public interface MediaItem {
    int TYPE_UNDEFINED = -1;
    int TYPE_VIDEO = 0;
    int TYPE_MUSIC = 1;
    int TYPE_CHANNEL = 2;
    int TYPE_PLAYLIST = 3;

    int getType();

    // Music/Video props
    boolean isLive();
    boolean isUpcoming();
    boolean isShorts();
    int getPercentWatched();
    int getStartTimeSeconds();
    String getAuthor();
    String getFeedbackToken();
    String getFeedbackToken2();

    // Playlist props
    String getPlaylistId();
    int getPlaylistIndex();
    
    String getParams(); // Replacement for playlist id in channel section (use with caution: such list can't be updated)
    String getReloadPageKey(); // Replacement for playlist id in channel section

    // Channel props
    boolean hasNewContent();

    // Generic props
    int getId();
    String getTitle();
    /**
     * Additional video info such as user, published etc.
     */
    CharSequence getSecondTitle();
    String getVideoId();
    /**
     * Mime content type.<br/>
     * Example: <b>"video/mp4"</b>
     */
    String getContentType();
    /**
     * Video duration in millis.<br/>
     */
    long getDurationMs();
    String getBadgeText();
    String getProductionDate();
    long getPublishedDate();
    String getCardImageUrl();
    String getBackgroundImageUrl();
    int getWidth();
    int getHeight();
    String getChannelId();
    String getVideoPreviewUrl();
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
    boolean isMovie();
    boolean hasUploads();
    String getClickTrackingParams();
    String getSearchQuery();
}

package com.liskovsoft.mediaserviceinterfaces.data;

import androidx.annotation.Nullable;

import java.util.List;

public interface MediaGroup {
    int TYPE_UNDEFINED = -1;
    int TYPE_HOME = 0;
    int TYPE_SEARCH = 1;
    int TYPE_RECOMMENDED = 2;
    int TYPE_HISTORY = 3;
    int TYPE_SUBSCRIPTIONS = 4;
    int TYPE_MUSIC = 5;
    int TYPE_NEWS = 6;
    int TYPE_GAMING = 7;
    int TYPE_USER_PLAYLISTS = 8;
    int TYPE_SUGGESTIONS = 9;
    int TYPE_CHANNEL = 10;
    int TYPE_SETTINGS = 11;
    int TYPE_CHANNEL_UPLOADS = 12;
    int TYPE_KIDS_HOME = 13;
    int TYPE_TRENDING = 14;
    int TYPE_SHORTS = 15;
    int TYPE_NOTIFICATIONS = 16;
    int TYPE_SPORTS = 17;
    int TYPE_MOVIES = 18;
    int TYPE_LIVE = 19;
    int TYPE_MY_VIDEOS = 20;
    int TYPE_PLAYBACK_QUEUE = 21;
    int getType();
    @Nullable
    List<MediaItem> getMediaItems();
    String getTitle();
    // Below settings for Channel section group
    String getChannelId();
    String getParams();
    String getReloadPageKey();
    String getNextPageKey();
    String getChannelUrl();
    boolean isEmpty();
}

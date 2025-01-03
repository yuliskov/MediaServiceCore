package com.liskovsoft.mediaserviceinterfaces.yt.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface MediaItemGroup {
    int getId();
    String getTitle();
    String getIconUrl();
    List<MediaItem> getMediaItems();
    MediaItem findMediaItem(String channelOrVideoId);
    void add(@NonNull MediaItem mediaItem);
    void addAll(@NonNull List<MediaItem> newMediaItems);
    void remove(String channelOrVideoId);
    boolean contains(String channelOrVideoId);
    boolean isEmpty();

    interface MediaItem {
        String getTitle();
        String getSubtitle();
        String getIconUrl();
        String getChannelId();
        String getVideoId();
    }
}

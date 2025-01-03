package com.liskovsoft.mediaserviceinterfaces.yt.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface ItemGroup {
    int getId();
    String getTitle();
    String getIconUrl();
    List<Item> getItems();
    Item findItem(String channelOrVideoId);
    void add(@NonNull Item mediaItem);
    void addAll(@NonNull List<Item> newMediaItems);
    void remove(String channelOrVideoId);
    boolean contains(String channelOrVideoId);
    boolean isEmpty();

    interface Item {
        String getTitle();
        String getSubtitle();
        String getIconUrl();
        String getChannelId();
        String getVideoId();
    }
}

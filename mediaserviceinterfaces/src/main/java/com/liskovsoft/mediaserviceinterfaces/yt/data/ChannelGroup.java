package com.liskovsoft.mediaserviceinterfaces.yt.data;

import androidx.annotation.NonNull;

import java.util.List;

public interface ChannelGroup {
    int getId();
    String getTitle();
    String getIconUrl();
    List<Channel> getChannels();
    Channel findChannel(String channelId);
    void add(@NonNull Channel channel);
    void remove(String channelId);
    boolean contains(String channelId);
    boolean isEmpty();

    interface Channel {
        String getTitle();
        String getIconUrl();
        String getChannelId();
    }
}

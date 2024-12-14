package com.liskovsoft.mediaserviceinterfaces.yt.data;

import java.util.List;

public interface ChannelGroup {
    int getId();
    String getTitle();
    String getIconUrl();
    List<Channel> getChannels();
    void add(Channel channel);
    void remove(String channelId);
    boolean contains(String channelId);
    boolean isEmpty();

    interface Channel {
        String getTitle();
        String getIconUrl();
        String getChannelId();
    }
}

package com.liskovsoft.mediaserviceinterfaces.yt;

import android.net.Uri;
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup;
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup.Channel;

import java.util.List;
import io.reactivex.Observable;

public interface ChannelGroupService {
    List<ChannelGroup> getChannelGroups();
    void addChannelGroup(ChannelGroup group);
    void removeChannelGroup(ChannelGroup group);
    ChannelGroup createChannelGroup(String title, String iconUrl, List<Channel> channels);
    void renameChannelGroup(ChannelGroup channelGroup, String title);
    Channel createChannel(String title, String iconUrl, String channelId);
    ChannelGroup findChannelGroup(int channelGroupId);
    ChannelGroup findChannelGroup(String title);
    String[] getChannelGroupIds(int channelGroupId);
    Observable<Void> importGroups(Uri uri);
    void exportData(String data);
    boolean isEmpty();
}

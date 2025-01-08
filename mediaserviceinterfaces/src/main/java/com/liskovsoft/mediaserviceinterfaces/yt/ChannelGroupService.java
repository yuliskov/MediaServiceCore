package com.liskovsoft.mediaserviceinterfaces.yt;

import android.net.Uri;

import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup;
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup.Item;

import java.io.File;
import java.util.List;
import io.reactivex.Observable;

public interface ChannelGroupService {
    List<ItemGroup> getChannelGroups();
    void addChannelGroup(ItemGroup group);
    void removeChannelGroup(ItemGroup group);
    ItemGroup createChannelGroup(String title, String iconUrl, List<Item> channels);
    void renameChannelGroup(ItemGroup channelGroup, String title);
    Item createChannel(String title, String iconUrl, String channelId);
    ItemGroup findChannelGroup(int channelGroupId);
    ItemGroup findChannelGroup(String title);
    String[] findChannelIdsForGroup(int channelGroupId);
    Observable<List<ItemGroup>> importGroupsObserve(Uri uri);
    Observable<List<ItemGroup>> importGroupsObserve(File file);
    void exportData(String data);
    boolean isEmpty();
}

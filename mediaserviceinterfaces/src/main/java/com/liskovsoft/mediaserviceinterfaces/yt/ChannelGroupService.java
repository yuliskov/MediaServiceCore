package com.liskovsoft.mediaserviceinterfaces.yt;

import android.net.Uri;

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup;
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup.MediaItem;

import java.io.File;
import java.util.List;
import io.reactivex.Observable;

public interface ChannelGroupService {
    List<MediaItemGroup> getChannelGroups();
    void addChannelGroup(MediaItemGroup group);
    void removeChannelGroup(MediaItemGroup group);
    MediaItemGroup createChannelGroup(String title, String iconUrl, List<MediaItem> channels);
    void renameChannelGroup(MediaItemGroup channelGroup, String title);
    MediaItem createChannel(String title, String iconUrl, String channelId);
    MediaItemGroup findChannelGroup(int channelGroupId);
    MediaItemGroup findChannelGroup(String title);
    String[] findChannelIdsForGroup(int channelGroupId);
    String[] getSubscribedChannelIds();
    MediaItemGroup getSubscribedChannelGroup();
    Observable<List<MediaItemGroup>> importGroupsObserve(Uri uri);
    Observable<List<MediaItemGroup>> importGroupsObserve(File file);
    void exportData(String data);
    boolean isEmpty();
}

package com.liskovsoft.mediaserviceinterfaces.yt;

import android.net.Uri;
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup;
import java.util.List;
import io.reactivex.Observable;

public interface ChannelGroupService {
    List<ChannelGroup> getChannelGroups();
    void addChannelGroup(ChannelGroup group);
    void removeChannelGroup(ChannelGroup group);
    ChannelGroup findChannelGroup(int channelGroupId);
    ChannelGroup findChannelGroup(String title);
    String[] getChannelGroupIds(int channelGroupId);
    Observable<Void> importGroups(Uri uri);
    void exportData(String data);
    boolean isEmpty();
}

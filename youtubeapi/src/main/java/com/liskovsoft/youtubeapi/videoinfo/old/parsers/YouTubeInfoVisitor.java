package com.liskovsoft.youtubeapi.videoinfo.old.parsers;

import android.net.Uri;
import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.GenericInfo;
import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.MediaItem;
import com.liskovsoft.youtubeapi.videoinfo.old.interfaces.Subtitle;

public abstract class YouTubeInfoVisitor {
    public void onMediaItem(MediaItem mediaItem){}
    public void onSubItem(Subtitle mediaItem){}
    public void onStorySpec(String spec){}
    public void onHlsUrl(Uri url) {}
    public void doneVisiting(){}
    public void onGenericInfo(GenericInfo info){}
    public void onDashUrl(Uri url) {}
    public void onTrackingUrl(Uri url) {}
}

package com.liskovsoft.youtubeapi.formatbuilders.parsers;

import android.net.Uri;
import com.liskovsoft.youtubeapi.formatbuilders.interfaces.GenericInfo;
import com.liskovsoft.youtubeapi.formatbuilders.interfaces.MediaItem;
import com.liskovsoft.youtubeapi.formatbuilders.interfaces.Subtitle;

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

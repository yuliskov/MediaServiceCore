package com.liskovsoft.youtubeapi.videoinfo.parsers;

import android.net.Uri;
import com.liskovsoft.youtubeapi.videoinfo.interfaces.GenericInfo;
import com.liskovsoft.youtubeapi.videoinfo.interfaces.VideoMetadata;
import com.liskovsoft.youtubeapi.videoinfo.mpdbuilder.MPDBuilder;

import java.util.List;

public abstract class OnMediaFoundCallback {
    public void onStart(){}
    public void onFalseCall(){}
    public void onDashMPDFound(MPDBuilder mpdBuilder){}
    public void onHLSFound(Uri hlsUrl){}
    public void onUrlListFound(List<String> uriList) {}
    public void onDashUrlFound(Uri dashUrl) {}
    public void onTrackingUrlFound(Uri trackingUrl) {}
    public void onRealTrackingUrlFound(Uri trackingUrl) {}
    public void onStorySpecFound(String spec) {}
    public void onGenericInfoFound(GenericInfo info){}
    public void onMetadata(VideoMetadata metadata){}
    public abstract void onDone(); // Required!!!
}

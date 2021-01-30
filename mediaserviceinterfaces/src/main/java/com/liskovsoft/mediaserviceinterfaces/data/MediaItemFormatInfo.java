package com.liskovsoft.mediaserviceinterfaces.data;

import io.reactivex.Observable;

import java.io.InputStream;
import java.util.List;

public interface MediaItemFormatInfo {
    List<MediaFormat> getAdaptiveFormats();
    List<MediaFormat> getRegularFormats();
    List<MediaSubtitle> getSubtitles();
    String getHlsManifestUrl();
    String getDashManifestUrl();
    // video metadata
    String getLengthSeconds();
    void setLengthSeconds(String lengthSeconds);
    String getTitle();
    void setTitle(String title);
    String getAuthor();
    void setAuthor(String author);
    String getViewCount();
    void setViewCount(String viewCount);
    String getTimestamp();
    void setTimestamp(String timestamp);
    String getDescription();
    void setDescription(String description);
    String getVideoId();
    void setVideoId(String videoId);
    String getChannelId();
    void setChannelId(String channelId);
    boolean isLive();
    boolean containsDashInfo();
    boolean containsDashVideoInfo();
    boolean containsHlsUrl();
    boolean containsDashUrl();
    boolean containsUrlListInfo();
    InputStream createMpdStream();
    Observable<InputStream> createMpdStreamObservable();
    List<String> createUrlList();
    MediaItemStoryboard createStoryboard();
    boolean isUnplayable();
    String getPlayabilityStatus();
    boolean isStreamSeekable();
}

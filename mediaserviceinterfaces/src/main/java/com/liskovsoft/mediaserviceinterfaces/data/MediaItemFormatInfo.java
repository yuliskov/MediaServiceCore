package com.liskovsoft.mediaserviceinterfaces.data;

import io.reactivex.Observable;

import java.io.InputStream;
import java.util.List;

public interface MediaItemFormatInfo {
    List<MediaFormat> getDashFormats();
    List<MediaFormat> getUrlFormats();
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
    String getDescription();
    void setDescription(String description);
    String getVideoId();
    void setVideoId(String videoId);
    String getChannelId();
    void setChannelId(String channelId);
    boolean isLive();
    boolean isLiveContent();
    boolean containsMedia();
    boolean containsDashFormats();
    boolean containsDashVideoFormats();
    boolean containsHlsUrl();
    boolean containsDashUrl();
    boolean containsUrlFormats();
    boolean hasExtendedHlsFormats();
    float getVolumeLevel();
    InputStream createMpdStream();
    Observable<InputStream> createMpdStreamObservable();
    List<String> createUrlList();
    MediaItemStoryboard createStoryboard();
    boolean isUnplayable();
    boolean isHistoryBroken();
    boolean isBotCheckError();
    String getPlayabilityStatus();
    boolean isStreamSeekable();
    /**
     * Stream start time in UTC (!!!).<br/>
     * E.g.: <b>2021-10-06T13:36:25+00:00</b>
     */
    String getStartTimestamp();
    String getUploadDate();
    /**
     * Stream start time in UNIX format.<br/>
     */
    long getStartTimeMs();
    /**
     * Number of the stream first segment
     */
    int getStartSegmentNum();
    /**
     * Precise segment duration.<br/>
     * Used inside live streams
     */
    int getSegmentDurationUs();
    String getPaidContentText();
}

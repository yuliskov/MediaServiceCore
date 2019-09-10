package com.liskovsoft.youtubeapi.browse.models;

import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;
import com.liskovsoft.youtubeapi.support.converters.jsonpath.JsonPath;

import java.util.List;

public class BrowseVideoItem {
    @JsonPath("$.videoId")
    private String videoId;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> thumbnails;
    @JsonPath("$.channelThumbnail.thumbnails[0]")
    private String channelThumbnail;
    @JsonPath("$.title.simpleText")
    private String title;
    @JsonPath("$.longBylineText.runs[0].text")
    private String userName;
    //@JsonPath("$.longBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId")
    private String channelId;
    //@JsonPath("$.longBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl")
    private String canonicalChannelUrl;
    @JsonPath("$.publishedTimeText.simpleText")
    private String publishedTime;
    //@JsonPath("$.viewCountText.runs[0].text")
    private String viewCount;
    //@JsonPath("$.shortViewCountText.runs[0].text")
    private String shortViewCount;
    @JsonPath("$.lengthText.simpleText")
    private String lengthText;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String accessibilityLengthText;
    //@JsonPath("$.badges[0].textBadge.label.runs[0].text")
    private String qualityBadge;

    public String getVideoId() {
        return videoId;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public String getChannelThumbnail() {
        return channelThumbnail;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getCanonicalChannelUrl() {
        return canonicalChannelUrl;
    }

    public String getPublishedTime() {
        return publishedTime;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getShortViewCount() {
        return shortViewCount;
    }

    public String getLengthText() {
        return lengthText;
    }

    public String getAccessibilityLength() {
        return accessibilityLengthText;
    }

    public String getQualityBadge() {
        return qualityBadge;
    }
}

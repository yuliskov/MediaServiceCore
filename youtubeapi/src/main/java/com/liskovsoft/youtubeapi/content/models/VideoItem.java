package com.liskovsoft.youtubeapi.content.models;

import com.liskovsoft.youtubeapi.support.JsonPath;

import java.util.List;

public class VideoItem {
    @JsonPath("$.gridVideoRenderer.videoId")
    private String videoId;
    @JsonPath("$.gridVideoRenderer.thumbnail.thumbnails")
    private List<Thumbnail> thumbnails;
    @JsonPath("$.gridVideoRenderer.title.runs[0].text")
    private String title;
    @JsonPath("$.gridVideoRenderer.longBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId")
    private String channelId;
    @JsonPath("$.gridVideoRenderer.longBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl")
    private String canonicalChannelUrl;
    @JsonPath("$.gridVideoRenderer.publishedTimeText.runs[0].text")
    private String publishedTime;
    @JsonPath("$.gridVideoRenderer.longBylineText.runs[0].navigationEndpoint.browseEndpoint.canonicalBaseUrl")
    private String viewCount;
    @JsonPath("$.gridVideoRenderer.lengthText.runs[0].text")
    private String length;
    @JsonPath("$.gridVideoRenderer.lengthText.accessibility.accessibilityData.label")
    private String accessibilityLength;
    @JsonPath("$.gridVideoRenderer.badges[0].textBadge.label.runs[0].text")
    private String qualityBadge;
    @JsonPath("$.gridVideoRenderer.channelThumbnail.thumbnails[0]")
    private String channelThumbnail;
    @JsonPath("$.gridVideoRenderer.shortViewCountText.runs[0].text")
    private String shortViewCount;
}

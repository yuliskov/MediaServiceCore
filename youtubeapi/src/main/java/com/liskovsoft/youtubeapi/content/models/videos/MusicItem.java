package com.liskovsoft.youtubeapi.content.models.videos;

import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.converters.jsonpath.JsonPathCollection;

public class MusicItem {
    @JsonPath("$.tvMusicVideoRenderer.navigationEndpoint.watchEndpoint.videoId")
    private String videoId;
    @JsonPath("$.tvMusicVideoRenderer.thumbnail.thumbnails[*]")
    private JsonPathCollection<Thumbnail> thumbnails = new JsonPathCollection<>(Thumbnail.class);
    @JsonPath("$.tvMusicVideoRenderer.primaryText.runs[0].text")
    private String title;
    @JsonPath("$.tvMusicVideoRenderer.navigationEndpoint.watchEndpoint.playlistId")
    private String playlistId;
    @JsonPath("$.tvMusicVideoRenderer.tertiaryText.runs[2].text")
    private String publishedTime;
    @JsonPath("$.tvMusicVideoRenderer.tertiaryText.runs[0].text")
    private String viewCount;
    @JsonPath("$.tvMusicVideoRenderer.lengthText.runs[0].text")
    private String length;
    @JsonPath("$.tvMusicVideoRenderer.lengthText.accessibility.accessibilityData.label")
    private String accessibilityLength;
    @JsonPath("$.gridVideoRenderer.badges[0].textBadge.label.runs[0].text")
    private String qualityBadge;
    @JsonPath("$.gridVideoRenderer.channelThumbnail.thumbnails[0]")
    private String channelThumbnail;
    @JsonPath("$.gridVideoRenderer.shortViewCountText.runs[0].text")
    private String shortViewCount;
}

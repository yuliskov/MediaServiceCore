package com.liskovsoft.youtubeapi.common.models.videos;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class MusicItem {
    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String videoId;
    @JsonPath("$.thumbnail.thumbnails[*]")
    private List<Thumbnail> thumbnails;
    @JsonPath({"$.primaryText.simpleText", "$.primaryText.runs[0].text"})
    private String title;
    @JsonPath({"$.secondaryText.simpleText", "$.secondaryText.runs[0].text"})
    private String userName;
    @JsonPath("$.navigationEndpoint.watchEndpoint.playlistId")
    private String playlistId;
    @JsonPath("$.tertiaryText.simpleText")
    private String viewsAndPublished;
    @JsonPath({"$.lengthText.simpleText", "$.lengthText.runs[0].text"})
    private String length;
    @JsonPath("$.lengthText.accessibility.accessibilityData.label")
    private String accessibilityLength;

    public String getVideoId() {
        return videoId;
    }

    public List<Thumbnail> getThumbnails() {
        return thumbnails;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getViewsAndPublished() {
        return viewsAndPublished;
    }

    public String getLength() {
        return length;
    }

    public String getAccessibilityLength() {
        return accessibilityLength;
    }
}

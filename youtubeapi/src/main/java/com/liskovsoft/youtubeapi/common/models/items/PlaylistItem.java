package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

// root element: pivotPlaylistRenderer (e.g. Album)
public class PlaylistItem {
    @JsonPath({"$.playlistId", "$.navigationEndpoint.watchEndpoint.playlistId"})
    private String mPlaylistId;
    @JsonPath("$.thumbnailRenderer.playlistCustomThumbnailRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.title.runs[0].text")
    private String mTitle;
    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId")
    private String mChannelId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String mVideoCountText;
    @JsonPath("$.shortBylineText.runs[0].text")
    private String mDescription;

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getVideoCountText() {
        return mVideoCountText;
    }

    public String getDescription() {
        return mDescription;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getChannelId() {
        return mChannelId;
    }
}

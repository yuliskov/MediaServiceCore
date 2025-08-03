package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

// root element: pivotPlaylistRenderer (e.g. Album)
public class PlaylistItem {
    @JsonPath({"$.thumbnail.thumbnails[*]",
               "$.thumbnailRenderer.playlistCustomThumbnailRenderer.thumbnail.thumbnails[*]",
               "$.thumbnailRenderer.playlistVideoThumbnailRenderer.thumbnail.thumbnails[*]"})
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mTitle;
    @JsonPath("$.shortBylineText.runs[0].text")
    private String mDescription;
    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.shortBylineText.runs[0].navigationEndpoint.browseEndpoint.browseId")
    private String mChannelId;
    @JsonPath({"$.playlistId", "$.navigationEndpoint.watchEndpoint.playlistId"})
    private String mPlaylistId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String mVideoCountText;
    @JsonPath("$.navigationEndpoint.watchEndpoint.params")
    private String mParams;

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

    public String getParams() {
        return mParams;
    }
}

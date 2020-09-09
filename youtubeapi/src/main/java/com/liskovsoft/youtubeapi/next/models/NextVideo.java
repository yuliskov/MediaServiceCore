package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class NextVideo {
    @JsonPath("$.endpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.endpoint.watchEndpoint.playlistId")
    private String mPlaylistId; // present only on playlists
    @JsonPath("$.endpoint.watchEndpoint.index")
    private String mPlaylistItemIndex; // index inside a playlist (present only on playlists)
    @JsonPath("$.item.previewButtonRenderer.title.runs[0].text")
    private String mTitle;
    @JsonPath("$.item.previewButtonRenderer.byline.runs[0].text")
    private String mAuthor;
    @JsonPath("$.item.previewButtonRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath("$.endpoint.watchEndpoint.params")
    private String mParams;

    public String getVideoId() {
        return mVideoId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }
}

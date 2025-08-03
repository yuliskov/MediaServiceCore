package com.liskovsoft.youtubeapi.next.v1.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

public class NextVideo {
    @JsonPath("$.item.previewButtonRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;
    @JsonPath({"$.item.previewButtonRenderer.title.simpleText", "$.item.previewButtonRenderer.title.runs[0].text"})
    private String mTitle;
    @JsonPath("$.endpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.endpoint.watchEndpoint.playlistId")
    private String mPlaylistId; // present only on playlists
    @JsonPath("$.endpoint.watchEndpoint.index")
    private int mPlaylistItemIndex = -1; // index inside a playlist (present only on playlists)
    @JsonPath({"$.item.previewButtonRenderer.byline.simpleText", "$.item.previewButtonRenderer.byline.runs[0].text"})
    private String mAuthor;
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

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public int getPlaylistItemIndex() {
        return mPlaylistItemIndex;
    }
}

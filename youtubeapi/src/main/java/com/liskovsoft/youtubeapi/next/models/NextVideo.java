package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.items.Thumbnail;

import java.util.List;

public class NextVideo {
    @JsonPath("$.maybeHistoryEndpointRenderer.endpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath("$.maybeHistoryEndpointRenderer.item.previewButtonRenderer.title.runs[0].text")
    private String mTitle;
    @JsonPath("$.maybeHistoryEndpointRenderer.item.previewButtonRenderer.byline.runs[0].text")
    private String mAuthor;
    @JsonPath("$.maybeHistoryEndpointRenderer.item.previewButtonRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mThumbnails;

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

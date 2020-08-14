package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.videos.Thumbnail;

import java.util.List;

public class NextVideo {
    @JsonPath("$.maybeHistoryEndpointRenderer.endpoint.watchEndpoint.videoId")
    private String mNextVideoId;
    @JsonPath("$.maybeHistoryEndpointRenderer.item.previewButtonRenderer.title.runs[0].text")
    private String mNextVideoTitle;
    @JsonPath("$.maybeHistoryEndpointRenderer.item.previewButtonRenderer.byline.runs[0].text")
    private String mNextVideoAuthor;
    @JsonPath("$.maybeHistoryEndpointRenderer.item.previewButtonRenderer.thumbnail.thumbnails[*]")
    private List<Thumbnail> mNextVideoThumbnails;

    public String getNextVideoId() {
        return mNextVideoId;
    }

    public String getNextVideoTitle() {
        return mNextVideoTitle;
    }

    public String getNextVideoAuthor() {
        return mNextVideoAuthor;
    }

    public List<Thumbnail> getNextVideoThumbnails() {
        return mNextVideoThumbnails;
    }
}

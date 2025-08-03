package com.liskovsoft.youtubeapi.common.models.items;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;
import com.liskovsoft.googlecommon.common.models.items.Thumbnail;

import java.util.List;

// root element: pivotRadioRenderer (e.g. YouTube Mix)
public class RadioItem {
    @JsonPath({"$.playlistId", "$.navigationEndpoint.watchEndpoint.playlistId"})
    private String mPlaylistId;
    @JsonPath("$.navigationEndpoint.watchEndpoint.videoId")
    private String mVideoId;
    @JsonPath({"$.title.runs[0].text", "$.title.simpleText"})
    private String mTitle;
    @JsonPath("$.secondaryNavigationEndpoint.watchEndpoint.videoId")
    private String mSecondVideoId;
    @JsonPath("$.videoCountText.runs[0].text")
    private String mVideoCountText;
    @JsonPath("$.videoCountShortText.runs[0].text")
    private String mVideoCountShortText;
    @JsonPath("$.longBylineText.simpleText")
    private String mDescription;
    @JsonPath("$.thumbnailText.runs[0].text")
    private String mThumbnailText1;
    @JsonPath("$.thumbnailText.runs[1].text")
    private String mThumbnailText2;
    @JsonPath({"$.thumbnailRenderer.playlistVideoThumbnailRenderer.thumbnail.thumbnails[*]", "$.thumbnail.thumbnails[*]"})
    private List<Thumbnail> mThumbnails;
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

    public String getSecondVideoId() {
        return mSecondVideoId;
    }

    public String getVideoCountText() {
        return mVideoCountText;
    }

    public String getVideoCountShortText() {
        return mVideoCountShortText;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getThumbnailText() {
        return mThumbnailText1 + " " + mThumbnailText2;
    }

    public List<Thumbnail> getThumbnails() {
        return mThumbnails;
    }

    public String getParams() {
        return mParams;
    }
}

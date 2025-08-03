package com.liskovsoft.youtubeapi.next.v1.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class Playlist {
    @JsonPath("$.title")
    private String mTitle;

    @JsonPath("$.playlistId")
    private String mPlaylistId;

    @JsonPath("$.currentIndex")
    private int mPlaylistIndex = -1;

    @JsonPath("$.totalVideos")
    private int mTotalVideos;

    public String getTitle() {
        return mTitle;
    }

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public int getPlaylistIndex() {
        return mPlaylistIndex;
    }

    public int getTotalVideos() {
        return mTotalVideos;
    }
}

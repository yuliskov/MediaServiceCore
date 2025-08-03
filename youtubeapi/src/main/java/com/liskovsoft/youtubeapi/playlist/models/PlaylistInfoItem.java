package com.liskovsoft.youtubeapi.playlist.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class PlaylistInfoItem {
    private static final String CONTAINS_SELECTED_NONE = "NONE";
    private static final String CONTAINS_SELECTED_ALL = "ALL";

    @JsonPath("$.playlistId")
    private String mPlaylistId;

    @JsonPath("$.title.simpleText")
    private String mTitle;

    @JsonPath("$.containsSelectedVideos")
    private String mContainsSelected;

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContainsSelected() {
        return mContainsSelected;
    }

    public boolean isSelected() {
        return CONTAINS_SELECTED_ALL.equals(mContainsSelected);
    }
}

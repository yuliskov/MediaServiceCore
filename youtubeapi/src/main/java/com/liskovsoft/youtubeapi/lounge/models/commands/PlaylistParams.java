package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class PlaylistParams {
    @JsonPath("$.videoId")
    private String mVideoId;

    @JsonPath("$.videoIds")
    private String mVideoIds;

    @JsonPath("$.listId")
    private String mPlaylistId;

    @JsonPath("$.currentTime")
    private String mCurrentTimeSec;

    @JsonPath("$.currentIndex")
    private String mPlaylistIndex;

    @JsonPath("$.ctt")
    private String mCtt;

    public String getPlaylistId() {
        return mPlaylistId;
    }

    public String getCurrentTimeSec() {
        return mCurrentTimeSec;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String[] getVideoIds() {
        if (mVideoIds == null) {
            return null;
        }

        return mVideoIds.split(",");
    }

    public String getPlaylistIndex() {
        return mPlaylistIndex;
    }

    public String getCtt() {
        return mCtt;
    }
}

package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class PlaylistParams {
    @JsonPath("$.videoId")
    private String mVideoId;

    /**
     * Comma separated list of video ids
     */
    @JsonPath("$.videoIds")
    private String mVideoIds;

    @JsonPath("$.languageCode")
    private String mLanguageCode;

    @JsonPath("$.listId")
    private String mPlaylistId;

    @JsonPath("$.currentIndex")
    private String mPlaylistIndex;

    @JsonPath("$.currentTime")
    private String mCurrentTimeSec;

    @JsonPath("$.ctt")
    private String mCtt;

    public String getLanguageCode() {
        return mLanguageCode;
    }

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

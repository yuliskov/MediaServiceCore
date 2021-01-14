package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class PlaylistData {
    @JsonPath("$.listId")
    private String mListId;

    @JsonPath("$.currentTime")
    private String mCurrentTime;

    @JsonPath("$.videoId")
    private String mVideoId;

    @JsonPath("$.videoIds")
    private String mVideoIds;

    @JsonPath("$.currentIndex")
    private String mCurrentIndex;

    @JsonPath("$.ctt")
    private String mCtt;

    public String getListId() {
        return mListId;
    }

    public String getCurrentTime() {
        return mCurrentTime;
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

    public String getCurrentIndex() {
        return mCurrentIndex;
    }

    public String getCtt() {
        return mCtt;
    }
}

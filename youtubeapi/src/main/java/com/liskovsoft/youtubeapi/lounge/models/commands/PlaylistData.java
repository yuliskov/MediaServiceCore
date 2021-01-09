package com.liskovsoft.youtubeapi.lounge.models.commands;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class PlaylistData {
    @JsonPath("$.listId")
    private String mListId;

    @JsonPath("$.currentTime")
    private String mCurrentTime;

    @JsonPath("$.videoId")
    private String mVideoId;

    @JsonPath("$.currentIndex")
    private String mCurrentIndex;

    public String getListId() {
        return mListId;
    }

    public String getCurrentTime() {
        return mCurrentTime;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public String getCurrentIndex() {
        return mCurrentIndex;
    }
}

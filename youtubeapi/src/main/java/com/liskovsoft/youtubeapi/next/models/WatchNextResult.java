package com.liskovsoft.youtubeapi.next.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class WatchNextResult {
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.contents.singleColumnWatchNextResults.pivot.pivot.contents[*].shelfRenderer")
    private List<WatchNextSection> mSections;

    public List<WatchNextSection> getSections() {
        return mSections;
    }
}

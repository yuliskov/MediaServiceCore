package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class Chip {
    @JsonPath("$.text.simpleText")
    private String mTitle;

    @JsonPath("$.content.horizontalListRenderer.continuations[0].reloadContinuationData.continuation")
    private String mReloadPageKey;

    public String getTitle() {
        return mTitle;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}

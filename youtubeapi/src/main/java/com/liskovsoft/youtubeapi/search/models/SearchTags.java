package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class SearchTags {
    @JsonPath("$[0]")
    private String mSearchQuery;

    @JsonPath("$[1][*][0]")
    private List<String> mSearchTags;

    public String getSearchQuery() {
        return mSearchQuery;
    }

    public List<String> getSearchTags() {
        return mSearchTags;
    }
}

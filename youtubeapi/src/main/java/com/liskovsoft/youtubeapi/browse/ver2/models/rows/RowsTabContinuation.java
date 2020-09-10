package com.liskovsoft.youtubeapi.browse.ver2.models.rows;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class RowsTabContinuation {
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.continuationContents.sectionListContinuation.contents[*].shelfRenderer")
    private List<Row> mRows;

    @JsonPath("$.continuationContents.sectionListContinuation.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public List<Row> getRows() {
        return mRows;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }
}

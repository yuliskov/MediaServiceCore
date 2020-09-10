package com.liskovsoft.youtubeapi.browse.ver2.models.rows;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class RowsTab {
    @JsonPath("$.title")
    private String title;

    @JsonPath("$.endpoint.browseEndpoint.browseId")
    private String browseId;

    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*].shelfRenderer")
    private List<Row> mRows;

    @JsonPath("$.content.tvSurfaceContentRenderer.content.sectionListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadPageKey;

    public String getTitle() {
        return title;
    }

    public String getBrowseId() {
        return browseId;
    }

    public List<Row> getRows() {
        return mRows;
    }


    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}

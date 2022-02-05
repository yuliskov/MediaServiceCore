package com.liskovsoft.youtubeapi.search.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TextItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class SearchSection {
    @JsonPath({
            "$.itemSectionRenderer.header.itemSectionHeaderRenderer.title",
            "$.itemSectionRenderer.contents[0].shelfRenderer.headerRenderer.shelfHeaderRenderer.title"
    })
    private TextItem mTitle;

    @JsonPath({
            "$.itemSectionRenderer.contents[0].shelfRenderer.content.horizontalListRenderer.items[*]",
            "$.itemSectionRenderer.contents[*]"
    })
    private List<ItemWrapper> mItemWrappers;

    @JsonPath("$.itemSectionRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public String getTitle() {
        return mTitle != null ? mTitle.getText() : null;
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }
}

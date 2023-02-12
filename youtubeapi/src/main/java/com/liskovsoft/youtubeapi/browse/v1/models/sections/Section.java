package com.liskovsoft.youtubeapi.browse.v1.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TextItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class Section {
    @JsonPath({"$.title", "$.headerRenderer.shelfHeaderRenderer.title"})
    private TextItem mTitle;
    @JsonPath("$.headerRenderer.chipCloudRenderer.chips[*].chipCloudChipRenderer")
    private List<Chip> mChips;
    @JsonPath("$.content.horizontalListRenderer.items[*]")
    private List<ItemWrapper> mItemWrappers;
    @JsonPath("$.content.horizontalListRenderer.continuations[0].nextContinuationData.continuation")
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

    public List<Chip> getChips() {
        return mChips;
    }
}

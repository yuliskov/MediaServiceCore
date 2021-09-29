package com.liskovsoft.youtubeapi.browse.models.sections.v2;

import com.liskovsoft.youtubeapi.browse.models.sections.Section;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.models.V2.TextItem;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;

import java.util.List;

public class TwoColumnSection extends Section {
    @JsonPath("$.leftColumn.entityMetadataRenderer.title")
    private TextItem mTitle;

    /**
     * New type of playlist. Example: type in search page: "Сто лучших клипов 90-х"
     */
    @JsonPath("$.rightColumn.playlistVideoListRenderer.contents[*]")
    private List<ItemWrapper> mItemWrappers;

    @JsonPath("$.rightColumn.playlistVideoListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey2;

    @Override
    public String getTitle() {
        return mTitle.getText();
    }

    @Override
    public String getNextPageKey() {
        return mNextPageKey2;
    }

    @Override
    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }
}

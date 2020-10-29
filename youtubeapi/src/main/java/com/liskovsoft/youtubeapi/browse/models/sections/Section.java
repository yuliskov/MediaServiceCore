package com.liskovsoft.youtubeapi.browse.models.sections;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;
import com.liskovsoft.youtubeapi.common.models.items.ItemWrapper;
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper;

import java.util.List;

public class Section {
    @JsonPath({"$.title.simpleText", "$.title.runs[0].text"})
    private String mTitle1;
    @JsonPath("$.title.runs[1].text")
    private String mTitle2;
    @JsonPath("$.title.runs[2].text")
    private String mTitle3;
    @JsonPath("$.content.horizontalListRenderer.items[*]")
    private List<ItemWrapper> mItemWrappers;
    @JsonPath("$.content.horizontalListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    public String getTitle() {
        return AppHelper.combineText(mTitle1, mTitle2, mTitle3);
    }

    public String getNextPageKey() {
        return mNextPageKey;
    }

    public List<ItemWrapper> getItemWrappers() {
        return mItemWrappers;
    }
}

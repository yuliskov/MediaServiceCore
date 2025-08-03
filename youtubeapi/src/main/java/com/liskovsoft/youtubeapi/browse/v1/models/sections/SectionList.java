package com.liskovsoft.youtubeapi.browse.v1.models.sections;

import com.liskovsoft.youtubeapi.browse.v1.models.sections.v2.TwoColumnSection;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.Collections;
import java.util.List;

/**
 * Used as Channel content
 */
public class SectionList {
    /**
     * Sections == Rows in web app version
     */
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSurfaceContentRenderer.content.sectionListRenderer.contents[*].shelfRenderer")
    private List<Section> mSections;

    /**
     * New type of playlist. Example: type in search page: "Сто лучших клипов 90-х"
     */
    @JsonPath("$.contents.tvBrowseRenderer.content.tvSurfaceContentRenderer.content.twoColumnRenderer")
    private TwoColumnSection mTwoColumnSection;

    @JsonPath("$.contents.tvBrowseRenderer.content.tvSurfaceContentRenderer.content.sectionListRenderer.continuations[0].nextContinuationData.continuation")
    private String mNextPageKey;

    @JsonPath("$.contents.tvBrowseRenderer.content.tvSurfaceContentRenderer.continuation.reloadContinuationData.continuation")
    private String mReloadPageKey;

    public List<Section> getSections() {
        return mSections != null ? mSections : mTwoColumnSection != null ? Collections.singletonList(mTwoColumnSection) : null;
    }
    
    public String getNextPageKey() {
        return mNextPageKey;
    }

    public String getReloadPageKey() {
        return mReloadPageKey;
    }
}

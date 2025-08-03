package com.liskovsoft.youtubeapi.browse.v1.models.guide;

import com.liskovsoft.youtubeapi.browse.v1.models.guide.TrackingParam.Param;
import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class Guide {
    @JsonPath("$.items[*].guideSectionRenderer.items[*].guideEntryRenderer")
    private List<GuideItem> mItems;

    @JsonPath("$.responseContext.serviceTrackingParams[*]")
    private List<TrackingParam> mTrackingParams;

    private String mSuggestToken;

    public List<GuideItem> getItems() {
        return mItems;
    }

    public String getSuggestToken() {
        if (mSuggestToken != null) {
            return mSuggestToken;
        }

        if (mTrackingParams != null) {
            for (TrackingParam param : mTrackingParams) {
                if (TrackingParam.SERVICE_SUGGEST.equals(param.getService())) {
                    if (param.getParams() != null) {
                        for (TrackingParam.Param innerParam : param.getParams()) {
                            if (Param.KEY_SUGGEST_TOKEN.equals(innerParam.getKey())) {
                                mSuggestToken = innerParam.getValue();
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }

        return mSuggestToken;
    }
}

package com.liskovsoft.youtubeapi.actions.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class ActionResult {
    @JsonPath("$.trackingParams")
    private String mTrackingParams;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    public String getTrackingParams() {
        return mTrackingParams;
    }

    public String getVisitorData() {
        return mVisitorData;
    }
}

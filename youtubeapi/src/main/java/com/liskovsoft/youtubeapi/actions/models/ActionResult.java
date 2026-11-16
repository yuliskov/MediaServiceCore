package com.liskovsoft.youtubeapi.actions.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

public class ActionResult {
    /**
     * Serves as result health checking
     */
    @JsonPath("$.responseContext.serviceTrackingParams[0].service")
    private String mTrackingParams;

    /**
     * Serves as result health checking
     */
    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    /**
     * Present in playlist edit responses (e.g. STATUS_SUCCEEDED)
     */
    @JsonPath("$.status")
    private String mStatus;

    public String getStatus() {
        return mStatus;
    }
}

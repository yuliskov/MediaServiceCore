package com.liskovsoft.youtubeapi.actions.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

public class ActionResult {
    @JsonPath("$.trackingParams")
    private String mTrackingParams;

    @JsonPath("$.responseContext.visitorData")
    private String mVisitorData;

    /**
     * Serves as result health checking
     */
    @JsonPath("$.responseContext.consistencyTokenJar.encryptedTokenJarContents")
    private String mEncryptedTokenJar;

    public String getTrackingParams() {
        return mTrackingParams;
    }

    public String getVisitorData() {
        return mVisitorData;
    }

    public String getEncryptedTokenJar() {
        return mEncryptedTokenJar;
    }
}

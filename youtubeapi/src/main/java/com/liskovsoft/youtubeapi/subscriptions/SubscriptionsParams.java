package com.liskovsoft.youtubeapi.subscriptions;

import com.liskovsoft.youtubeapi.subscriptions.models.Subscriptions;

public class SubscriptionsParams {
    private static final String JSON_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7.20190214\"," +
            "\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\",\"utcOffsetMinutes\":180," +
            "\"webpSupport\":false,\"animatedWebpSupport\":false,\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"}," +
            "\"acceptRegion\":\"UA\",\"experimentsToken\":\"GgIQAA%3D%3D\",\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\"," +
            "\"platform\":\"TV\"},\"request\":{\"consistencyTokenJars\":[]},\"user\":{\"enableSafetyMode\":false}," +
            "\"clickTracking\":{\"clickTrackingParams\":\"CAYQtSwYAiITCPOIqL3bweQCFYGimwod1FcD9DIKZy1wZXJzb25hbA==\"}}," +
            "\"browseId\":\"FEsubscriptions\"}";
    private static final String JSON_CONTINUATION_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7" +
            ".20190214\",\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\"," +
            "\"utcOffsetMinutes\":180,\"webpSupport\":false,\"animatedWebpSupport\":false," +
            "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"},\"acceptRegion\":\"UA\"," +
            "\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\",\"platform\":\"TV\"},\"request\":{\"consistencyTokenJars\":[]}," +
            "\"user\":{\"enableSafetyMode\":false},\"clickTracking\":{\"clickTrackingParams\":\"CNkIEMm3AiITCJCMppLaxOQCFcdimwodFqUFqA==\"}}," +
            "\"continuation\":\"%s\"}";

    public static String getBrowseQuery() {
        return JSON_DATA_TEMPLATE;
    }

    /**
     * Get data param for the next search
     * @param nextPageKey {@link Subscriptions#getNextPageKey()}
     * @return data param
     */
    public static String getNextBrowseQuery(String nextPageKey) {
        return String.format(JSON_CONTINUATION_DATA_TEMPLATE, nextPageKey);
    }
}

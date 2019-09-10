package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.BrowseResult;

public class BrowseParams {
    private static final String JSON_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7.20190214\"," +
            "\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\",\"utcOffsetMinutes\":180," +
            "\"webpSupport\":false,\"animatedWebpSupport\":false,\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"}," +
            "\"acceptRegion\":\"UA\",\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\"," +
            "\"platform\":\"TV\"},\"request\":{\"consistencyTokenJars\":[]},\"user\":{\"enableSafetyMode\":false}," +
            "\"clickTracking\":{\"clickTrackingParams\":\"CAYQtSwYAiITCPOIqL3bweQCFYGimwod1FcD9DIKZy1wZXJzb25hbA==\"}}," +
            "\"browseId\":\"%s\"}";
    private static final String JSON_CONTINUATION_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7" +
            ".20190214\",\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\"," +
            "\"utcOffsetMinutes\":180,\"webpSupport\":false,\"animatedWebpSupport\":false," +
            "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"},\"acceptRegion\":\"UA\"," +
            "\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\",\"platform\":\"TV\"},\"request\":{\"consistencyTokenJars\":[]}," +
            "\"user\":{\"enableSafetyMode\":false},\"clickTracking\":{\"clickTrackingParams\":\"CNkIEMm3AiITCJCMppLaxOQCFcdimwodFqUFqA==\"}}," +
            "\"continuation\":\"%s\"}";
    private static final String SUBSCRIPTIONS = "FEsubscriptions";
    private static final String HISTORY = "FEmy_youtube";
    private static final String HOME = "FEtopics";

    public static String getSubscriptionsQuery() {
        return String.format(JSON_DATA_TEMPLATE, SUBSCRIPTIONS);
    }

    public static String getHistoryQuery() {
        return String.format(JSON_DATA_TEMPLATE, HISTORY);
    }

    /**
     * Get data param for the next search
     * @param nextPageKey {@link BrowseResult#getNextPageKey()}
     * @return data param
     */
    public static String getNextBrowseQuery(String nextPageKey) {
        return String.format(JSON_CONTINUATION_DATA_TEMPLATE, nextPageKey);
    }

    public static String getHomeQuery() {
        return String.format(JSON_DATA_TEMPLATE, HOME);
    }
}

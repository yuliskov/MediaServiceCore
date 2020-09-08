package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.BrowseResult;

public class BrowseManagerParams {
    public static int RECOMMENDED_TAB_INDEX = 0;
    public static int NEWS_TAB_INDEX = 1;
    public static int MUSIC_TAB_INDEX = 2;
    public static int GAMING_TAB_INDEX = 3;
    // TODO: too many hardcoded params: clickTrackingParams, deviceModel, clientVersion, webpSupport, animatedWebpSupport (preview animations)
    private static final String JSON_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7.20190214\"," +
            "\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\",\"utcOffsetMinutes\":180," +
            "\"webpSupport\":false,\"animatedWebpSupport\":false,\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"}," +
            "\"acceptRegion\":\"UA\",\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\"," +
            "\"platform\":\"TV\"},\"request\":{\"consistencyTokenJars\":[]},\"user\":{\"enableSafetyMode\":false}," +
            "\"clickTracking\":{\"clickTrackingParams\":\"CAYQtSwYAiITCPOIqL3bweQCFYGimwod1FcD9DIKZy1wZXJzb25hbA==\"}}," +
            "%s}";
    private static final String JSON_CONTINUATION_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7" +
            ".20190214\",\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\"," +
            "\"utcOffsetMinutes\":180,\"webpSupport\":false,\"animatedWebpSupport\":false," +
            "\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"},\"acceptRegion\":\"UA\"," +
            "\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\",\"platform\":\"TV\"},\"request\":{\"consistencyTokenJars\":[]}," +
            "\"user\":{\"enableSafetyMode\":false},\"clickTracking\":{\"clickTrackingParams\":\"CNkIEMm3AiITCJCMppLaxOQCFcdimwodFqUFqA==\"}}," +
            "\"continuation\":\"%s\"}";
    private static final String SUBSCRIPTIONS = "\"browseId\":\"FEsubscriptions\"";
    private static final String HISTORY = "\"browseId\":\"FEmy_youtube\"";
    private static final String HOME = "\"browseId\":\"FEtopics\"";
    private static final String GAMING = "\"browseId\":\"FEtopics\",\"params\":\"-gIGZ2FtaW5n\"";
    private static final String NEWS = "\"browseId\":\"FEtopics\",\"params\":\"-gINaGFwcGVuaW5nX25vdw%3D%3D\"";
    private static final String MUSIC = "\"browseId\":\"FEtopics\",\"params\":\"-gIFbXVzaWM%3D\"";

    public static String getSubscriptionsQuery() {
        return String.format(JSON_DATA_TEMPLATE, SUBSCRIPTIONS);
    }

    public static String getHistoryQuery() {
        return String.format(JSON_DATA_TEMPLATE, HISTORY);
    }

    public static String getGamingQuery() {
        return String.format(JSON_DATA_TEMPLATE, GAMING);
    }

    public static String getNewsQuery() {
        return String.format(JSON_DATA_TEMPLATE, NEWS);
    }

    public static String getMusicQuery() {
        return String.format(JSON_DATA_TEMPLATE, MUSIC);
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

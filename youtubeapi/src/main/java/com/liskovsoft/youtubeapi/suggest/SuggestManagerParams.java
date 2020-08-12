package com.liskovsoft.youtubeapi.suggest;

public class SuggestManagerParams {
    private static final String JSON_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"6.20180913\"," +
            "\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\",\"utcOffsetMinutes\":180," +
            "\"webpSupport\":false,\"animatedWebpSupport\":false,\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"}," +
            "\"acceptRegion\":\"UA\",\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\",\"platform\":\"TV\",\"acceptLanguage\":\"%s\"}," + "\"request" +
            "\":{\"consistencyTokenJars\":[]},\"user\":{\"enableSafetyMode\":false}},%s\"racyCheckOk\":true," +
            "\"contentCheckOk\":true}";
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

    public static String getSuggestionQuery(String videoId) {
        return getSuggestionQuery(videoId, null, null);
    }

    public static String getSuggestionQuery(String videoId, String playlistId, String lang) {
        // always presents
        String videoData = String.format("\"videoId\":\"%s\",", videoId);

        // present only on play lists
        if (playlistId != null) {
            videoData += String.format("\"playlistId\":\"%s\",", playlistId);
        }

        if (lang == null) {
            lang = "en";
        }

        return String.format(JSON_DATA_TEMPLATE, lang, videoData);
    }
}

package com.liskovsoft.youtubeapi.next;

public class WatchNextManagerParams {
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

    public static String getWatchNextQuery(String videoId) {
        return getWatchNextQuery(videoId, null, null);
    }

    public static String getWatchNextQuery(String videoId, String playlistId) {
        return getWatchNextQuery(videoId, playlistId, null);
    }

    /**
     * Video query from playlist example: "params":"OAI%3D","playlistId":"RDx7g_SWE90O8","videoId":"x7g_SWE90O8"<br/>
     * Video query example: "videoId":"x7g_SWE90O8"
     */
    public static String getWatchNextQuery(String videoId, String playlistId, String lang) {
        // always presents
        String videoData = String.format("\"videoId\":\"%s\",", videoId);

        // present only on play lists
        // sometimes "params" present too: "params":"OAI%3D"
        if (playlistId != null) {
            videoData += String.format("\"playlistId\":\"%s\",", playlistId);
        }

        if (lang == null) {
            lang = "en"; // TODO: hardcoded language
        }

        return String.format(JSON_DATA_TEMPLATE, lang, videoData);
    }
}

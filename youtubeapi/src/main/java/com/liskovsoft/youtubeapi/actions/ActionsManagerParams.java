package com.liskovsoft.youtubeapi.actions;

public class ActionsManagerParams {
    // TODO: too many hardcoded params: clientScreenNonce, deviceModel, clientVersion
    private static final String JSON_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"6.20180913\"," +
            "\"screenWidthPoints\":1280,\"screenHeightPoints\":720,\"screenPixelDensity\":1,\"theme\":\"CLASSIC\",\"utcOffsetMinutes\":180," +
            "\"webpSupport\":false,\"animatedWebpSupport\":false,\"tvAppInfo\":{\"appQuality\":\"TV_APP_QUALITY_LIMITED_ANIMATION\"}," +
            "\"acceptRegion\":\"UA\",\"deviceMake\":\"LG\",\"deviceModel\":\"42LA660S-ZA\",\"platform\":\"TV\"}," + "\"request" +
            "\":{\"consistencyTokenJars\":[]},\"user\":{\"enableSafetyMode\":false},\"clientScreenNonce\":\"MC4zODEwOTU4MjI5NDAwMzQ1\"},%s}";

    private static final String VIDEO_ID_TEMPLATE = String.format(JSON_DATA_TEMPLATE, "\"target\":{\"videoId\":\"%s\"}");
    private static final String CHANNEL_ID_TEMPLATE = String.format(JSON_DATA_TEMPLATE, "\"channelIds\":[\"%s\"]");

    public static String getLikeActionQuery(String videoId) {
        return String.format(VIDEO_ID_TEMPLATE, videoId);
    }

    public static String getSubscribeActionQuery(String channelId) {
        return String.format(CHANNEL_ID_TEMPLATE, channelId);
    }
}

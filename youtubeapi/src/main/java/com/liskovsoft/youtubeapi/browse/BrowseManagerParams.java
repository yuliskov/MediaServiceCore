package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;

public class BrowseManagerParams {
    private static final String JSON_POST_DATA_TEMPLATE = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7" +
            ".20190214\",\"webpSupport\":false,\"animatedWebpSupport\":false,\"acceptRegion\":\"US\",\"acceptLanguage\":\"en\"}," +
            "\"user\":{\"enableSafetyMode\":false}}," +
            "%s}";
    private static final String SUBSCRIPTIONS = "\"browseId\":\"FEsubscriptions\"";
    private static final String MY_LIBRARY = "\"browseId\":\"FEmy_youtube\"";
    private static final String HOME = "\"browseId\":\"FEtopics\"";
    private static final String GAMING = "\"browseId\":\"FEtopics\",\"params\":\"-gIGZ2FtaW5n\"";
    private static final String NEWS = "\"browseId\":\"FEtopics\",\"params\":\"-gINaGFwcGVuaW5nX25vdw%3D%3D\"";
    private static final String MUSIC = "\"browseId\":\"FEtopics\",\"params\":\"-gIFbXVzaWM%3D\"";
    private static final String CONTINUATION = "\"continuation\":\"%s\"";

    public static String getHomeQuery() {
        return String.format(JSON_POST_DATA_TEMPLATE, HOME);
    }

    public static String getSubscriptionsQuery() {
        return String.format(JSON_POST_DATA_TEMPLATE, SUBSCRIPTIONS);
    }

    public static String getMyLibraryQuery() {
        return String.format(JSON_POST_DATA_TEMPLATE, MY_LIBRARY);
    }

    public static String getGamingQuery() {
        return String.format(JSON_POST_DATA_TEMPLATE, GAMING);
    }

    public static String getNewsQuery() {
        return String.format(JSON_POST_DATA_TEMPLATE, NEWS);
    }

    public static String getMusicQuery() {
        return String.format(JSON_POST_DATA_TEMPLATE, MUSIC);
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey {@link GridTab#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return String.format(JSON_POST_DATA_TEMPLATE, continuation);
    }
}

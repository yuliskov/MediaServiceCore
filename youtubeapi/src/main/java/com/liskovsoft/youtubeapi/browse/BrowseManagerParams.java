package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.app.AppConstants;
import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.common.locale.LocaleManager;

public class BrowseManagerParams {
    private static final String JSON_POST_DATA_TEMPLATE_OLD = "{\"context\":{\"client\":{\"clientName\":\"TVHTML5\",\"clientVersion\":\"7" +
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
        return createQuery(HOME);
    }

    public static String getSubscriptionsQuery() {
        return createQuery(SUBSCRIPTIONS);
    }

    public static String getMyLibraryQuery() {
        return createQuery(MY_LIBRARY);
    }

    public static String getGamingQuery() {
        return createQuery(GAMING);
    }

    public static String getNewsQuery() {
        return createQuery(NEWS);
    }

    public static String getMusicQuery() {
        return createQuery(MUSIC);
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey {@link GridTab#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return createQuery(continuation);
    }

    private static String createQuery(String template) {
        LocaleManager localeManager = LocaleManager.instance();
        return String.format(AppConstants.JSON_POST_DATA_TEMPLATE, localeManager.getCountry(), localeManager.getLanguage(), template);
    }
}

package com.liskovsoft.youtubeapi.browse;

import com.liskovsoft.youtubeapi.browse.models.grid.GridTab;
import com.liskovsoft.youtubeapi.common.helpers.AppHelper;

public class BrowseManagerParams {
    private static final String SUBSCRIPTIONS = "\"browseId\":\"FEsubscriptions\"";
    private static final String MY_LIBRARY = "\"browseId\":\"FEmy_youtube\"";
    private static final String HOME = "\"browseId\":\"FEtopics\"";
    private static final String GAMING = "\"browseId\":\"FEtopics\",\"params\":\"-gIGZ2FtaW5n\"";
    private static final String NEWS = "\"browseId\":\"FEtopics\",\"params\":\"-gINaGFwcGVuaW5nX25vdw%3D%3D\"";
    private static final String MUSIC = "\"browseId\":\"FEtopics\",\"params\":\"-gIFbXVzaWM%3D\"";
    private static final String CONTINUATION = "\"continuation\":\"%s\"";
    private static final String CHANNEL = "\"browseId\":\"%s\"";

    public static String getHomeQuery() {
        return AppHelper.createQuery(HOME);
    }

    public static String getSubscriptionsQuery() {
        return AppHelper.createQuery(SUBSCRIPTIONS);
    }

    public static String getMyLibraryQuery() {
        return AppHelper.createQuery(MY_LIBRARY);
    }

    public static String getGamingQuery() {
        return AppHelper.createQuery(GAMING);
    }

    public static String getNewsQuery() {
        return AppHelper.createQuery(NEWS);
    }

    public static String getMusicQuery() {
        return AppHelper.createQuery(MUSIC);
    }

    public static String getChannelQuery(String channelId) {
        String channelTemplate = String.format(CHANNEL, channelId);
        return AppHelper.createQuery(channelTemplate);
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey {@link GridTab#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return AppHelper.createQuery(continuation);
    }

    public static String getGuideQuery() {
        return AppHelper.createQuery("");
    }
}

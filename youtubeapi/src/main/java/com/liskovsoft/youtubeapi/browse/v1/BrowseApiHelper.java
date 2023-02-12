package com.liskovsoft.youtubeapi.browse.v1;

import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class BrowseApiHelper {
    private static final String HOME = "\"browseId\":\"default\"";
    private static final String GAMING = "\"browseId\":\"FEtopics_gaming\"";
    private static final String NEWS = "\"browseId\":\"FEtopics_news\"";
    private static final String SPORTS = "\"browseId\":\"FEtopics_sports\"";
    private static final String MUSIC = "\"browseId\":\"FEtopics_music\"";
    private static final String MOVIES = "\"browseId\":\"FEtopics_movies\"";
    private static final String SUBSCRIPTIONS = "\"browseId\":\"FEsubscriptions\"";
    private static final String MY_LIBRARY = "\"browseId\":\"FEmy_youtube\"";
    private static final String HISTORY = "\"browseId\":\"FEhistory\"";
    private static final String LIKED_MUSIC_BROWSE_ID = "FEmusic_liked_videos";
    private static final String SUBSCRIBED_MUSIC_BROWSE_ID = "FEmusic_library_corpus_artists";
    private static final String CONTINUATION = "\"continuation\":\"%s\"";
    private static final String CHANNEL = "\"browseId\":\"%s\"";
    private static final String CHANNEL_FULL = "\"browseId\":\"%s\",\"params\":\"%s\"";
    //private static final String HOME = "\"browseId\":\"FEtopics\"";
    //private static final String GAMING = "\"browseId\":\"FEtopics\",\"params\":\"-gIGZ2FtaW5n\"";
    //private static final String NEWS = "\"browseId\":\"FEtopics\",\"params\":\"-gINaGFwcGVuaW5nX25vdw%3D%3D\"";
    //private static final String MUSIC = "\"browseId\":\"FEtopics\",\"params\":\"-gIFbXVzaWM%3D\"";

    public static String getHomeQuery() {
        return ServiceHelper.createQuery(HOME);
    }

    public static String getSubscriptionsQuery() {
        return ServiceHelper.createQuery(SUBSCRIPTIONS);
    }

    public static String getSubscriptionsQueryWeb() {
        return ServiceHelper.createQueryWeb(SUBSCRIPTIONS);
    }

    public static String getMyLibraryQuery() {
        return ServiceHelper.createQuery(MY_LIBRARY);
    }

    public static String getHistoryQuery() {
        return ServiceHelper.createQuery(HISTORY);
    }

    public static String getGamingQuery() {
        return ServiceHelper.createQuery(GAMING);
    }

    public static String getNewsQuery() {
        return ServiceHelper.createQuery(NEWS);
    }

    public static String getNewsQueryUA() {
        return ServiceHelper.createQueryUA(NEWS);
    }

    public static String getMusicQuery() {
        return ServiceHelper.createQuery(MUSIC);
    }

    public static String getChannelQuery(String channelId) {
        return getChannelQuery(channelId, null);
    }

    public static String getChannelQuery(String channelId, String params) {
        String channelTemplate = params != null ? String.format(CHANNEL_FULL, channelId, params) : String.format(CHANNEL, channelId);
        return ServiceHelper.createQuery(channelTemplate);
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey {@link GridTab#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return ServiceHelper.createQuery(continuation);
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey {@link GridTab#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQueryWeb(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return ServiceHelper.createQueryWeb(continuation);
    }

    public static String getGuideQuery() {
        return ServiceHelper.createQuery("");
    }

    public static boolean isGridChannel(String channelId) {
        return Helpers.equalsAny(channelId, LIKED_MUSIC_BROWSE_ID, SUBSCRIBED_MUSIC_BROWSE_ID);
    }
}

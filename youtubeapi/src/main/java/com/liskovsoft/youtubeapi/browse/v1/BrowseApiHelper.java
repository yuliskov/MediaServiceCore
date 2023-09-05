package com.liskovsoft.youtubeapi.browse.v1;

import com.liskovsoft.youtubeapi.browse.v1.models.grid.GridTab;
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class BrowseApiHelper {
    private static final String HOME = "\"browseId\":\"default\"";
    private static final String KIDS_HOME = "\"browseId\":\"FEkids_home\"";
    private static final String KIDS_HOME_PARAMS = "\"browseId\":\"FEkids_home\",\"params\":\"%s\"";
    private static final String WHAT_TO_WATCH = "\"browseId\":\"FEwhat_to_watch\"";
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
    private static final String PLAYED_MUSIC_BROWSE_ID = "FEmusic_last_played";
    private static final String CONTINUATION = "\"continuation\":\"%s\"";
    private static final String REEL_CONTINUATION = "\"sequenceParams\":\"%s\"";
    private static final String CHANNEL = "\"browseId\":\"%s\"";
    private static final String CHANNEL_FULL = "\"browseId\":\"%s\",\"params\":\"%s\"";
    private static final String CHANNEL_VIDEOS = "\"browseId\":\"%s\",\"params\":\"EgZ2aWRlb3PyBgQKAjoA\"";
    private static final String CHANNEL_LIVE = "\"browseId\":\"%s\",\"params\":\"EgdzdHJlYW1z8gYECgJ6AA%%3D%%3D\"";
    private static final String REEL = "\"disablePlayerResponse\":true,\"inputType\":\"REEL_WATCH_INPUT_TYPE_SEEDLESS\",\"params\":\"CA8%3D\"";
    private static final String REEL_DETAILS = "\"disablePlayerResponse\":true,\"params\":\"%s\",\"playerRequest\":{\"videoId\":\"%s\"}";
    private static final String TRENDING = "\"browseId\":\"FEtrending\",\"params\":\"6gQJRkVleHBsb3Jl\"";
    //private static final String HOME = "\"browseId\":\"FEtopics\"";
    //private static final String GAMING = "\"browseId\":\"FEtopics\",\"params\":\"-gIGZ2FtaW5n\"";
    //private static final String NEWS = "\"browseId\":\"FEtopics\",\"params\":\"-gINaGFwcGVuaW5nX25vdw%3D%3D\"";
    //private static final String MUSIC = "\"browseId\":\"FEtopics\",\"params\":\"-gIFbXVzaWM%3D\"";

    public static String getHomeQuery() {
        return ServiceHelper.createQueryTV(HOME);
    }

    public static String getHomeQueryWeb() {
        return ServiceHelper.createQueryWeb(WHAT_TO_WATCH);
    }

    public static String getHomeQueryMWEB() {
        return ServiceHelper.createQueryMWeb(WHAT_TO_WATCH);
    }

    public static String getTrendingQueryWeb() {
        return ServiceHelper.createQueryWeb(TRENDING);
    }

    public static String getChannelQueryWeb(String browseId, String params) {
        String channelTemplate = params != null ? String.format(CHANNEL_FULL, browseId, params) : String.format(CHANNEL, browseId);
        return ServiceHelper.createQueryWeb(channelTemplate);
    }

    public static String getChannelVideosQueryWeb(String channelId) {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_VIDEOS, channelId));
    }

    public static String getChannelLiveQueryWeb(String channelId) {
        return ServiceHelper.createQueryWeb(String.format(CHANNEL_LIVE, channelId));
    }

    public static String getKidsHomeQuery() {
        return ServiceHelper.createQueryKids(KIDS_HOME);
    }

    public static String getKidsHomeQuery(String params) {
        return ServiceHelper.createQueryKids(String.format(KIDS_HOME_PARAMS, params));
    }

    public static String getSubscriptionsQuery() {
        return ServiceHelper.createQueryTV(SUBSCRIPTIONS);
    }

    public static String getSubscriptionsQueryWeb() {
        return ServiceHelper.createQueryWeb(SUBSCRIPTIONS);
    }

    public static String getMyLibraryQuery() {
        return ServiceHelper.createQueryTV(MY_LIBRARY);
    }

    public static String getHistoryQuery() {
        return ServiceHelper.createQueryTV(HISTORY);
    }

    public static String getGamingQuery() {
        return ServiceHelper.createQueryTV(GAMING);
    }

    public static String getNewsQuery() {
        return ServiceHelper.createQueryTV(NEWS);
    }

    public static String getNewsQueryUA() {
        return ServiceHelper.createQueryTV_UA(NEWS);
    }

    public static String getMusicQuery() {
        return ServiceHelper.createQueryTV(MUSIC);
    }

    public static String getChannelQuery(String channelId) {
        return getChannelQuery(channelId, null);
    }

    public static String getChannelQuery(String channelId, String params) {
        String channelTemplate = params != null ? String.format(CHANNEL_FULL, channelId, params) : String.format(CHANNEL, channelId);
        return ServiceHelper.createQueryTV(channelTemplate);
    }

    /**
     * Get data param for the next search/grid etc
     * @param nextPageKey {@link GridTab#getNextPageKey()}
     * @return data param
     */
    public static String getContinuationQuery(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return ServiceHelper.createQueryTV(continuation);
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
        return ServiceHelper.createQueryTV("");
    }

    public static String getReelQuery() {
        return ServiceHelper.createQueryWeb(REEL);
    }

    public static String getReelDetailsQuery(String videoId, String params) {
        String details = String.format(REEL_DETAILS, params, videoId);
        return ServiceHelper.createQueryWeb(details);
    }

    public static String getReelContinuationQuery(String sequenceParams) {
        String continuation = String.format(REEL_CONTINUATION, sequenceParams);
        return ServiceHelper.createQueryWeb(continuation);
    }

    public static String getReelContinuation2Query(String nextPageKey) {
        String continuation = String.format(CONTINUATION, nextPageKey);
        return ServiceHelper.createQueryWeb(continuation);
    }

    public static boolean isGridChannel(String channelId) {
        //return Helpers.equalsAny(channelId, LIKED_MUSIC_BROWSE_ID, SUBSCRIBED_MUSIC_BROWSE_ID, PLAYED_MUSIC_BROWSE_ID);
        // NOTE: user channel starts with 'UC'
        return channelId != null && channelId.startsWith("FE");
    }
}

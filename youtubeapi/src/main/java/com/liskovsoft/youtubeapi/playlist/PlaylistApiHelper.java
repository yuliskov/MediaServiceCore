package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper;

public class PlaylistApiHelper {
    private static final String PLAYLISTS_INFO_QUERY = "\"videoIds\":[\"%s\"]";
    private static final String ADD_TO_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"addedVideoId\":\"%s\",\"action\":\"ACTION_ADD_VIDEO\"}]";
    private static final String PLAYLIST_ORDER_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"playlistVideoOrder\":\"%s\",\"action\":\"ACTION_SET_PLAYLIST_VIDEO_ORDER\"}]";
    private static final String REMOVE_FROM_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"removedVideoId\":\"%s\",\"action\":\"ACTION_REMOVE_VIDEO_BY_VIDEO_ID\"}]";
    private static final String RENAME_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"playlistName\":\"%s\",\"action\":\"ACTION_SET_PLAYLIST_NAME\"}]";
    private static final String SAVE_REMOVE_PLAYLIST_QUERY = "\"target\":{\"playlistId\":\"%s\"}";
    private static final String CREATE_PLAYLIST_QUERY = "\"title\":\"%s\"";
    private static final String CREATE_PLAYLIST_AND_ADD_QUERY = "\"title\":\"%s\",\"videoIds\":[\"%s\"]";
    private static final String DELETE_PLAYLIST_QUERY = "\"playlistId\":\"%s\"";

    public static String getPlaylistsInfoQuery(String videoId) {
        String queryTemplate = String.format(PLAYLISTS_INFO_QUERY, videoId);
        return PostDataHelper.createQueryTV(queryTemplate);
    }

    public static String getAddToPlaylistQuery(String playlistId, String videoId) {
        String queryTemplate = String.format(ADD_TO_PLAYLISTS_QUERY, playlistId, videoId);
        return PostDataHelper.createQueryTV(queryTemplate);
    }

    public static String getRemoveFromPlaylistsQuery(String playlistId, String videoId) {
        String queryTemplate = String.format(REMOVE_FROM_PLAYLISTS_QUERY, playlistId, videoId);
        return PostDataHelper.createQueryTV(queryTemplate);
    }

    public static String getRenamePlaylistsQuery(String playlistId, String newName) {
        String queryTemplate = String.format(RENAME_PLAYLISTS_QUERY, playlistId, newName);
        return PostDataHelper.createQueryTV(queryTemplate);
    }

    public static String getPlaylistOrderQuery(String playlistId, int playlistOrder) {
        String queryTemplate = String.format(PLAYLIST_ORDER_QUERY, playlistId, playlistOrder);
        return PostDataHelper.createQueryTV(queryTemplate);
    }

    public static String getSaveRemoveForeignPlaylistQuery(String playlistId) {
        String queryTemplate = String.format(SAVE_REMOVE_PLAYLIST_QUERY, playlistId);
        return PostDataHelper.createQueryTV(queryTemplate);
    }

    public static String getCreatePlaylistQuery(String playlistName, String videoId) {
        String queryTemplate = videoId == null ?
                String.format(CREATE_PLAYLIST_QUERY, playlistName) : String.format(CREATE_PLAYLIST_AND_ADD_QUERY, playlistName, videoId);
        return PostDataHelper.createQueryWeb(queryTemplate);
    }

    public static String getRemovePlaylistQuery(String playlistName) {
        String queryTemplate = String.format(DELETE_PLAYLIST_QUERY, playlistName);
        return PostDataHelper.createQueryWeb(queryTemplate);
    }
}

package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper;

public class PlaylistManagerParams {
    private static final String PLAYLISTS_QUERY = "\"videoIds\":[\"%s\"]";
    private static final String ADD_TO_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"addedVideoId\":\"%s\",\"action\":\"ACTION_ADD_VIDEO\"}]";
    private static final String REMOVE_FROM_PLAYLISTS_QUERY = "\"playlistId\":\"%s\"," +
            "\"actions\":[{\"removedVideoId\":\"%s\",\"action\":\"ACTION_REMOVE_VIDEO_BY_VIDEO_ID\"}]";

    public static String getAllPlaylistsQuery(String videoId) {
        String channelTemplate = String.format(PLAYLISTS_QUERY, videoId);
        return ServiceHelper.createQuery(channelTemplate);
    }

    public static String getAddToPlaylistQuery(String playlistId, String videoId) {
        String channelTemplate = String.format(ADD_TO_PLAYLISTS_QUERY, playlistId, videoId);
        return ServiceHelper.createQuery(channelTemplate);
    }

    public static String getRemoveFromPlaylistsQuery(String playlistId, String videoId) {
        String channelTemplate = String.format(REMOVE_FROM_PLAYLISTS_QUERY, playlistId, videoId);
        return ServiceHelper.createQuery(channelTemplate);
    }
}

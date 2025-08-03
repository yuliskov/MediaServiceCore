package com.liskovsoft.youtubeapi.next.v1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper;

public class WatchNextApiHelper {
    public static String getWatchNextQuery(@NonNull String videoId) {
        return getWatchNextQuery(videoId, null, 0);
    }

    public static String getWatchNextQuery(@Nullable String videoId, @Nullable String playlistId, int playlistIndex) {
        return getWatchNextQuery(videoId, playlistId, playlistIndex, null);
    }

    /**
     * Video query from playlist example: "params":"OAI%3D","playlistId":"RDx7g_SWE90O8","playlistIndex": 2,"videoId":"x7g_SWE90O8"<br/>
     * Video query example: "videoId":"x7g_SWE90O8"<br/>
     * PlaylistParams is used inside browse channel queries
     */
    public static String getWatchNextQuery(@Nullable String videoId, @Nullable String playlistId, int playlistIndex, String playlistParams) {
        // always presents
        String videoData = String.format("\"videoId\":\"%s\"", videoId);

        // present only on play lists
        // sometimes "params" present too: "params":"OAI%3D"
        if (playlistId != null) {
            videoData += String.format(",\"playlistId\":\"%s\",\"playlistIndex\":%s", playlistId, Math.max(playlistIndex, 0));
        }

        if (playlistParams != null) {
            videoData += String.format(",\"params\":\"%s\"", playlistParams);
        }

        return PostDataHelper.createQueryTV(videoData);
    }
}

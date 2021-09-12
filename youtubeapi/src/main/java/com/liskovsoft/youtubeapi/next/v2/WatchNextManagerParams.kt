package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

object WatchNextManagerParams {
    fun getWatchNextQuery(videoId: String): String {
        return getWatchNextQuery(videoId, null, 0)
    }

    fun getWatchNextQuery(videoId: String?, playlistId: String?, playlistIndex: Int): String {
        return getWatchNextQuery(videoId, playlistId, playlistIndex, null)
    }

    /**
     * Video query from playlist example: "params":"OAI%3D","playlistId":"RDx7g_SWE90O8","playlistIndex": 2,"videoId":"x7g_SWE90O8"<br></br>
     * Video query example: "videoId":"x7g_SWE90O8"<br></br>
     * PlaylistParams is used inside browse channel queries
     */
    fun getWatchNextQuery(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): String {
        // always presents
        var videoData = String.format("\"videoId\":\"%s\"", videoId)

        // present only on play lists
        // sometimes "params" present too: "params":"OAI%3D"
        if (playlistId != null) {
            videoData += String.format(",\"playlistId\":\"%s\",\"playlistIndex\":%s", playlistId, playlistIndex.coerceAtLeast(0))
        }
        if (playlistParams != null) {
            videoData += String.format(",\"params\":\"%s\"", playlistParams)
        }
        return ServiceHelper.createQuery(videoData)
    }
}
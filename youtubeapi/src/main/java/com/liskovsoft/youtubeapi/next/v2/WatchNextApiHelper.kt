package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal object WatchNextApiHelper {
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
        // To load next data by index, videoId should be empty
        var videoData = String.format("\"videoId\":\"%s\"", videoId ?: "")

        // Presents only on playlists
        // Note, that negative playlistIndex values produce error
        if (playlistId != null) {
            videoData += String.format(",\"playlistId\":\"%s\",\"playlistIndex\":%s", playlistId, playlistIndex.coerceAtLeast(0))
        }

        // Sometimes "params" presents too. E.g.: "params":"OAI%3D"
        if (playlistParams != null) {
            videoData += String.format(",\"params\":\"%s\"", playlistParams)
        }

        return ServiceHelper.createQueryTV(videoData)
    }

    fun getUnlocalizedTitleQuery(videoId: String): String {
        return "https://www.youtube.com/watch?v=$videoId"
    }
}
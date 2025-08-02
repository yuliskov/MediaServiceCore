package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.common.helpers.PostDataType
import com.liskovsoft.youtubeapi.common.helpers.QueryBuilder

internal object WatchNextApiHelper {
    fun getWatchNextQuery(client: AppClient, videoId: String): String {
        return getWatchNextQuery(client, videoId, null, 0)
    }

    fun getWatchNextQuery(appClient: AppClient, videoId: String?, playlistId: String?, playlistIndex: Int): String {
        return getWatchNextQuery(appClient, videoId, playlistId, playlistIndex, null)
    }

    /**
     * Video query from playlist example: "params":"OAI%3D","playlistId":"RDx7g_SWE90O8","playlistIndex": 2,"videoId":"x7g_SWE90O8"<br></br>
     *
     * Video query example: "videoId":"x7g_SWE90O8"<br></br>
     *
     * PlaylistParams is used inside browse channel queries<br/>
     *
     * NOTE: To load next data by index, videoId should be empty<br/>
     *
     * NOTE: Negative playlistIndex values produce error
     *
     * NOTE: Sometimes "params" presents too. E.g.: "params":"OAI%3D"
     */
    fun getWatchNextQuery(client: AppClient, videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): String {
        return QueryBuilder(client)
            .setType(PostDataType.Browse)
            .setVideoId(videoId ?: "") // To load next data by index, videoId should be empty
            .setPlaylistId(playlistId)
            .setPlaylistIndex(playlistIndex)
            .setParams(playlistParams) // Sometimes "params" presents too. E.g.: "params":"OAI%3D"
            .build()
    }

    fun getUnlocalizedTitleQuery(videoId: String): String {
        return "https://www.youtube.com/watch?v=$videoId"
    }
}
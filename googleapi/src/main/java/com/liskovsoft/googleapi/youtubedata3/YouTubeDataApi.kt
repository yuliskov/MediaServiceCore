package com.liskovsoft.googleapi.youtubedata3

import com.liskovsoft.googleapi.common.ApiKeys
import com.liskovsoft.googleapi.youtubedata3.data.SnippetResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

internal interface YouTubeDataApi {
    @GET("https://www.googleapis.com/youtube/v3/channels?part=snippet&key=${ApiKeys.YOUTUBE_DATA_API_KEY}")
    fun getChannelMetadata(@Query("id") ids: String): Call<SnippetResponse?>

    @GET("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails&key=${ApiKeys.YOUTUBE_DATA_API_KEY}")
    fun getVideoMetadata(@Query("id") ids: String): Call<SnippetResponse?>

    @GET("https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails&key=${ApiKeys.YOUTUBE_DATA_API_KEY}")
    fun getPlaylistMetadata(@Query("id") ids: String): Call<SnippetResponse?>
}
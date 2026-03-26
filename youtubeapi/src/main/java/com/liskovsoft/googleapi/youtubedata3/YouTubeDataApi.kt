package com.liskovsoft.googleapi.youtubedata3

import com.liskovsoft.googleapi.youtubedata3.data.SnippetResponse
import com.liskovsoft.googlecommon.common.constants.ConstantsService
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

@WithGson
internal interface YouTubeDataApi {
    @GET("https://www.googleapis.com/youtube/v3/channels?part=snippet")
    fun getChannelMetadata(@Query("id") ids: String, @Query("key") key: String? = getKey()): Call<SnippetResponse?>

    @GET("https://www.googleapis.com/youtube/v3/videos?part=snippet,contentDetails")
    fun getVideoMetadata(@Query("id") ids: String, @Query("key") key: String? = getKey()): Call<SnippetResponse?>

    @GET("https://www.googleapis.com/youtube/v3/playlists?part=snippet,contentDetails")
    fun getPlaylistMetadata(@Query("id") ids: String, @Query("key") key: String? = getKey()): Call<SnippetResponse?>

    private fun getKey(): String? = ConstantsService.constants?.youtubeDataApiKey
}
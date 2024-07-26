package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPathClass;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
@JsonPathClass
public interface PlaylistApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/playlist/get_add_to_playlist")
    Call<PlaylistsResult> getPlaylistsInfo(@Body String playlistsInfoQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse/edit_playlist")
    Call<ActionResult> editPlaylist(@Body String editPlaylistQuery);

    /**
     * Works with foreign playlists
     */
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/like/like")
    Call<ActionResult> savePlaylist(@Body String saveRemovePlaylistQuery);

    /**
     * Works with foreign playlists
     */
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/like/removelike")
    Call<ActionResult> removePlaylist(@Body String saveRemovePlaylistQuery);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/playlist/create")
    Call<ActionResult> createPlaylist(@Body String createPlaylistQuery);

    /**
     * Works with user playlists
     */
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/playlist/delete")
    Call<ActionResult> deletePlaylist(@Body String deletePlaylistQuery);
}

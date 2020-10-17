package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsInfo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * For signed users!
 */
public interface PlaylistManager {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/playlist/get_add_to_playlist")
    Call<PlaylistsInfo> getPlaylistsInfo(@Body String playlistQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse/edit_playlist")
    Call<ActionResult> addToPlaylist(@Body String playlistQuery, @Header("Authorization") String auth);

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/browse/edit_playlist")
    Call<ActionResult> removeFromPlaylist(@Body String playlistQuery, @Header("Authorization") String auth);
}

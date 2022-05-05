package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import retrofit2.Call;

public class PlaylistService {
    private static PlaylistService sInstance;
    private final PlaylistManager mPlaylistManager;

    private PlaylistService() {
        mPlaylistManager = RetrofitHelper.withJsonPath(PlaylistManager.class);
    }

    public static PlaylistService instance() {
        if (sInstance == null) {
            sInstance = new PlaylistService();
        }

        return sInstance;
    }

    public PlaylistsResult getPlaylistsInfo(String videoId, String authorization) {
        Call<PlaylistsResult> wrapper =
                mPlaylistManager.getPlaylistsInfo(PlaylistManagerParams.getPlaylistsInfoQuery(videoId), authorization);

        return RetrofitHelper.get(wrapper);
    }

    public void addToPlaylist(String playlistId, String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistManagerParams.getAddToPlaylistQuery(playlistId, videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeFromPlaylist(String playlistId, String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistManagerParams.getRemoveFromPlaylistsQuery(playlistId, videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void savePlaylist(String playlistId, String authorization) {
        Call<ActionResult> wrapper =
                mPlaylistManager.savePlaylist(PlaylistManagerParams.getSaveRemovePlaylistQuery(playlistId), authorization);

        ActionResult result = RetrofitHelper.get(wrapper);

        if (result == null) {
            throw new IllegalStateException("Can't savePlaylist. Unknown error.");
        }
    }

    public void removePlaylist(String playlistId, String authorization) {
        // Try to remove foreign playlist first
        Call<ActionResult> removeWrapper =
                mPlaylistManager.removePlaylist(PlaylistManagerParams.getSaveRemovePlaylistQuery(playlistId), authorization);
        ActionResult removeResult = RetrofitHelper.get(removeWrapper);

        // Then, delete user playlist
        Call<ActionResult> deleteWrapper =
                mPlaylistManager.deletePlaylist(PlaylistManagerParams.getDeletePlaylistQuery(playlistId), authorization);
        ActionResult deleteResult = RetrofitHelper.get(deleteWrapper);

        if (removeResult == null && deleteResult == null) {
            throw new IllegalStateException("Can't removePlaylist. Unknown error.");
        }
    }

    public void createPlaylist(String playlistName, String videoId, String authorization) {
        Call<ActionResult> wrapper =
                mPlaylistManager.createPlaylist(PlaylistManagerParams.getCreatePlaylistQuery(playlistName, videoId), authorization);

        RetrofitHelper.get(wrapper); // ignore result
    }
}

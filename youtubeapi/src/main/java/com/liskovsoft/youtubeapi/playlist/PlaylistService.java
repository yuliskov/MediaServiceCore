package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import retrofit2.Call;

public class PlaylistService {
    private static PlaylistService sInstance;
    private final PlaylistApi mPlaylistManager;

    private PlaylistService() {
        mPlaylistManager = RetrofitHelper.withJsonPath(PlaylistApi.class);
    }

    public static PlaylistService instance() {
        if (sInstance == null) {
            sInstance = new PlaylistService();
        }

        return sInstance;
    }

    public PlaylistsResult getPlaylistsInfo(String videoId) {
        Call<PlaylistsResult> wrapper =
                mPlaylistManager.getPlaylistsInfo(PlaylistApiHelper.getPlaylistsInfoQuery(videoId));

        return RetrofitHelper.get(wrapper);
    }

    public void addToPlaylist(String playlistId, String videoId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getAddToPlaylistQuery(playlistId, videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void removeFromPlaylist(String playlistId, String videoId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getRemoveFromPlaylistsQuery(playlistId, videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    public void renamePlaylist(String playlistId, String newName) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getRenamePlaylistsQuery(playlistId, newName));

        ActionResult result = RetrofitHelper.get(wrapper);

        if (result == null) {
            throw new IllegalStateException("Can't renamePlaylist. Unknown error.");
        }
    }

    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getPlaylistOrderQuery(playlistId, playlistOrder));

        ActionResult result = RetrofitHelper.get(wrapper);

        if (result == null) {
            throw new IllegalStateException("Can't setPlaylistOrder. Unknown error.");
        }
    }

    public void savePlaylist(String playlistId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.savePlaylist(PlaylistApiHelper.getSaveRemovePlaylistQuery(playlistId));

        ActionResult result = RetrofitHelper.get(wrapper);

        if (result == null) {
            throw new IllegalStateException("Can't savePlaylist. Unknown error.");
        }
    }

    public void removePlaylist(String playlistId) {
        // Try to remove foreign playlist first
        Call<ActionResult> removeWrapper =
                mPlaylistManager.removePlaylist(PlaylistApiHelper.getSaveRemovePlaylistQuery(playlistId));
        ActionResult removeResult = RetrofitHelper.get(removeWrapper);

        // Then, delete user playlist
        Call<ActionResult> deleteWrapper =
                mPlaylistManager.deletePlaylist(PlaylistApiHelper.getDeletePlaylistQuery(playlistId));
        ActionResult deleteResult = RetrofitHelper.get(deleteWrapper);

        if (removeResult == null && deleteResult == null) {
            throw new IllegalStateException("Can't removePlaylist. Unknown error.");
        }
    }

    public void createPlaylist(String playlistName, String videoId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.createPlaylist(PlaylistApiHelper.getCreatePlaylistQuery(playlistName, videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }
}

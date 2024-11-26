package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import retrofit2.Call;

public class PlaylistService {
    private static PlaylistService sInstance;
    private final PlaylistApi mPlaylistManager;

    private PlaylistService() {
        mPlaylistManager = RetrofitHelper.create(PlaylistApi.class);
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

        RetrofitHelper.getWithErrors(wrapper);
    }

    public void setPlaylistOrder(String playlistId, int playlistOrder) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getPlaylistOrderQuery(playlistId, playlistOrder));

        RetrofitHelper.getWithErrors(wrapper);
    }

    public void savePlaylist(String playlistId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.saveForeignPlaylist(PlaylistApiHelper.getSaveRemoveForeignPlaylistQuery(playlistId));

        RetrofitHelper.getWithErrors(wrapper);
    }

    public void removePlaylist(String playlistId) {
        // Try to remove foreign playlist first
        Call<ActionResult> removeWrapper =
                mPlaylistManager.removeForeignPlaylist(PlaylistApiHelper.getSaveRemoveForeignPlaylistQuery(playlistId));
        RetrofitHelper.getWithErrors(removeWrapper);

        // Then, delete user playlist
        Call<ActionResult> deleteWrapper =
                mPlaylistManager.removePlaylist(PlaylistApiHelper.getRemovePlaylistQuery(playlistId));
        RetrofitHelper.getWithErrors(deleteWrapper);
    }

    public void createPlaylist(String playlistName, String videoId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.createPlaylist(PlaylistApiHelper.getCreatePlaylistQuery(playlistName, videoId));

        RetrofitHelper.getWithErrors(wrapper); // ignore result
    }
}

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
                mPlaylistManager.getPlaylistsInfo(PlaylistManagerParams.getAllPlaylistsQuery(videoId), authorization);

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
}

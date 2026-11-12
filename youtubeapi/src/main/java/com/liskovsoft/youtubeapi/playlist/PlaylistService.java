package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.actions.models.ActionResult;
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper;
import com.liskovsoft.youtubeapi.playlist.impl.YouTubePlaylistInfo;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;

import java.util.List;

import retrofit2.Call;

public class PlaylistService {
    private static final String STATUS_SUCCEEDED = "STATUS_SUCCEEDED";
    private static final String CHANNEL_ID_PREFIX = "UC";
    // Playlist that holds the regular videos of a channel (no Shorts, no live streams)
    private static final String CHANNEL_VIDEOS_PLAYLIST_PREFIX = "UULF";
    private final PlaylistApi mPlaylistManager;

    public PlaylistService() {
        mPlaylistManager = RetrofitHelper.create(PlaylistApi.class);
    }

    public List<PlaylistInfo> getPlaylistsInfo(String videoId) {
        Call<PlaylistsResult> wrapper =
                mPlaylistManager.getPlaylistsInfo(PlaylistApiHelper.getPlaylistsInfoQuery(videoId));

        return YouTubePlaylistInfo.from(RetrofitHelper.get(wrapper));
    }

    public void addToPlaylist(String playlistId, String videoId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getAddToPlaylistQuery(playlistId, videoId));

        RetrofitHelper.get(wrapper); // ignore result
    }

    /**
     * Adds all videos of the source playlist in a single request.<br/>
     * Videos that already exist in the target playlist are skipped.
     */
    public void addPlaylistToPlaylist(String playlistId, String sourcePlaylistId) {
        Call<ActionResult> wrapper =
                mPlaylistManager.editPlaylist(PlaylistApiHelper.getAddPlaylistToPlaylistQuery(playlistId, sourcePlaylistId));

        ActionResult result = RetrofitHelper.getWithErrors(wrapper);
        String status = result != null ? result.getStatus() : null;

        if (!Helpers.equals(status, STATUS_SUCCEEDED)) {
            throw new IllegalStateException("Can't add playlist to playlist. Status: " + status);
        }
    }

    /**
     * Adds all regular videos of the channel (no Shorts, no live streams) in a single request.
     */
    public void addChannelToPlaylist(String playlistId, String channelId) {
        if (!Helpers.startsWith(channelId, CHANNEL_ID_PREFIX)) {
            throw new IllegalStateException("Unsupported channel id: " + channelId);
        }

        addPlaylistToPlaylist(playlistId, CHANNEL_VIDEOS_PLAYLIST_PREFIX + channelId.substring(CHANNEL_ID_PREFIX.length()));
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
        if (playlistId.length() < 5) {
            throw new IllegalStateException("Built-in playlists cannot be removed");
        }

        try {
            // Try to remove foreign playlist first
            removeForeignPlaylist(playlistId);
        } catch (IllegalStateException e) {
            // Then, delete user playlist
            removeUserPlaylist(playlistId);
        }
    }

    private void removeForeignPlaylist(String playlistId) {
        Call<ActionResult> removeWrapper =
                mPlaylistManager.removeForeignPlaylist(PlaylistApiHelper.getSaveRemoveForeignPlaylistQuery(playlistId));
        RetrofitHelper.getWithErrors(removeWrapper);
    }

    private void removeUserPlaylist(String playlistId) {
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

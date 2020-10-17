package com.liskovsoft.youtubeapi.playlist.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class PlaylistsInfo {
    @JsonPath("$.contents[0].addToPlaylistRenderer.playlists[*].playlistAddToOptionRenderer")
    private List<Playlist> mPlaylists;

    public List<Playlist> getPlaylists() {
        return mPlaylists;
    }
}

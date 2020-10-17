package com.liskovsoft.youtubeapi.playlist.models;

import com.liskovsoft.youtubeapi.common.converters.jsonpath.JsonPath;

import java.util.List;

public class PlaylistsResult {
    @JsonPath("$.contents[0].addToPlaylistRenderer.playlists[*].playlistAddToOptionRenderer")
    private List<PlaylistInfo> mPlaylists;

    public List<PlaylistInfo> getPlaylists() {
        return mPlaylists;
    }
}

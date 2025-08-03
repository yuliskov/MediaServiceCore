package com.liskovsoft.youtubeapi.playlist.models;

import com.liskovsoft.googlecommon.common.converters.jsonpath.JsonPath;

import java.util.List;

public class PlaylistsResult {
    @JsonPath("$.contents[0].addToPlaylistRenderer.playlists[*].playlistAddToOptionRenderer")
    private List<PlaylistInfoItem> mPlaylists;

    public List<PlaylistInfoItem> getPlaylists() {
        return mPlaylists;
    }
}

package com.liskovsoft.youtubeapi.playlist;

public class PlaylistServiceWrapper extends PlaylistService {
    private static PlaylistServiceWrapper sInstance;

    public static PlaylistServiceWrapper instance() {
        if (sInstance == null) {
            sInstance = new PlaylistServiceWrapper();
        }

        return sInstance;
    }

    @Override
    public void createPlaylist(String playlistName, String videoId) {
        super.createPlaylist(playlistName, videoId);
    }
}

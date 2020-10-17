package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.VideoPlaylistInfo;
import com.liskovsoft.youtubeapi.playlist.models.Playlist;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsInfo;

import java.util.ArrayList;
import java.util.List;

public class YouTubeVideoPlaylistInfo implements VideoPlaylistInfo {
    private String mTitle;
    private String mPlaylistId;
    private boolean mIsSelected;

    public static List<VideoPlaylistInfo> from(PlaylistsInfo info) {
        List<VideoPlaylistInfo> result = new ArrayList<>();

        for (Playlist playlist : info.getPlaylists()) {
            result.add(from(playlist));
        }

        return result;
    }

    private static YouTubeVideoPlaylistInfo from(Playlist playlist) {
        YouTubeVideoPlaylistInfo info = new YouTubeVideoPlaylistInfo();

        info.mTitle = playlist.getTitle();
        info.mPlaylistId = playlist.getPlaylistId();
        info.mIsSelected = playlist.isSelected();

        return info;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getPlaylistId() {
        return mPlaylistId;
    }

    @Override
    public boolean isSelected() {
        return mIsSelected;
    }
}

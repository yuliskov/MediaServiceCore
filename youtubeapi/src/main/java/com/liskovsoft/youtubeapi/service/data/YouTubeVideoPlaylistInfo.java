package com.liskovsoft.youtubeapi.service.data;

import com.liskovsoft.mediaserviceinterfaces.data.VideoPlaylistInfo;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistInfo;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;

import java.util.ArrayList;
import java.util.List;

public class YouTubeVideoPlaylistInfo implements VideoPlaylistInfo {
    private String mTitle;
    private String mPlaylistId;
    private boolean mIsSelected;

    public static List<VideoPlaylistInfo> from(PlaylistsResult info) {
        if (info == null) {
            return null;
        }

        List<VideoPlaylistInfo> result = new ArrayList<>();

        for (PlaylistInfo playlist : info.getPlaylists()) {
            result.add(from(playlist));
        }

        return result;
    }

    private static YouTubeVideoPlaylistInfo from(PlaylistInfo playlist) {
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

    @Override
    public int getSize() {
        return -1;
    }

    @Override
    public int getCurrentIndex() {
        return -1;
    }

    public void setSelected(boolean selected) {
        mIsSelected = selected;
    }
}

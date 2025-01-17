package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.googleapi.youtubedata3.YouTubeDataServiceInt;
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl;

import java.util.Collections;
import java.util.List;

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
        try {
            super.createPlaylist(playlistName, videoId);
        } catch (IllegalStateException e) {
            List<ItemMetadata> metadata = YouTubeDataServiceInt.getVideoMetadata(videoId);
            if (metadata != null && !metadata.isEmpty()) {
                ItemMetadata info = metadata.get(0);
                ItemGroup playlist = PlaylistGroupServiceImpl.createPlaylistGroup(playlistName, info.getCardImageUrl(),
                        Collections.singletonList(
                                new ItemImpl(info.getChannelId(), info.getTitle(), info.getCardImageUrl(), info.getVideoId(), info.getSecondTitle())));
                PlaylistGroupServiceImpl.addPlaylistGroup(playlist);
            }
        }
    }

    @Override
    public PlaylistsResult getPlaylistsInfo(String videoId) {
        return super.getPlaylistsInfo(videoId);
    }
}

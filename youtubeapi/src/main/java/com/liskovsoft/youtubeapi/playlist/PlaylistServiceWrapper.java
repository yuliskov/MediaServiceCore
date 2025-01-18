package com.liskovsoft.youtubeapi.playlist;

import com.liskovsoft.googleapi.youtubedata3.YouTubeDataServiceInt;
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup;
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImplKt;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl;
import com.liskovsoft.youtubeapi.playlist.impl.YouTubePlaylistInfo;
import com.liskovsoft.youtubeapi.playlist.models.PlaylistsResult;
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl;

import java.util.ArrayList;
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
            // NOP
        }
        
        createCachedPlaylist(playlistName, videoId);
    }

    private static void createCachedPlaylist(String playlistName, String videoId) {
        List<ItemMetadata> metadata = YouTubeDataServiceInt.getVideoMetadata(videoId);
        if (metadata != null && !metadata.isEmpty()) {
            ItemMetadata info = metadata.get(0);
            ItemGroup playlist = PlaylistGroupServiceImpl.createPlaylistGroup(playlistName, info.getCardImageUrl(),
                    Collections.singletonList(
                            new ItemImpl(info.getChannelId(), info.getTitle(), info.getCardImageUrl(), info.getVideoId(), info.getSecondTitle())));
            PlaylistGroupServiceImpl.addPlaylistGroup(playlist);
        }
    }

    @Override
    public List<PlaylistInfo> getPlaylistsInfo(String videoId) {
        return getCachedPlaylistInfo(super.getPlaylistsInfo(videoId), videoId);
    }

    private List<PlaylistInfo> getCachedPlaylistInfo(List<PlaylistInfo> playlistsInfo, String videoId) {
        List<ItemGroup> playlistGroups = PlaylistGroupServiceImpl.getPlaylistGroups();
        if (!playlistGroups.isEmpty()) {
            List<PlaylistInfo> result = new ArrayList<>();

            if (playlistsInfo != null && !playlistsInfo.isEmpty()) {
                result.add(playlistsInfo.get(0)); // WatchLater
            }

            for (ItemGroup itemGroup : playlistGroups) {
                result.add(YouTubePlaylistInfo.from(itemGroup, itemGroup.contains(videoId)));
            }

            if (playlistsInfo != null && !playlistsInfo.isEmpty()) {
                result.addAll(playlistsInfo.subList(1, playlistsInfo.size())); // exclude WatchLater
            }

            return result;
        }

        return playlistsInfo;
    }

    @Override
    public void addToPlaylist(String playlistId, String videoId) {
        super.addToPlaylist(playlistId, videoId);

        addToCachedPlaylist(playlistId, videoId);
    }

    private static void addToCachedPlaylist(String playlistId, String videoId) {
        ItemGroup playlistGroup = PlaylistGroupServiceImpl.findPlaylistGroup(Helpers.parseInt(playlistId));

        if (playlistGroup == null) {
            return;
        }

        List<ItemMetadata> metadata = YouTubeDataServiceInt.getVideoMetadata(videoId);

        if (metadata != null && !metadata.isEmpty()) {
            ItemMetadata item = metadata.get(0);
            playlistGroup.add(new ItemImpl(item.getChannelId(), item.getTitle(), item.getCardImageUrl(), item.getVideoId(), item.getSecondTitle()));
            PlaylistGroupServiceImpl.addPlaylistGroup(playlistGroup); // move to the top
        }
    }

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        try {
            super.renamePlaylist(playlistId, newName);
        } catch (IllegalStateException e) {
            // NOP
        }

        renameCachedPlaylist(playlistId, newName);
    }

    private static void renameCachedPlaylist(String playlistId, String newName) {
        ItemGroup playlistGroup = PlaylistGroupServiceImpl.findPlaylistGroup(Helpers.parseInt(playlistId));

        if (playlistGroup != null) {
            PlaylistGroupServiceImpl.renamePlaylistGroup(playlistGroup, newName);
        }
    }
}

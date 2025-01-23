package com.liskovsoft.youtubeapi.playlist;

import androidx.annotation.Nullable;

import com.liskovsoft.googleapi.youtubedata3.YouTubeDataServiceInt;
import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup;
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl;
import com.liskovsoft.youtubeapi.playlist.impl.YouTubePlaylistInfo;
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlaylistServiceWrapper extends PlaylistService {
    private static PlaylistServiceWrapper sInstance;
    private @Nullable List<PlaylistInfo> mCachedPlaylistInfos;

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
                    Collections.singletonList(ItemImpl.fromMetadata(info)));
            PlaylistGroupServiceImpl.addPlaylistGroup(playlist);
        }
    }

    @Override
    public List<PlaylistInfo> getPlaylistsInfo(String videoId) {
        return getCachedPlaylistInfo(super.getPlaylistsInfo(videoId), videoId);
    }

    private List<PlaylistInfo> getCachedPlaylistInfo(List<PlaylistInfo> playlistsInfos, String videoId) {
        List<ItemGroup> playlistGroups = PlaylistGroupServiceImpl.getPlaylistGroups();
        if (!playlistGroups.isEmpty()) {
            mCachedPlaylistInfos = playlistsInfos;
            List<PlaylistInfo> result = new ArrayList<>();

            if (playlistsInfos != null && !playlistsInfos.isEmpty()) {
                result.add(playlistsInfos.get(0)); // WatchLater
            }

            for (ItemGroup itemGroup : playlistGroups) {
                // Merge local and remote
                if (playlistsInfos != null && !playlistsInfos.isEmpty()) {
                    PlaylistInfo item = findFirst(playlistsInfos, itemGroup.getTitle()); // More robust to find by id?
                    if (item != null) {
                        if (!result.contains(item)) {
                            result.add(item);
                        }
                        continue;
                    }
                }

                result.add(YouTubePlaylistInfo.from(itemGroup, itemGroup.contains(videoId)));
            }
            
            if (playlistsInfos != null && !playlistsInfos.isEmpty()) {
                for (PlaylistInfo info : playlistsInfos) {
                    if (!result.contains(info)) {
                        result.add(info);
                    }
                }
            }

            return result;
        }

        return playlistsInfos;
    }

    @Override
    public void addToPlaylist(String playlistId, String videoId) {
        super.addToPlaylist(playlistId, videoId);

        addToCachedPlaylist(playlistId, videoId);
    }

    private void addToCachedPlaylist(String playlistId, String videoId) {
        ItemGroup playlistGroup = PlaylistGroupServiceImpl.findPlaylistGroup(playlistId);

        if (playlistGroup == null) {
            String title = findTitle(playlistId);

            if (title == null) {
                return;
            }

            playlistGroup = PlaylistGroupServiceImpl.createPlaylistGroup(
                    playlistId, title, (String) null);
        }

        List<ItemMetadata> metadata = YouTubeDataServiceInt.getVideoMetadata(videoId);

        if (metadata != null && !metadata.isEmpty()) {
            ItemMetadata item = metadata.get(0);
            playlistGroup.add(ItemImpl.fromMetadata(item));
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
        ItemGroup playlistGroup = PlaylistGroupServiceImpl.findPlaylistGroup(playlistId);

        if (playlistGroup != null) {
            PlaylistGroupServiceImpl.renamePlaylistGroup(playlistGroup, newName);
        }
    }

    @Override
    public void removePlaylist(String playlistId) {
        try {
            super.removePlaylist(playlistId);
        } catch (IllegalStateException e) {
            // NOP
        }

        PlaylistGroupServiceImpl.removePlaylistGroup(playlistId);
    }

    @Override
    public void removeFromPlaylist(String playlistId, String videoId) {
        super.removeFromPlaylist(playlistId, videoId);

        removeFromCachedPlaylist(playlistId, videoId);
    }

    private void removeFromCachedPlaylist(String playlistId, String videoId) {
        ItemGroup playlistGroup = PlaylistGroupServiceImpl.findPlaylistGroup(playlistId);

        if (playlistGroup != null) {
            playlistGroup.remove(videoId);
        }
    }

    @Override
    public void savePlaylist(String playlistId) {
        super.savePlaylist(playlistId);

        List<ItemMetadata> metadata = YouTubeDataServiceInt.getPlaylistMetadata(playlistId);

        if (metadata != null && !metadata.isEmpty()) {
            PlaylistGroupServiceImpl.addPlaylistGroup(ItemGroupImpl.fromMetadata(metadata.get(0)));
        }
    }

    // More robust to find by id?
    private PlaylistInfo findFirst(List<PlaylistInfo> playlistsInfos, String title) {
        return Helpers.findFirst(playlistsInfos, item -> Helpers.equals(item.getTitle(), title));
    }

    private String findTitle(String playlistId) {
        PlaylistInfo first = Helpers.findFirst(mCachedPlaylistInfos, item -> Helpers.equals(item.getPlaylistId(), playlistId));

        return first != null ? first.getTitle() : null;
    }
}

package com.liskovsoft.youtubeapi.playlist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem;
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata;
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo;
import com.liskovsoft.sharedutils.helpers.Helpers;
import com.liskovsoft.youtubeapi.browse.v2.BrowseApiHelper;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemGroupImpl;
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl;
import com.liskovsoft.youtubeapi.next.v2.WatchNextService;
import com.liskovsoft.youtubeapi.next.v2.WatchNextServiceWrapper;
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

    private static void createCachedPlaylist(String playlistName, @Nullable String videoId) {
        MediaItem cachedVideo = PlaylistGroupServiceImpl.cachedItem;

        if (videoId == null) {
            ItemGroup playlist = PlaylistGroupServiceImpl.createPlaylistGroup(playlistName, null, Collections.emptyList());
            PlaylistGroupServiceImpl.addPlaylistGroup(playlist);
        } else if (cachedVideo != null && Helpers.equals(cachedVideo.getVideoId(), videoId)) {
            ItemGroup playlist = PlaylistGroupServiceImpl.createPlaylistGroup(playlistName, cachedVideo.getCardImageUrl(),
                    Collections.singletonList(ItemImpl.fromMediaItem(cachedVideo)));
            PlaylistGroupServiceImpl.addPlaylistGroup(playlist);
        } else {
            MediaItemMetadata ytMetadata = getWatchNextService().getMetadata(videoId);
            String title = ytMetadata != null ? ytMetadata.getTitle() : null;
            CharSequence subtitle = ytMetadata != null ? ytMetadata.getSecondTitle() : null;
            String badgeText = ytMetadata != null ? ytMetadata.getBadgeText() : null;
            String channelId = ytMetadata != null ? ytMetadata.getChannelId() : null;
            ItemGroup playlist = PlaylistGroupServiceImpl.createPlaylistGroup(playlistName, null,
                    Collections.singletonList(new ItemImpl(channelId, title, null, videoId, subtitle, badgeText, -1)));
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

            int firstIdx = -1;
            int firstIdxShift = -1;

            for (ItemGroup itemGroup : playlistGroups) {
                firstIdxShift++;
                // Replace local pl with remote one
                if (playlistsInfos != null && !playlistsInfos.isEmpty()) {
                    PlaylistInfo item = findFirst(playlistsInfos, itemGroup.getTitle()); // More robust to find by id?
                    if (item != null) {
                        if (!result.contains(item)) {
                            result.add(item);

                            if (firstIdx == -1) { // Save for later
                                firstIdx = playlistsInfos.indexOf(item);
                            }
                        }
                        continue;
                    }
                }

                // Add remained local playlists
                result.add(YouTubePlaylistInfo.from(itemGroup, itemGroup.contains(videoId)));
            }

            // Add remained remote playlists
            if (playlistsInfos != null && !playlistsInfos.isEmpty()) {
                int idx = -1;
                for (PlaylistInfo info : playlistsInfos) {
                    idx++;
                    if (!result.contains(info)) {
                        // Move newer playlists before
                        if (idx < firstIdx && result.size() > (idx + firstIdxShift)) {
                            result.add(idx + firstIdxShift, info);
                        } else {
                            result.add(info);
                        }
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

        MediaItem cachedVideo = PlaylistGroupServiceImpl.cachedItem;

        if (cachedVideo != null && Helpers.equals(cachedVideo.getVideoId(), videoId)) {
            playlistGroup.add(ItemImpl.fromMediaItem(cachedVideo));
            PlaylistGroupServiceImpl.addPlaylistGroup(playlistGroup); // move to the top
        } else { // Google api quota exceeded
            MediaItemMetadata ytMetadata = getWatchNextService().getMetadata(videoId);
            String title = ytMetadata != null ? ytMetadata.getTitle() : null;
            CharSequence subtitle = ytMetadata != null ? ytMetadata.getSecondTitle() : null;
            String badgeText = ytMetadata != null ? ytMetadata.getBadgeText() : null;
            String channelId = ytMetadata != null ? ytMetadata.getChannelId() : null;
            playlistGroup.add(new ItemImpl(channelId, title, null, videoId, subtitle, badgeText, -1));
            PlaylistGroupServiceImpl.addPlaylistGroup(playlistGroup); // move to the top
        }
    }

    @Override
    public void renamePlaylist(String playlistId, String newName) {
        try {
            super.renamePlaylist(playlistId, newName);
        } catch (IllegalStateException e) {
            if (Helpers.equals(e.getMessage(), "ErrorResponse: The caller does not have permission")) {
                throw e;
            }
        }

        renameCachedPlaylist(playlistId, newName);
    }

    private static void renameCachedPlaylist(String playlistId, String newName) {
        if (Helpers.equalsAny(playlistId, BrowseApiHelper.LIKED_PLAYLIST, BrowseApiHelper.WATCH_LATER_PLAYLIST)) {
            throw new IllegalStateException("Can't rename a special playlist!");
        }

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
            if (Helpers.equals(e.getMessage(), "ErrorResponse: The caller does not have permission")) {
                throw e;
            }
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

        MediaItem cachedList = PlaylistGroupServiceImpl.cachedItem;

        if (cachedList != null && Helpers.equals(cachedList.getPlaylistId(), playlistId)) {
            PlaylistGroupServiceImpl.addPlaylistGroup(ItemGroupImpl.fromMediaItem(cachedList));
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

    @NonNull
    private static WatchNextService getWatchNextService() {
        return WatchNextServiceWrapper.INSTANCE;
    }
}

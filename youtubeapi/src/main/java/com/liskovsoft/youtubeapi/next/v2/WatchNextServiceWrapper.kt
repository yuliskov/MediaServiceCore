package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata

internal object WatchNextServiceWrapper: WatchNextService() {
    override fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        return super.getMetadata(videoId, playlistId, playlistIndex, playlistParams)?.let {
            if (playlistId != null && it.suggestions?.firstOrNull()?.mediaItems?.firstOrNull()?.playlistId != playlistId) {
                transformMetadata(it, videoId, playlistId, playlistIndex)
            } else {
                it
            }
        } ?: transformMetadata(null, videoId, playlistId, playlistIndex)
    }

    private fun transformMetadata(metadata: MediaItemMetadata?, videoId: String?, playlistId: String?, playlistIndex: Int) =
        getCachedGroup(playlistId)?.let { cached ->
            val currentIdx = cached.mediaItems?.indexOfFirst { it.videoId == videoId }.takeIf { it != -1 } ?: playlistIndex
            val currentItem = cached.mediaItems?.getOrNull(currentIdx)
            YouTubeMediaItemMetadata().apply {
                this.videoId = currentItem?.videoId
                title = currentItem?.title
                secondTitle = currentItem?.secondTitle
                suggestions = mutableListOf()
                suggestions.add(cached)
                metadata?.suggestions?.let { suggestions.addAll(it) }
                nextVideo = cached.mediaItems?.getOrNull(currentIdx + 1)
                playlistInfo = object : PlaylistInfo {
                    override fun getTitle() = cached.title
                    override fun getPlaylistId() = playlistId
                    override fun isSelected() = false
                    override fun getSize() = cached.mediaItems?.size ?: -1
                    override fun getCurrentIndex() = currentIdx
                }
            }
        } ?: metadata

    private fun getCachedGroup(playlistId: String?): MediaGroup? {
        val group = PlaylistGroupServiceImpl.findPlaylistGroup(playlistId)
        if (group != null && !group.isEmpty) {
            return YouTubeMediaGroup(MediaGroup.TYPE_SUGGESTIONS).apply {
                title = group.title
                mediaItems = group.items?.map {
                    YouTubeMediaItem().apply {
                        title = it.title
                        secondTitle = it.subtitle
                        cardImageUrl = it.iconUrl
                        this.videoId = it.videoId
                        channelId = it.channelId
                        this.playlistId = playlistId
                        badgeText = it.badge
                    }
                }
            }
        }

        return null
    }
}
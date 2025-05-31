package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata

internal object WatchNextServiceWrapper: WatchNextService() {
    override fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        return super.getMetadata(videoId, playlistId, playlistIndex, playlistParams)?.let {
            if (playlistId != null && it.suggestions?.firstOrNull()?.mediaItems?.firstOrNull()?.playlistId != playlistId) {
                getCachedGroup(playlistId)?.let { cached ->
                    YouTubeMediaItemMetadata().apply {
                        suggestions = mutableListOf()
                        suggestions.add(cached)
                        suggestions.addAll(it.suggestions)
                        nextVideo = cached.mediaItems?.firstOrNull { it.videoId == videoId }
                    }
                } ?: it
            } else {
                it
            }
        }
    }

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
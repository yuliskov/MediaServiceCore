package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata

internal class WatchNextServiceWrapper : WatchNextService() {
    override fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        return getCachedGroup(playlistId, MediaGroup.TYPE_SUGGESTIONS) ?: super.getMetadata(videoId, playlistId, playlistIndex, playlistParams)
    }

    private fun getCachedGroup(id: String?, type: Int): MediaItemMetadata? {
        val group = PlaylistGroupServiceImpl.findPlaylistGroup(id)
        if (group != null && !group.isEmpty) {
            val result = YouTubeMediaGroup(type).apply {
                title = group.title
                mediaItems = group.items?.map {
                    YouTubeMediaItem().apply {
                        title = it.title
                        secondTitle = it.subtitle
                        cardImageUrl = it.iconUrl
                        videoId = it.videoId
                        channelId = it.channelId
                        badgeText = it.badge
                    }
                }
            }
            return YouTubeMediaItemMetadata().apply {
                suggestions = listOf(result)
            }
        }

        return null
    }

    companion object {
        @JvmStatic
        val instance: WatchNextServiceWrapper by lazy { WatchNextServiceWrapper() }
    }
}
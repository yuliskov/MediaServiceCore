package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemMetadata

internal object WatchNextServiceWrapper: WatchNextService() {
    override fun getMetadata(videoId: String?, playlistId: String?, playlistIndex: Int, playlistParams: String?): MediaItemMetadata? {
        return super.getMetadata(videoId, playlistId, playlistIndex, playlistParams) ?: getCachedGroup(videoId, playlistId)
    }

    private fun getCachedGroup(videoId: String?, playlistId: String?): MediaItemMetadata? {
        if (videoId != null) { // Dynamic suggestions needed
            return null
        }

        val group = PlaylistGroupServiceImpl.findPlaylistGroup(playlistId)
        if (group != null && !group.isEmpty) {
            val result = YouTubeMediaGroup(MediaGroup.TYPE_SUGGESTIONS).apply {
                title = group.title
                mediaItems = group.items?.map {
                    YouTubeMediaItem().apply {
                        title = it.title
                        secondTitle = it.subtitle
                        cardImageUrl = it.iconUrl
                        this.videoId = it.videoId
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
}
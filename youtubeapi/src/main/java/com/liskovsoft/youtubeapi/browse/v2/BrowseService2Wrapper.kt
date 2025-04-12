package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl
import com.liskovsoft.youtubeapi.rss.RssService
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem

internal object BrowseService2Wrapper: BrowseService2() {
    override fun getSubscriptions(): MediaGroup? {
        val subscriptions = super.getSubscriptions()

        if (subscriptions == null || subscriptions.isEmpty) {
            val channelIds = ChannelGroupServiceImpl.getSubscribedChannelIds()

            return channelIds?.let { RssService.getFeed(*it) }
        }

        return subscriptions
    }

    override fun getSubscribedChannels(): MediaGroup? {
        // Backup channels ones
        // Add each channel on subscribe
        return getCachedChannels(super.getSubscribedChannels())
    }

    override fun getSubscribedChannelsByName(): MediaGroup? {
        // Backup channels ones
        // Add each channel on subscribe
        return getCachedChannels(super.getSubscribedChannelsByName())
    }

    override fun getSubscribedChannelsByNewContent(): MediaGroup? {
        // Backup channels ones
        // Add each channel on subscribe
        return getCachedChannels(super.getSubscribedChannelsByNewContent())
    }

    private fun getCachedChannels(subscribedChannels: MediaGroup?): MediaGroup? {
        if (subscribedChannels == null || subscribedChannels.isEmpty) {
            val channelGroup = ChannelGroupServiceImpl.getSubscribedChannelGroup()

            return if (channelGroup.isEmpty) null else channelGroup.let {
                YouTubeMediaGroup(MediaGroup.TYPE_CHANNEL_UPLOADS).apply {
                    mediaItems = it.items?.map {
                        YouTubeMediaItem().apply {
                            title = it.title
                            secondTitle = it.subtitle
                            channelId = it.channelId
                            cardImageUrl = it.iconUrl
                            badgeText = it.badge
                        }
                    }
                }
            }
        }

        // NOTE: Can't backup. ReloadPageKey cannot be used without an account.
        // The channels contain reloadPageKey instead of channelId field.

        return subscribedChannels
    }

    override fun getMyPlaylists(): MediaGroup? {
        return getCachedPlaylists(super.getMyPlaylists())
    }

    private fun getCachedPlaylists(myPlaylists: MediaGroup?): MediaGroup? {
        val playlistGroups = PlaylistGroupServiceImpl.getPlaylistGroups()
        if (playlistGroups.isNotEmpty()) {
            val result: MutableList<MediaItem> = mutableListOf()
            myPlaylists?.mediaItems?.getOrNull(0)?.let { result.add(it) } // WatchLater

            var firstIdx: Int = -1
            var firstIdxShift: Int = -1

            playlistGroups.forEach {
                firstIdxShift++
                // Replace local pl with matched remote one
                if (myPlaylists?.mediaItems?.isNotEmpty() == true) {
                    // Can't match by playlistId because we have only reloadPageKey
                    findFirst(myPlaylists.mediaItems, it.title)?.let {
                        if (!result.contains(it)) {
                            result.add(it)

                            if (firstIdx == -1) { // Save for later
                                firstIdx = myPlaylists.mediaItems?.indexOf(it) ?: -1
                            }
                        }
                        return@forEach
                    }
                }

                // Add remained local playlists
                result.add(YouTubeMediaItem().apply {
                    title = it.title
                    cardImageUrl = it.items?.firstOrNull()?.iconUrl ?: it.iconUrl
                    playlistId = it.id
                    channelId = it.id
                    //reloadPageKey = it.id
                    badgeText = it.badge ?: "${it.items.size} videos"
                })
            }

            // Add remained remote playlists
            myPlaylists?.mediaItems?.forEachIndexed { idx, item ->
                if (!result.contains(item)) {
                    // Move newer playlists before
                    if (idx < firstIdx && result.size > (idx + firstIdxShift)) {
                        result.add(idx + firstIdxShift, item)
                    } else {
                        result.add(item)
                    }
                }
            }

            return YouTubeMediaGroup(myPlaylists?.type ?: MediaGroup.TYPE_USER_PLAYLISTS).apply {
                mediaItems = result
            }
        }

        return myPlaylists
    }

    override fun getGroup(reloadPageKey: String, type: Int, title: String?): MediaGroup? {
        return getCachedGroup(reloadPageKey, type) ?: super.getGroup(reloadPageKey, type, title)
    }

    override fun getChannel(channelId: String?, params: String?): Pair<List<MediaGroup?>?, String?>? {
        return getCachedGroup(channelId, MediaGroup.TYPE_CHANNEL_UPLOADS)?.let { Pair(listOf(it), null) } ?: super.getChannel(channelId, params)
    }

    override fun getChannelAsGrid(channelId: String?): MediaGroup? {
        return getCachedGroup(channelId, MediaGroup.TYPE_CHANNEL_UPLOADS) ?: super.getChannelAsGrid(channelId)
    }

    private fun getCachedGroup(reloadPageKey: String?, type: Int): MediaGroup? {
        val group = PlaylistGroupServiceImpl.findPlaylistGroup(reloadPageKey)
        if (group != null && !group.isEmpty) {
            return YouTubeMediaGroup(type).apply {
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
        }

        return null
    }

    private fun findFirst(mediaItems: List<MediaItem>?, title: String): MediaItem? {
        return Helpers.findFirst(mediaItems) { Helpers.equals(it.title, title) }
    }
}
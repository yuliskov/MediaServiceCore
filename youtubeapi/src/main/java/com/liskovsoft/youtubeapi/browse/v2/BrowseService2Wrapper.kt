package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.PlaylistInfo
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl
import com.liskovsoft.youtubeapi.playlistgroups.PlaylistGroupServiceImpl
import com.liskovsoft.youtubeapi.rss.RssService
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem

internal class BrowseService2Wrapper: BrowseService2() {
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

            playlistGroups.forEach {
                // Merge local and remote
                if (myPlaylists?.mediaItems?.isNotEmpty() == true) {
                    // Can't match by playlistId because we have only reloadPageKey
                    findFirst(myPlaylists.mediaItems, it.title)?.let {
                        result.add(it)
                        return@forEach
                    }
                }

                result.add(YouTubeMediaItem().apply {
                    title = it.title
                    cardImageUrl = it.iconUrl
                    playlistId = "${it.id}"
                    channelId = "${it.id}"
                    reloadPageKey = "${it.id}"
                    badgeText = "${it.items.size} videos"
                })
            }
            
            myPlaylists?.mediaItems?.forEach {
                if (!result.contains(it)) {
                    result.add(it)
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

    private fun getCachedGroup(reloadPageKey: String?, type: Int): MediaGroup? {
        val group = PlaylistGroupServiceImpl.findPlaylistGroup(convertToId(reloadPageKey))
        if (group != null) {
            return YouTubeMediaGroup(type).apply {
                title = group.title
                mediaItems = group.items?.map {
                    YouTubeMediaItem().apply {
                        title = it.title
                        secondTitle = it.subtitle
                        cardImageUrl = it.iconUrl
                        videoId = it.videoId
                        channelId = it.channelId
                    }
                }
            }
        }

        return null
    }

    private fun convertToId(playlistId: String?): Int {
        if (playlistId == null) {
            return -1
        }

        val id = Helpers.parseInt(playlistId)
        return if (id != -1) id else Helpers.hashCode(playlistId)
    }

    private fun findFirst(mediaItems: List<MediaItem>?, title: String): MediaItem? {
        return Helpers.findFirst(mediaItems) { Helpers.equals(it.title, title) }
    }

    companion object {
        @JvmStatic
        val instance: BrowseService2Wrapper by lazy { BrowseService2Wrapper() }
    }
}
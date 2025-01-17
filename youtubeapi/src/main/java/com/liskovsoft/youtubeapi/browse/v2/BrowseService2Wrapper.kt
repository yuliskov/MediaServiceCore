package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
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
        if (PlaylistGroupServiceImpl.getPlaylistGroups().isNotEmpty()) {
            val result: MutableList<MediaItem> = mutableListOf()
            // Place WatchLater before all
            myPlaylists?.mediaItems?.getOrNull(0)?.let { result.add(it) }

            PlaylistGroupServiceImpl.getPlaylistGroups().forEach {
                result.add(YouTubeMediaItem().apply {
                    title = it.title
                    cardImageUrl = it.iconUrl
                    playlistId = "${it.id}"
                    badgeText = "${it.items.size} videos"
                })
            }

            // Remove WatchLater
            myPlaylists?.mediaItems?.drop(1)?.let {
                result.addAll(it)
            }

            return YouTubeMediaGroup(myPlaylists?.type ?: MediaGroup.TYPE_USER_PLAYLISTS).apply {
                mediaItems = result
            }
        }

        return myPlaylists
    }

    companion object {
        @JvmStatic
        val instance: BrowseService2Wrapper by lazy { BrowseService2Wrapper() }
    }
}
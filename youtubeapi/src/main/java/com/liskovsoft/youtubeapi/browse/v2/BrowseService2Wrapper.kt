package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
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

            return channelIds?.let { RssService.getFeed(*it, type = MediaGroup.TYPE_SUBSCRIPTIONS) }
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
            // Pin playlists
            myPlaylists?.mediaItems?.getOrNull(0)?.let { result.add(it) } // WatchLater
            myPlaylists?.mediaItems?.firstOrNull { it.channelId?.startsWith(BrowseApiHelper.LIKED_CHANNEL_ID) ?: false }?.let { result.add(it) } // Liked

            var firstIdx: Int = -1
            var firstIdxShift: Int = -1

            playlistGroups.forEach { group ->
                firstIdxShift++
                // Replace local pl with matched remote one
                if (myPlaylists?.mediaItems?.isNotEmpty() == true) {
                    myPlaylists.mediaItems?.firstOrNull {
                        // Can't match only by playlistId because we have only reloadPageKey
                        it.title == group.title || it.playlistId == group.id
                    }?.let {
                        if (!result.contains(it)) {
                            result.add(it)

                            if (firstIdx == -1) { // Save idx of the first unpinned playlist (see above)
                                firstIdx = myPlaylists.mediaItems?.indexOf(it) ?: -1
                            }
                        }
                        return@forEach
                    }
                }

                // Add remained local playlists
                result.add(YouTubeMediaItem().apply {
                    title = group.title
                    cardImageUrl = group.items?.firstOrNull()?.iconUrl ?: group.iconUrl
                    playlistId = group.id
                    channelId = group.id
                    //reloadPageKey = group.id
                    badgeText = group.badge ?: "${group.items.size} videos"
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
        return super.getGroup(reloadPageKey, type, title) ?: getCachedGroup(reloadPageKey, type)
    }

    override fun getChannel(channelId: String?, params: String?): Pair<List<MediaGroup?>?, String?>? {
        return super.getChannel(channelId, params) ?: getCachedGroup(channelId, MediaGroup.TYPE_CHANNEL_UPLOADS)?.let { Pair(listOf(it), null) }
    }

    override fun getChannelAsGrid(channelId: String?): MediaGroup? {
        return super.getChannelAsGrid(channelId) ?: getCachedGroup(channelId, MediaGroup.TYPE_CHANNEL_UPLOADS)
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
                        playlistId = reloadPageKey
                        badgeText = it.badge
                    }
                }
            }
        }

        return null
    }
}
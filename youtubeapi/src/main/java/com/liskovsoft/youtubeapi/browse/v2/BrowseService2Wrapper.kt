package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl
import com.liskovsoft.youtubeapi.rss.RssService
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItem

internal class BrowseService2Wrapper: BrowseService2() {
    override fun getSubscriptions(): MediaGroup? {
        val subscriptions = super.getSubscriptions()

        if (subscriptions == null || subscriptions.isEmpty()) {
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
        if (subscribedChannels == null || subscribedChannels.isEmpty()) {
            val channelGroup = ChannelGroupServiceImpl.getSubscribedChannelGroup()

            return channelGroup?.let {
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

        return subscribedChannels
    }

    companion object {
        @JvmStatic
        val instance: BrowseService2Wrapper by lazy { BrowseService2Wrapper() }
    }
}
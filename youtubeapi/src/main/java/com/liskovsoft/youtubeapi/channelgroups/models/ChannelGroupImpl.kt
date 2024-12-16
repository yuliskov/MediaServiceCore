package com.liskovsoft.youtubeapi.channelgroups.models

import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup.Channel
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl

private const val ITEM_DELIM = "&sgi;"
private const val LIST_DELIM = "&sga;"

internal data class ChannelGroupImpl(
    private val id: Int = Helpers.getRandomNumber(ChannelGroupServiceImpl.SUBSCRIPTION_GROUP_ID + 100, Integer.MAX_VALUE),
    private val title: String,
    private val iconUrl: String? = null,
    private val channels: MutableList<Channel>
): ChannelGroup {
    override fun getId(): Int {
        return id
    }

    override fun getTitle(): String {
        return title
    }

    override fun getIconUrl(): String? {
        return iconUrl
    }

    override fun getChannels(): List<Channel> {
        return channels
    }

    override fun add(channel: Channel) {
        if (!channels.contains(channel)) {
            channels.add(0, channel)
        }
    }

    override fun remove(channelId: String) {
        Helpers.removeIf(channels) { channel -> channel.channelId == channelId }
    }

    override fun contains(channelId: String): Boolean {
        return Helpers.containsIf(channels) { value -> Helpers.equals(value.channelId, channelId) }
    }

    override fun isEmpty(): Boolean {
        return channels.isEmpty()
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, id, title, iconUrl, Helpers.mergeList(LIST_DELIM, channels))
    }

    override fun equals(other: Any?): Boolean {
        if (other is ChannelGroup) {
            return other.title == title
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): ChannelGroup? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val id = Helpers.parseInt(split, 0)
            val title = Helpers.parseStr(split, 1)
            val groupIconUrl = Helpers.parseStr(split, 2)
            val channels: MutableList<Channel> = Helpers.parseList(split, 3, LIST_DELIM, ChannelImpl.Companion::fromString)

            return ChannelGroupImpl(id, title, groupIconUrl, channels)
        }
    }
}

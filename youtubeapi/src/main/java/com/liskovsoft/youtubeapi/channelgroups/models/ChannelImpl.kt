package com.liskovsoft.youtubeapi.channelgroups.models

import com.liskovsoft.mediaserviceinterfaces.yt.data.ChannelGroup.Channel
import com.liskovsoft.sharedutils.helpers.Helpers

private const val ITEM_DELIM = "&ci;"

internal data class ChannelImpl(
    private val title: String? = null,
    private val iconUrl: String? = null,
    private val channelId: String
): Channel {
    override fun getTitle(): String? {
        return title
    }

    override fun getIconUrl(): String? {
        return iconUrl
    }

    override fun getChannelId(): String {
        return channelId
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, title, iconUrl, channelId)
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): Channel? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val title = Helpers.parseStr(split, 0)
            val groupIconUrl = Helpers.parseStr(split, 1)
            val channelId = Helpers.parseStr(split, 2)

            return ChannelImpl(title, groupIconUrl, channelId)
        }
    }
}

package com.liskovsoft.youtubeapi.channelgroups.models

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers

private const val ITEM_DELIM = "&ci;"

internal data class MediaItemImpl(
    private val channelId: String? = null,
    private val title: String? = null,
    private val iconUrl: String? = null,
    private val videoId: String? = null,
    private val subtitle: String? = null
): MediaItem {
    override fun getTitle(): String? {
        return title
    }

    override fun getIconUrl(): String? {
        return iconUrl
    }

    override fun getChannelId(): String? {
        return channelId
    }

    override fun getVideoId(): String? {
        return videoId
    }

    override fun getSubtitle(): String? {
        return subtitle
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, title, iconUrl, channelId, videoId, subtitle)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MediaItem) {
            return other.channelId == channelId && other.videoId == videoId
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): MediaItem? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val title = Helpers.parseStr(split, 0)
            val groupIconUrl = Helpers.parseStr(split, 1)
            val channelId = Helpers.parseStr(split, 2)
            val videoId = Helpers.parseStr(split, 3)
            val subtitle = Helpers.parseStr(split, 4)

            if (channelId == null && videoId == null) {
                return null
            }

            return MediaItemImpl(channelId, title, groupIconUrl, videoId, subtitle)
        }
    }
}

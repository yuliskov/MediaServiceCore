package com.liskovsoft.youtubeapi.channelgroups.models

import com.liskovsoft.googleapi.youtubedata3.impl.ItemMetadata
import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup.Item
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.helpers.DateHelper
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper

private const val ITEM_DELIM = "&ci;"

internal data class ItemImpl(
    private val channelId: String? = null,
    private val title: String? = null,
    private val iconUrl: String? = null,
    private val videoId: String? = null,
    private val subtitle: CharSequence? = null,
    private val badge: String? = null,
    private val likeCount: Int = -1,
): Item {
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

    override fun getSubtitle(): CharSequence? {
        return subtitle
    }

    override fun getBadge(): String? {
        return badge
    }

    override fun getLikeCount(): Int {
        return likeCount
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, title, iconUrl, channelId, videoId, subtitle, badge, likeCount)
    }

    override fun equals(other: Any?): Boolean {
        if (other is Item) {
            return other.channelId == channelId && other.videoId == videoId
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): Item? {
            if (spec == null)
                return null

            val split = Helpers.split(spec, ITEM_DELIM)

            val title = Helpers.parseStr(split, 0)
            val groupIconUrl = Helpers.parseStr(split, 1)
            val channelId = Helpers.parseStr(split, 2)
            val videoId = Helpers.parseStr(split, 3)
            val subtitle = Helpers.parseStr(split, 4)
            val badge = Helpers.parseStr(split, 5)
            val likeCount = Helpers.parseInt(split, 6, -1)

            if (channelId == null && videoId == null) {
                return null
            }

            return ItemImpl(channelId, title, groupIconUrl, videoId, subtitle, badge, likeCount)
        }

        @JvmStatic
        fun fromMetadata(metadata: ItemMetadata): Item {
            val secondTitle = ServiceHelper.createInfo(metadata.channelTitle, DateHelper.toShortDate(metadata.publishedAt, true, true, false))
            val badge = ServiceHelper.convertIsoDurationToHHMMSS(metadata.durationIso)
            return ItemImpl(metadata.channelId, metadata.title, metadata.cardImageUrl, metadata.videoId, secondTitle, badge)
        }

        @JvmStatic
        fun fromMediaItem(mediaItem: MediaItem): Item {
            return ItemImpl(mediaItem.channelId, mediaItem.title, mediaItem.cardImageUrl, mediaItem.videoId, mediaItem.secondTitle, mediaItem.badgeText)
        }
    }
}

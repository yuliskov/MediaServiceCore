package com.liskovsoft.youtubeapi.channelgroups.models

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItemGroup.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl

private const val ITEM_DELIM = "&sgi;"
private const val LIST_DELIM = "&sga;"

internal data class MediaItemGroupImpl(
    private val id: Int = Helpers.getRandomNumber(ChannelGroupServiceImpl.SUBSCRIPTION_GROUP_ID + 100, Integer.MAX_VALUE),
    private val title: String,
    private val iconUrl: String? = null,
    private val mediaItems: MutableList<MediaItem>,
    var onChange: (MediaItemGroup) -> Unit = { ChannelGroupServiceImpl.persistData() }
): MediaItemGroup {
    override fun getId(): Int {
        return id
    }

    override fun getTitle(): String {
        return title
    }

    override fun getIconUrl(): String? {
        return iconUrl
    }

    override fun getMediaItems(): List<MediaItem> {
        return mediaItems
    }

    override fun findMediaItem(channelOrVideoId: String): MediaItem? {
        return Helpers.findFirst(mediaItems) { mediaItem -> mediaItem.channelId == channelOrVideoId || mediaItem.videoId == channelOrVideoId }
    }

    override fun add(mediaItem: MediaItem) {
        mediaItems.remove(mediaItem)
        mediaItems.add(0, mediaItem)
        onChange.invoke(this)
    }

    override fun addAll(newMediaItems: List<MediaItem>) {
        mediaItems.removeAll(newMediaItems)
        mediaItems.addAll(newMediaItems)
        onChange.invoke(this)
    }

    override fun remove(channelOrVideoId: String) {
        val removed = Helpers.removeIf(mediaItems) { mediaItem -> mediaItem.channelId == channelOrVideoId || mediaItem.videoId == channelOrVideoId }
        if (!removed.isNullOrEmpty()) {
            onChange.invoke(this)
        }
    }

    override fun contains(channelOrVideoId: String): Boolean {
        return Helpers.containsIf(mediaItems) { mediaItem -> mediaItem.channelId == channelOrVideoId || mediaItem.videoId == channelOrVideoId }
    }

    override fun isEmpty(): Boolean {
        return mediaItems.isEmpty()
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, id, title, iconUrl, Helpers.mergeList(LIST_DELIM, mediaItems))
    }

    override fun equals(other: Any?): Boolean {
        if (other is MediaItemGroup) {
            return other.id == id
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): MediaItemGroup? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val id = Helpers.parseInt(split, 0)
            val title = Helpers.parseStr(split, 1) ?: return null
            val groupIconUrl = Helpers.parseStr(split, 2)
            val mediaItems: MutableList<MediaItem> = Helpers.parseList(split, 3, LIST_DELIM, MediaItemImpl::fromString)

            return MediaItemGroupImpl(id, title, groupIconUrl, mediaItems)
        }
    }
}

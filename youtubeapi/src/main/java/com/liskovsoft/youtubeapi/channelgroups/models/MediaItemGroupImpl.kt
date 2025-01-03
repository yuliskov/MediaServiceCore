package com.liskovsoft.youtubeapi.channelgroups.models

import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.ItemGroup.Item
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl

private const val ITEM_DELIM = "&sgi;"
private const val LIST_DELIM = "&sga;"

internal data class MediaItemGroupImpl(
    private val id: Int = Helpers.getRandomNumber(ChannelGroupServiceImpl.SUBSCRIPTION_GROUP_ID + 100, Integer.MAX_VALUE),
    private val title: String,
    private val iconUrl: String? = null,
    private val items: MutableList<Item>,
    var onChange: (ItemGroup) -> Unit = { ChannelGroupServiceImpl.persistData() }
): ItemGroup {
    override fun getId(): Int {
        return id
    }

    override fun getTitle(): String {
        return title
    }

    override fun getIconUrl(): String? {
        return iconUrl
    }

    override fun getItems(): List<Item> {
        return items
    }

    override fun findItem(channelOrVideoId: String): Item? {
        return Helpers.findFirst(items) { mediaItem -> mediaItem.channelId == channelOrVideoId || mediaItem.videoId == channelOrVideoId }
    }

    override fun add(item: Item) {
        items.remove(item)
        items.add(0, item)
        onChange.invoke(this)
    }

    override fun addAll(newItems: List<Item>) {
        items.removeAll(newItems)
        items.addAll(newItems)
        onChange.invoke(this)
    }

    override fun remove(channelOrVideoId: String) {
        val removed = Helpers.removeIf(items) { mediaItem -> mediaItem.channelId == channelOrVideoId || mediaItem.videoId == channelOrVideoId }
        if (!removed.isNullOrEmpty()) {
            onChange.invoke(this)
        }
    }

    override fun contains(channelOrVideoId: String): Boolean {
        return Helpers.containsIf(items) { mediaItem -> mediaItem.channelId == channelOrVideoId || mediaItem.videoId == channelOrVideoId }
    }

    override fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, id, title, iconUrl, Helpers.mergeList(LIST_DELIM, items))
    }

    override fun equals(other: Any?): Boolean {
        if (other is ItemGroup) {
            return other.id == id
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): ItemGroup? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val id = Helpers.parseInt(split, 0)
            val title = Helpers.parseStr(split, 1) ?: return null
            val groupIconUrl = Helpers.parseStr(split, 2)
            val items: MutableList<Item> = Helpers.parseList(split, 3, LIST_DELIM, MediaItemImpl::fromString)

            return MediaItemGroupImpl(id, title, groupIconUrl, items)
        }
    }
}

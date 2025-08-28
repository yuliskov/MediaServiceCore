package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.mediaserviceinterfaces.data.ItemGroup
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.channelgroups.ChannelGroupServiceImpl
import com.liskovsoft.youtubeapi.channelgroups.models.ItemImpl
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs

internal object NotificationStorage: MediaServicePrefs.ProfileChangeListener {
    private const val NOTIFICATION_DATA = "notification_data"
    private const val MIN_LIKE_COUNT = 5

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    fun addChannel(channelId: String?) {
        channelId?.let {
            val notifications = ChannelGroupServiceImpl.getNotificationChannelGroup()
            notifications.add(ItemImpl(it))
        }
    }

    fun removeChannel(channelId: String?) {
        channelId?.let {
            val notifications = ChannelGroupServiceImpl.getNotificationChannelGroup()
            notifications.remove(it)
        }
    }

    fun getChannels(): List<String>? {
        return ChannelGroupServiceImpl.getNotificationChannelGroup().items.filter { it.likeCount == -1 || it.likeCount > MIN_LIKE_COUNT }.mapNotNull { it.channelId }
    }

    fun contains(channelId: String?): Boolean {
        return ChannelGroupServiceImpl.getNotificationChannelGroup().contains(channelId)
    }

    @JvmStatic
    fun setLike(up: Boolean) {
        val channelId = ChannelGroupServiceImpl.cachedChannel?.channelId ?: return
        val notifications = ChannelGroupServiceImpl.getNotificationChannelGroup()
        val channel: ItemGroup.Item? = notifications.findItem(channelId)

        if (channel == null) {
            notifications.add(ItemImpl(channelId = channelId, likeCount = 1))
            return
        }

        // Disable filter by likes for manually added channels
        if (channel.likeCount == -1) {
            return
        }

        if (up) {
            notifications.add(ItemImpl(channelId = channelId, likeCount = channel.likeCount.inc()))
        } else if (channel.likeCount == 1) {
            notifications.remove(channelId)
        } else {
            notifications.add(ItemImpl(channelId = channelId, likeCount = channel.likeCount.dec()))
        }
    }

    override fun onProfileChanged() {
        restoreData()
    }

    private fun restoreData() {
        val data = MediaServicePrefs.getData(NOTIFICATION_DATA) ?: return

        val split = Helpers.splitData(data)

        val channelIds = Helpers.parseStrList(split, 0)

        val notifications = ChannelGroupServiceImpl.getNotificationChannelGroup()

        channelIds.forEach {
            notifications.add(ChannelGroupServiceImpl.createChannel(it, null, null))
        }

        MediaServicePrefs.setData(NOTIFICATION_DATA, null)
    }
}
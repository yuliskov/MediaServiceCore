package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.service.internal.MediaServicePrefs

internal object NotificationStorage: MediaServicePrefs.ProfileChangeListener {
    private const val NOTIFICATION_DATA = "notification_data"
    private val channels: MutableSet<String> = mutableSetOf()

    init {
        MediaServicePrefs.addListener(this)
        restoreData()
    }

    fun addChannel(channelId: String?) {
        channelId?.let {
            channels.add(it)
            persistData()
        }
    }

    fun removeChannel(channelId: String?) {
        channelId?.let {
            channels.remove(it)
            persistData()
        }
    }

    fun getChannels(): Set<String>? {
        return channels.ifEmpty { null }
    }

    fun contains(channelId: String?): Boolean {
        return channels.contains(channelId)
    }

    @JvmStatic
    fun clear() {
        channels.clear()

        persistData()
    }

    override fun onProfileChanged() {
        restoreData()
    }

    private fun restoreData() {
        channels.clear()

        val data = MediaServicePrefs.getData(NOTIFICATION_DATA)

        val split = Helpers.splitData(data)

        channels.addAll(Helpers.parseStrList(split, 0))
    }

    private fun persistData() {
        MediaServicePrefs.setData(NOTIFICATION_DATA, Helpers.mergeData(channels))
    }
}
package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.yt.data.NotificationState
import com.liskovsoft.youtubeapi.common.models.impl.NotificationStateImpl
import com.liskovsoft.youtubeapi.rss.RssService

internal object NotificationsServiceWrapper {
    fun getItems(): MediaGroup? {
        return try {
            NotificationsServiceInt.getItems()
        } catch (e: IllegalStateException) {
            NotificationStorage.getChannels()?.let { RssService.getFeed(*it.toTypedArray()) }
        }
    }

    fun hideNotification(item: MediaItem?) {
        return NotificationsServiceInt.hideNotification(item)
    }

    fun modifyNotification(notificationState: NotificationState?) {
        if (notificationState is NotificationStateImpl) {
            if (notificationState.index == 0)
                NotificationStorage.addChannel(notificationState.channelId)
            else
                NotificationStorage.removeChannel(notificationState.channelId)
        }

        NotificationsServiceInt.modifyNotification(notificationState)
    }
}
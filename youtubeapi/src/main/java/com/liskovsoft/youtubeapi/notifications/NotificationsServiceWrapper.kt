package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.yt.data.NotificationState
import com.liskovsoft.youtubeapi.common.models.gen.NotificationStateItem
import com.liskovsoft.youtubeapi.common.models.impl.NotificationStateImpl
import com.liskovsoft.youtubeapi.rss.RssService
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaGroup

private const val ALL = 0 // Enable notifications
private const val PERSONALIZED = 1 // Disable notifications
private const val NONE = 2 // Disable notifications

internal object NotificationsServiceWrapper {
    fun getItems(): MediaGroup? {
        return try {
            NotificationsServiceInt.getItems()
        } catch (e: IllegalStateException) {
            NotificationStorage.getChannels()?.let { RssService.getFeed(*it.toTypedArray()) } ?: YouTubeMediaGroup(MediaGroup.TYPE_NOTIFICATIONS)
        }
    }

    fun hideNotification(item: MediaItem?) {
        return NotificationsServiceInt.hideNotification(item)
    }

    fun modifyNotification(notificationState: NotificationState?) {
        if (notificationState is NotificationStateImpl) {
            if (notificationState.index == ALL)
                NotificationStorage.addChannel(notificationState.channelId)
            else
                NotificationStorage.removeChannel(notificationState.channelId)
        }

        try {
            NotificationsServiceInt.modifyNotification(notificationState)
        } catch (e: IllegalStateException) {
            // Notification cannot be modified
        }
    }
}

internal class NotificationStateImplWrapper(
    notificationStateItem: NotificationStateItem,
    selectedSateId: Int?,
    channelId: String?,
    params: String?,
    isSubscribed: Boolean
): NotificationStateImpl(notificationStateItem, selectedSateId, channelId, params, isSubscribed) {
    override fun isSelected(): Boolean {
        return if (NotificationStorage.contains(channelId))
             if (index == ALL) true else false
        else if (super.isSelected() && index == ALL) {
            // Set to none if selected globally (global notifications doesn't work)
            allStates.getOrNull(NONE)?.setSelected()
            false
        }
        else super.isSelected()
    }
}
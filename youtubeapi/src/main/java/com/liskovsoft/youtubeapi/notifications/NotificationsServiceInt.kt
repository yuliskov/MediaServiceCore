package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.NotificationsMediaGroup
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.NotificationMediaItem

object NotificationsServiceInt {
    private val mService: NotificationsApi = RetrofitHelper.withGson(NotificationsApi::class.java)

    fun getItems(): MediaGroup? {
        val result = mService.getNotifications(NotificationsParams.getNotificationsQuery())

        return RetrofitHelper.get(result)?.let { NotificationsMediaGroup(it) }
    }

    fun hideNotification(item: MediaItem?) {
        if (item is NotificationMediaItem) {
            hideNotification(item.hideNotificationToken)
        }
    }

    fun hideNotification(hideNotificationToken: String?) {
        if (hideNotificationToken == null) {
            return
        }

        val result = mService.getHideNotification(NotificationsParams.getHideNotificationQuery(hideNotificationToken))

        RetrofitHelper.get(result)
    }
}
package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.yt.data.NotificationState
import com.liskovsoft.youtubeapi.actions.ActionsService
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.NotificationsMediaGroup
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.models.impl.NotificationStateImpl
import com.liskovsoft.youtubeapi.common.models.impl.mediaitem.NotificationMediaItem

internal object NotificationsServiceInt {
    private val mService: NotificationsApi = RetrofitHelper.withGson(NotificationsApi::class.java)

    fun getItems(): MediaGroup? {
        val result = mService.getNotifications(NotificationsApiHelper.getNotificationsQuery())

        return RetrofitHelper.get(result)?.let { NotificationsMediaGroup(it) }
    }

    fun hideNotification(item: MediaItem?) {
        if (item is NotificationMediaItem) {
            hideNotification(item.hideNotificationToken)
        }
    }

    fun modifyNotification(notificationState: NotificationState?) {
        if (notificationState is NotificationStateImpl) {
            notificationState.setSelected()

            if (!notificationState.isSubscribed) {
                ActionsService.instance().subscribe(notificationState.channelId, notificationState.params)
            }

            modifyNotification(notificationState.stateParams)
        }
    }

    private fun hideNotification(hideNotificationToken: String?) {
        if (hideNotificationToken == null) {
            return
        }

        val result = mService.getHideNotification(NotificationsApiHelper.getHideNotificationQuery(hideNotificationToken))

        RetrofitHelper.get(result)
    }

    private fun modifyNotification(modifyNotificationParams: String?) {
        if (modifyNotificationParams == null) {
            return
        }

        val result = mService.getModifyNotification(NotificationsApiHelper.getModifyNotificationQuery(modifyNotificationParams))

        RetrofitHelper.get(result)
    }
}
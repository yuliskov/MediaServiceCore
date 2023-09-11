package com.liskovsoft.youtubeapi.service

import com.liskovsoft.mediaserviceinterfaces.NotificationsService
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.NotificationState
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.notifications.NotificationsServiceInt
import io.reactivex.Observable

object YouTubeNotificationsService: NotificationsService {
    override fun getNotificationItems(): MediaGroup? {
        return NotificationsServiceInt.getItems()
    }

    override fun hideNotification(item: MediaItem?) {
        NotificationsServiceInt.hideNotification(item)
    }

    override fun getNotificationItemsObserve(): Observable<MediaGroup> {
        return RxHelper.fromNullable { notificationItems }
    }

    override fun hideNotificationObserve(item: MediaItem?): Observable<Void> {
        return RxHelper.fromVoidable { hideNotification(item) }
    }

    override fun setNotificationState(state: NotificationState?) {
        NotificationsServiceInt.modifyNotification(state)
    }

    override fun setNotificationStateObserve(state: NotificationState?): Observable<Void> {
        return RxHelper.fromVoidable { setNotificationState(state) }
    }
}
package com.liskovsoft.youtubeapi.service

import com.liskovsoft.mediaserviceinterfaces.NotificationsService
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.notifications.NotificationsServiceInt
import io.reactivex.Observable

object YouTubeNotificationsService: NotificationsService {
    override fun getNotificationItems(): MediaGroup? {
        return NotificationsServiceInt.getItems()
    }

    override fun getNotificationItemsObserve(): Observable<MediaGroup> {
        return RxHelper.fromNullable { notificationItems }
    }
}
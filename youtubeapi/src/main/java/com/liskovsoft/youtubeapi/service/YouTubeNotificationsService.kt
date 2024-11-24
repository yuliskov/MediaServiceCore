package com.liskovsoft.youtubeapi.service

import com.liskovsoft.mediaserviceinterfaces.yt.NotificationsService
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.yt.data.NotificationState
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.notifications.NotificationsServiceWrapper
import io.reactivex.Observable

object YouTubeNotificationsService: NotificationsService {
    private val mSignInService = YouTubeSignInService.instance()

    override fun getNotificationItems(): MediaGroup? {
        checkSigned()

        return NotificationsServiceWrapper.getItems()
    }

    override fun hideNotification(item: MediaItem?) {
        checkSigned()

        NotificationsServiceWrapper.hideNotification(item)
    }

    override fun setNotificationState(state: NotificationState?) {
        checkSigned()

        NotificationsServiceWrapper.modifyNotification(state)
    }

    override fun getNotificationItemsObserve(): Observable<MediaGroup> {
        return RxHelper.fromNullable { notificationItems }
    }

    override fun hideNotificationObserve(item: MediaItem?): Observable<Void> {
        return RxHelper.fromVoidable { hideNotification(item) }
    }

    override fun setNotificationStateObserve(state: NotificationState?): Observable<Void> {
        return RxHelper.fromVoidable { setNotificationState(state) }
    }

    private fun checkSigned() {
        mSignInService.checkAuth()
    }
}
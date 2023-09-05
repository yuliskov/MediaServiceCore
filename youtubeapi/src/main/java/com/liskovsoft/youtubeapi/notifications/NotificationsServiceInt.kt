package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.youtubeapi.common.models.impl.mediagroup.NotificationsMediaGroup
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

object NotificationsServiceInt {
    private val mService: NotificationsApi = RetrofitHelper.withGson(NotificationsApi::class.java)

    fun getItems(): MediaGroup? {
        val result = mService.getNotifications(NotificationsParams.getNotificationsQuery())

        return RetrofitHelper.get(result)?.let { NotificationsMediaGroup(it) }
    }
}
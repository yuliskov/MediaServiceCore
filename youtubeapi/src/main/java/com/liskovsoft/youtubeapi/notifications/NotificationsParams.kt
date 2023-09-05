package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

object NotificationsParams {
    fun getNotificationsQuery(): String {
        return ServiceHelper.createQueryWeb("\"notificationsMenuRequestType\":\"NOTIFICATIONS_MENU_REQUEST_TYPE_INBOX\"")
    }
}
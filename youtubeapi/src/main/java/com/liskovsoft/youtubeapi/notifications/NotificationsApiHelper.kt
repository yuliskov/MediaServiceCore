package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal object NotificationsApiHelper {
    fun getNotificationsQuery(): String {
        return ServiceHelper.createQueryTV("\"notificationsMenuRequestType\":\"NOTIFICATIONS_MENU_REQUEST_TYPE_INBOX\"")
    }

    fun getHideNotificationQuery(hideNotificationToken: String): String {
        return ServiceHelper.createQueryTV("\"serializedRecordNotificationInteractionsRequest\":\"$hideNotificationToken\"")
    }

    fun getModifyNotificationQuery(modifyNotificationParams: String): String {
        return ServiceHelper.createQueryTV("\"params\":\"$modifyNotificationParams\"")
    }
}
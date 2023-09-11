package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal object NotificationsApiHelper {
    fun getNotificationsQuery(): String {
        return ServiceHelper.createQueryWeb("\"notificationsMenuRequestType\":\"NOTIFICATIONS_MENU_REQUEST_TYPE_INBOX\"")
    }

    fun getHideNotificationQuery(hideNotificationToken: String): String {
        return ServiceHelper.createQueryWeb("\"serializedRecordNotificationInteractionsRequest\":\"$hideNotificationToken\"")
    }

    fun getModifyNotificationQuery(modifyNotificationParams: String): String {
        return ServiceHelper.createQueryWeb("\"params\":\"$modifyNotificationParams\"")
    }
}
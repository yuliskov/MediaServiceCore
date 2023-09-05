package com.liskovsoft.youtubeapi.notifications.gen

import com.liskovsoft.youtubeapi.common.models.gen.getText

fun NotificationsResult.getItems(): List<NotificationItem?>? =
    actions?.firstOrNull()?.openPopupAction?.popup?.multiPageMenuRenderer?.sections?.firstOrNull()?.multiPageMenuNotificationSectionRenderer?.items

fun NotificationItem.getVideoId() = notificationRenderer?.navigationEndpoint?.watchEndpoint?.videoId
fun NotificationItem.getThumbnails() = notificationRenderer?.videoThumbnail
fun NotificationItem.getTitle() = notificationRenderer?.shortMessage?.getText()
fun NotificationItem.getSecondTitle() = notificationRenderer?.sentTimeText?.getText()
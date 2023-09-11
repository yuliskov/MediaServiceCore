package com.liskovsoft.youtubeapi.notifications.gen

import com.liskovsoft.youtubeapi.common.models.gen.getNotificationToken
import com.liskovsoft.youtubeapi.common.models.gen.getText

internal fun NotificationsResult.getItems(): List<NotificationItem?>? =
    actions?.firstOrNull()?.openPopupAction?.popup?.multiPageMenuRenderer?.sections?.firstOrNull()?.multiPageMenuNotificationSectionRenderer?.items

internal fun NotificationItem.getVideoId() = notificationRenderer?.navigationEndpoint?.watchEndpoint?.videoId
internal fun NotificationItem.getThumbnails() = notificationRenderer?.videoThumbnail
internal fun NotificationItem.getTitle() = notificationRenderer?.shortMessage?.getText()
internal fun NotificationItem.getSecondTitle() = notificationRenderer?.sentTimeText?.getText()
internal fun NotificationItem.getNotificationToken() = notificationRenderer?.contextualMenu?.getNotificationToken()
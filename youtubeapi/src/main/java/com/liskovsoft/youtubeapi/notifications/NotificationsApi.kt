package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.youtubeapi.notifications.gen.NotificationsResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationsApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/notification/get_notification_menu")
    fun getNotifications(@Body notificationsQuery: String?): Call<NotificationsResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/notification/record_interactions")
    fun getHideNotification(@Body notificationsTokenQuery: String?): Call<Void>
}
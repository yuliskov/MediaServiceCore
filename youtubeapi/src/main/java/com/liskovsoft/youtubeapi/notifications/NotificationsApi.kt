package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import com.liskovsoft.youtubeapi.notifications.gen.NotificationsResult
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

@WithGson
internal interface NotificationsApi {
    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/notification/get_notification_menu")
    fun getNotifications(@Body notificationsQuery: String?): Call<NotificationsResult?>?

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/notification/record_interactions")
    fun getHideNotification(@Body hideNotificationQuery: String?): Call<Void>

    @Headers("Content-Type: application/json")
    @POST("https://www.youtube.com/youtubei/v1/notification/modify_channel_preference")
    fun getModifyNotification(@Body modifyNotificationQuery: String?): Call<Void>
}
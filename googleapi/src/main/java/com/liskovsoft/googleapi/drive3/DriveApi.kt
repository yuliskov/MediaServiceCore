package com.liskovsoft.googleapi.drive3

import com.liskovsoft.googleapi.drive3.data.GeneratedIds
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

internal interface DriveApi {
    /**
     * Metadata: https://developers.google.com/drive/api/reference/rest/v3/files#File
     */
    @Multipart
    @POST("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart")
    fun uploadFileAndMetadata(@Part("file") file: RequestBody, @Part("metadata") metadata: RequestBody): Call<Unit?>?

    @Headers("Content-Type: text/plain")
    @POST("https://www.googleapis.com/upload/drive/v3/files?uploadType=media")
    fun uploadFile(@Body file: RequestBody): Call<Unit?>?

    @GET("https://www.googleapis.com/drive/v3/files/generateIds")
    fun generateIds(): Call<GeneratedIds?>?
}
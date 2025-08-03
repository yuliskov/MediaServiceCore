package com.liskovsoft.googleapi.drive3

import com.liskovsoft.googleapi.drive3.data.FileMetadata
import com.liskovsoft.googleapi.drive3.data.GeneratedIds
import com.liskovsoft.googleapi.drive3.data.ListResult
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * https://developers.google.com/drive/api/reference/rest/v3
 */
@WithGson
internal interface DriveApi {
    /**
     * Metadata: https://developers.google.com/drive/api/reference/rest/v3/files#File
     */
    @Multipart
    @POST("https://www.googleapis.com/upload/drive/v3/files?uploadType=multipart")
    fun uploadFile(@Part("metadata") metadata: FileMetadata, @Part("file") file: RequestBody): Call<FileMetadata?>

    @Headers("Content-Type: text/plain")
    @POST("https://www.googleapis.com/upload/drive/v3/files?uploadType=media")
    fun uploadFile(@Body file: RequestBody): Call<FileMetadata?>

    @PATCH("https://www.googleapis.com/upload/drive/v3/files/{fileId}?uploadType=media")
    fun updateFile(@Path("fileId") fileId: String, @Body file: RequestBody): Call<FileMetadata?>

    @DELETE("https://www.googleapis.com/drive/v3/files/{fileId}")
    fun deleteFile(@Path("fileId") fileId: String): Call<Unit?>

    /**
     * https://developers.google.com/drive/api/reference/rest/v3/files/get
     */
    @GET("https://www.googleapis.com/drive/v3/files/{fileId}?alt=media")
    fun getFile(@Path("fileId") fileId: String): Call<ResponseBody?>

    @GET("https://www.googleapis.com/drive/v3/files/generateIds")
    fun generateIds(): Call<GeneratedIds?>

    @Headers("Content-Type: application/json")
    @POST("https://www.googleapis.com/drive/v3/files")
    fun createFolder(@Body metadata: FileMetadata): Call<FileMetadata?>

    /**
     * Query example: mimeType='text/plain' and parents in '1JfVoj74d1TBf-pAe8GxGVo0LTSWi6sNP'
     *
     * https://stackoverflow.com/questions/45432906/how-to-search-file-inside-a-specific-folder-in-google-api-v3
     *
     * https://developers.google.com/drive/api/guides/ref-search-terms
     *
     * https://developers.google.com/drive/api/reference/rest/v3/drives/list
     *
     * https://developers.google.com/drive/api/guides/mime-types
     *
     * https://developers.google.com/drive/api/guides/search-files
     *
     * https://developers.google.com/drive/api/reference/rest/v3/files/list
     */
    @GET("https://www.googleapis.com/drive/v3/files")
    fun getList(@Query("q") query: String): Call<ListResult?>
}
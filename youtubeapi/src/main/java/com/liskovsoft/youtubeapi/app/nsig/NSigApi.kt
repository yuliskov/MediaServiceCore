package com.liskovsoft.youtubeapi.app.nsig

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

internal interface NSigApi {
    @GET
    fun getFileContent(@Url fileUrl: String?): Call<FileContent?>?
}
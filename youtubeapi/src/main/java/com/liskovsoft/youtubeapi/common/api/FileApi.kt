package com.liskovsoft.youtubeapi.common.api

import com.liskovsoft.youtubeapi.common.converters.regexp.RegExpClass
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

@RegExpClass
internal interface FileApi {
    @GET
    fun getContent(@Url fileUrl: String?): Call<FileContent?>?

    @GET
    fun getHeaders(@Url fileUrl: String?): Call<Void?>?
}
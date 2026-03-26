package com.liskovsoft.googlecommon.common.constants

import com.liskovsoft.googlecommon.common.constants.data.ConstantsResult
import com.liskovsoft.googlecommon.common.converters.gson.WithGson
import retrofit2.Call
import retrofit2.http.GET

@WithGson
internal interface ConstantsApi {
    @GET("https://github.com/yuliskov/SmartTube/releases/download/latest/constants.json")
    fun getConstants(): Call<ConstantsResult?>
}
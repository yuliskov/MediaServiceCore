package com.liskovsoft.youtubeapi.next.v2.mock

import co.infinum.retromock.Retromock
import com.liskovsoft.sharedutils.TestHelpers
import com.liskovsoft.googlecommon.common.converters.gson.GsonConverterFactory
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

object MockUtils {
    fun <T> mockWithGson(clazz: Class<T>): T {
        val builder = Retromock.Builder()
        val retrofit = builder.retrofit(RetrofitHelper.buildRetrofit(GsonConverterFactory.create()))
        val defaultBodyFactory = retrofit.defaultBodyFactory { TestHelpers.openResource(it) }
        val build = defaultBodyFactory.build()

        return build.create(clazz)
    }
}
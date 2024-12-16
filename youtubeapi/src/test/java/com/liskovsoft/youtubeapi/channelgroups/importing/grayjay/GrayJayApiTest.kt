package com.liskovsoft.youtubeapi.channelgroups.importing.grayjay

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen.GrayJayGroup
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import java.lang.IllegalStateException

@RunWith(RobolectricTestRunner::class)
class GrayJayApiTest {
    private lateinit var mFileService: FileApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mFileService = RetrofitHelper.create(FileApi::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testRawResponse() {
        val content = mFileService.getContent("https://github.com/yuliskov/SmartTube/releases/download/latest/grayjay_subscription_groups")

        val grayJayContent = RetrofitHelper.get(content)?.content

        // replace:
        // "{ => {
        // }" => }
        // \" => "

        val grayJayContentFixed = grayJayContent
            ?.replace("\"{", "{")
            ?.replace("}\"", "}")
            ?.replace("\\\"", "\"")

        val gson = Gson()
        val listType = object : TypeToken<List<GrayJayGroup>>() {}.type

        val response: List<GrayJayGroup> = gson.fromJson(grayJayContentFixed, listType)

        assertTrue("Response not empty", response.isNotEmpty())
    }

    @Test
    fun testWrongResponse() {
        val content = mFileService.getContent("https://github.com/yuliskov/SmartTube/releases/download/latest/pockettube.json")

        val grayJayContent = RetrofitHelper.get(content)?.content

        // replace:
        // "{ => {
        // }" => }
        // \" => "

        val grayJayContentFixed = grayJayContent
            ?.replace("\"{", "{")
            ?.replace("}\"", "}")
            ?.replace("\\\"", "\"")

        val gson = Gson()
        val listType = object : TypeToken<List<GrayJayGroup>>() {}.type

        val response: List<GrayJayGroup> = try {
            gson.fromJson(grayJayContentFixed, listType)
        } catch (e: JsonSyntaxException) {
            return
        }

        assertTrue("Response not empty", response.isNotEmpty())
    }
}
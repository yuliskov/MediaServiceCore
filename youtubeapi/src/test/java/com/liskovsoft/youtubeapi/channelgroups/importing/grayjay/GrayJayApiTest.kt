package com.liskovsoft.youtubeapi.channelgroups.importing.grayjay

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.liskovsoft.sharedutils.TestHelpers
import com.liskovsoft.youtubeapi.channelgroups.importing.grayjay.gen.GrayJayGroup
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class GrayJayApiTest {
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
    }

    @Test
    fun testRawResponse() {
        val grayJayContent = TestHelpers.readResource("channelgroups/grayjay_subscription_groups")

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
        val grayJayContent = TestHelpers.readResource("channelgroups/grayjay_subscription_groups")

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
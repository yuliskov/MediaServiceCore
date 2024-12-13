package com.liskovsoft.youtubeapi.channelgroups.grayjay

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import org.junit.Assert
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class GrayJayApiTest {
    private lateinit var mService: GrayJayApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.create(GrayJayApi::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testChannelGroupsResponse() {
        val groups = mService.getChannelGroups("https://github.com/yuliskov/SmartTube/releases/download/latest/grayjay_subscription_groups2")

        val response = RetrofitHelper.get(groups)

        assertTrue("Response not empty", response?.isNotEmpty() == true)
    }

    @Test
    fun testRawResponse() {
        // replace:
        // "{ => {
        // }" => }
        // \" => "

        // parse Gson
    }
}
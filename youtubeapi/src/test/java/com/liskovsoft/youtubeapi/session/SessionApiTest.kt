package com.liskovsoft.youtubeapi.session

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.session.models.SessionDataResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class SessionApiTest {
    private lateinit var mSessionApi: SessionApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.disableCompression = true
        mSessionApi = RetrofitHelper.create(SessionApi::class.java)
    }

    @Test
    fun testThatSessionDataNotEmpty() {
        val sessionData = getSessionDataResult()

        Assert.assertNotNull("ytcfg not null", sessionData?.ytcfg?.apiKey)
    }

    @Test
    fun testThatDeviceInfoNotEmpty() {
        val sessionData = getSessionDataResult()

        Assert.assertNotNull("Device info not null", sessionData?.ytcfg?.deviceInfo)
        Assert.assertNotNull("Has visitor data", sessionData?.ytcfg?.deviceInfo?.visitorData)
    }

    private fun getSessionDataResult(): SessionDataResult? {
        val sessionDataResult = mSessionApi.getSessionData(SessionApiHelper.createSessionDataHeaders())
        return RetrofitHelper.get(sessionDataResult)
    }
}
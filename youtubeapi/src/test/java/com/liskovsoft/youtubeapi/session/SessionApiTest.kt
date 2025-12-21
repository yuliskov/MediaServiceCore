package com.liskovsoft.youtubeapi.session

import com.google.gson.GsonBuilder
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.session.models.ContextInfo
import com.liskovsoft.youtubeapi.session.models.InnertubeConfigResult
import com.liskovsoft.youtubeapi.session.models.SessionArgs
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
    private lateinit var mInnertubeConfigApi: InnertubeConfigApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.disableCompression = true
        mSessionApi = RetrofitHelper.create(SessionApi::class.java)
        mInnertubeConfigApi = RetrofitHelper.create(InnertubeConfigApi::class.java)
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

    @Test
    fun testThatInnertubeConfigNotEmpty() {
        val innertubeConfig = retrieveInnertubeConfig()

        Assert.assertNotNull("innertubeConfig not null", innertubeConfig)
    }

    private fun getSessionDataResult(): SessionDataResult? {
        val sessionDataResult = mSessionApi.getSessionData(SessionApiHelper.createSessionDataHeaders())
        return RetrofitHelper.get(sessionDataResult)
    }

    private fun retrieveInnertubeConfig(): InnertubeConfigResult? {
        val sessionData = getSessionDataResult() ?: return null
        val deviceInfo = sessionData.ytcfg?.deviceInfo ?: return null
        val options = SessionArgs()

        val contextInfo = ContextInfo(options, deviceInfo)
        val gson = GsonBuilder().create() // nulls are ignored by default
        val json = gson.toJson(mapOf("context" to contextInfo))

        val innertubeConfigResult =
            mInnertubeConfigApi.retrieveInnertubeConfig(SessionApiHelper.createInnertubeConfigHeaders(sessionData), json)
        return RetrofitHelper.get(innertubeConfigResult)
    }
}
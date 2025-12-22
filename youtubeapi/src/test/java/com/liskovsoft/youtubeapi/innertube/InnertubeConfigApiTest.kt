package com.liskovsoft.youtubeapi.innertube

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.innertube.models.ContextInfo
import com.liskovsoft.youtubeapi.innertube.models.InnertubeConfigResult
import com.liskovsoft.youtubeapi.innertube.models.SessionArgs
import com.liskovsoft.youtubeapi.innertube.models.SessionDataProcessed
import com.liskovsoft.youtubeapi.innertube.models.SessionDataResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class InnertubeConfigApiTest {
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
        val sessionData = getSessionDataResult()
        Assert.assertNotNull("session data not null", sessionData)
        val deviceInfo = sessionData!!.deviceInfo
        Assert.assertNotNull("session data/device info not null", deviceInfo)

        val options = SessionArgs()
        val context = ContextInfo(options, deviceInfo!!) // needed later

        val innertubeConfig = retrieveInnertubeConfig(sessionData, context)

        Assert.assertNotNull("innertubeConfig not null", innertubeConfig)
        Assert.assertNotNull("Has coldConfigData", innertubeConfig!!.responseContext?.globalConfigGroup?.rawColdConfigGroup?.configData)
        Assert.assertNotNull("Has coldHashData", innertubeConfig.responseContext?.globalConfigGroup?.coldHashData)
        Assert.assertNotNull("Has hotHashData", innertubeConfig.responseContext?.globalConfigGroup?.hotHashData)
        Assert.assertNotNull("Has configData", innertubeConfig.configData)
    }

    @Test
    fun testCreateProcessedSessionData() {
        val sessionData = getSessionDataResult()
        Assert.assertNotNull("session data not null", sessionData)
        val deviceInfo = sessionData!!.deviceInfo
        Assert.assertNotNull("session data/device info not null", deviceInfo)

        val options = SessionArgs()
        val context = ContextInfo(options, deviceInfo!!) // needed later

        val innertubeConfig = retrieveInnertubeConfig(sessionData, context)
        val coldConfigData = innertubeConfig?.responseContext?.globalConfigGroup?.rawColdConfigGroup?.configData
        val coldHashData = innertubeConfig?.responseContext?.globalConfigGroup?.coldHashData
        val hotHashData = innertubeConfig?.responseContext?.globalConfigGroup?.hotHashData
        val configData = innertubeConfig?.configData

        val sessionDataProcessed = SessionDataProcessed(
            sessionData.apiKey!!,
            "v1", // Constants.CLIENTS.WEB.API_VERSION
            configData!!,
            context.apply {
                client.configInfo!!.coldConfigData = coldConfigData!!
                client.configInfo.coldHashData = coldHashData!!
                client.configInfo.hotHashData = hotHashData!!
            }
        )
    }

    private fun getSessionDataResult(): SessionDataResult? {
        val sessionDataResult = mSessionApi.getSessionData(InnertubeApiHelper.createSessionDataHeaders())
        return RetrofitHelper.get(sessionDataResult)
    }

    private fun retrieveInnertubeConfig(sessionData: SessionDataResult, context: ContextInfo): InnertubeConfigResult? {
        val innertubeConfigResult =
            mInnertubeConfigApi.retrieveInnertubeConfig(
                InnertubeApiHelper.createInnertubeConfigHeaders(sessionData),
                InnertubeApiHelper.createInnertubeJsonConfig(context)
            )
        return RetrofitHelper.get(innertubeConfigResult)
    }
}
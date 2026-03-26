package com.liskovsoft.googlecommon.common.constants

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class ConstantsServiceTest {
    private lateinit var mService: ConstantsApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")

        ShadowLog.stream = System.out // catch Log class output

        mService = RetrofitHelper.create(ConstantsApi::class.java)

        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testThatConstantsNotEmpty() {
        val constants = RetrofitHelper.get(mService.getConstants())

        Assert.assertNotNull("Has clientId", constants?.clientId)
        Assert.assertNotNull("Has clientSecret", constants?.clientSecret)
        Assert.assertNotNull("Has youtubeDataApiKey", constants?.youtubeDataApiKey)
    }
}
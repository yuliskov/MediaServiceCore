package com.liskovsoft.youtubeapi.dearrow

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class DeArrowApiTest {
    companion object {
        private const val VIDEO_ID = "0e3GPea1Tyg" // Mr. Beast
    }
    private var mService: DeArrowApi? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.create(DeArrowApi::class.java)
    }

    @Test
    fun testThatBrandingResultNotEmpty() {
        val wrapper = mService!!.getBranding(VIDEO_ID)
        val brandingList = RetrofitHelper.get(wrapper)
        val titles = brandingList?.titles
        val thumbnails = brandingList?.thumbnails
        Assert.assertFalse("Title list not empty", titles?.isEmpty() ?: true)
        Assert.assertFalse("Thumbnails list not empty", thumbnails?.isEmpty() ?: true)
    }
}
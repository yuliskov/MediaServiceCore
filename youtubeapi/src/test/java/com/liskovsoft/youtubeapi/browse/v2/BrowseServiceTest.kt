package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseServiceTest {
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
    }

    @Test
    fun testThatShortsNotEmpty() {
        val sections = BrowseService2.getHome()

        val shorts = sections?.get(1)

        assertTrue("Shorts not empty", shorts?.mediaItems?.isNotEmpty() == true)
    }

    @Test
    fun testThatHomeContainsFeedbackTokens() {
        val sections = BrowseService2.getHome()

        val home = sections?.get(0)

        val mediaItem = home?.mediaItems?.get(0)

        assertTrue("Home row contains token 1", mediaItem?.feedbackToken != null)
        assertTrue("Home row contains token 2", mediaItem?.feedbackToken2 != null)
    }
}
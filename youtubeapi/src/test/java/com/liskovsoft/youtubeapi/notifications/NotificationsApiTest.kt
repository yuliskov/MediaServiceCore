package com.liskovsoft.youtubeapi.notifications

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.notifications.gen.NotificationsResult
import com.liskovsoft.youtubeapi.notifications.gen.getItems
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import org.junit.Assert.assertNotNull
import org.junit.Ignore

@Ignore("Won't work with TV auth headers")
@RunWith(RobolectricTestRunner::class)
class NotificationsApiTest {
    /**
     * Authorization should be updated each hour
     */
    private var mService: NotificationsApi? = null

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.create(NotificationsApi::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpers.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testThatNotificationsContainNeededItems() {
        val notifications: NotificationsResult? = getNotifications()

        assertNotNull("Contains content", notifications?.getItems())
    }

    private fun getNotifications(): NotificationsResult? {
        val result = mService?.getNotifications(NotificationsApiHelper.getNotificationsQuery())

        return RetrofitHelper.get(result)
    }
}
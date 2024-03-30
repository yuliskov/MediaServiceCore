package com.liskovsoft.googleapi.drive3

import com.liskovsoft.googleapi.common.helpers.RetrofitHelper
import com.liskovsoft.googleapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googleapi.common.helpers.tests.TestHelpersV2
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class DriveApiTest {
    private lateinit var mService: DriveApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")

        ShadowLog.stream = System.out // catch Log class output

        mService = RetrofitHelper.withGson(DriveApi::class.java)

        RetrofitOkHttpHelper.disableCompression = true
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
    }

    @Test
    fun testGenerateIds() {
        val generatedIds = RetrofitHelper.get(mService.generateIds())
        assertTrue("Has ids", (generatedIds?.ids?.size ?: 0) > 0)
    }

    @Test
    fun testCreateFolder() {
        TODO("Not yet implemented")
    }

    @Test
    fun testUploadFile() {
        // RequestBody file = RequestBody.create(MediaType.parse("text/plain"), file);
        // RequestBody file = RequestBody.create(MediaType.parse("application/json"), file);
    }
}
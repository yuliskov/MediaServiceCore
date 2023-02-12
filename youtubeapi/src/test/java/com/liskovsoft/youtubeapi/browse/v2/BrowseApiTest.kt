package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.browse.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.browse.v2.gen.getItems
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpClient
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.youtubeapi.common.models.gen.getFeedbackToken
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseApiTest {
    /**
     * Authorization should be updated each hour
     */
    private var mService: BrowseApi? = null

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mService = RetrofitHelper.withGson(BrowseApi::class.java)
        RetrofitOkHttpClient.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
    }

    @Test
    fun testThatSubsNotEmpty() {
        val subsResult = mService?.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        val subs = RetrofitHelper.get(subsResult)

        assertNotNull("Contains videos", subs?.getItems()?.getOrNull(0))
    }

    @Test
    fun testThatSubContainsFeedbackToken() {
        val subsResult = mService?.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        val subs = RetrofitHelper.get(subsResult)

        assertNotNull("Contains feedback token", subs?.getItems()?.getOrNull(0)?.getFeedbackToken())
    }

    @Test
    fun testThatSubsCanBeContinued() {
        val subsResult = mService?.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        val subs = RetrofitHelper.get(subsResult)

        assertNotNull("Contains continuation token", subs?.getContinuationToken())

        val continuationResult = mService?.getContinuationResult(BrowseApiHelper.getContinuationQueryWeb(subs?.getContinuationToken()))

        val continuation = RetrofitHelper.get(continuationResult)

        assertNotNull("Contains items", continuation?.getItems()?.getOrNull(0))

        assertNotNull("Contains continuation token", continuation?.getContinuationToken())
    }
}
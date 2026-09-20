package com.liskovsoft.youtubeapi.panel

import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.browse.v2.gen.getFeedbackToken
import com.liskovsoft.youtubeapi.next.v2.gen.getMenuItems
import com.liskovsoft.youtubeapi.panel.gen.PanelResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

private const val FEEDBACK_PANEL_ID = "PAcontext_menu"
private const val FEEDBACK_PANEL_PARAMS = "-golCgtOTzZaQjlpRjhrYyCeAUITCAISD0ZFd2hhdF90b193YXRjaA%3D%3D"

@RunWith(RobolectricTestRunner::class)
class PanelApiTest {
    private lateinit var mPanelApi: PanelApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mPanelApi = RetrofitHelper.create(PanelApi::class.java)
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpers.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testThatPanelNotEmpty() {
        Assert.assertNotNull("Panel result not empty", getPanelResult()?.content?.engagementPanelSectionListRenderer)
    }

    @Test
    fun testThatPanelContainsFeedbackTokens() {
        val result = getPanelResult()

        val items = result?.content?.getMenuItems()

        val tokens = items?.mapNotNull { it?.getFeedbackToken() }

        Assert.assertTrue("Contains 2 tokens: not interested and not recommended", (tokens?.size ?: 0) == 2)
    }

    private fun getPanelResult(): PanelResult? {
        val panelResultWrapper = mPanelApi.getPanel(
            PanelApiHelper.getPanelQuery(FEEDBACK_PANEL_ID, FEEDBACK_PANEL_PARAMS))

        return RetrofitHelper.get(panelResultWrapper)
    }
}
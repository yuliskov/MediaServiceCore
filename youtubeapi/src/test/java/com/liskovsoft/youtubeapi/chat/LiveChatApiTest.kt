package com.liskovsoft.youtubeapi.chat

import com.liskovsoft.youtubeapi.chat.gen.kt.LiveChatResult
import com.liskovsoft.youtubeapi.chat.helpers.getActions
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import org.junit.Assert.assertNotNull

@RunWith(RobolectricTestRunner::class)
class LiveChatApiTest {
    companion object {
        private const val LIVE_CHAT_KEY =
            "0ofMyANuGlhDaWtxSndvWVZVTnFUR1JqY1d3dGVrdHFaWFpwUjJ4cVRURlNUVWhCRWd0WlJtZFJVMUI1U2sxNGR4b1Q2cWpkdVFFTkNndFpSbWRSVTFCNVNrMTRkeUFCMAGCAQIIBKABkc7B-pLb-AKoAQA%3D"
    }
    private var mApi: LiveChatApi? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mApi = RetrofitHelper.withGson(LiveChatApi::class.java)
    }

    @Test
    fun testThatLiveChatResultIsNotEmpty() {
        val liveChatResult = getLiveChatResult()

        assertNotNull("chat result not null", liveChatResult)
        assertNotNull("has actions", liveChatResult?.getActions()?.getOrNull(0))
    }

    private fun getLiveChatResult(): LiveChatResult? {
        val chatQuery = LiveChatApiParams.getLiveChatQuery(LIVE_CHAT_KEY)
        val wrapper = mApi!!.getLiveChat(chatQuery)
        return RetrofitHelper.get(wrapper)
    }
}
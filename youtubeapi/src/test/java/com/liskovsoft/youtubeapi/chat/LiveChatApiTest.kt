package com.liskovsoft.youtubeapi.chat

import com.liskovsoft.youtubeapi.chat.gen.kt.LiveChatResult
import com.liskovsoft.youtubeapi.chat.helpers.getActions
import com.liskovsoft.youtubeapi.chat.helpers.getContinuation
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
            "0ofMyAN0GlhDaWtxSndvWVZVTnBjWFJVWW5veGR6Vk9jRzVGVGxFelRHZzFNM2xSRWd0RldGUllXRXBtT1dWaFFSb1Q2cWpkdVFFTkNndEZXRlJZV0VwbU9XVmhRU0FCMAGCAQIIBIgBAaABmN2Q297f-AKoAQCyAQA%3D"
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
        val liveChatResult = getLiveChatResult(LIVE_CHAT_KEY)

        assertNotNull("chat result not null", liveChatResult)
        assertNotNull("has actions", liveChatResult?.getActions()?.getOrNull(0))
    }

    @Test
    fun testThatContinuationIsWorking() {
        var liveChatResult = getLiveChatResult(LIVE_CHAT_KEY)
        var timeoutMs = liveChatResult?.getContinuation()?.timeoutMs ?: -1
        var nextChatResult: LiveChatResult? = null

        for (i in 1..6) {
            if (timeoutMs != -1) {
                Thread.sleep(timeoutMs.toLong())
                liveChatResult = getLiveChatResult(liveChatResult?.getContinuation()?.continuation)
                timeoutMs = liveChatResult?.getContinuation()?.timeoutMs ?: -1

                if (liveChatResult?.getActions()?.getOrNull(0) != null) {
                    nextChatResult = liveChatResult
                    break
                }
            }
        }

        assertNotNull("has actions", nextChatResult?.getActions()?.getOrNull(0))
    }

    private fun getLiveChatResult(chatKey: String?): LiveChatResult? {
        if (chatKey.isNullOrEmpty()) {
            return null
        }

        val chatQuery = LiveChatApiParams.getLiveChatQuery(chatKey)
        val wrapper = mApi!!.getLiveChat(chatQuery)
        return RetrofitHelper.get(wrapper)
    }
}
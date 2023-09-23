package com.liskovsoft.youtubeapi.chat

import com.liskovsoft.youtubeapi.chat.gen.LiveChatResult
import com.liskovsoft.youtubeapi.chat.gen.getActions
import com.liskovsoft.youtubeapi.chat.gen.getContinuation
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.youtubeapi.next.v2.WatchNextApi
import com.liskovsoft.youtubeapi.next.v2.WatchNextApiHelper
import com.liskovsoft.youtubeapi.next.v2.gen.getLiveChatToken
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog
import org.junit.Assert.assertNotNull

@RunWith(RobolectricTestRunner::class)
class LiveChatApiTest {
    private var mApi: LiveChatApi? = null
    private var mApi2: WatchNextApi? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mApi = RetrofitHelper.withGson(LiveChatApi::class.java)
        mApi2 = RetrofitHelper.withGson(WatchNextApi::class.java)
    }

    @Test
    fun testThatLiveChatResultIsNotEmpty() {
        val watchNextResult = mApi2?.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(TestHelpersV2.VIDEO_ID_LIVE))
        val watchNext = watchNextResult?.execute()?.body()

        val liveChatResult = getLiveChatResult(watchNext?.getLiveChatToken())

        assertNotNull("chat result not null", liveChatResult)
        assertNotNull("has actions", liveChatResult?.getActions()?.getOrNull(0))
    }

    @Test
    fun testThatContinuationIsWorking() {
        val watchNextResult = mApi2?.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(TestHelpersV2.VIDEO_ID_LIVE))
        val watchNext = watchNextResult?.execute()?.body()

        var liveChatResult = getLiveChatResult(watchNext?.getLiveChatToken())
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
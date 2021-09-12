package com.liskovsoft.youtubeapi.next.v2

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.junit.Before
import org.robolectric.shadows.ShadowLog
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.next.v1.WatchNextManagerParams
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1
import org.junit.Assert
import org.junit.Test

@RunWith(RobolectricTestRunner::class)
class WatchNextManagerUnsignedTest {
    private var mManager: WatchNextManagerUnsigned? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mManager = RetrofitHelper.withGson(WatchNextManagerUnsigned::class.java)
    }

    @Test
    fun testSuggestedItemsNotNull() {
        val watchNextQuery = WatchNextManagerParams.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS)
        val wrapper = mManager!!.getWatchNextResult(watchNextQuery)
        val watchNextResult = RetrofitHelper.get(wrapper)

        Assert.assertNotNull("watchNextResult not null", watchNextResult)
    }

//    @Test
//    fun testThatWatchNextContainsAllRequiredFields() {
//        checkWatchNextResultFields(watchNextResult)
//    }
//
//    @Test
//    fun testThatResultProperlyLocalized() {
//        LocaleManager.instance().language = "en"
//        var watchNextResult = watchNextResult
//        var firstSuggesting = watchNextResult.suggestedSections[0]
//        Assert.assertEquals("Suggestion title is localized to english", "Suggestions", firstSuggesting.title)
//        LocaleManager.instance().language = "ru"
//        watchNextResult = watchNextResult
//        firstSuggesting = watchNextResult.suggestedSections[0]
//        Assert.assertEquals("Suggestion title is localized to russian", "Рекомендации", firstSuggesting.title)
//    }
//
//    @Test
//    fun testThatWatchNextRowsCouldBeContinued() {
//        val watchNextResult = watchNextResult
//        val rootNextPageKey = watchNextResult.suggestedSections[0].nextPageKey
//        Assert.assertNotNull("Root contains next key", rootNextPageKey)
//        val continuation = continueWatchNext(rootNextPageKey)
//        val nextPageKey = continuation.nextPageKey
//        Assert.assertNotNull("Continuations contains next key", nextPageKey)
//    }
//
//    private fun continueWatchNext(nextPageKey: String): WatchNextResultContinuation {
//        val wrapper = mManager!!.continueWatchNextResult(BrowseManagerParams.getContinuationQuery(nextPageKey))
//        return RetrofitHelper.get(wrapper)
//    }
//
//    private val watchNextResult: WatchNextResult
//        private get() {
//            val wrapper = mManager!!.getWatchNextResult(WatchNextManagerParams.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS))
//            return RetrofitHelper.get(wrapper)
//        }
}
package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1
import com.liskovsoft.youtubeapi.next.v1.WatchNextManagerParams
import com.liskovsoft.youtubeapi.next.v2.gen.kt.WatchNextResult
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class WatchNextManagerTest {
    private var mManager: WatchNextManager? = null
    private var mServiceV2: WatchNextServiceV2? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mManager = RetrofitHelper.withGson(WatchNextManager::class.java)
        mServiceV2 = WatchNextServiceV2.instance()
    }

    @Test
    fun testThatWatchNextResultNotNull() {
        val watchNextResult = getWatchNextResult()

        assertNotNull("watchNextResult not null", watchNextResult)
    }

    @Test
    fun testThatWatchNextResultCanBeConverted() {
        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Test
    fun testThatSuggestedContinuationNotNull() {
        val metadata = getMediaItemMetadataUnsigned()

        val firstGroup = metadata?.suggestions?.first()
        val continuation = mServiceV2!!.continueGroup(firstGroup)

        assertNotNull("Continuation not null", continuation)
    }

    @Test
    fun testThatMetadataContainsRequiredFields() {
        val metadata = getMediaItemMetadataUnsigned();

        testBaseFields(metadata)
    }

    private fun testBaseFields(metadata: MediaItemMetadata?) {
        assertNotNull("Contains title", metadata?.title)
        assertNotNull("Contains desc", metadata?.description)
        assertNotNull("Contains desc alt", metadata?.descriptionAlt)
        assertNotNull("Contains desc full", metadata?.fullDescription)
        assertNotNull("Contains video id", metadata?.videoId)
        assertNotNull("Contains view count", metadata?.viewCount)
        assertNotNull("Contains date", metadata?.publishedDate)
    }

    private fun getMediaItemMetadataUnsigned() = mServiceV2!!.getMetadata(TestHelpersV1.VIDEO_ID_CAPTIONS)

    private fun getWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextManagerParams.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS)
        val wrapper = mManager!!.getWatchNextResultUnsigned(watchNextQuery)
        return RetrofitHelper.get(wrapper)
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
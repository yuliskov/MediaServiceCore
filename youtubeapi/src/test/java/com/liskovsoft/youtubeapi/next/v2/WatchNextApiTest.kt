package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1
import com.liskovsoft.youtubeapi.next.v2.WatchNextApiHelper
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.mock.MockUtils
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock2
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class WatchNextApiTest {
    private var mApi: WatchNextApi? = null
    private var mManagerMock: WatchNextApiMock? = null
    private var mManagerMock2: WatchNextApiMock2? = null
    private var mService: WatchNextService? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mApi = RetrofitHelper.withGson(WatchNextApi::class.java)
        mService = WatchNextService.instance()

        mManagerMock = MockUtils.mockWithGson(WatchNextApiMock::class.java)
        mManagerMock2 = MockUtils.mockWithGson(WatchNextApiMock2::class.java)
    }

    @Test
    fun testThatMockedWatchNextResultNotNull() {
        val watchNextResult = getMockedWatchNextResult()

        assertNotNull("watchNextResult not null", watchNextResult)
    }

    @Test
    fun testThatWatchNextResultNotNull() {
        val watchNextResult = getWatchNextResult()

        assertNotNull("watchNextResult not null", watchNextResult)
    }

    @Test
    fun testThatMockedWatchNextResultCanBeConverted() {
        mService!!.setWatchNextManager(mManagerMock as WatchNextApi)

        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty() {
        mService!!.setWatchNextManager(mManagerMock as WatchNextApi)

        val metadata = getMediaItemMetadataUnsigned()

        val anyItem = metadata?.suggestions?.getOrNull(0)?.mediaItems?.getOrNull(0)

        assertNotNull("anyItem isn't null", anyItem)
    }

    @Test
    fun testThatMockedWatchNextResultCanBeConverted2() {
        mService!!.setWatchNextManager(mManagerMock2 as WatchNextApi)

        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty2() {
        // Testing groups inside a Chip

        mService!!.setWatchNextManager(mManagerMock2 as WatchNextApi)

        val metadata = getMediaItemMetadataUnsigned()

        val firstGroup = metadata?.suggestions?.getOrNull(0)

        assertNotNull("Group inside a Chip has a title", firstGroup?.title)

        val anyItem = firstGroup?.mediaItems?.getOrNull(0)

        assertNotNull("anyItem isn't null", anyItem)
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
        val continuation = mService!!.continueGroup(firstGroup)

        assertNotNull("Continuation not null", continuation)
    }

    @Test
    fun testThatMetadataContainsRequiredFields() {
        val metadata = getMediaItemMetadataUnsigned();

        testBaseFields(metadata)
    }

    private fun testBaseFields(metadata: MediaItemMetadata?) {
        assertNotNull("Contains title", metadata?.title)
        assertNotNull("Contains desc", metadata?.secondTitle)
        assertNotNull("Contains desc alt", metadata?.secondTitleAlt)
        assertNotNull("Contains desc full", metadata?.description)
        assertNotNull("Contains video id", metadata?.videoId)
        assertNotNull("Contains view count", metadata?.viewCount)
        assertNotNull("Contains date", metadata?.publishedDate)
    }

    private fun getMediaItemMetadataUnsigned() = mService!!.getMetadata(TestHelpersV1.VIDEO_ID_CAPTIONS)

    private fun getWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextApiHelper.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS)
        val wrapper = mApi!!.getWatchNextResult(watchNextQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun getMockedWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextApiHelper.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS)
        val wrapper = mManagerMock!!.getWatchNextResult(watchNextQuery)
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
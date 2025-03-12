package com.liskovsoft.youtubeapi.next.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV1
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.mock.MockUtils
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock2
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock3
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class WatchNextApiTest {
    private var mApi: WatchNextApi? = null
    private var mApiMock: WatchNextApiMock? = null
    private var mApiMock2: WatchNextApiMock2? = null
    private var mApiMock3: WatchNextApiMock3? = null
    private var mService: WatchNextService? = null
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mApi = RetrofitHelper.create(WatchNextApi::class.java)
        mService = WatchNextService()

        mApiMock = MockUtils.mockWithGson(WatchNextApiMock::class.java)
        mApiMock2 = MockUtils.mockWithGson(WatchNextApiMock2::class.java)
        mApiMock3 = MockUtils.mockWithGson(WatchNextApiMock3::class.java)
        RetrofitOkHttpHelper.disableCompression = true
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
        mService!!.setWatchNextApi(mApiMock as WatchNextApi)

        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty() {
        mService!!.setWatchNextApi(mApiMock as WatchNextApi)

        val metadata = getMediaItemMetadataUnsigned()

        val anyItem = metadata?.suggestions?.getOrNull(0)?.mediaItems?.getOrNull(0)

        assertNotNull("anyItem isn't null", anyItem)
    }

    @Test
    fun testThatMockedWatchNextResultCanBeConverted2() {
        testThatMockedWatchNextResultCanBeConverted(mApiMock2 as WatchNextApi)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty2() {
        testThatMockedWatchNextSuggestionsNotEmpty(mApiMock2 as WatchNextApi)
    }

    @Test
    fun testThatMockedWatchNextResultCanBeConverted3() {
        testThatMockedWatchNextResultCanBeConverted(mApiMock3 as WatchNextApi)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty3() {
        testThatMockedWatchNextSuggestionsNotEmpty(mApiMock3 as WatchNextApi)
    }

    @Test
    fun testThatWatchNextResultCanBeConverted() {
        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Test
    fun testThatSuggestedContinuationNotNull() {
        val metadata = getMediaItemMetadataUnsigned()

        val continuation = mService!!.continueGroup(getContinuableRow(metadata))

        assertNotNull("Continuation not null", continuation)
    }

    @Test
    fun testThatMetadataContainsRequiredFields() {
        val metadata = getMediaItemMetadataUnsigned();

        testBaseFields(metadata)
    }

    @Test
    fun testThatDislikesIsWorking() {
        val dislikes = mApi!!.getDislikes("KIeAtU-Toxw")
        val dislikesResult = RetrofitHelper.get(dislikes)

        assertNotNull("Contains dislikes", dislikesResult?.dislikes)
        assertTrue("Dislikes count bigger than zero", (dislikesResult?.dislikes ?: 0) > 0)
    }

    private fun testBaseFields(metadata: MediaItemMetadata?) {
        assertNotNull("Contains title", metadata?.title)
        assertNotNull("Contains desc", metadata?.secondTitle)
        //assertNotNull("Contains desc alt", metadata?.secondTitleAlt)
        assertNotNull("Contains desc full", metadata?.description)
        assertNotNull("Contains video id", metadata?.videoId)
        assertNotNull("Contains view count", metadata?.viewCount)
        assertNotNull("Contains date", metadata?.publishedDate)
        assertNotNull("Contains subs count", metadata?.subscriberCount)
    }

    private fun getMediaItemMetadataUnsigned() = mService!!.getMetadata(TestHelpersV1.VIDEO_ID_CAPTIONS)

    private fun getWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextApiHelper.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS)
        val wrapper = mApi!!.getWatchNextResult(watchNextQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun getMockedWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextApiHelper.getWatchNextQuery(TestHelpersV1.VIDEO_ID_CAPTIONS)
        val wrapper = mApiMock!!.getWatchNextResult(watchNextQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun testThatMockedWatchNextResultCanBeConverted(api: WatchNextApi) {
        mService!!.setWatchNextApi(api)

        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    private fun testThatMockedWatchNextSuggestionsNotEmpty(api: WatchNextApi) {
        // Testing groups inside a Chip

        mService!!.setWatchNextApi(api)

        val metadata = getMediaItemMetadataUnsigned()

        val firstGroup = metadata?.suggestions?.getOrNull(0)

        assertNotNull("Group inside a Chip has a title", firstGroup?.title)

        val anyItem = firstGroup?.mediaItems?.getOrNull(0)

        assertNotNull("anyItem isn't null", anyItem)
    }

    private fun getContinuableRow(metadata: MediaItemMetadata?): MediaGroup? {
        metadata?.suggestions?.forEach {
            if (YouTubeHelper.extractNextKey(it) != null) return it
        }

        return null
    }
}
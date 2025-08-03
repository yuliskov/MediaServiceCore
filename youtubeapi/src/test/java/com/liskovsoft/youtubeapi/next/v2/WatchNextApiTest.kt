package com.liskovsoft.youtubeapi.next.v2

import androidx.test.platform.app.InstrumentationRegistry
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import com.liskovsoft.youtubeapi.next.v2.gen.WatchNextResult
import com.liskovsoft.youtubeapi.next.v2.mock.MockUtils
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock2
import com.liskovsoft.youtubeapi.next.v2.mock.WatchNextApiMock3
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class WatchNextApiTest {
    private lateinit var mApi: WatchNextApi
    private lateinit var mApiMock: WatchNextApiMock
    private lateinit var mApiMock2: WatchNextApiMock2
    private lateinit var mApiMock3: WatchNextApiMock3
    private lateinit var mWatchNextService: WatchNextService
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output

        GlobalPreferences.instance(InstrumentationRegistry.getInstrumentation().context)

        mApi = RetrofitHelper.create(WatchNextApi::class.java)
        mWatchNextService = WatchNextService()

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
        mWatchNextService.setWatchNextApi(mApiMock)

        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty() {
        mWatchNextService.setWatchNextApi(mApiMock)

        val metadata = getMediaItemMetadataUnsigned()

        val anyItem = metadata?.suggestions?.getOrNull(0)?.mediaItems?.getOrNull(0)

        assertNotNull("anyItem isn't null", anyItem)
    }

    @Test
    fun testThatMockedWatchNextResultCanBeConverted2() {
        testThatMockedWatchNextResultCanBeConverted(mApiMock2)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty2() {
        testThatMockedWatchNextSuggestionsNotEmpty(mApiMock2)
    }

    @Test
    fun testThatMockedWatchNextResultCanBeConverted3() {
        testThatMockedWatchNextResultCanBeConverted(mApiMock3)
    }

    @Test
    fun testThatMockedWatchNextSuggestionsNotEmpty3() {
        testThatMockedWatchNextSuggestionsNotEmpty(mApiMock3)
    }

    @Test
    fun testThatWatchNextResultCanBeConverted() {
        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    @Ignore("Latest Suggestions aren't continuable")
    @Test
    fun testThatSuggestedContinuationNotNull() {
        val metadata = getMediaItemMetadataUnsigned()

        val continuation = mWatchNextService.continueGroup(getContinuableRow(metadata))

        assertNotNull("Continuation not null", continuation)
    }

    @Test
    fun testThatMetadataContainsRequiredFields() {
        val metadata = getMediaItemMetadataUnsigned();

        testBaseFields(metadata)
    }

    @Test
    fun testThatDislikesIsWorking() {
        val dislikes = mApi.getDislikes("KIeAtU-Toxw")
        val dislikesResult = RetrofitHelper.get(dislikes)

        assertNotNull("Contains dislikes", dislikesResult?.dislikes)
        assertTrue("Dislikes count bigger than zero", (dislikesResult?.dislikes ?: 0) > 0)
    }

    @Test
    fun testUnlocalizedTitleIsWorking() {
        val wrapper = mApi.getUnlocalizedTitle("https://www.youtube.com/watch?v=KIeAtU-Toxw")
        val unlocalizedTitleResult = RetrofitHelper.get(wrapper)

        assertNotNull("Contains title", unlocalizedTitleResult?.title)
    }

    @Test
    fun testUnlocalizedShortTitleIsWorking() {
        val wrapper = mApi.getUnlocalizedTitle("https://www.youtube.com/shorts/SYdsEqmJs4k")
        val unlocalizedTitleResult = RetrofitHelper.get(wrapper)

        assertNotNull("Contains title", unlocalizedTitleResult?.title)
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

    private fun getMediaItemMetadataUnsigned() = mWatchNextService.getMetadata(TestHelpers.VIDEO_ID_CAPTIONS)

    private fun getWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextApiHelper.getWatchNextQuery(AppClient.TV, TestHelpers.VIDEO_ID_CAPTIONS)
        val wrapper = mApi.getWatchNextResult(watchNextQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun getMockedWatchNextResult(): WatchNextResult? {
        val watchNextQuery = WatchNextApiHelper.getWatchNextQuery(AppClient.TV, TestHelpers.VIDEO_ID_CAPTIONS)
        val wrapper = mApiMock.getWatchNextResult(watchNextQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun testThatMockedWatchNextResultCanBeConverted(api: WatchNextApi) {
        mWatchNextService.setWatchNextApi(api)

        val metadata = getMediaItemMetadataUnsigned()

        assertNotNull("Metadata isn't null", metadata)
    }

    private fun testThatMockedWatchNextSuggestionsNotEmpty(api: WatchNextApi) {
        // Testing groups inside a Chip

        mWatchNextService.setWatchNextApi(api)

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
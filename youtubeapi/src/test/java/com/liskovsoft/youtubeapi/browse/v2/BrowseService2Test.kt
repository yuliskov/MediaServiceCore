package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseService2Test {
    private lateinit var mBrowseService2: BrowseService2

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpers.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
        mBrowseService2 = BrowseService2()
    }

    @Ignore("Shorts may be removed by the user")
    @Test
    fun testThatShortsNotEmpty() {
        val sections = mBrowseService2.getHome()

        val shorts = sections?.first?.get(1)

        assertTrue("Shorts not empty", shorts?.mediaItems?.isNotEmpty() == true)
    }

    @Test
    fun testThatHomeContainsFeedbackTokens() {
        val sections = mBrowseService2.getHome()

        val home = sections?.first?.get(0)

        val mediaItem = home?.mediaItems?.first { !YouTubeHelper.isEmpty(it) } // skip ad cards

        assertTrue("Home row contains token 1", mediaItem?.feedbackToken != null)
        assertTrue("Home row contains token 2", mediaItem?.feedbackToken2 != null)
    }

    @Test
    fun testThatSubscribedChannelsNotEmpty() {
        val channels = mBrowseService2.getSubscribedChannels()

        assertTrue("Channel list not empty", channels?.mediaItems?.size ?: 0 > 30)

        assertTrue("Contains new content",
            channels?.mediaItems?.fold(0) { acc, mediaItem -> acc + if (mediaItem.hasNewContent()) 1 else 0 } ?: 0 > 3
        )

        BrowseTestHelper.checkGuideMediaItem(channels?.mediaItems?.getOrNull(0)!!)
    }

    @Test
    fun testThatTrendingNotEmpty() {
        val trending = mBrowseService2.getTrending()

        assertTrue("Trending not empty", trending?.get(0)?.mediaItems?.size ?: 0 > 10)
    }

    @Test
    fun testThatChannelSortingNotEmpty() {
        val sorting = mBrowseService2.getChannelSorting(TestHelpers.CHANNEL_ID_3)

        assertTrue("Has sorting entries", (sorting?.size ?: 0) == 3)
        assertTrue("Has continuations", sorting?.mapNotNull { it?.nextPageKey }?.size == 3)

        val continueGroup = mBrowseService2.continueGroup(sorting?.firstOrNull())
        assertTrue("Can continue", (continueGroup?.mediaItems?.size ?: 0) > 3)
    }

    @Test
    fun testThatChannelSearchNotEmpty() {
        val search = mBrowseService2.getChannelSearch(TestHelpers.CHANNEL_ID_3, "army now")

        assertTrue("Has items", (search?.mediaItems?.size ?: 0) > 3)
    }
}
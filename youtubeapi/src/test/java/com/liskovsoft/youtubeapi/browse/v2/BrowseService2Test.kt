package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.youtubeapi.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class BrowseService2Test {
    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpersV2.getAuthorization()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Ignore("Shorts may be removed by the user")
    @Test
    fun testThatShortsNotEmpty() {
        val sections = BrowseService2.getHome()

        val shorts = sections?.get(1)

        assertTrue("Shorts not empty", shorts?.mediaItems?.isNotEmpty() == true)
    }

    @Test
    fun testThatHomeContainsFeedbackTokens() {
        val sections = BrowseService2.getHome()

        val home = sections?.get(0)

        val mediaItem = home?.mediaItems?.get(0)

        assertTrue("Home row contains token 1", mediaItem?.feedbackToken != null)
        assertTrue("Home row contains token 2", mediaItem?.feedbackToken2 != null)
    }

    @Test
    fun testThatSubscribedChannelsNotEmpty() {
        val channels = BrowseService2.getSubscribedChannels()

        assertTrue("Channel list not empty", channels?.mediaItems?.size ?: 0 > 30)

        assertTrue("Contains new content",
            channels?.mediaItems?.fold(0) { acc, mediaItem -> acc + if (mediaItem.hasNewContent()) 1 else 0 } ?: 0 > 3
        )

        BrowseTestHelper.checkGuideMediaItem(channels?.mediaItems?.getOrNull(0)!!)
    }

    @Test
    fun testThatTrendingNotEmpty() {
        val trending = BrowseService2.getTrending()

        assertTrue("Trending not empty", trending?.get(0)?.mediaItems?.size ?: 0 > 10)
    }

    @Test
    fun testThatChannelSortingNotEmpty() {
        val sorting = BrowseService2.getChannelSorting(TestHelpersV2.CHANNEL_ID_3)

        assertTrue("Has sorting entries", (sorting?.size ?: 0) == 3)
        assertTrue("Has continuations", sorting?.mapNotNull { it?.nextPageKey }?.size == 3)

        val continueGroup = BrowseService2.continueGroup(sorting?.firstOrNull())
        assertTrue("Can continue", (continueGroup?.mediaItems?.size ?: 0) > 3)
    }

    @Test
    fun testThatChannelSearchNotEmpty() {
        val search = BrowseService2.getChannelSearch(TestHelpersV2.CHANNEL_ID_3, "army now")

        assertTrue("Has items", (search?.mediaItems?.size ?: 0) > 3)
    }
}
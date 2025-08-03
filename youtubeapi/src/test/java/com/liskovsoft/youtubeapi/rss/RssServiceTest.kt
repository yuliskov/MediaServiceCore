package com.liskovsoft.youtubeapi.rss

import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RssServiceTest {
    @Test
    fun testGetFeed() {
        val feed = RssService.getFeed(TestHelpers.CHANNEL_ID_3, TestHelpers.CHANNEL_ID_2)

        assertNotNull("Feed is empty", feed)
        assertTrue("Feed contains items", feed?.mediaItems?.isNotEmpty() == true)
    }
}
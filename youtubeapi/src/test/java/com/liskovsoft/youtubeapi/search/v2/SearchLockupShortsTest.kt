package com.liskovsoft.youtubeapi.search.v2

import com.google.gson.Gson
import com.liskovsoft.sharedutils.TestHelpers
import com.liskovsoft.youtubeapi.common.models.gen.isShorts
import com.liskovsoft.youtubeapi.search.v2.gen.SearchResult
import com.liskovsoft.youtubeapi.search.v2.gen.getSections
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * TV search (TVHTML5) now returns every card as `lockupViewModel`.
 * Shorts are marked with `contentType: LOCKUP_CONTENT_TYPE_SHORT`, both in the dedicated
 * "Shorts" shelf and mixed into the regular results shelf.
 *
 * Fixture: trimmed real response for the query "capibara" (2026-11-16).
 */
class SearchLockupShortsTest {
    private val sections by lazy {
        Gson().fromJson(
            TestHelpers.readResource("search/2026.11.16_search_tv_lockup_shorts.json"),
            SearchResult::class.java
        ).getSections()!!
    }

    @Test
    fun shortsShelfItemsAreDetectedAsShorts() {
        val shortsShelf = sections[1]!!.itemWrappers!!

        assertEquals(3, shortsShelf.size)
        shortsShelf.forEach { assertTrue("Shorts shelf item must be detected as Shorts", it!!.isShorts()) }
    }

    @Test
    fun shortMixedIntoRegularResultsIsDetected() {
        val results = sections[0]!!.itemWrappers!!

        // Order in fixture: VIDEO, MUSIC, PLAYLIST, SHORT
        assertEquals(4, results.size)
        assertTrue("Short mixed into regular results must be detected", results[3]!!.isShorts())
    }

    @Test
    fun regularVideoMusicAndPlaylistAreNotShorts() {
        val results = sections[0]!!.itemWrappers!!

        assertFalse("Regular video is not Shorts", results[0]!!.isShorts())
        assertFalse("Music video is not Shorts", results[1]!!.isShorts())
        assertFalse("Playlist is not Shorts", results[2]!!.isShorts())
    }
}

package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * TEMPORARY live test (not committed): posts a real comment through the
 * implemented Kotlin path. Requires AT env var (OAuth access token).
 */
class CreateCommentLiveTest {
    @Test
    fun postRealCommentThroughKotlinPath() {
        val at = System.getenv("AT")
        assertTrue("AT env var missing", !at.isNullOrBlank())
        RetrofitOkHttpHelper.authHeaders["Authorization"] = "Bearer $at"
        CommentsServiceInt.createComment("Gp3BRbNSLEA", "[SmartTube test comment 2 - Kotlin code path]")
    }
}

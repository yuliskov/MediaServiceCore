package com.liskovsoft.youtubeapi.comments

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CreateCommentParamsTest {
    @Test
    fun createCommentParamsMatchesKnownGood() {
        // Ground-truth captured from a working create_comment request
        assertEquals("EgtHcDNCUmJOU0xFQSoAUAc%3D", CommentsApiParams.encodeCreateCommentParamsForTest("Gp3BRbNSLEA"))
    }

}

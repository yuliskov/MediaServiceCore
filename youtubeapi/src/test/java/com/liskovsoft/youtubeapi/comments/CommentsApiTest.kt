package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.youtubeapi.comments.gen.*
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.models.gen.getContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getKey
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class CommentsApiTest {
    companion object {
        private const val COMMENTS_KEY =
            "Eg0SC1pEcUYxUVBsWFJrGAYyMCIuIgtaRHFGMVFQbFhSazAAeAGqAhpVZ3lwNUxGbnYxWEV6QW1rQmJwNEFhQUJBZw%3D%3D"
    }
    private var mApi: CommentsApi? = null

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mApi = RetrofitHelper.withGson(CommentsApi::class.java)
    }

    @Test
    fun testThatCommentsResultIsNotEmpty() {
        val commentsResult = getCommentsResult(COMMENTS_KEY)

        assertNotNull("chat result not null", commentsResult)
        assertNotNull("has actions", commentsResult?.getComments()?.isNotEmpty());
    }

    @Test
    fun testThatCommentCanBeContinued() {
        val commentsResult = getCommentsResult(COMMENTS_KEY)

        assertNotNull("Has continuation key", commentsResult?.getComments()?.getOrNull(0)?.getCommentItem()?.getContinuationKey())
        assertNotNull("Has continuation key", commentsResult?.getContinuationKey())
    }

    @Test
    fun testThatContinuationIsWorking() {
        val commentsResult = getCommentsResult(COMMENTS_KEY)

        val commentsResultNext = getCommentsResult(commentsResult?.getContinuationKey())
        assertNotNull("Has continuations", commentsResultNext?.getComments()?.isNotEmpty())

        val nestedCommentsResult = getCommentsResult(commentsResult?.getComments()?.getOrNull(0)?.getCommentItem()?.getContinuationKey())
        assertNotNull("Has nested comments", nestedCommentsResult?.getComments()?.isNotEmpty())
    }

    private fun getCommentsResult(key: String?): CommentsResult? {
        if (key.isNullOrEmpty()) {
            return null
        }

        val commentsQuery = CommentsApiParams.getCommentsQuery(key)
        val wrapper = mApi!!.getComments(commentsQuery, null)
        return RetrofitHelper.get(wrapper)
    }
}
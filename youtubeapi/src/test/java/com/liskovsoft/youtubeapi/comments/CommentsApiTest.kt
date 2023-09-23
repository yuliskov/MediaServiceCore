package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.youtubeapi.comments.gen.*
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.helpers.tests.TestHelpersV2
import com.liskovsoft.youtubeapi.next.v2.WatchNextApi
import com.liskovsoft.youtubeapi.next.v2.WatchNextApiHelper
import com.liskovsoft.youtubeapi.next.v2.gen.getCommentPanel
import com.liskovsoft.youtubeapi.next.v2.gen.getTopCommentsToken
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowLog

@RunWith(RobolectricTestRunner::class)
class CommentsApiTest {
    private var mApi: CommentsApi? = null
    private var mApi2: WatchNextApi? = null

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mApi = RetrofitHelper.withGson(CommentsApi::class.java)
        mApi2 = RetrofitHelper.withGson(WatchNextApi::class.java)
    }

    @Test
    fun testThatCommentsResultIsNotEmpty() {
        val commentsResult = getCommentsResult(getCommentsKey())

        assertNotNull("chat result not null", commentsResult)
        assertNotNull("has actions", commentsResult?.getComments()?.isNotEmpty());
    }

    @Test
    fun testThatCommentCanBeContinued() {
        val commentsResult = getCommentsResult(getCommentsKey())

        assertNotNull("Has continuation key", getContinuableComment(commentsResult)?.getContinuationKey())
        assertNotNull("Has continuation key", commentsResult?.getContinuationKey())
    }

    @Test
    fun testThatContinuationIsWorking() {
        val commentsResult = getCommentsResult(getCommentsKey())

        val commentsResultNext = getCommentsResult(commentsResult?.getContinuationKey())
        assertNotNull("Has continuations", commentsResultNext?.getComments()?.isNotEmpty())

        val nestedCommentsResult = getCommentsResult(getContinuableComment(commentsResult)?.getContinuationKey())
        assertNotNull("Has nested comments", nestedCommentsResult?.getComments()?.isNotEmpty())
    }

    private fun getCommentsResult(key: String?): CommentsResult? {
        if (key.isNullOrEmpty()) {
            return null
        }

        val commentsQuery = CommentsApiParams.getCommentsQuery(key)
        val wrapper = mApi!!.getComments(commentsQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun getCommentsKey(): String? {
        val watchNextResult = mApi2?.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(TestHelpersV2.VIDEO_ID_CAPTIONS))
        val watchNext = watchNextResult?.execute()?.body()
        val commentsKey = watchNext?.getCommentPanel()?.getTopCommentsToken()
        return commentsKey
    }

    private fun getContinuableComment(commentsResult: CommentsResult?): CommentRenderer? {
        commentsResult?.getComments()?.forEach {
            val commentItem = it?.getCommentItem()
            if (commentItem?.getContinuationKey() != null) return commentItem
        }

        return null
    }
}
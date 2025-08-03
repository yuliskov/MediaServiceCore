package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.youtubeapi.comments.gen.*
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.googlecommon.common.helpers.RetrofitOkHttpHelper
import com.liskovsoft.googlecommon.common.helpers.tests.TestHelpers
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

private const val videoId = "unfpnIF0OMo" // THE DOR BROTHERS

@RunWith(RobolectricTestRunner::class)
class CommentsApiTest {
    private lateinit var mCommentsApi: CommentsApi
    private lateinit var mWatchNextApi: WatchNextApi

    @Before
    fun setUp() {
        // fix issue: No password supplied for PKCS#12 KeyStore
        // https://github.com/robolectric/robolectric/issues/5115
        System.setProperty("javax.net.ssl.trustStoreType", "JKS")
        ShadowLog.stream = System.out // catch Log class output
        mCommentsApi = RetrofitHelper.create(CommentsApi::class.java)
        mWatchNextApi = RetrofitHelper.create(WatchNextApi::class.java)

        RetrofitOkHttpHelper.authHeaders.clear()
        RetrofitOkHttpHelper.disableCompression = true
    }

    @Test
    fun testThatCommentsResultIsNotEmpty() {
        val commentsResult = getCommentsResult(getCommentsKey())

        assertNotNull("chat result not null", commentsResult)
        assertNotNull("has actions", commentsResult?.getComments()?.isNotEmpty())
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

    @Test
    fun testToggleLike() {
        RetrofitOkHttpHelper.authHeaders["Authorization"] = TestHelpers.getAuthorization()

        val commentsResult = getCommentsResult(getCommentsKey())

        val comments = commentsResult?.getComments()

        val first = comments?.get(1)

        val continuationKey = first?.getCommentItem()?.getContinuationKey()

        assertNotNull("Has key", continuationKey)

        val nestedComments = getCommentsResult(continuationKey)

        assertNotNull("Has body", nestedComments)

        val likeParams = nestedComments?.getActiveCommentItem()?.getLikeParams()

        assertNotNull("Has like action id", likeParams)

        val commentAction = mCommentsApi.commentAction(CommentsApiParams.getActionQuery(likeParams!!))

        RetrofitHelper.getWithErrors(commentAction)
    }

    private fun getCommentsResult(key: String?): CommentsResult? {
        if (key.isNullOrEmpty()) {
            return null
        }

        val commentsQuery = CommentsApiParams.getCommentsQuery(key)
        val wrapper = mCommentsApi.getComments(commentsQuery)
        return RetrofitHelper.get(wrapper)
    }

    private fun getCommentsKey(): String? {
        val watchNextResult = mWatchNextApi.getWatchNextResult(WatchNextApiHelper.getWatchNextQuery(AppClient.TV, TestHelpers.VIDEO_ID_CAPTIONS))
        val watchNext = watchNextResult.execute().body()
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
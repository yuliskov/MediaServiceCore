package com.liskovsoft.youtubeapi.comments.impl

import com.liskovsoft.mediaserviceinterfaces.data.CommentGroup
import com.liskovsoft.mediaserviceinterfaces.data.CommentItem
import com.liskovsoft.youtubeapi.comments.gen.CommentsResult
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken

internal data class CommentGroupImpl(val commentsResult: CommentsResult): CommentGroup {
    private val itemSectionContinuation by lazy {
        commentsResult.continuationContents?.itemSectionContinuation
    }

    private val commentItemWrappers by lazy {
        itemSectionContinuation?.contents
    }

    private val nextCommentsKeyItem by lazy {
        itemSectionContinuation?.continuations?.getContinuationToken()
    }

    private val commentItems by lazy {
        commentItemWrappers?.mapNotNull { it?.let { CommentItemImpl(it) } }
    }

    override fun getComments(): List<CommentItem?>? = commentItems

    override fun getNextCommentsKey(): String? = nextCommentsKeyItem
}

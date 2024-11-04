package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.common.models.gen.getAccessibilityLabel
import com.liskovsoft.youtubeapi.common.models.gen.getContinuations
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationKey

internal fun CommentsResult.getComments(): List<CommentItemWrapper?>? = continuationContents?.itemSectionContinuation?.contents
internal fun CommentsResult.getContinuationKey(): String? = continuationContents?.itemSectionContinuation?.continuations
    ?.getContinuationKey()

internal fun CommentItemWrapper.getCommentItem() = commentThreadRenderer?.comment?.commentRenderer

internal fun CommentRenderer.getContinuationKey() = detailViewEndpoint?.getContinuations()?.getContinuationKey()
internal fun CommentRenderer.getContinuationLabel() = repliesCount?.getAccessibilityLabel()
package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.common.models.gen.getAccessibilityLabel
import com.liskovsoft.youtubeapi.common.models.gen.getContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getToken

internal fun CommentsResult.getComments(): List<CommentItemWrapper?>? = continuationContents?.itemSectionContinuation?.contents
internal fun CommentsResult.getContinuationKey(): String? = continuationContents?.itemSectionContinuation?.continuations
    ?.getOrNull(0)?.getToken()

internal fun CommentItemWrapper.getCommentItem() = commentThreadRenderer?.comment?.commentRenderer

internal fun CommentRenderer.getContinuationKey() = detailViewEndpoint?.getContinuation()?.getToken()
internal fun CommentRenderer.getContinuationLabel() = repliesCount?.getAccessibilityLabel()
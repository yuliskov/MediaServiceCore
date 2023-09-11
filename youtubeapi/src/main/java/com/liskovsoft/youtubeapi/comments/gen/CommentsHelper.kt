package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.common.models.gen.getContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getKey
import com.liskovsoft.youtubeapi.next.v2.gen.getLabel

internal fun CommentsResult.getComments(): List<CommentItemWrapper?>? = continuationContents?.itemSectionContinuation?.contents
internal fun CommentsResult.getContinuationKey(): String? = continuationContents?.itemSectionContinuation?.continuations
    ?.getOrNull(0)?.getKey()

internal fun CommentItemWrapper.getCommentItem() = commentThreadRenderer?.comment?.commentRenderer

internal fun CommentRenderer.getContinuationKey() = detailViewEndpoint?.getContinuation()?.getKey()
internal fun CommentRenderer.getContinuationLabel() = detailViewEndpoint?.getContinuation()?.getLabel()
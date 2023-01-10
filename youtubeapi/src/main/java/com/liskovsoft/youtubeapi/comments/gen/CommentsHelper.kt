package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.common.models.gen.getContinuation
import com.liskovsoft.youtubeapi.next.v2.gen.getKey
import com.liskovsoft.youtubeapi.next.v2.gen.getLabel

fun CommentsResult.getComments(): List<CommentItemWrapper?>? = continuationContents?.itemSectionContinuation?.contents
fun CommentsResult.getContinuationKey(): String? = continuationContents?.itemSectionContinuation?.continuations
    ?.getOrNull(0)?.getKey()

fun CommentItemWrapper.getCommentItem() = commentThreadRenderer?.comment?.commentRenderer

fun CommentRenderer.getContinuationKey() = detailViewEndpoint?.getContinuation()?.getKey()
fun CommentRenderer.getContinuationLabel() = detailViewEndpoint?.getContinuation()?.getLabel()
package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.common.models.gen.getContinuationKey
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationKey

fun CommentsResult.getComments(): List<CommentItemWrapper?>? = continuationContents?.itemSectionContinuation?.contents
fun CommentsResult.getContinuationKey(): String? = continuationContents?.itemSectionContinuation?.continuations
    ?.getOrNull(0)?.getContinuationKey()

fun CommentItemWrapper.getContinuationKey(): String? =
    commentThreadRenderer?.comment?.commentRenderer?.detailViewEndpoint?.getContinuationKey()
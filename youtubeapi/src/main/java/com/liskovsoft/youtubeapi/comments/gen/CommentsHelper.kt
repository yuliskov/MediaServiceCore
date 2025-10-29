package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.common.models.gen.getAccessibilityLabel
import com.liskovsoft.youtubeapi.common.models.gen.getContinuations
import com.liskovsoft.youtubeapi.common.models.gen.getDefaultParams
import com.liskovsoft.youtubeapi.common.models.gen.getToggleParams
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationToken

internal fun CommentsResult.getComments(): List<CommentItemWrapper?>? = continuationContents?.itemSectionContinuation?.contents
internal fun CommentsResult.getContinuationKey(): String? = continuationContents?.itemSectionContinuation?.continuations
    ?.getContinuationToken()
internal fun CommentsResult.getActiveCommentItem(): CommentRenderer? = getComments()?.getOrNull(0)?.commentRenderer

internal fun CommentItemWrapper.getCommentItem() = commentThreadRenderer?.comment?.commentRenderer

internal fun CommentRenderer.getContinuationKey() = detailViewEndpoint?.getContinuations()?.getContinuationToken()
internal fun CommentRenderer.getContinuationLabel() = repliesCount?.getAccessibilityLabel()
internal fun CommentRenderer.getLikeParams() = actionButtons?.commentActionButtonsRenderer?.likeButton?.toggleButtonRenderer?.getDefaultParams()
internal fun CommentRenderer.getUnLikeParams() = actionButtons?.commentActionButtonsRenderer?.likeButton?.toggleButtonRenderer?.getToggleParams()
internal fun CommentRenderer.getDislikeParams() = actionButtons?.commentActionButtonsRenderer?.dislikeButton?.toggleButtonRenderer?.getDefaultParams()
internal fun CommentRenderer.getUnDislikeParams() = actionButtons?.commentActionButtonsRenderer?.dislikeButton?.toggleButtonRenderer?.getToggleParams()
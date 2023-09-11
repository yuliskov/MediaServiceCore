package com.liskovsoft.youtubeapi.comments.gen

import com.liskovsoft.youtubeapi.next.v2.gen.ContinuationItem

internal data class CommentsResult(
    val continuationContents: ContinuationContents?
) {
    data class ContinuationContents(
        val itemSectionContinuation: ItemSectionContinuation?
    ) {
        data class ItemSectionContinuation(
            val contents: List<CommentItemWrapper?>?,
            val continuations: List<ContinuationItem?>?
        )
    }
}
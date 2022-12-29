package com.liskovsoft.youtubeapi.comments.gen.kt

data class CommentsResult(
    val continuationContents: ContinuationContents?
) {
    data class ContinuationContents(
        val itemSectionContinuation: ItemSectionContinuation?
    ) {
        data class ItemSectionContinuation(
            val contents: List<CommentItem?>?
        )
    }
}
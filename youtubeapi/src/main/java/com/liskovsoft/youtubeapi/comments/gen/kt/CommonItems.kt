package com.liskovsoft.youtubeapi.comments.gen.kt

import com.liskovsoft.youtubeapi.common.models.kt.TextItem
import com.liskovsoft.youtubeapi.common.models.kt.ThumbnailItem

data class CommentItem(
    val commentThreadRenderer: CommentThreadRenderer?
) {
    data class CommentThreadRenderer(
        val comment: Comment?
    ) {
        data class Comment(
            val commentRenderer: CommentRenderer?
        )
    }
}

data class CommentRenderer(
    val authorText: TextItem?,
    val authorThumbnail: ThumbnailItem?,
    val contentText: TextItem?
)
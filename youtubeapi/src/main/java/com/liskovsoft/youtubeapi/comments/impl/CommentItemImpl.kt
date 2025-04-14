package com.liskovsoft.youtubeapi.comments.impl

import com.liskovsoft.mediaserviceinterfaces.data.CommentItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.comments.gen.CommentItemWrapper
import com.liskovsoft.youtubeapi.comments.gen.getContinuationKey
import com.liskovsoft.youtubeapi.comments.gen.getContinuationLabel
import com.liskovsoft.youtubeapi.common.models.gen.getOptimalResThumbnailUrl
import com.liskovsoft.youtubeapi.common.models.gen.getText

internal data class CommentItemImpl(val commentItemWrapper: CommentItemWrapper): CommentItem {
    private val commentRenderer by lazy {
        commentItemWrapper.commentThreadRenderer?.comment?.commentRenderer
    }

    private val idItem by lazy { commentRenderer?.commentId }

    private val messageIem by lazy { commentRenderer?.contentText?.getText() }

    private val authorNameItem by lazy { commentRenderer?.authorText?.getText() }

    private val authorPhotoItem by lazy { commentRenderer?.authorThumbnail?.getOptimalResThumbnailUrl() }

    private val publishedDateItem by lazy { commentRenderer?.publishedTimeText?.getText() }

    private val nestedCommentKeyItem by lazy { commentRenderer?.getContinuationKey() }

    private val replyCountItem by lazy { commentRenderer?.getContinuationLabel()?.replace(" ", Helpers.NON_BREAKING_SPACE) }

    private val isLikedItem by lazy { commentRenderer?.isLiked ?: false }

    private val likeCountItem by lazy { commentRenderer?.voteCount?.getText() }

    override fun getId(): String? = idItem

    override fun getMessage(): String? = messageIem

    override fun getAuthorName(): String? = authorNameItem

    override fun getAuthorPhoto(): String? = authorPhotoItem

    override fun getPublishedDate(): String? = publishedDateItem

    override fun getNestedCommentsKey(): String? = nestedCommentKeyItem

    override fun isLiked(): Boolean = isLikedItem

    override fun getLikeCount(): String? = likeCountItem

    override fun getReplyCount(): String? = replyCountItem

    override fun isEmpty(): Boolean = replyCountItem == null || nestedCommentKeyItem == null // empty replies fix
}
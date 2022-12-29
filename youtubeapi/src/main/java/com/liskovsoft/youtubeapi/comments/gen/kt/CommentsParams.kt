package com.liskovsoft.youtubeapi.comments.gen.kt

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

object CommentsParams {
    fun getCommentsQuery(commentsKey: String): String {
        return ServiceHelper.createQuery(null)
    }
}
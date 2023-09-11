package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal object CommentsApiParams {
    fun getCommentsQuery(commentsKey: String?): String? {
        val chatData = String.format("\"continuation\":\"%s\"", commentsKey)
        return ServiceHelper.createQueryTV(chatData)
    }
}
package com.liskovsoft.youtubeapi.comments

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper

internal object CommentsApiParams {
    private const val COMMENT_ACTION_TEMPLATE: String = "\"actions\":[\"%s\"]"

    fun getCommentsQuery(commentsKey: String): String {
        val chatData = String.format("\"continuation\":\"%s\"", commentsKey)
        return ServiceHelper.createQueryTV(chatData)
    }

    fun getActionQuery(actionKey: String): String {
        return ServiceHelper.createQueryTV(String.format(COMMENT_ACTION_TEMPLATE, actionKey))
    }
}
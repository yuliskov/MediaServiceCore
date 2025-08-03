package com.liskovsoft.youtubeapi.chat

import com.liskovsoft.youtubeapi.common.helpers.PostDataHelper

internal object LiveChatApiParams {
    fun getLiveChatQuery(chatKey: String): String {
        val chatData = String.format("\"continuation\":\"%s\"", chatKey)
        return PostDataHelper.createQueryTV(chatData)
    }
}
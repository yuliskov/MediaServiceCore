package com.liskovsoft.youtubeapi.chat.impl

import com.liskovsoft.mediaserviceinterfaces.data.ChatItem
import com.liskovsoft.youtubeapi.chat.gen.kt.LiveChatAction

data class ChatItemImpl(val liveChatAction: LiveChatAction): ChatItem {
    override fun getId(): String {
        TODO("Not yet implemented")
    }

    override fun getMessage(): String {
        TODO("Not yet implemented")
    }

    override fun getAuthorName(): String {
        TODO("Not yet implemented")
    }

    override fun getAuthorPhoto(): String {
        TODO("Not yet implemented")
    }
}

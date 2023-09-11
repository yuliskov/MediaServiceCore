package com.liskovsoft.youtubeapi.chat.impl

import com.liskovsoft.mediaserviceinterfaces.data.ChatItem
import com.liskovsoft.youtubeapi.chat.gen.LiveChatAction
import com.liskovsoft.youtubeapi.common.models.gen.getOptimalResThumbnailUrl
import com.liskovsoft.youtubeapi.common.models.gen.getText

internal data class ChatItemImpl(val liveChatAction: LiveChatAction): ChatItem {
    private val messageRenderer by lazy {
        liveChatAction.addChatItemAction?.item?.liveChatTextMessageRenderer ?:
        liveChatAction.addBannerToLiveChatCommand?.bannerRenderer?.liveChatBannerRenderer?.contents?.liveChatTextMessageRenderer
    }

    override fun getId(): String? {
        return messageRenderer?.id
    }

    override fun getMessage(): String? {
        return messageRenderer?.message?.getText()
    }

    override fun getAuthorName(): String? {
        return messageRenderer?.authorName?.getText()
    }

    override fun getAuthorPhoto(): String? {
        return messageRenderer?.authorPhoto?.getOptimalResThumbnailUrl()
    }
}

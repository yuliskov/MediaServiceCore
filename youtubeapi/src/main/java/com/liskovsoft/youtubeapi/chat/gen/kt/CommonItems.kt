package com.liskovsoft.youtubeapi.chat.gen.kt

import com.liskovsoft.youtubeapi.next.v2.gen.kt.TextItem
import com.liskovsoft.youtubeapi.next.v2.gen.kt.ThumbnailItem

data class LiveChatAction(
    val addBannerToLiveChatCommand: AddBannerToLiveChatCommand?,
    val addChatItemAction: AddChatItemAction?
)

data class AddBannerToLiveChatCommand(
    val bannerRenderer: BannerRenderer?
)

data class AddChatItemAction(
    val item: LiveChatTextMessageRendererItem?
)

data class LiveChatTextMessageRendererItem(
    val liveChatTextMessageRenderer: LiveChatTextMessageRenderer?
)

data class LiveChatTextMessageRenderer(
    val message: TextItem?,
    val authorName: TextItem?,
    val authorPhoto: ThumbnailItem?,
    val id: String?,
    val authorExternalChannelId: String?
)

data class LiveChatBannerHeaderRendererItem(
    val liveChatBannerHeaderRenderer: LiveChatBannerHeaderRenderer?
)

data class LiveChatBannerHeaderRenderer(
    val text: TextItem?
)

data class BannerRenderer(
    val liveChatBannerRenderer: LiveChatBannerRenderer?
)

data class LiveChatBannerRenderer(
    val header: LiveChatBannerHeaderRendererItem?,
    val contents: LiveChatTextMessageRendererItem?
)
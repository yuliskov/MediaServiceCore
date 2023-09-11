package com.liskovsoft.youtubeapi.chat.gen

import com.liskovsoft.youtubeapi.common.models.gen.TextItem
import com.liskovsoft.youtubeapi.common.models.gen.ThumbnailItem

internal data class LiveChatAction(
    val addBannerToLiveChatCommand: AddBannerToLiveChatCommand?,
    val addChatItemAction: AddChatItemAction?
)

internal data class AddBannerToLiveChatCommand(
    val bannerRenderer: BannerRenderer?
)

internal data class AddChatItemAction(
    val item: LiveChatTextMessageRendererItem?
)

internal data class LiveChatTextMessageRendererItem(
    val liveChatTextMessageRenderer: LiveChatTextMessageRenderer?
)

internal data class LiveChatTextMessageRenderer(
    val message: TextItem?,
    val authorName: TextItem?,
    val authorPhoto: ThumbnailItem?,
    val id: String?,
    val authorExternalChannelId: String?
)

internal data class LiveChatBannerHeaderRendererItem(
    val liveChatBannerHeaderRenderer: LiveChatBannerHeaderRenderer?
)

internal data class LiveChatBannerHeaderRenderer(
    val text: TextItem?
)

internal data class BannerRenderer(
    val liveChatBannerRenderer: LiveChatBannerRenderer?
)

internal data class LiveChatBannerRenderer(
    val header: LiveChatBannerHeaderRendererItem?,
    val contents: LiveChatTextMessageRendererItem?
)
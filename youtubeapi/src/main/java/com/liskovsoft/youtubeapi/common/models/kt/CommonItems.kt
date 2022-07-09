package com.liskovsoft.youtubeapi.common.models.kt

import com.liskovsoft.youtubeapi.next.v2.gen.kt.NavigationEndpointItem

data class TextItem(
    val runs: List<Run?>?,
    val simpleText: String?
) {
    data class Run(
        val text: String?,
        val emoji: LiveChatEmoji?,
        val navigationEndpoint: NavigationEndpointItem?
    )

    override fun toString(): String {
        return getText() ?: super.toString()
    }
}

data class LiveChatEmoji(
    val emojiId: String?,
    val image: ThumbnailItem?,
    val variantIds: List<String?>?,
    val shortcuts: List<String?>?,
    val isCustomEmoji: Boolean?
)

data class ThumbnailItem(
    val thumbnails: List<Thumbnail?>?
) {
    data class Thumbnail(
        val url: String?,
        val width: String?,
        val height: String?
    )
}
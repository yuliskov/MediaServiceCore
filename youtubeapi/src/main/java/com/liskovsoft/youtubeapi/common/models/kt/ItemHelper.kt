package com.liskovsoft.youtubeapi.common.models.kt

import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: it?.emoji?.getText() ?: "" } ?: simpleText

/**
 * Use shortcut name as workaround to display custom emoji. Custom emoji are images.
 */
//fun LiveChatEmoji.getText() = if (isCustomEmoji == true) shortcuts?.getOrElse(0) { "" } else emojiId
/**
 * Use empty string as workaround to display custom emoji. Custom emoji are images.
 */
fun LiveChatEmoji.getText() = if (isCustomEmoji == true) "" else emojiId

/**
 * Find optimal thumbnail for tv screen
 */
fun ThumbnailItem.findLowResThumbnailUrl() = thumbnails?.getOrElse(YouTubeMediaServiceHelper.LOW_RES_THUMBNAIL_INDEX) { thumbnails.lastOrNull() } ?.getUrl()
fun ThumbnailItem.findHighResThumbnailUrl() = thumbnails?.lastOrNull()?.getUrl()
fun ThumbnailItem.Thumbnail.getUrl(): String? {
    var newUrl = if (url?.startsWith("//") == true) "https:$url" else url

    newUrl = YouTubeMediaServiceHelper.avatarBlockFix(newUrl)

    return newUrl
}
package com.liskovsoft.youtubeapi.common.models.kt

import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: it?.emoji?.getText() ?: "" } ?: simpleText

/**
 * Custom emoji should be rendered as images. Images not supported at this moment. Use shortcut name as workaround.
 */
fun LiveChatEmoji.getText() = if (isCustomEmoji == true) shortcuts?.getOrElse(0) { "" } else emojiId

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
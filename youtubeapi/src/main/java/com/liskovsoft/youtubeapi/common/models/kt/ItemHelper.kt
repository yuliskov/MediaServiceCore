package com.liskovsoft.youtubeapi.common.models.kt

import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: it?.emoji?.getText() ?: "" } ?: simpleText
fun LiveChatEmoji.getText() = emojiId

/**
 * Find optimal thumbnail for tv screen
 */
fun ThumbnailItem.findLowResThumbnailUrl() = thumbnails?.getOrElse(YouTubeMediaServiceHelper.LOW_RES_THUMBNAIL_INDEX) { thumbnails.lastOrNull() } ?.getUrl()
fun ThumbnailItem.findHighResThumbnailUrl() = thumbnails?.lastOrNull()?.getUrl()
fun ThumbnailItem.Thumbnail.getUrl() = if (url?.startsWith("//") == true) "https:$url" else url
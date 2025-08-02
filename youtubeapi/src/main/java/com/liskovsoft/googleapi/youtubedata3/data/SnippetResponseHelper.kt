package com.liskovsoft.googleapi.youtubedata3.data

private const val TYPE_VIDEO = "youtube#video"
private const val TYPE_CHANNEL = "youtube#channel"
private const val TYPE_PLAYLIST = "youtube#playlist"

internal fun SnippetWrapper.getTitle(): String? = snippet?.title
internal fun SnippetWrapper.getVideoId(): String? = if (isVideo()) id else null
internal fun SnippetWrapper.getChannelId(): String? = snippet?.channelId ?: id
internal fun SnippetWrapper.getPlaylistId(): String? = id
internal fun SnippetWrapper.getChannelTitle(): String? = snippet?.channelTitle ?: snippet?.title
internal fun SnippetWrapper.getPublishedAt(): String? = snippet?.publishedAt
internal fun SnippetWrapper.getDescription(): String? = snippet?.description
internal fun SnippetWrapper.getChannelUrl(): String? = snippet?.customUrl
internal fun SnippetWrapper.getCategoryId(): String? = snippet?.categoryId
internal fun SnippetWrapper.getThumbnailUrl(): String? = snippet?.thumbnails?.medium?.url
internal fun SnippetWrapper.getDurationIso(): String? = contentDetails?.duration
internal fun SnippetWrapper.getItemCount(): Int? = contentDetails?.itemCount
private fun SnippetWrapper.isVideo() = kind == TYPE_VIDEO
private fun SnippetWrapper.isChannel() = kind == TYPE_CHANNEL
private fun SnippetWrapper.isPlaylist() = kind == TYPE_PLAYLIST
package com.liskovsoft.youtubeapi.next.v2.helpers

import com.liskovsoft.youtubeapi.next.v2.result.gen.*

fun TextItem.getText() = runs?.joinToString { it?.text ?: "" } ?: simpleText

fun ItemWrapper.getTileItem() = tileRenderer
fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: pivotVideoRenderer
fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer
fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer
fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer
fun ThumbnailItem.findHighResThumbnailUrl() = if (thumbnails.isNullOrEmpty()) null else thumbnails.last()?.url

val MusicItem.videoId
    get() = this.navigationEndpoint?.watchEndpoint?.videoId

val MusicItem.playlistId
    get() = this.navigationEndpoint?.watchEndpoint?.playlistId

val TileItem.videoId
    get() = this.onSelectCommand?.watchEndpoint?.videoId

val TileItem.playlistId
    get() = this.onSelectCommand?.watchEndpoint?.playlistId
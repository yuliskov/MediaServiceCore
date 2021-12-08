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

//////////

val VideoItem.descBadgeText
    get() = badges?.getOrNull(0)?.metadataBadgeRenderer?.label

val VideoItem.userName
    get() = shortBylineText ?: longBylineText

val VideoItem.publishedTime
    get() = publishedTimeText

val VideoItem.viewCount
    get() = shortViewCountText ?: viewCountText

////////////

val MusicItem.title
    get() = primaryText

val MusicItem.userName
    get() = secondaryText

val MusicItem.videoId
    get() = navigationEndpoint?.watchEndpoint?.videoId

val MusicItem.playlistId
    get() = navigationEndpoint?.watchEndpoint?.playlistId

val MusicItem.descBadgeText
    get() = null

///////////

val TileItem.title
    get() = metadata?.tileMetadataRenderer?.title

val TileItem.videoId
    get() = onSelectCommand?.watchEndpoint?.videoId

val TileItem.playlistId
    get() = onSelectCommand?.watchEndpoint?.playlistId

val TileItem.descBadgeText
    get() =
        metadata?.tileMetadataRenderer?.lines?.map { it?.lineRenderer?.items?.getOrNull(0)?.lineItemRenderer?.badge?.metadataBadgeRenderer?.label }?.firstOrNull()

////////////

val ItemWrapper.videoId
    get() = getVideoItem()?.videoId ?: getMusicItem()?.videoId ?: getTileItem()?.videoId

val ItemWrapper.title
    get() = getVideoItem()?.title?.getText() ?: getMusicItem()?.title?.getText() ?: getTileItem()?.title?.getText()

val ItemWrapper.descBadgeText
    get() = getVideoItem()?.descBadgeText ?: getMusicItem()?.descBadgeText ?: getTileItem()?.descBadgeText

val ItemWrapper.userName
    get() = getVideoItem()?.userName?.getText()

val ItemWrapper.publishedTime
    get() = getVideoItem()?.publishedTime?.getText()

val ItemWrapper.viewCountText
    get() = getVideoItem()?.viewCountText?.getText()

val ItemWrapper.upcomingEventText
    get() = getVideoItem()?.upcomingEventData?.upcomingEventText
package com.liskovsoft.youtubeapi.next.v2.helpers

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.gen.kt.*
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: "" } ?: simpleText

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

val MusicItem.viewsAndPublished
    get() = tertiaryText

val MusicItem.viewsCountText
    get() = null

val MusicItem.upcomingEventText
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

val TileItem.userName
    get() = null

val TileItem.publishedTIme
    get() = null

val TileItem.viewCountText
    get() = YouTubeMediaServiceHelper.createDescription(*metadata?.tileMetadataRenderer?.lines?.map {
        ServiceHelper.combineItems(" ", *it?.lineRenderer?.items?.map { it?.lineItemRenderer?.text }?.toTypedArray() ?: null)
    }?.toTypedArray() ?: null) ?: null

val TileItem.upcomingEventText
    get() = null

////////////

fun ItemWrapper.getTileItem() = tileRenderer
fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: pivotVideoRenderer
fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer
fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer
fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer
fun ItemWrapper.getType(): Int {
    if (getChannelItem() != null)
        return MediaItem.TYPE_CHANNEL
    if (getPlaylistItem() != null)
        return MediaItem.TYPE_PLAYLIST
    if (getRadioItem() != null)
        return MediaItem.TYPE_PLAYLIST
    if (getVideoItem() != null)
        return MediaItem.TYPE_VIDEO
    if (getMusicItem() != null)
        return MediaItem.TYPE_MUSIC

    return MediaItem.TYPE_UNDEFINED;
}

val ItemWrapper.videoId
    get() = getVideoItem()?.videoId ?: getMusicItem()?.videoId ?: getTileItem()?.videoId

val ItemWrapper.title
    get() = getVideoItem()?.title?.getText() ?: getMusicItem()?.title?.getText() ?: getTileItem()?.title?.getText()

val ItemWrapper.descBadgeText
    get() = getVideoItem()?.descBadgeText ?: getMusicItem()?.descBadgeText ?: getTileItem()?.descBadgeText

val ItemWrapper.userName
    get() = getVideoItem()?.userName?.getText() ?: getMusicItem()?.secondaryText?.getText() ?: getTileItem()?.userName

val ItemWrapper.publishedTime
    get() = getVideoItem()?.publishedTime?.getText() ?: getMusicItem()?.viewsAndPublished?.getText() ?: getTileItem()?.publishedTIme

val ItemWrapper.viewCountText
    get() = getVideoItem()?.viewCountText?.getText() ?: getMusicItem()?.viewsCountText ?: getTileItem()?.viewCountText

val ItemWrapper.upcomingEventText
    get() = getVideoItem()?.upcomingEventData?.upcomingEventText ?: getMusicItem()?.upcomingEventText ?: getTileItem()?.upcomingEventText

val ItemWrapper.thumbnail
    get() = getVideoItem()?.thumbnail ?: getVideoItem()?.thumbnail ?: getTileItem()?.header?.tileHeaderRenderer?.thumbnail


/////

fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed
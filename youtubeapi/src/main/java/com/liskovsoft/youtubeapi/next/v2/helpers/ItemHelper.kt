package com.liskovsoft.youtubeapi.next.v2.helpers

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.gen.kt.*
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: "" } ?: simpleText

fun ThumbnailItem.findHighResThumbnailUrl() = if (thumbnails.isNullOrEmpty()) null else thumbnails.last()?.url

//////////

fun VideoItem.getTitle() = title?.getText()
fun VideoItem.getVideoId() = videoId
fun VideoItem.getThumbnail() = thumbnail
fun VideoItem.getDescBadgeText() = badges?.getOrNull(0)?.metadataBadgeRenderer?.label
fun VideoItem.getUserName() = shortBylineText?.getText() ?: longBylineText?.getText()
fun VideoItem.getPublishedTimeText() = publishedTimeText?.getText()
fun VideoItem.getViewCount() = shortViewCountText?.getText() ?: viewCountText?.getText()
fun VideoItem.getUpcomingEventText() = upcomingEventData?.upcomingEventText?.getText()

////////////

fun MusicItem.getTitle() = primaryText?.getText()
fun MusicItem.getUserName() = secondaryText?.getText()
fun MusicItem.getThumbnail() = thumbnail
fun MusicItem.getVideoId() = navigationEndpoint?.watchEndpoint?.videoId
fun MusicItem.getPlaylistId() = navigationEndpoint?.watchEndpoint?.playlistId
fun MusicItem.getDescBadgeText() = null
fun MusicItem.getViewsAndPublished() = tertiaryText?.getText()
fun MusicItem.getViewsCountText() = null
fun MusicItem.getUpcomingEventText() = null

///////////

fun TileItem.getTitle() = metadata?.tileMetadataRenderer?.title?.getText()
fun TileItem.getVideoId() = onSelectCommand?.watchEndpoint?.videoId
fun TileItem.getPlaylistId() = onSelectCommand?.watchEndpoint?.playlistId
fun TileItem.getDescBadgeText() = metadata?.tileMetadataRenderer?.lines?.map { it?.lineRenderer?.items?.getOrNull(0)?.lineItemRenderer?.badge?.metadataBadgeRenderer?.label }?.firstOrNull()
fun TileItem.getUserName() = null
fun TileItem.getPublishedTime() = null
fun TileItem.getViewCountText() =
    YouTubeMediaServiceHelper.createDescription(*metadata?.tileMetadataRenderer?.lines?.map {
        ServiceHelper.combineItems(" ", *it?.lineRenderer?.items?.map { it?.lineItemRenderer?.text }?.toTypedArray() ?: null)
    }?.toTypedArray() ?: null) ?: null
fun TileItem.getUpcomingEventText() = null
fun TileItem.getThumbnail() = header?.tileHeaderRenderer?.thumbnail

////////////

private fun ItemWrapper.getTileItem() = tileRenderer
private fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: pivotVideoRenderer
private fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
private fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer
private fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer
private fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer

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
    get() = getVideoItem()?.getVideoId() ?: getMusicItem()?.getVideoId() ?: getTileItem()?.getVideoId()

val ItemWrapper.title
    get() = getVideoItem()?.getTitle() ?: getMusicItem()?.getTitle() ?: getTileItem()?.getTitle()

val ItemWrapper.descBadgeText
    get() = getVideoItem()?.getDescBadgeText() ?: getMusicItem()?.getDescBadgeText() ?: getTileItem()?.getDescBadgeText()

val ItemWrapper.userName
    get() = getVideoItem()?.getUserName() ?: getMusicItem()?.getUserName() ?: getTileItem()?.getUserName()

val ItemWrapper.publishedTime
    get() = getVideoItem()?.getPublishedTimeText() ?: getMusicItem()?.getViewsAndPublished() ?: getTileItem()?.getPublishedTime()

val ItemWrapper.viewCountText
    get() = getVideoItem()?.getViewCount() ?: getMusicItem()?.getViewsCountText() ?: getTileItem()?.getViewCountText()

val ItemWrapper.upcomingEventText
    get() = getVideoItem()?.getUpcomingEventText() ?: getMusicItem()?.getUpcomingEventText() ?: getTileItem()?.getUpcomingEventText()

val ItemWrapper.thumbnail
    get() = getVideoItem()?.getThumbnail() ?: getMusicItem()?.getThumbnail() ?: getTileItem()?.getThumbnail()


/////

fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed

/////

fun WatchNextResult.getSuggestedSections() = contents?.singleColumnWatchNextResults?.pivot?.pivot?.contents?.map { it?.shelfRenderer }
fun WatchNextResult.getVideoMetadata() = contents?.singleColumnWatchNextResults?.results?.results?.contents?.getOrNull(0)?.
    itemSectionRenderer?.contents?.map { it?.videoMetadataRenderer ?: it?.musicWatchMetadataRenderer }?.firstOrNull()

fun WatchNextResult.getNextVideoItem() = contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.sets?.getOrNull(0)?.
    nextVideoRenderer?.let { it.maybeHistoryEndpointRenderer ?: it.autoplayEndpointRenderer }

fun WatchNextResult.getVideoDetails() = contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.replayVideoRenderer?.pivotVideoRenderer
fun WatchNextResult.getReplayItemWrapper() = contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.replayVideoRenderer
fun WatchNextResult.getButtonStateItem() = transportControls?.transportControlsRenderer

///////

fun VideoMetadataItem.getVideoOwner() = owner?.videoOwnerRenderer
fun VideoMetadataItem.getTitle() = title?.getText()
fun VideoMetadataItem.getViewCountText() = viewCount?.videoViewCountRenderer?.viewCount?.getText() ?: viewCountText?.getText()
fun VideoMetadataItem.isLive() = viewCount?.videoViewCountRenderer?.isLive
fun VideoMetadataItem.getDateText() = dateText?.getText()
fun VideoMetadataItem.getPublishedTime() = publishedTimeText?.getText() ?: albumName?.getText()
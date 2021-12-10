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
fun VideoItem.getChannelId() =
        shortBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
        longBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
        menu?.menuRenderer?.items?.firstNotNullOfOrNull { it?.menuNavigationItemRenderer?.navigationEndpoint?.getBrowseId() }
fun VideoItem.getPlaylistId() = navigationEndpoint?.watchEndpoint?.playlistId

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
fun TileItem.getPlaylistId() = onSelectCommand?.watchEndpoint?.playlistId ?: onSelectCommand?.watchPlaylistEndpoint?.playlistId
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

fun ItemWrapper.getVideoId() = getVideoItem()?.getVideoId() ?: getMusicItem()?.getVideoId() ?: getTileItem()?.getVideoId()

fun ItemWrapper.getTitle() = getVideoItem()?.getTitle() ?: getMusicItem()?.getTitle() ?: getTileItem()?.getTitle()

fun ItemWrapper.getDescBadgeText() = getVideoItem()?.getDescBadgeText() ?: getMusicItem()?.getDescBadgeText() ?: getTileItem()?.getDescBadgeText()

fun ItemWrapper.getUserName() = getVideoItem()?.getUserName() ?: getMusicItem()?.getUserName() ?: getTileItem()?.getUserName()

fun ItemWrapper.getPublishedTime() = getVideoItem()?.getPublishedTimeText() ?: getMusicItem()?.getViewsAndPublished() ?: getTileItem()?.getPublishedTime()

fun ItemWrapper.getViewCountText() = getVideoItem()?.getViewCount() ?: getMusicItem()?.getViewsCountText() ?: getTileItem()?.getViewCountText()

fun ItemWrapper.getUpcomingEventText() = getVideoItem()?.getUpcomingEventText() ?: getMusicItem()?.getUpcomingEventText() ?: getTileItem()?.getUpcomingEventText()

fun ItemWrapper.getThumbnail() = getVideoItem()?.getThumbnail() ?: getMusicItem()?.getThumbnail() ?: getTileItem()?.getThumbnail()

fun ItemWrapper.getPlaylistId() = null

/////

fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed
fun VideoOwnerItem.getChannelId() = navigationEndpoint?.getBrowseId() ?: subscribeButton?.subscribeButtonRenderer?.channelId

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

const val LIKE_STATUS_LIKE = "LIKE"
const val LIKE_STATUS_DISLIKE = "DISLIKE"
const val LIKE_STATUS_INDIFFERENT = "INDIFFERENT"
fun VideoMetadataItem.getVideoOwner() = owner?.videoOwnerRenderer
fun VideoMetadataItem.getTitle() = title?.getText()
fun VideoMetadataItem.getViewCountText() = viewCount?.videoViewCountRenderer?.viewCount?.getText() ?: viewCountText?.getText()
fun VideoMetadataItem.isLive() = viewCount?.videoViewCountRenderer?.isLive
fun VideoMetadataItem.getDateText() = dateText?.getText()
fun VideoMetadataItem.getPublishedTime() = publishedTimeText?.getText() ?: albumName?.getText()
fun VideoMetadataItem.getLikeStatus() = likeStatus
fun VideoMetadataItem.isUpcoming() = badges?.firstNotNullOfOrNull { it?.upcomingEventBadge?.label?.getText() }?.let { true } ?: false
fun VideoMetadataItem.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched } ?: 0

////////

fun NavigationEndpointItem.getBrowseId() = browseEndpoint?.browseId

////////

fun ButtonStateItem.isLikeToggled() = likeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isDislikeToggled() = dislikeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isSubscribeToggled() = subscribeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.getChannelId() = channelButton?.videoOwnerRenderer?.getChannelId()
package com.liskovsoft.youtubeapi.next.v2.helpers

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.gen.kt.*
import com.liskovsoft.youtubeapi.service.YouTubeMediaServiceHelper

fun TextItem.getText() = runs?.joinToString("") { it?.text ?: "" } ?: simpleText

fun ThumbnailItem.findHighResThumbnailUrl() = if (thumbnails.isNullOrEmpty()) null else thumbnails.last()?.getUrl()
fun ThumbnailItem.Thumbnail.getUrl() = if (url?.startsWith("//") == true) "https:$url" else url

//////////

fun VideoItem.getTitle() = title?.getText()
fun VideoItem.getVideoId() = videoId
fun VideoItem.getThumbnails() = thumbnail
fun VideoItem.getDescBadgeText() = badges?.getOrNull(0)?.metadataBadgeRenderer?.label
fun VideoItem.getBadgeText() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() } ?:
    badges?.firstNotNullOfOrNull { it?.liveBadge?.label?.getText() ?: it?.upcomingEventBadge?.label?.getText() }
fun VideoItem.getUserName() = shortBylineText?.getText() ?: longBylineText?.getText()
fun VideoItem.getPublishedTimeText() = publishedTimeText?.getText()
fun VideoItem.getViewCount() = shortViewCountText?.getText() ?: viewCountText?.getText()
fun VideoItem.getUpcomingEventText() = upcomingEventData?.upcomingEventText?.getText()
fun VideoItem.getChannelId() =
    shortBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    longBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    menu?.getBrowseId()
fun VideoItem.getPlaylistId() = navigationEndpoint?.watchEndpoint?.playlistId
fun VideoItem.getPlaylistIndex() = navigationEndpoint?.watchEndpoint?.index
fun VideoItem.getLengthText() = lengthText?.getText()
fun VideoItem.isLive() = false
fun VideoItem.isUpcoming() = false

////////////

fun MusicItem.getTitle() = primaryText?.getText()
fun MusicItem.getUserName() = secondaryText?.getText()
fun MusicItem.getThumbnails() = thumbnail
fun MusicItem.getVideoId() = navigationEndpoint?.watchEndpoint?.videoId
fun MusicItem.getPlaylistId() = navigationEndpoint?.watchEndpoint?.playlistId
fun MusicItem.getBadgeText() = lengthText?.getText()
fun MusicItem.getLengthText() = lengthText?.getText()
fun MusicItem.getViewsAndPublished() = tertiaryText?.getText()
fun MusicItem.getChannelId() = menu?.getBrowseId()
fun MusicItem.getPlaylistIndex() = navigationEndpoint?.watchEndpoint?.index
fun MusicItem.getDescBadgeText() = null
fun MusicItem.getViewsCountText() = null
fun MusicItem.getUpcomingEventText() = null
fun MusicItem.isLive() = false
fun MusicItem.isUpcoming() = false

///////////

private const val TILE_CONTENT_TYPE_UNDEFINED = "UNDEFINED"
private const val TILE_CONTENT_TYPE_CHANNEL = "TILE_CONTENT_TYPE_CHANNEL"
private const val TILE_CONTENT_TYPE_PLAYLIST = "TILE_CONTENT_TYPE_PLAYLIST"
private const val TILE_CONTENT_TYPE_VIDEO = "TILE_CONTENT_TYPE_VIDEO"
private const val BADGE_STYLE_LIVE = "LIVE"
private const val BADGE_STYLE_UPCOMING = "UPCOMING"
private const val BADGE_STYLE_DEFAULT = "DEFAULT"

fun TileItem.getTitle() = metadata?.tileMetadataRenderer?.title?.getText()
fun TileItem.getVideoId() = onSelectCommand?.watchEndpoint?.videoId
fun TileItem.getPlaylistId() = onSelectCommand?.watchEndpoint?.playlistId ?: onSelectCommand?.watchPlaylistEndpoint?.playlistId
fun TileItem.getPlaylistIndex() = 0
fun TileItem.getDescBadgeText() = metadata?.tileMetadataRenderer?.lines?.map { it?.lineRenderer?.items?.getOrNull(0)?.lineItemRenderer?.badge?.metadataBadgeRenderer?.label }?.firstOrNull()
fun TileItem.getBadgeText() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() }
fun TileItem.getUserName() = null
fun TileItem.getPublishedTime() = null
fun TileItem.getViewCountText() =
    YouTubeMediaServiceHelper.createInfo(*metadata?.tileMetadataRenderer?.lines?.map {
        ServiceHelper.combineItems(" ", *it?.lineRenderer?.items?.map { it?.lineItemRenderer?.text }?.toTypedArray() ?: null)
    }?.toTypedArray() ?: null) ?: null
fun TileItem.getUpcomingEventText() = null
fun TileItem.getThumbnails() = header?.tileHeaderRenderer?.thumbnail
fun TileItem.getBadgeStyle() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }
fun TileItem.getPercentWatched() = header?.tileHeaderRenderer?.thumbnailOverlays?.getOrNull(0)?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched
fun TileItem.getMovingThumbnailUrl() = header?.tileHeaderRenderer?.movingThumbnail?.thumbnails?.getOrNull(0)?.url
fun TileItem.getLengthText() = header?.tileHeaderRenderer?.thumbnailOverlays?.getOrNull(0)?.thumbnailOverlayTimeStatusRenderer?.text?.getText()
fun TileItem.getChannelId() = menu?.getBrowseId()
fun TileItem.isLive() = BADGE_STYLE_LIVE == header?.getBadgeStyle() ?: metadata?.getBadgeStyle()
fun TileItem.isUpcoming() = BADGE_STYLE_UPCOMING == header?.getBadgeStyle() ?: metadata?.getBadgeStyle()

fun TileItem.Header.getBadgeStyle() = tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }
fun TileItem.Metadata.getBadgeStyle() = tileMetadataRenderer?.lines?.firstNotNullOfOrNull { it?.lineRenderer?.items?.firstNotNullOfOrNull { it?.lineItemRenderer?.badge?.metadataBadgeRenderer?.style } }

fun TileItem.getContentType() = contentType

////////////

private fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: pivotVideoRenderer ?: compactVideoRenderer
private fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
private fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer ?: compactChannelRenderer
private fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer ?: compactPlaylistRenderer
private fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer ?: compactRadioRenderer
private fun ItemWrapper.getTileItem() = tileRenderer

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
    if (getTileItem() != null)
        return when (getTileItem()?.getContentType()) {
            TILE_CONTENT_TYPE_CHANNEL -> MediaItem.TYPE_CHANNEL
            TILE_CONTENT_TYPE_PLAYLIST -> MediaItem.TYPE_PLAYLIST
            TILE_CONTENT_TYPE_VIDEO -> MediaItem.TYPE_VIDEO
            else -> MediaItem.TYPE_UNDEFINED
        }

    return MediaItem.TYPE_UNDEFINED;
}

fun ItemWrapper.getVideoId() = getVideoItem()?.getVideoId() ?: getMusicItem()?.getVideoId() ?: getTileItem()?.getVideoId()
fun ItemWrapper.getTitle() = getVideoItem()?.getTitle() ?: getMusicItem()?.getTitle() ?: getTileItem()?.getTitle()
fun ItemWrapper.getThumbnails() = getVideoItem()?.getThumbnails() ?: getMusicItem()?.getThumbnails() ?: getTileItem()?.getThumbnails()
fun ItemWrapper.getDescBadgeText() = getVideoItem()?.getDescBadgeText() ?: getMusicItem()?.getDescBadgeText() ?: getTileItem()?.getDescBadgeText()
fun ItemWrapper.getBadgeText() = getVideoItem()?.getBadgeText() ?: getMusicItem()?.getBadgeText() ?: getTileItem()?.getBadgeText()
fun ItemWrapper.getUserName() = getVideoItem()?.getUserName() ?: getMusicItem()?.getUserName() ?: getTileItem()?.getUserName()
fun ItemWrapper.getPublishedTime() = getVideoItem()?.getPublishedTimeText() ?: getMusicItem()?.getViewsAndPublished() ?: getTileItem()?.getPublishedTime()
fun ItemWrapper.getViewCountText() = getVideoItem()?.getViewCount() ?: getMusicItem()?.getViewsCountText() ?: getTileItem()?.getViewCountText()
fun ItemWrapper.getUpcomingEventText() = getVideoItem()?.getUpcomingEventText() ?: getMusicItem()?.getUpcomingEventText() ?: getTileItem()?.getUpcomingEventText()
fun ItemWrapper.getPlaylistId() = getVideoItem()?.getPlaylistId() ?: getMusicItem()?.getPlaylistId() ?: getTileItem()?.getPlaylistId()
fun ItemWrapper.getLengthText() = getVideoItem()?.getLengthText() ?: getMusicItem()?.getLengthText() ?: getTileItem()?.getLengthText()
fun ItemWrapper.getChannelId() = getVideoItem()?.getChannelId() ?: getMusicItem()?.getChannelId() ?: getTileItem()?.getChannelId()
fun ItemWrapper.getPlaylistIndex() = getVideoItem()?.getPlaylistIndex() ?: getMusicItem()?.getPlaylistIndex() ?: getTileItem()?.getPlaylistIndex()
fun ItemWrapper.isLive() = getVideoItem()?.isLive() ?: getMusicItem()?.isLive() ?: getTileItem()?.isLive()
fun ItemWrapper.isUpcoming() = getVideoItem()?.isUpcoming() ?: getMusicItem()?.isUpcoming() ?: getTileItem()?.isUpcoming()

/////

fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed
fun VideoOwnerItem.getChannelId() = navigationEndpoint?.getBrowseId() ?: subscribeButton?.subscribeButtonRenderer?.channelId

/////

fun WatchNextResult.getSuggestedSections() = contents?.singleColumnWatchNextResults?.pivot?.let { it.pivot ?: it.sectionListRenderer }?.contents?.map { it?.shelfRenderer }
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
fun VideoMetadataItem.getLikeStatus() = likeStatus ?: likeButton?.likeButtonRenderer?.likeStatus
fun VideoMetadataItem.isUpcoming() = badges?.firstNotNullOfOrNull { it?.upcomingEventBadge?.label?.getText() }?.let { true } ?: false
fun VideoMetadataItem.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched } ?: 0

////////

fun NavigationEndpointItem.getBrowseId() = browseEndpoint?.browseId

////////

fun ButtonStateItem.isLikeToggled() = likeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isDislikeToggled() = dislikeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isSubscribeToggled() = subscribeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.getChannelId() = channelButton?.videoOwnerRenderer?.getChannelId()


////////

fun MenuItem.getBrowseId() = menuRenderer?.items?.firstNotNullOfOrNull { it?.menuNavigationItemRenderer?.navigationEndpoint?.getBrowseId() }

///////

fun ShelfItem.getTitle() = title?.getText() ?: headerRenderer?.shelfHeaderRenderer?.title?.getText()
fun ShelfItem.getItemWrappers() = content?.horizontalListRenderer?.items
fun ShelfItem.getNextPageKey() = content?.horizontalListRenderer?.continuations?.firstNotNullOfOrNull { it?.nextContinuationData?.continuation }
fun ShelfItem.getChipItems() = headerRenderer?.chipCloudRenderer?.chips

////////

/**
 * In some cases chip item contains multiple shelfs<br/>
 * Other regular shelfs in this case is empty
 */
fun ChipItem.getShelfItems() = chipCloudChipRenderer?.content?.sectionListRenderer?.contents?.map { it?.shelfRenderer }
fun ChipItem.getTitle() = chipCloudChipRenderer?.text?.getText()

//////

fun NextVideoItem.getVideoId() = endpoint?.watchEndpoint?.videoId
fun NextVideoItem.getTitle() = item?.previewButtonRenderer?.title?.getText()
fun NextVideoItem.getAuthor() = item?.previewButtonRenderer?.byline?.getText()
fun NextVideoItem.getThumbnails() = item?.previewButtonRenderer?.thumbnail
fun NextVideoItem.getPlaylistId() = endpoint?.watchEndpoint?.playlistId
fun NextVideoItem.getPlaylistIndex() = endpoint?.watchEndpoint?.index
fun NextVideoItem.getParams() = endpoint?.watchEndpoint?.params

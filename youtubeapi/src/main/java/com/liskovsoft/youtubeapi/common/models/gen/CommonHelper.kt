package com.liskovsoft.youtubeapi.common.models.gen

import com.liskovsoft.youtubeapi.common.helpers.YouTubeHelper
import com.liskovsoft.mediaserviceinterfaces.yt.data.MediaItem
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.browse.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationKey

private const val TILE_CONTENT_TYPE_UNDEFINED = "UNDEFINED"
private const val TILE_CONTENT_TYPE_CHANNEL = "TILE_CONTENT_TYPE_CHANNEL"
private const val TILE_CONTENT_TYPE_PLAYLIST = "TILE_CONTENT_TYPE_PLAYLIST"
private const val TILE_CONTENT_TYPE_VIDEO = "TILE_CONTENT_TYPE_VIDEO"
private const val BADGE_STYLE_LIVE = "LIVE"
private const val BADGE_STYLE_UPCOMING = "UPCOMING"
private const val BADGE_STYLE_SHORTS = "SHORTS"
private const val BADGE_STYLE_DEFAULT = "DEFAULT"
private const val BADGE_STYLE_MOVIE = "BADGE_STYLE_TYPE_YPC"
private const val OLD_BADGE_STYLE_LIVE = "BADGE_STYLE_TYPE_LIVE_NOW"
private const val ICON_TYPE_NOT_INTERESTED = "NOT_INTERESTED"
private const val ICON_TYPE_REMOVE = "REMOVE"

///////////

internal fun TextItem.getText() = runs?.joinToString("") { it?.text ?: it?.emoji?.getText() ?: "" } ?: simpleText
internal fun TextItem.getAccessibilityLabel() = accessibility?.accessibilityData?.label

/**
 * Use shortcut name as workaround to display custom emoji. Custom emoji are images.
 */
//fun LiveChatEmoji.getText() = if (isCustomEmoji == true) shortcuts?.getOrElse(0) { "" } else emojiId
/**
 * Use empty string as workaround to display custom emoji. Custom emoji are images.
 */
internal fun LiveChatEmoji.getText() = if (isCustomEmoji == true) "" else emojiId

/**
 * Find optimal thumbnail for tv screen
 */
internal fun ThumbnailItem.getOptimalResThumbnailUrl() = thumbnails?.getOrElse(YouTubeHelper.OPTIMAL_RES_THUMBNAIL_INDEX) { thumbnails.lastOrNull() } ?.getUrl()
internal fun ThumbnailItem.getHighResThumbnailUrl() = thumbnails?.lastOrNull()?.getUrl()
internal fun ThumbnailItem.Thumbnail.getUrl(): String? {
    var newUrl = if (url?.startsWith("//") == true) "https:$url" else url

    newUrl = YouTubeHelper.avatarBlockFix(newUrl)

    return newUrl
}

////////

internal fun NavigationEndpointItem.getBrowseId() = browseEndpoint?.browseId
internal fun NavigationEndpointItem.getBrowseParams() = browseEndpoint?.params
internal fun NavigationEndpointItem.getOverlayToggleButton() = getContent()?.overlayPanelItemListRenderer?.items?.firstNotNullOfOrNull { it?.toggleButtonRenderer }
internal fun NavigationEndpointItem.getOverlaySubscribeButton() = getContent()?.overlayPanelItemListRenderer?.items?.firstNotNullOfOrNull { it?.subscribeButtonRenderer }
internal fun NavigationEndpointItem.isSubscribed() = getOverlaySubscribeButton()?.subscribed
internal fun NavigationEndpointItem.getContinuation() = getContent()?.itemSectionRenderer?.continuations?.firstOrNull() ?: getEngagementPanel()?.content?.sectionListRenderer?.contents?.firstOrNull()?.itemSectionRenderer?.continuations?.firstOrNull()
internal fun NavigationEndpointItem.getTitle() = getHeader()?.overlayPanelHeaderRenderer?.title?.getText()
internal fun NavigationEndpointItem.getSubtitle() = getHeader()?.overlayPanelHeaderRenderer?.subtitle?.getText()
internal fun NavigationEndpointItem.getStartTimeSeconds() = watchEndpoint?.startTimeSeconds
internal fun NavigationEndpointItem.getVideoId() = watchEndpoint?.videoId
internal fun NavigationEndpointItem.getPlaylistId() = watchEndpoint?.playlistId ?: watchPlaylistEndpoint?.playlistId
internal fun NavigationEndpointItem.getIndex() = watchEndpoint?.index
private fun NavigationEndpointItem.getOverlayPanel() = openPopupAction?.popup?.overlaySectionRenderer?.overlay
    ?.overlayTwoPanelRenderer?.actionPanel?.overlayPanelRenderer
private fun NavigationEndpointItem.getEngagementPanel() = showEngagementPanelEndpoint?.engagementPanel?.engagementPanelSectionListRenderer
private fun NavigationEndpointItem.getContent() = getOverlayPanel()?.content
private fun NavigationEndpointItem.getHeader() = getOverlayPanel()?.header

////////

internal fun MenuWrapper.getBrowseId() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getBrowseId() }
internal fun MenuWrapper.getNotificationToken() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getNotificationToken() }
internal fun MenuWrapper.getFeedbackTokens(): List<String?>? = menuRenderer?.items?.mapNotNull { it?.getFeedbackToken() }
// Filter by icon not robust. Icon item not always present.
internal fun MenuWrapper.getVideoToken() = menuRenderer?.items?.firstOrNull {
        it?.getIconType() == ICON_TYPE_NOT_INTERESTED
    }?.getFeedbackToken()
// Filter by icon not robust. Icon item not always present.
internal fun MenuWrapper.getChannelToken() = menuRenderer?.items?.firstOrNull {
        it?.getIconType() == ICON_TYPE_REMOVE
    }?.getFeedbackToken()

//////////

// gridVideoRenderer
internal fun VideoItem.getTitle() = title?.getText() ?: headline?.getText()
internal fun VideoItem.getVideoId() = videoId
internal fun VideoItem.getThumbnails() = thumbnail
internal fun VideoItem.getMovingThumbnails() = richThumbnail?.movingThumbnailRenderer?.movingThumbnailDetails
internal fun VideoItem.getDescBadgeText() = badges?.getOrNull(0)?.metadataBadgeRenderer?.label
internal fun VideoItem.getLengthText() = lengthText?.getText()
internal fun VideoItem.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched }
internal fun VideoItem.getStartTimeSeconds() = navigationEndpoint?.getStartTimeSeconds()
internal fun VideoItem.getBadgeText() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() } ?:
    badges?.firstNotNullOfOrNull { it?.liveBadge?.label?.getText() ?: it?.upcomingEventBadge?.label?.getText() }
internal fun VideoItem.getUserName() = shortBylineText?.getText() ?: longBylineText?.getText()
internal fun VideoItem.getPublishedTimeText() = publishedTimeText?.getText()
internal fun VideoItem.getViewCount() = shortViewCountText?.getText() ?: viewCountText?.getText() ?: videoInfo?.getText()
// No real date, just placeholder. We should do this themselves.
internal fun VideoItem.getUpcomingEventText() = upcomingEventData?.upcomingEventText?.getText()
    ?.replace("DATE_PLACEHOLDER", Helpers.toShortDate(upcomingEventData.getStartTimeMs()))
internal fun VideoItem.getChannelId() =
    shortBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    longBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    menu?.getBrowseId()
internal fun VideoItem.getPlaylistId() = navigationEndpoint?.getPlaylistId()
internal fun VideoItem.getPlaylistIndex() = navigationEndpoint?.getIndex()
internal fun VideoItem.isLive(): Boolean = OLD_BADGE_STYLE_LIVE == getOldBadgeStyle() || BADGE_STYLE_LIVE == getBadgeStyle()
internal fun VideoItem.isUpcoming() = BADGE_STYLE_UPCOMING == getBadgeStyle()
internal fun VideoItem.isShorts() = BADGE_STYLE_SHORTS == getBadgeStyle()
internal fun VideoItem.isMovie() = BADGE_STYLE_MOVIE == getBadgeStyle()
internal fun VideoItem.getFeedbackTokens() = menu?.getFeedbackTokens()
private fun VideoItem.getOldBadgeStyle() = badges?.firstNotNullOfOrNull { it?.metadataBadgeRenderer?.style }
private fun VideoItem.getBadgeStyle() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }

////////////

internal fun MusicItem.getTitle() = primaryText?.getText()
internal fun MusicItem.getUserName() = secondaryText?.getText()
internal fun MusicItem.getThumbnails() = thumbnail
internal fun MusicItem.getVideoId() = navigationEndpoint?.getVideoId()
internal fun MusicItem.getPlaylistId() = navigationEndpoint?.getPlaylistId()
internal fun MusicItem.getBadgeText() = lengthText?.getText()
internal fun MusicItem.getLengthText() = lengthText?.getText()
internal fun MusicItem.getViewsAndPublished() = tertiaryText?.getText()
internal fun MusicItem.getChannelId() = menu?.getBrowseId()
internal fun MusicItem.getPlaylistIndex() = navigationEndpoint?.getIndex()
internal fun MusicItem.getDescBadgeText() = null
internal fun MusicItem.getViewsCountText() = null
internal fun MusicItem.getUpcomingEventText() = null
internal fun MusicItem.isLive() = false
internal fun MusicItem.isUpcoming() = false

///////////

internal fun TileItem.getTitle() = metadata?.tileMetadataRenderer?.title?.getText()
internal fun TileItem.getVideoId() = onSelectCommand?.getVideoId()
internal fun TileItem.getPlaylistId() = onSelectCommand?.getPlaylistId()
internal fun TileItem.getPlaylistIndex() = 0
internal fun TileItem.getDescBadgeText() = metadata?.tileMetadataRenderer?.lines?.map { it?.lineRenderer?.items?.getOrNull(0)?.lineItemRenderer?.badge?.metadataBadgeRenderer?.label }?.firstOrNull()
internal fun TileItem.getBadgeText() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() }
internal fun TileItem.getPercentWatched() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched }
internal fun TileItem.getStartTimeSeconds() = onSelectCommand?.getStartTimeSeconds()
internal fun TileItem.getUserName() = null
internal fun TileItem.getPublishedTime() = null
internal fun TileItem.getViewCountText() =
    YouTubeHelper.createInfo(*metadata?.tileMetadataRenderer?.lines?.map {
        ServiceHelper.combineItems(" ", *it?.lineRenderer?.items?.map { it?.lineItemRenderer?.text }?.toTypedArray() ?: emptyArray())
    }?.toTypedArray() ?: emptyArray()) ?: null
internal fun TileItem.getUpcomingEventText() = null
internal fun TileItem.getThumbnails() = header?.tileHeaderRenderer?.thumbnail
internal fun TileItem.getMovingThumbnails() = header?.tileHeaderRenderer?.let { it.movingThumbnail ?: it.onFocusThumbnail }
internal fun TileItem.getMovingThumbnailUrl() = header?.tileHeaderRenderer?.movingThumbnail?.thumbnails?.getOrNull(0)?.url
internal fun TileItem.getChannelId() = getMenu()?.getBrowseId()
internal fun TileItem.getFeedbackTokens() = getMenu()?.getFeedbackTokens()
internal fun TileItem.isLive() = BADGE_STYLE_LIVE == getBadgeStyle()
internal fun TileItem.getContentType() = contentType
internal fun TileItem.getRichTextTileText() = header?.richTextTileHeaderRenderer?.textContent?.get(0)?.getText()
internal fun TileItem.getContinuationToken() = onSelectCommand?.getContinuation()?.getContinuationKey()
internal fun TileItem.isUpcoming() = BADGE_STYLE_UPCOMING == getBadgeStyle()
internal fun TileItem.isMovie() = BADGE_STYLE_MOVIE == getBadgeStyle()
internal fun TileItem.isShorts() = false // TODO: not implemented
private fun TileItem.Header.getBadgeStyle() = tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }
private fun TileItem.Metadata.getBadgeStyle() = tileMetadataRenderer?.lines?.firstNotNullOfOrNull { it?.lineRenderer?.items?.firstNotNullOfOrNull { it?.lineItemRenderer?.badge?.metadataBadgeRenderer?.style } }
private fun TileItem.getMenu() = menu ?: onLongPressCommand?.showMenuCommand?.menu
private fun TileItem.getBadgeStyle() = header?.getBadgeStyle() ?: metadata?.getBadgeStyle()

////////////

internal fun PlaylistItem.getTitle() = title?.getText()
internal fun PlaylistItem.getPlaylistId() = playlistId
internal fun PlaylistItem.getThumbnails() = thumbnail ?: thumbnails?.getOrNull(0)
internal fun PlaylistItem.getBadgeText() = videoCountText?.getText()

////////////

internal fun ChannelItem.getTitle() = title?.getText()
internal fun ChannelItem.getThumbnails() = thumbnail
internal fun ChannelItem.getChannelId() = channelId
internal fun ChannelItem.getBadgeText() = videoCountText?.getText()
internal fun ChannelItem.getDescBadgeText() = subscriberCountText?.getText()

////////////

private fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: videoRenderer ?: pivotVideoRenderer ?: compactVideoRenderer ?: reelItemRenderer ?: playlistVideoRenderer
private fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
private fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer ?: compactChannelRenderer
private fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer ?: compactPlaylistRenderer ?: playlistRenderer
private fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer ?: compactRadioRenderer
private fun ItemWrapper.getTileItem() = tileRenderer
private fun ItemWrapper.getContinuationItem() = continuationItemRenderer

internal fun ItemWrapper.getType(): Int {
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

internal fun ItemWrapper.getVideoId() = getVideoItem()?.getVideoId() ?: getMusicItem()?.getVideoId() ?: getTileItem()?.getVideoId()
internal fun ItemWrapper.getTitle() = getVideoItem()?.getTitle() ?: getMusicItem()?.getTitle() ?: getTileItem()?.getTitle() ?: getPlaylistItem()?.getTitle() ?: getChannelItem()?.getTitle()
internal fun ItemWrapper.getThumbnails() = getVideoItem()?.getThumbnails() ?: getMusicItem()?.getThumbnails() ?: getTileItem()?.getThumbnails() ?: getPlaylistItem()?.getThumbnails() ?: getChannelItem()?.getThumbnails()
internal fun ItemWrapper.getMovingThumbnails() = getVideoItem()?.getMovingThumbnails() ?: getTileItem()?.getMovingThumbnails()
internal fun ItemWrapper.getDescBadgeText() = getVideoItem()?.getDescBadgeText() ?: getMusicItem()?.getDescBadgeText() ?: getTileItem()?.getDescBadgeText() ?: getChannelItem()?.getDescBadgeText()
internal fun ItemWrapper.getLengthText() = getVideoItem()?.getLengthText() ?: getMusicItem()?.getLengthText() ?: getTileItem()?.getBadgeText()
internal fun ItemWrapper.getPercentWatched() = getVideoItem()?.getPercentWatched() ?: getTileItem()?.getPercentWatched()
internal fun ItemWrapper.getStartTimeSeconds() = getVideoItem()?.getStartTimeSeconds() ?: getTileItem()?.getStartTimeSeconds()
internal fun ItemWrapper.getBadgeText() = getVideoItem()?.getBadgeText() ?: getMusicItem()?.getBadgeText() ?: getTileItem()?.getBadgeText() ?: getPlaylistItem()?.getBadgeText() ?: getChannelItem()?.getBadgeText()
internal fun ItemWrapper.getUserName() = getVideoItem()?.getUserName() ?: getMusicItem()?.getUserName() ?: getTileItem()?.getUserName()
internal fun ItemWrapper.getPublishedTime() = getVideoItem()?.getPublishedTimeText() ?: getMusicItem()?.getViewsAndPublished() ?: getTileItem()?.getPublishedTime()
internal fun ItemWrapper.getViewCountText() = getVideoItem()?.getViewCount() ?: getMusicItem()?.getViewsCountText() ?: getTileItem()?.getViewCountText()
internal fun ItemWrapper.getUpcomingEventText() = getVideoItem()?.getUpcomingEventText() ?: getMusicItem()?.getUpcomingEventText() ?: getTileItem()?.getUpcomingEventText()
internal fun ItemWrapper.getPlaylistId() = getVideoItem()?.getPlaylistId() ?: getMusicItem()?.getPlaylistId() ?: getTileItem()?.getPlaylistId() ?: getPlaylistItem()?.getPlaylistId()
internal fun ItemWrapper.getChannelId() = getVideoItem()?.getChannelId() ?: getMusicItem()?.getChannelId() ?: getTileItem()?.getChannelId() ?: getChannelItem()?.getChannelId()
internal fun ItemWrapper.getPlaylistIndex() = getVideoItem()?.getPlaylistIndex() ?: getMusicItem()?.getPlaylistIndex() ?: getTileItem()?.getPlaylistIndex()
internal fun ItemWrapper.isLive() = getVideoItem()?.isLive() ?: getMusicItem()?.isLive() ?: getTileItem()?.isLive()
internal fun ItemWrapper.isUpcoming() = getVideoItem()?.isUpcoming() ?: getMusicItem()?.isUpcoming() ?: getTileItem()?.isUpcoming()
internal fun ItemWrapper.isMovie() = getVideoItem()?.isMovie() ?: getTileItem()?.isMovie()
internal fun ItemWrapper.isShorts() = reelItemRenderer != null || getVideoItem()?.isShorts() ?: getTileItem()?.isShorts() ?: false
internal fun ItemWrapper.getDescriptionText() = getTileItem()?.getRichTextTileText()
internal fun ItemWrapper.getContinuationToken() = getTileItem()?.getContinuationToken() ?: getContinuationItem()?.getContinuationToken()
internal fun ItemWrapper.getFeedbackToken() = getFeedbackTokens()?.getOrNull(0)
internal fun ItemWrapper.getFeedbackToken2() = getFeedbackTokens()?.getOrNull(1)
private fun ItemWrapper.getFeedbackTokens() = getVideoItem()?.getFeedbackTokens() ?: getTileItem()?.getFeedbackTokens()

/////

internal fun DefaultServiceEndpoint.getChannelIds() = getSubscribeEndpoint()?.channelIds
internal fun DefaultServiceEndpoint.getParams() = getSubscribeEndpoint()?.params
private fun DefaultServiceEndpoint.getSubscribeEndpoint() =
    authDeterminedCommand?.authenticatedCommand?.subscribeEndpoint

/////

internal fun ToggleButtonRenderer.getSubscribeParams() = defaultServiceEndpoint?.getParams()
internal fun ToggleButtonRenderer.getUnsubscribeParams() = toggledServiceEndpoint?.unsubscribeEndpoint?.params

//////

internal fun SubscribeButtonRenderer.getParams() = serviceEndpoints?.firstNotNullOfOrNull { it?.getParams() }

//////

internal fun VideoItem.UpcomingEvent.getStartTimeMs() = startTime?.toLong()?.let { it * 1_000 } ?: -1

//////

internal fun IconItem.getType() = iconType

//////

internal fun MenuItem.getIconType() = menuServiceItemRenderer?.icon?.getType()
internal fun MenuItem.getFeedbackToken() = menuServiceItemRenderer?.serviceEndpoint?.feedbackEndpoint?.feedbackToken
internal fun MenuItem.getNotificationToken() = menuServiceItemRenderer?.serviceEndpoint?.recordNotificationInteractionsEndpoint?.serializedInteractionsRequest
internal fun MenuItem.getBrowseId() = menuNavigationItemRenderer?.navigationEndpoint?.getBrowseId()

//////

internal fun NotificationPreferenceButton.getItems() = subscriptionNotificationToggleButtonRenderer?.states?.filter { it?.getStateParams() != null }
internal fun NotificationPreferenceButton.getCurrentStateId() = subscriptionNotificationToggleButtonRenderer?.currentStateId ?: -1
internal fun NotificationStateItem.getTitle() = inlineMenuButton?.buttonRenderer?.text?.getText()
internal fun NotificationStateItem.getStateId() = stateId
internal fun NotificationStateItem.getStateParams() = inlineMenuButton?.buttonRenderer?.serviceEndpoint?.modifyChannelNotificationPreferenceEndpoint?.params
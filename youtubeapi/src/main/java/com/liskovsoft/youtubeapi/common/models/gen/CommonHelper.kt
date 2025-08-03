package com.liskovsoft.youtubeapi.common.models.gen

import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.sharedutils.helpers.DateHelper
import com.liskovsoft.youtubeapi.browse.v2.gen.getContinuationToken
import com.liskovsoft.youtubeapi.browse.v2.gen.getThumbnails
import com.liskovsoft.youtubeapi.browse.v2.gen.getVideoId
import com.liskovsoft.googlecommon.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.gen.getContinuationKey

// A badge before the image
private const val BADGE_STYLE_LIVE = "LIVE"
private const val BADGE_STYLE_UPCOMING = "UPCOMING"
private const val BADGE_STYLE_SHORTS = "SHORTS"
private const val BADGE_STYLE_DEFAULT = "DEFAULT"
// A badge before the subtitle
private const val STATUS_STYLE_MOVIE = "BADGE_STYLE_TYPE_YPC" // This mark sometimes presents on regular videos (e.g. fundraiser mark)
private const val STATUS_STYLE_QUALITY = "BADGE_STYLE_TYPE_SIMPLE"
private const val STATUS_STYLE_LIVE = "BADGE_STYLE_TYPE_LIVE_NOW"

///////////

internal fun TextItem.getText() = runs?.joinToString("") { it?.text ?: it?.emoji?.getText() ?: "" } ?: simpleText ?: content
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
internal fun ThumbnailItem.getOptimalResThumbnailUrl() = (thumbnails ?: sources)?.getOrElse(YouTubeHelper.OPTIMAL_RES_THUMBNAIL_INDEX) { (thumbnails ?: sources)?.lastOrNull() } ?.getUrl()
internal fun ThumbnailItem.getHighResThumbnailUrl() = (thumbnails ?: sources)?.lastOrNull()?.getUrl()
internal fun ThumbnailItem.Thumbnail.getUrl(): String? {
    var newUrl = if (url?.startsWith("//") == true) "https:$url" else url

    newUrl = YouTubeHelper.avatarBlockFix(newUrl)

    return newUrl
}

////////

internal fun NavigationEndpointItem.getBrowseId() = browseEndpoint?.browseId
internal fun NavigationEndpointItem.getParams() = browseEndpoint?.params ?: watchEndpoint?.params
internal fun NavigationEndpointItem.getOverlayToggleButton() = getOverlayItems()?.firstNotNullOfOrNull { it?.toggleButtonRenderer }
internal fun NavigationEndpointItem.getOverlaySubscribeButton() = getOverlayItems()?.firstNotNullOfOrNull { it?.subscribeButtonRenderer }
internal fun NavigationEndpointItem.isSubscribed() = getOverlaySubscribeButton()?.subscribed
internal fun NavigationEndpointItem.getContinuations() = getOverlayContent()?.itemSectionRenderer?.continuations
    ?: getEngagementContents()?.firstOrNull()?.itemSectionRenderer?.continuations
internal fun NavigationEndpointItem.getTitle() = getOverlayHeader()?.title?.getText()
internal fun NavigationEndpointItem.getSubtitle() = getOverlayHeader()?.subtitle?.getText()
internal fun NavigationEndpointItem.getStartTimeSeconds() = watchEndpoint?.startTimeSeconds
internal fun NavigationEndpointItem.getVideoId() = watchEndpoint?.videoId ?: reelWatchEndpoint?.videoId
internal fun NavigationEndpointItem.getPlaylistId() = watchEndpoint?.playlistId ?: watchPlaylistEndpoint?.playlistId
internal fun NavigationEndpointItem.getIndex() = watchEndpoint?.index
internal fun NavigationEndpointItem.getFeedbackToken() =
    getOverlayItems()?.firstNotNullOfOrNull {
        it?.compactLinkRenderer?.serviceEndpoint?.commandExecutorCommand?.commands?.firstNotNullOfOrNull { it.feedbackEndpoint?.feedbackToken }
    }
private fun NavigationEndpointItem.getOverlayPanel() = openPopupAction?.popup?.overlaySectionRenderer?.overlay
    ?.overlayTwoPanelRenderer?.actionPanel?.overlayPanelRenderer
private fun NavigationEndpointItem.getEngagementPanel() = showEngagementPanelEndpoint?.engagementPanel?.engagementPanelSectionListRenderer
private fun NavigationEndpointItem.getOverlayContent() = getOverlayPanel()?.content
private fun NavigationEndpointItem.getOverlayHeader() = getOverlayPanel()?.header?.overlayPanelHeaderRenderer
private fun NavigationEndpointItem.getOverlayItems() = getOverlayContent()?.overlayPanelItemListRenderer?.items
private fun NavigationEndpointItem.getEngagementContents() = getEngagementPanel()?.content?.sectionListRenderer?.contents
private fun NavigationEndpointItem.getQuery() = searchEndpoint?.query

////////

private const val MENU_ICON_TYPE_NOT_INTERESTED = "NOT_INTERESTED"
private const val MENU_ICON_TYPE_REMOVE = "REMOVE"

internal fun MenuWrapper.getBrowseId() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getBrowseId() }
internal fun MenuWrapper.getPlaylistId() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getPlaylistId() }
internal fun MenuWrapper.getVideoId() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getVideoId() }
internal fun MenuWrapper.getNotificationToken() = menuRenderer?.items?.firstNotNullOfOrNull { it?.getNotificationToken() }
internal fun MenuWrapper.getFeedbackTokens(): List<String?>? = menuRenderer?.items?.mapNotNull { it?.getFeedbackToken() }
// Filter by icon not robust. Icon item not always present.
internal fun MenuWrapper.getVideoToken() = menuRenderer?.items?.firstOrNull {
        it?.getIconType() == MENU_ICON_TYPE_NOT_INTERESTED
    }?.getFeedbackToken()
// Filter by icon not robust. Icon item not always present.
internal fun MenuWrapper.getChannelToken() = menuRenderer?.items?.firstOrNull {
        it?.getIconType() == MENU_ICON_TYPE_REMOVE
    }?.getFeedbackToken()

//////////

// gridVideoRenderer
internal fun VideoItem.getTitle() = title?.getText() ?: headline?.getText()
internal fun VideoItem.getVideoId() = videoId
internal fun VideoItem.getThumbnails() = thumbnail
internal fun VideoItem.getMovingThumbnails() = richThumbnail?.movingThumbnailRenderer?.movingThumbnailDetails
internal fun VideoItem.getSubTitle() = badges?.getOrNull(0)?.metadataBadgeRenderer?.label
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
    ?.replace("DATE_PLACEHOLDER", DateHelper.toShortDate(upcomingEventData.getStartTimeMs(), true, true, true))
internal fun VideoItem.getChannelId() =
    shortBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    longBylineText?.runs?.firstNotNullOfOrNull { it?.navigationEndpoint?.getBrowseId() } ?:
    menu?.getBrowseId()
internal fun VideoItem.getPlaylistId() = navigationEndpoint?.getPlaylistId()
internal fun VideoItem.getPlaylistIndex() = navigationEndpoint?.getIndex()
internal fun VideoItem.isLive(): Boolean = STATUS_STYLE_LIVE == getStatusStyle() || BADGE_STYLE_LIVE == getBadgeStyle()
internal fun VideoItem.isUpcoming() = BADGE_STYLE_UPCOMING == getBadgeStyle()
internal fun VideoItem.isShorts() = BADGE_STYLE_SHORTS == getBadgeStyle()
internal fun VideoItem.isMovie() = STATUS_STYLE_MOVIE == getStatusStyle() && getVideoId() == null
internal fun VideoItem.getFeedbackTokens() = menu?.getFeedbackTokens()
private fun VideoItem.getStatusStyle() = badges?.firstNotNullOfOrNull { it?.metadataBadgeRenderer?.style }
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
internal fun MusicItem.getSubTitle() = null
internal fun MusicItem.getViewsCountText() = null
internal fun MusicItem.getUpcomingEventText() = null
internal fun MusicItem.isLive() = false
internal fun MusicItem.isUpcoming() = false

////////////

internal fun RadioItem.getTitle() = title?.getText()
internal fun RadioItem.getUserName() = subtitle?.getText()
internal fun RadioItem.getThumbnails() = thumbnail ?: thumbnailRenderer?.musicThumbnailRenderer?.thumbnail
internal fun RadioItem.getVideoId() = navigationEndpoint?.getVideoId() ?: menu?.getVideoId()
internal fun RadioItem.getPlaylistId() = navigationEndpoint?.getPlaylistId() ?: menu?.getPlaylistId()
internal fun RadioItem.getBadgeText() = null
internal fun RadioItem.getLengthText() = null
internal fun RadioItem.getViewsAndPublished() = null
//internal fun RadioItem.getChannelId() = navigationEndpoint?.getBrowseId() ?: menu?.getBrowseId()
internal fun RadioItem.getChannelId() = menu?.getBrowseId()
internal fun RadioItem.getPlaylistIndex() = navigationEndpoint?.getIndex()
internal fun RadioItem.getSubTitle() = null
internal fun RadioItem.getViewsCountText() = null
internal fun RadioItem.getUpcomingEventText() = null
internal fun RadioItem.isLive() = false
internal fun RadioItem.isUpcoming() = false

///////////

// 'tileRenderer.style' values:
private const val TILE_CONTENT_TYPE_UNDEFINED = "UNDEFINED"
private const val TILE_CONTENT_TYPE_CHANNEL = "TILE_CONTENT_TYPE_CHANNEL"
private const val TILE_CONTENT_TYPE_PLAYLIST = "TILE_CONTENT_TYPE_PLAYLIST"
private const val TILE_CONTENT_TYPE_VIDEO = "TILE_CONTENT_TYPE_VIDEO"
private const val TILE_CONTENT_TYPE_EDU = "TILE_CONTENT_TYPE_EDU" // a search query tile in Home section

// 'tileRenderer.contentType' values:
private const val TILE_STYLE_DEFAULT = "TILE_STYLE_YTLR_DEFAULT"
private const val TILE_STYLE_SHORTS = "TILE_STYLE_YTLR_SHORTS"
private const val TILE_STYLE_QUERY = "TILE_STYLE_YTLR_EDU" // a search query tile in Home section

internal fun TileItem.getTitle() = metadata?.tileMetadataRenderer?.title?.getText()
    ?: header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.tileMetadataRenderer?.title?.getText() }
    ?: header?.trackTileHeaderRenderer?.title?.getText()
internal fun TileItem.getVideoId() = onSelectCommand?.getVideoId()
internal fun TileItem.getPlaylistId() = onSelectCommand?.getPlaylistId() ?: getMenu()?.getPlaylistId()
internal fun TileItem.getPlaylistIndex() = 0
internal fun TileItem.getSubTitle() = metadata?.tileMetadataRenderer?.lines?.map { it?.lineRenderer?.items?.getOrNull(0)?.lineItemRenderer?.badge?.metadataBadgeRenderer?.label }?.firstOrNull()
internal fun TileItem.getBadgeText() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.text?.getText() }
    ?: header?.trackTileHeaderRenderer?.duration?.getText()
internal fun TileItem.getPercentWatched() = header?.tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched }
internal fun TileItem.getStartTimeSeconds() = onSelectCommand?.getStartTimeSeconds()
internal fun TileItem.getUserName() = null
internal fun TileItem.getPublishedTime() = null
internal fun TileItem.getViewCountText() =
    YouTubeHelper.createInfo(*metadata?.tileMetadataRenderer?.lines?.map {
        ServiceHelper.combineItems(" ", *it?.lineRenderer?.items?.map { it?.lineItemRenderer?.text }?.toTypedArray() ?: emptyArray())
    }?.toTypedArray() ?: emptyArray())
internal fun TileItem.getUpcomingEventText() = null
internal fun TileItem.getThumbnails() = header?.tileHeaderRenderer?.thumbnail ?: header?.trackTileHeaderRenderer?.thumbnail
internal fun TileItem.getMovingThumbnails() = header?.tileHeaderRenderer?.let { it.movingThumbnail ?: it.onFocusThumbnail }
internal fun TileItem.getMovingThumbnailUrl() = header?.tileHeaderRenderer?.movingThumbnail?.thumbnails?.getOrNull(0)?.url
internal fun TileItem.getChannelId() = onSelectCommand?.getBrowseId() ?: getMenu()?.getBrowseId()
internal fun TileItem.getFeedbackTokens() = getMenu()?.getFeedbackTokens()
internal fun TileItem.isLive() = BADGE_STYLE_LIVE == getBadgeStyle()
internal fun TileItem.getContentType() = contentType
internal fun TileItem.getRichTextTileText() = header?.richTextTileHeaderRenderer?.textContent?.get(0)?.getText()
internal fun TileItem.getContinuationToken() = onSelectCommand?.getContinuations()?.getContinuationKey()
internal fun TileItem.isUpcoming() = BADGE_STYLE_UPCOMING == getBadgeStyle()
internal fun TileItem.isMovie() = STATUS_STYLE_MOVIE == getStatusStyle() && getVideoId() == null // a movie has browseId instead of videoId
internal fun TileItem.isShorts() = BADGE_STYLE_SHORTS == getBadgeStyle() || TILE_STYLE_SHORTS == getTileStyle()
internal fun TileItem.isShortsLegacy(): Boolean = getThumbnails()?.getOptimalResThumbnailUrl()?.let {
    it.contains("-AG2CIACgA-") || it.contains("-oaymwEmCIAFEOAD8quKqQMa8AEB-AHOBYAC") } ?: false
internal fun TileItem.getQuery() = onSelectCommand?.getQuery()
private fun TileItem.Header.getBadgeStyle() = tileHeaderRenderer?.thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayTimeStatusRenderer?.style }
private fun TileItem.Metadata.getStatusStyle() = tileMetadataRenderer?.lines?.firstNotNullOfOrNull { it?.lineRenderer?.items?.firstNotNullOfOrNull { it?.lineItemRenderer?.badge?.metadataBadgeRenderer?.style } }
private fun TileItem.getMenu() = menu ?: onLongPressCommand?.showMenuCommand?.menu
private fun TileItem.getTileStyle() = style
private fun TileItem.getBadgeStyle() = header?.getBadgeStyle()
private fun TileItem.getStatusStyle() = metadata?.getStatusStyle()

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
internal fun ChannelItem.getSubTitle() = subscriberCountText?.getText()

////////////

internal fun ShortsItem.getTitle() = overlayMetadata?.primaryText?.getText()
internal fun ShortsItem.getSubTitle() = overlayMetadata?.secondaryText?.getText()
internal fun ShortsItem.getVideoId() = onTap?.innertubeCommand?.reelWatchEndpoint?.getVideoId()
internal fun ShortsItem.getThumbnails() = onTap?.innertubeCommand?.reelWatchEndpoint?.getThumbnails()

////////////

internal fun LockupItem.getTitle() = metadata?.lockupMetadataViewModel?.title?.getText()
internal fun LockupItem.getSubTitle() = YouTubeHelper.createInfo(
    *metadata?.lockupMetadataViewModel?.metadata?.contentMetadataViewModel?.metadataRows?.mapNotNull {
        it?.metadataParts?.mapNotNull { it?.text?.getText() } }?.flatten()?.toTypedArray() ?: emptyArray<String>()
)
internal fun LockupItem.getVideoId() = getWatchEndpoint()?.videoId
internal fun LockupItem.getPlaylistId() = getWatchEndpoint()?.playlistId
internal fun LockupItem.getThumbnails() = getThumbnailView()?.image
internal fun LockupItem.getBadgeText() = getBadge()?.text
internal fun LockupItem.isLive() = BADGE_STYLE_LIVE == getBadge()?.badgeStyle
internal fun LockupItem.getPercentWatched() = getOverlays()?.firstNotNullOfOrNull {
    it?.thumbnailBottomOverlayViewModel?.progressBar?.thumbnailOverlayProgressBarViewModel?.startPercent }
// The video without a badge, probably Watch again
internal fun LockupItem.isEmpty() = getPercentWatched() == 100 && getBadgeText() == null
internal fun LockupItem.getFeedbackTokens() =
    metadata?.lockupMetadataViewModel?.menuButton?.buttonViewModel?.onTap?.innertubeCommand?.showSheetCommand?.panelLoadingStrategy
        ?.inlineContent?.sheetViewModel?.content?.listViewModel?.listItems?.mapNotNull {
            it?.listItemViewModel?.rendererContext?.commandContext?.onTap?.innertubeCommand?.feedbackEndpoint?.feedbackToken
        }
private fun LockupItem.getBadge() = getOverlays()?.firstNotNullOfOrNull {
    it?.thumbnailOverlayBadgeViewModel }?.thumbnailBadges?.firstNotNullOfOrNull { it?.thumbnailBadgeViewModel }
private fun LockupItem.getOverlays() = getThumbnailView()?.overlays
private fun LockupItem.getThumbnailView() = contentImage?.thumbnailViewModel ?: contentImage?.collectionThumbnailViewModel?.primaryThumbnail?.thumbnailViewModel
private fun LockupItem.getWatchEndpoint() = rendererContext?.commandContext?.onTap?.innertubeCommand?.watchEndpoint

////////////

private fun ItemWrapper.getVideoItem() = gridVideoRenderer ?: videoRenderer ?: pivotVideoRenderer ?: compactVideoRenderer ?: reelItemRenderer ?: playlistVideoRenderer
private fun ItemWrapper.getMusicItem() = tvMusicVideoRenderer
private fun ItemWrapper.getChannelItem() = gridChannelRenderer ?: pivotChannelRenderer ?: compactChannelRenderer
private fun ItemWrapper.getPlaylistItem() = gridPlaylistRenderer ?: pivotPlaylistRenderer ?: compactPlaylistRenderer ?: playlistRenderer
private fun ItemWrapper.getRadioItem() = gridRadioRenderer ?: pivotRadioRenderer ?: compactRadioRenderer ?: musicTwoRowItemRenderer
private fun ItemWrapper.getTileItem() = tileRenderer
private fun ItemWrapper.getContinuationItem() = continuationItemRenderer
private fun ItemWrapper.getShortsItem() = shortsLockupViewModel
private fun ItemWrapper.getLockupItem() = lockupViewModel

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

    return MediaItem.TYPE_UNDEFINED
}

internal fun ItemWrapper.getVideoId() = getVideoItem()?.getVideoId() ?: getMusicItem()?.getVideoId() ?: getTileItem()?.getVideoId() ?: getRadioItem()?.getVideoId()
    ?: getShortsItem()?.getVideoId() ?: getLockupItem()?.getVideoId()
internal fun ItemWrapper.getTitle() = getVideoItem()?.getTitle() ?: getMusicItem()?.getTitle() ?: getTileItem()?.getTitle() ?: getPlaylistItem()?.getTitle()
    ?: getChannelItem()?.getTitle() ?: getRadioItem()?.getTitle() ?: getShortsItem()?.getTitle() ?: getLockupItem()?.getTitle()
internal fun ItemWrapper.getThumbnails() = getVideoItem()?.getThumbnails() ?: getMusicItem()?.getThumbnails() ?: getTileItem()?.getThumbnails()
    ?: getPlaylistItem()?.getThumbnails() ?: getChannelItem()?.getThumbnails() ?: getRadioItem()?.getThumbnails() ?: getShortsItem()?.getThumbnails() ?: getLockupItem()?.getThumbnails()
internal fun ItemWrapper.getMovingThumbnails() = getVideoItem()?.getMovingThumbnails() ?: getTileItem()?.getMovingThumbnails()
internal fun ItemWrapper.getSubTitle() = getVideoItem()?.getSubTitle() ?: getMusicItem()?.getSubTitle() ?: getTileItem()?.getSubTitle()
    ?: getChannelItem()?.getSubTitle() ?: getShortsItem()?.getSubTitle() ?: getLockupItem()?.getSubTitle()
internal fun ItemWrapper.getLengthText() = getVideoItem()?.getLengthText() ?: getMusicItem()?.getLengthText() ?: getTileItem()?.getBadgeText()
internal fun ItemWrapper.getBadgeText() = getVideoItem()?.getBadgeText() ?: getMusicItem()?.getBadgeText() ?: getTileItem()?.getBadgeText()
?: getPlaylistItem()?.getBadgeText() ?: getChannelItem()?.getBadgeText() ?: getLockupItem()?.getBadgeText()
internal fun ItemWrapper.getPercentWatched() = getVideoItem()?.getPercentWatched() ?: getTileItem()?.getPercentWatched() ?: getLockupItem()?.getPercentWatched()
internal fun ItemWrapper.getStartTimeSeconds() = getVideoItem()?.getStartTimeSeconds() ?: getTileItem()?.getStartTimeSeconds()
internal fun ItemWrapper.getUserName() = getVideoItem()?.getUserName() ?: getMusicItem()?.getUserName() ?: getTileItem()?.getUserName()
    ?: getRadioItem()?.getUserName()
internal fun ItemWrapper.getPublishedTime() = getVideoItem()?.getPublishedTimeText() ?: getMusicItem()?.getViewsAndPublished() ?: getTileItem()?.getPublishedTime()
internal fun ItemWrapper.getViewCountText() = getVideoItem()?.getViewCount() ?: getMusicItem()?.getViewsCountText() ?: getTileItem()?.getViewCountText()
internal fun ItemWrapper.getUpcomingEventText() = getVideoItem()?.getUpcomingEventText() ?: getMusicItem()?.getUpcomingEventText()
    ?: getTileItem()?.getUpcomingEventText()
internal fun ItemWrapper.getPlaylistId() = getVideoItem()?.getPlaylistId() ?: getMusicItem()?.getPlaylistId() ?: getTileItem()?.getPlaylistId() ?: getLockupItem()?.getPlaylistId()
    ?: getPlaylistItem()?.getPlaylistId() ?: getRadioItem()?.getPlaylistId()
internal fun ItemWrapper.getChannelId() = getVideoItem()?.getChannelId() ?: getMusicItem()?.getChannelId() ?: getTileItem()?.getChannelId()
    ?: getChannelItem()?.getChannelId() ?: getRadioItem()?.getChannelId()
internal fun ItemWrapper.getPlaylistIndex() = getVideoItem()?.getPlaylistIndex() ?: getMusicItem()?.getPlaylistIndex() ?: getTileItem()?.getPlaylistIndex()
internal fun ItemWrapper.isLive() = getVideoItem()?.isLive() ?: getMusicItem()?.isLive() ?: getTileItem()?.isLive() ?: getLockupItem()?.isLive() ?: false
internal fun ItemWrapper.isUpcoming() = getVideoItem()?.isUpcoming() ?: getMusicItem()?.isUpcoming() ?: getTileItem()?.isUpcoming() ?: false
internal fun ItemWrapper.isMovie() = getVideoItem()?.isMovie() ?: getTileItem()?.isMovie() ?: false
internal fun ItemWrapper.isShorts() = reelItemRenderer != null || shortsLockupViewModel != null || getVideoItem()?.isShorts() ?: getTileItem()?.isShorts() ?: false
internal fun ItemWrapper.isShortsLegacy() = getTileItem()?.isShortsLegacy() ?: false
internal fun ItemWrapper.getDescriptionText() = getTileItem()?.getRichTextTileText()
internal fun ItemWrapper.getContinuationToken() = getTileItem()?.getContinuationToken() ?: getContinuationItem()?.getContinuationToken()
internal fun ItemWrapper.getFeedbackToken() = getFeedbackTokens()?.getOrNull(0)
internal fun ItemWrapper.getFeedbackToken2() = getFeedbackTokens()?.getOrNull(1)
internal fun ItemWrapper.isEmpty() = getLockupItem()?.isEmpty() ?: false
internal fun ItemWrapper.getQuery() = getTileItem()?.getQuery()
private fun ItemWrapper.getFeedbackTokens() = getVideoItem()?.getFeedbackTokens() ?: getTileItem()?.getFeedbackTokens() ?: getLockupItem()?.getFeedbackTokens()

/////

internal fun DefaultServiceEndpoint.getChannelIds() = getSubscribeEndpoint()?.channelIds
internal fun DefaultServiceEndpoint.getParams() = getSubscribeEndpoint()?.params
private fun DefaultServiceEndpoint.getSubscribeEndpoint() = authDeterminedCommand?.authenticatedCommand?.subscribeEndpoint

/////

internal fun ToggledServiceEndpoint.getParams() = subscribeEndpoint?.params ?: unsubscribeEndpoint?.params ?: performCommentActionEndpoint?.action

/////

internal fun ToggleButtonRenderer.getParams() = defaultServiceEndpoint?.getParams() ?: toggledServiceEndpoint?.getParams()
internal fun ToggleButtonRenderer.getDefaultParams() = defaultServiceEndpoint?.getParams()
internal fun ToggleButtonRenderer.getToggleParams() = toggledServiceEndpoint?.getParams()

//////

internal fun SubscribeButtonRenderer.getParams() = serviceEndpoints?.firstNotNullOfOrNull { it?.getParams() } ?: onSubscribeEndpoints?.firstNotNullOfOrNull { it?.getParams() }

//////

internal fun VideoItem.UpcomingEvent.getStartTimeMs() = startTime?.toLong()?.let { it * 1_000 } ?: -1

//////

internal fun IconItem.getType() = iconType

//////

internal fun MenuItem.getIconType() = menuServiceItemRenderer?.icon?.getType()
internal fun MenuItem.getFeedbackToken() = menuServiceItemRenderer?.serviceEndpoint?.feedbackEndpoint?.feedbackToken
    ?: menuServiceItemRenderer?.command?.getFeedbackToken()
internal fun MenuItem.getNotificationToken() = menuServiceItemRenderer?.serviceEndpoint?.recordNotificationInteractionsEndpoint?.serializedInteractionsRequest
internal fun MenuItem.getBrowseId() = menuNavigationItemRenderer?.navigationEndpoint?.getBrowseId()
internal fun MenuItem.getPlaylistId() = menuNavigationItemRenderer?.navigationEndpoint?.getPlaylistId()
internal fun MenuItem.getVideoId() = menuNavigationItemRenderer?.navigationEndpoint?.getVideoId()

//////

internal fun NotificationPreferenceButton.getItems() = subscriptionNotificationToggleButtonRenderer?.states?.filter { it?.getStateParams() != null }
internal fun NotificationPreferenceButton.getCurrentStateId() = subscriptionNotificationToggleButtonRenderer?.currentStateId ?: -1
internal fun NotificationStateItem.getTitle() = inlineMenuButton?.buttonRenderer?.text?.getText()
internal fun NotificationStateItem.getStateId() = stateId
internal fun NotificationStateItem.getStateParams() = inlineMenuButton?.buttonRenderer?.serviceEndpoint?.modifyChannelNotificationPreferenceEndpoint?.params

//////

private const val SERVICE_SUGGEST = "SUGGEST"
private const val SERVICE_GFEEDBACK = "GFEEDBACK"

private const val KEY_E = "e"
private const val KEY_LOGGED_IN = "logged_in"
private const val KEY_SUGGESTXP = "sugexp"
private const val KEY_SUGGEST_TOKEN = "suggest_token"

internal fun ResponseContext.getSuggestToken(): String? = serviceTrackingParams?.firstNotNullOfOrNull {
    if (it?.service == SERVICE_SUGGEST) {
        it.params?.firstOrNull { it?.key == KEY_SUGGEST_TOKEN }?.value
    } else null
}
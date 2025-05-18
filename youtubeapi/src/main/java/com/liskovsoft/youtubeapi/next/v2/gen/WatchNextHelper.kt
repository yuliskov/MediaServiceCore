package com.liskovsoft.youtubeapi.next.v2.gen

import com.liskovsoft.youtubeapi.browse.v2.gen.GridRenderer
import com.liskovsoft.youtubeapi.browse.v2.gen.PlaylistVideoListRenderer
import com.liskovsoft.youtubeapi.browse.v2.gen.Shelf
import com.liskovsoft.youtubeapi.common.models.gen.*

//////

internal fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed ?:
    navigationEndpoint?.getOverlayToggleButton()?.isToggled ?: navigationEndpoint?.getOverlaySubscribeButton()?.subscribed
internal fun VideoOwnerItem.getChannelId() = navigationEndpoint?.getBrowseId() ?: subscribeButton?.subscribeButtonRenderer?.channelId
internal fun VideoOwnerItem.getThumbnails() = thumbnail
internal fun VideoOwnerItem.getParams() =
    navigationEndpoint?.getOverlayToggleButton()?.getParams() ?: navigationEndpoint?.getOverlaySubscribeButton()?.getParams() ?: subscribeButton?.subscribeButtonRenderer?.getParams()
internal fun VideoOwnerItem.getNotificationPreference() = subscribeButton?.subscribeButtonRenderer?.notificationPreferenceButton
internal fun VideoOwnerItem.getSubscriberCount() = subscriberCountText?.getText() ?: subscribeButton?.subscribeButtonRenderer?.longSubscriberCountText?.getText()
internal fun VideoOwnerItem.getShortSubscriberCount() = subscribeButton?.subscribeButtonRenderer?.shortSubscriberCountText?.getText()

/////

private const val MARKER_TYPE_HEATMAP = "MARKER_TYPE_HEATMAP"
private const val MARKER_TYPE_CHAPTERS = "MARKER_TYPE_CHAPTERS"

private fun WatchNextResult.getWatchNextResults() = contents?.singleColumnWatchNextResults
private fun WatchNextResult.getPlayerOverlays() = playerOverlays?.playerOverlayRenderer
internal fun WatchNextResult.getSuggestedSections() = getWatchNextResults()?.pivot?.let { it.pivot ?: it.sectionListRenderer }?.contents?.mapNotNull { it?.shelfRenderer }
internal fun WatchNextResult.getVideoMetadata() = getWatchNextResults()?.results?.results?.contents?.getOrNull(0)?.
    itemSectionRenderer?.contents?.map { it?.videoMetadataRenderer ?: it?.musicWatchMetadataRenderer }?.firstOrNull()

internal fun WatchNextResult.getNextVideoItem() = getWatchNextResults()?.autoplay?.autoplay?.sets?.getOrNull(0)?.
    nextVideoRenderer?.let { it.maybeHistoryEndpointRenderer ?: it.autoplayEndpointRenderer ?: it.autoplayVideoWrapperRenderer?.primaryEndpointRenderer?.autoplayEndpointRenderer }

internal fun WatchNextResult.getVideoDetails() = getReplayItemWrapper()?.pivotVideoRenderer
internal fun WatchNextResult.getReplayItemWrapper() = getWatchNextResults()?.autoplay?.autoplay?.replayVideoRenderer
internal fun WatchNextResult.getButtonStateItem() = transportControls?.transportControlsRenderer
internal fun WatchNextResult.getLiveChatToken() = getWatchNextResults()?.conversationBar?.liveChatRenderer?.continuations?.getOrNull(0)?.reloadContinuationData?.continuation
internal fun WatchNextResult.getPlaylistInfo() = getWatchNextResults()?.playlist?.playlist
internal fun WatchNextResult.getChapters() = getPlayerOverlays()?.decoratedPlayerBarRenderer?.decoratedPlayerBarRenderer?.
    playerBar?.multiMarkersPlayerBarRenderer?.markersMap?.firstOrNull()?.value?.chapters ?:
    engagementPanels?.firstNotNullOfOrNull { it?.engagementPanelSectionListRenderer?.content?.macroMarkersListRenderer?.contents } ?:
    frameworkUpdates?.entityBatchUpdate?.mutations?.firstNotNullOfOrNull { it?.payload?.macroMarkersListEntity?.markersList?.takeIf { it.markerType == MARKER_TYPE_CHAPTERS }?.markers }
internal fun WatchNextResult.getCommentPanel() = engagementPanels?.firstOrNull { it?.isCommentsSection() == true }
internal fun WatchNextResult.getDescriptionPanel() = engagementPanels?.firstOrNull { it?.isDescriptionSection() == true }
internal fun WatchNextResult.isEmpty(): Boolean = getSuggestedSections()?.isEmpty() ?: true

internal fun WatchNextResultContinuation.isEmpty(): Boolean = getItems() == null
internal fun WatchNextResultContinuation.getItems(): List<ItemWrapper?>? = getContinuation()?.let { it.items ?: it.contents }
internal fun WatchNextResultContinuation.getNextPageKey(): String? = getContinuation()?.continuations?.getContinuationKey()
    ?: getSectionContinuation()?.continuations?.getContinuationKey()
internal fun WatchNextResultContinuation.getShelves(): List<Shelf?>? = getSectionContinuation()?.contents
private fun WatchNextResultContinuation.getContinuation() = continuationContents?.horizontalListContinuation
    ?: continuationContents?.gridContinuation ?: continuationContents?.playlistVideoListContinuation
    ?: continuationContents?.tvSurfaceContentContinuation?.content?.gridRenderer
private fun WatchNextResultContinuation.getSectionContinuation() =
    continuationContents?.sectionListContinuation ?: continuationContents?.tvSurfaceContentContinuation?.content?.sectionListRenderer

///////

const val LIKE_STATUS_LIKE = "LIKE"
const val LIKE_STATUS_DISLIKE = "DISLIKE"
const val LIKE_STATUS_INDIFFERENT = "INDIFFERENT"
internal fun VideoMetadataRenderer.getVideoOwner() = owner?.videoOwnerRenderer
internal fun VideoMetadataRenderer.getTitle() = title?.getText()
internal fun VideoMetadataRenderer.getLongViewCountText() = viewCount?.videoViewCountRenderer?.viewCount?.getText() ?: viewCountText?.getText()
internal fun VideoMetadataRenderer.getViewCountText() = viewCount?.videoViewCountRenderer?.shortViewCount?.getText() ?: shortViewCountText?.getText()
internal fun VideoMetadataRenderer.isLive() = viewCount?.videoViewCountRenderer?.isLive
internal fun VideoMetadataRenderer.getDateText() = dateText?.getAccessibilityLabel() // contains relative published date (e.g. 1 hour ago)
internal fun VideoMetadataRenderer.getPublishedTime() = publishedTimeText?.getText() ?: publishedTime?.getText()
internal fun VideoMetadataRenderer.getAlbumName() = albumName?.getText()
internal fun VideoMetadataRenderer.getLikeStatus() = likeStatus ?: likeButton?.likeButtonRenderer?.likeStatus
internal fun VideoMetadataRenderer.getLikeCount() = likeStatus ?: likeButton?.likeButtonRenderer?.likeCountText?.getText()
internal fun VideoMetadataRenderer.getLikeCountInt() = likeButton?.likeButtonRenderer?.likeCount
internal fun VideoMetadataRenderer.isUpcoming() = badges?.firstNotNullOfOrNull { it?.upcomingEventBadge?.label?.getText() }?.let { true } ?: false
internal fun VideoMetadataRenderer.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched } ?: 0

////////

const val TYPE_CHANNEL = "TRANSPORT_CONTROLS_BUTTON_TYPE_CHANNEL_BUTTON"
const val TYPE_SKIP_PREVIOUS = "TRANSPORT_CONTROLS_BUTTON_TYPE_SKIP_PREVIOUS"
const val TYPE_SKIP_NEXT = "TRANSPORT_CONTROLS_BUTTON_TYPE_SKIP_NEXT"
const val TYPE_LIKE = "TRANSPORT_CONTROLS_BUTTON_TYPE_LIKE_BUTTON"
const val TYPE_DISLIKE = "TRANSPORT_CONTROLS_BUTTON_TYPE_DISLIKE_BUTTON"
const val TYPE_ADD_TO_PLAYLIST = "TRANSPORT_CONTROLS_BUTTON_TYPE_ADD_TO_PLAYLIST"

internal fun ButtonStateItem.isLikeToggled() = likeButton?.toggleButtonRenderer?.isToggled ?: getButton(TYPE_LIKE)?.toggleButtonRenderer?.isToggled
internal fun ButtonStateItem.isDislikeToggled() = dislikeButton?.toggleButtonRenderer?.isToggled ?: getButton(TYPE_DISLIKE)?.toggleButtonRenderer?.isToggled
internal fun ButtonStateItem.isSubscribeToggled() = subscribeButton?.toggleButtonRenderer?.isToggled
internal fun ButtonStateItem.getChannelId() = getChannelOwner()?.getChannelId()
internal fun ButtonStateItem.getChannelOwner() = channelButton?.videoOwnerRenderer ?: getButton(TYPE_CHANNEL)?.videoOwnerRenderer
private fun ButtonStateItem.getButton(type: String) = buttons?.firstOrNull { it?.type == type }?.button

///////

internal fun ShelfRenderer.getTitle() = title?.getText() ?: getShelf()?.title?.getText() ?: getShelf()?.avatarLockup?.avatarLockupRenderer?.title?.getText()
internal fun ShelfRenderer.getItemWrappers() = content?.horizontalListRenderer?.items
internal fun ShelfRenderer.getNextPageKey() = content?.horizontalListRenderer?.continuations?.getContinuationKey()
internal fun ShelfRenderer.getChipItems() = headerRenderer?.chipCloudRenderer?.chips
private fun ShelfRenderer.getShelf() = headerRenderer?.shelfHeaderRenderer

///////

internal fun GridRenderer.getNextPageKey() = continuations?.firstOrNull()?.getContinuationKey() ?: items?.lastOrNull()?.getContinuationToken()

///////

internal fun PlaylistVideoListRenderer.getNextPageKey() = continuations?.firstOrNull()?.getContinuationKey() ?: contents?.lastOrNull()?.getContinuationToken()

////////

/**
 * In some cases chip item contains multiple shelfs<br/>
 * Other regular shelfs in this case is empty
 */
internal fun ChipItem.getShelfItems() = chipCloudChipRenderer?.content?.sectionListRenderer?.contents?.map { it?.shelfRenderer }
internal fun ChipItem.getTitle() = chipCloudChipRenderer?.text?.getText()

//////

internal fun NextVideoItem.getVideoId() = endpoint?.watchEndpoint?.videoId
internal fun NextVideoItem.getTitle() = item?.previewButtonRenderer?.title?.getText()
internal fun NextVideoItem.getAuthor() = item?.previewButtonRenderer?.byline?.getText()
internal fun NextVideoItem.getThumbnails() = item?.previewButtonRenderer?.thumbnail
internal fun NextVideoItem.getPlaylistId() = endpoint?.watchEndpoint?.playlistId
internal fun NextVideoItem.getPlaylistIndex() = endpoint?.watchEndpoint?.index
internal fun NextVideoItem.getParams() = endpoint?.watchEndpoint?.params

/////// Chapters wrapper

internal fun ChapterItemWrapper.getTitle() = chapterRenderer?.getTitle() ?: macroMarkersListItemRenderer?.getTitle() ?: title?.toString()
internal fun ChapterItemWrapper.getStartTimeMs() = chapterRenderer?.getStartTimeMs() ?: macroMarkersListItemRenderer?.getStartTimeMs() ?: startMillis?.toLong()
internal fun ChapterItemWrapper.getThumbnailUrl() = chapterRenderer?.getThumbnailUrl() ?: macroMarkersListItemRenderer?.getThumbnailUrl() ?: thumbnailDetails?.getOptimalResThumbnailUrl()

/////// Chapters V1

internal fun ChapterRenderer.getTitle() = title?.toString()
internal fun ChapterRenderer.getStartTimeMs() = timeRangeStartMillis
internal fun ChapterRenderer.getThumbnailUrl() = thumbnail?.getOptimalResThumbnailUrl()

/////// Chapters V2

internal fun MacroMarkersListItemRenderer.getTitle() = title?.toString()
internal fun MacroMarkersListItemRenderer.getStartTimeMs(): Long? = onTap?.watchEndpoint?.startTimeSeconds?.let { it.toLong() * 1_000 }
internal fun MacroMarkersListItemRenderer.getThumbnailUrl() = thumbnail?.getOptimalResThumbnailUrl()

/////// Chapters V3 (replaced with ChapterItemWrapper)

internal fun Marker.getTitle(): String? = title?.toString()
internal fun Marker.getStartTimeMs(): Long? = startMillis?.toLong()
internal fun Marker.getDurationTimeMs(): Long? = durationMillis?.toLong()
internal fun Marker.getThumbnailUrl(): String? = thumbnailDetails?.getOptimalResThumbnailUrl()

///////

internal fun ContinuationItem.getContinuationKey(): String? =
    nextContinuationData?.continuation ?: nextRadioContinuationData?.continuation ?: reloadContinuationData?.continuation
internal fun ContinuationItem.getLabel(): String? = nextContinuationData?.label?.getText()

//internal fun List<ContinuationItem?>.getContinuationKey(): String? = firstOrNull()?.getContinuationKey()
internal fun List<ContinuationItem?>.getContinuationKey(): String? = firstNotNullOfOrNull { it?.getContinuationKey() }

///////

internal fun EngagementPanel.getTopCommentsToken(): String? = getSubMenuItems()?.getOrNull(0)?.continuation?.getContinuationKey() ?:
    getSections()?.firstNotNullOfOrNull { it?.itemSectionRenderer?.continuations?.getContinuationKey() }
internal fun EngagementPanel.getNewCommentsToken(): String? = getSubMenuItems()?.getOrNull(1)?.continuation?.getContinuationKey()
internal fun EngagementPanel.isCommentsSection(): Boolean = engagementPanelSectionListRenderer?.panelIdentifier == "comment-item-section"
internal fun EngagementPanel.isDescriptionSection(): Boolean = engagementPanelSectionListRenderer?.panelIdentifier == "video-description-ep-identifier"
internal fun EngagementPanel.getTitle(): String? = getDescriptionHeader()?.title?.getText()
internal fun EngagementPanel.getChannelName(): String? = getDescriptionHeader()?.channel?.getText()
internal fun EngagementPanel.getViews(): String? = getDescriptionHeader()?.views?.getText()
internal fun EngagementPanel.getPublishDate(): String? = getDescriptionHeader()?.publishDate?.getText()
internal fun EngagementPanel.getBrowseId(): String? = getDescriptionHeader()?.channelNavigationEndpoint?.getBrowseId()
internal fun EngagementPanel.getLikeCount(): String? = getDescriptionHeader()?.factoid?.firstOrNull()?.getValue()
internal fun EngagementPanel.getDescriptionText(): String? = getDescriptionBody()?.descriptionBodyText?.getText()
private fun EngagementPanel.getDescriptionHeader(): VideoDescriptionHeaderRenderer? =
    getDescriptionItems()?.firstNotNullOfOrNull { it?.videoDescriptionHeaderRenderer }
private fun EngagementPanel.getDescriptionBody(): ExpandableVideoDescriptionBodyRenderer? =
    getDescriptionItems()?.firstNotNullOfOrNull { it?.expandableVideoDescriptionBodyRenderer }
private fun EngagementPanel.getSections() = engagementPanelSectionListRenderer?.content?.sectionListRenderer?.contents
private fun EngagementPanel.getSubMenuItems() =
    engagementPanelSectionListRenderer?.header?.engagementPanelTitleHeaderRenderer?.menu?.sortFilterSubMenuRenderer?.subMenuItems
private fun EngagementPanel.getDescriptionItems() = engagementPanelSectionListRenderer?.content?.structuredDescriptionContentRenderer?.items

///////

// Presents on Album Music videos (array of: likes count, view count, published date)
internal fun Factoid.getValue(): String? = factoidRenderer?.value?.getText()
internal fun Factoid.getLabel(): String? = factoidRenderer?.label?.getText()
internal fun Factoid.getAccessibilityText(): String? = factoidRenderer?.accessibilityText


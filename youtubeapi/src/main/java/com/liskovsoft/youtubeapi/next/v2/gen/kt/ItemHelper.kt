package com.liskovsoft.youtubeapi.next.v2.gen.kt

import com.liskovsoft.youtubeapi.common.models.kt.getBrowseId
import com.liskovsoft.youtubeapi.common.models.kt.getOverlaySubscribeButton
import com.liskovsoft.youtubeapi.common.models.kt.getSubscribeParams
import com.liskovsoft.youtubeapi.common.models.kt.getText

//////

fun VideoOwnerItem.isSubscribed() = subscriptionButton?.subscribed ?: subscribed ?: subscribeButton?.subscribeButtonRenderer?.subscribed ?:
    navigationEndpoint?.getOverlaySubscribeButton()?.isToggled
fun VideoOwnerItem.getChannelId() = navigationEndpoint?.getBrowseId() ?: subscribeButton?.subscribeButtonRenderer?.channelId
fun VideoOwnerItem.getThumbnails() = thumbnail
fun VideoOwnerItem.getParams() = navigationEndpoint?.getOverlaySubscribeButton()?.getSubscribeParams()

/////

private fun WatchNextResult.getWatchNextResults() = contents?.singleColumnWatchNextResults
fun WatchNextResult.getSuggestedSections() = getWatchNextResults()?.pivot?.let { it.pivot ?: it.sectionListRenderer }?.contents?.map { it?.shelfRenderer }
fun WatchNextResult.getVideoMetadata() = getWatchNextResults()?.results?.results?.contents?.getOrNull(0)?.
    itemSectionRenderer?.contents?.map { it?.videoMetadataRenderer ?: it?.musicWatchMetadataRenderer }?.firstOrNull()

fun WatchNextResult.getNextVideoItem() = getWatchNextResults()?.autoplay?.autoplay?.sets?.getOrNull(0)?.
    nextVideoRenderer?.let { it.maybeHistoryEndpointRenderer ?: it.autoplayEndpointRenderer ?: it.autoplayVideoWrapperRenderer?.primaryEndpointRenderer?.autoplayEndpointRenderer }

fun WatchNextResult.getVideoDetails() = getReplayItemWrapper()?.pivotVideoRenderer
fun WatchNextResult.getReplayItemWrapper() = getWatchNextResults()?.autoplay?.autoplay?.replayVideoRenderer
fun WatchNextResult.getButtonStateItem() = transportControls?.transportControlsRenderer
fun WatchNextResult.getLiveChatKey() = getWatchNextResults()?.conversationBar?.liveChatRenderer?.continuations?.getOrNull(0)?.reloadContinuationData?.continuation
fun WatchNextResult.getPlaylistInfo() = getWatchNextResults()?.playlist?.playlist

///////

const val LIKE_STATUS_LIKE = "LIKE"
const val LIKE_STATUS_DISLIKE = "DISLIKE"
const val LIKE_STATUS_INDIFFERENT = "INDIFFERENT"
fun VideoMetadataItem.getVideoOwner() = owner?.videoOwnerRenderer
fun VideoMetadataItem.getTitle() = title?.getText()
fun VideoMetadataItem.getViewCountText() = viewCount?.videoViewCountRenderer?.viewCount?.getText() ?: viewCountText?.getText()
fun VideoMetadataItem.isLive() = viewCount?.videoViewCountRenderer?.isLive
fun VideoMetadataItem.getDateText() = dateText?.getText()
fun VideoMetadataItem.getPublishedTime() = publishedTimeText?.getText() ?: publishedTime?.getText() ?: albumName?.getText()
fun VideoMetadataItem.getLikeStatus() = likeStatus ?: likeButton?.likeButtonRenderer?.likeStatus
fun VideoMetadataItem.isUpcoming() = badges?.firstNotNullOfOrNull { it?.upcomingEventBadge?.label?.getText() }?.let { true } ?: false
fun VideoMetadataItem.getPercentWatched() = thumbnailOverlays?.firstNotNullOfOrNull { it?.thumbnailOverlayResumePlaybackRenderer?.percentDurationWatched } ?: 0

////////

fun ButtonStateItem.isLikeToggled() = likeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isDislikeToggled() = dislikeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.isSubscribeToggled() = subscribeButton?.toggleButtonRenderer?.isToggled
fun ButtonStateItem.getChannelId() = getChannelOwner()?.getChannelId()
fun ButtonStateItem.getChannelOwner() = channelButton?.videoOwnerRenderer

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

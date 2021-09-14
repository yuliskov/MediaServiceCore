package com.liskovsoft.youtubeapi.next.v2.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemMetadata
import com.liskovsoft.youtubeapi.next.v2.helpers.getText
import com.liskovsoft.youtubeapi.next.v2.result.gen.WatchNextResult

// TODO: implement full conversion
data class MediaItemMetadataImpl(val watchNextResult: WatchNextResult) : MediaItemMetadata {
    private val suggestedSections by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.pivot?.pivot?.contents?.map { it?.shelfRenderer }
    }
    private val videoMetadata by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.results?.results?.contents?.getOrNull(0)?.itemSectionRenderer?.contents?.map {
            it?.videoMetadataRenderer ?: it?.musicWatchMetadataRenderer
        }?.firstOrNull()
    }
    private val nextVideo by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.sets?.map {
            it?.nextVideoRenderer?.maybeHistoryEndpointRenderer ?: it?.nextVideoRenderer?.autoplayEndpointRenderer
        }?.firstOrNull()
    }
    private val replayItemWrapper by lazy {
        watchNextResult.contents?.singleColumnWatchNextResults?.autoplay?.autoplay?.replayVideoRenderer
    }
    private val buttonStateItem by lazy {
        watchNextResult.transportControls?.transportControlsRenderer
    }
    private val videoOwner by lazy {
        videoMetadata?.owner?.videoOwnerRenderer
    }
    private val videoTitle by lazy {
        val textItem = videoMetadata?.title
        textItem?.getText()
    }
    private val videoDescription by lazy { videoMetadata?.description?.getText() }
    private val videoAuthor by lazy { videoMetadata?.title?.getText() }
    private val suggestionList by lazy {
        suggestedSections?.map { it?.let { MediaGroupImpl(it) } }
    }

    override fun getTitle(): String? {
        return videoTitle
    }

    override fun getDescription(): String? {
        return videoDescription
    }

    override fun getFullDescription(): String? {
        return null;
    }

    override fun getAuthor(): String? {
        return videoAuthor
    }

    override fun getViewCount(): String? {
        return null
    }

    override fun getLikesCount(): String? {
        return null
    }

    override fun getDislikesCount(): String? {
        return null
    }

    override fun getPublishedDate(): String? {
        return null
    }

    override fun getVideoId(): String? {
        return null
    }

    override fun getNextVideo(): MediaItem? {
        return null
    }

    override fun isSubscribed(): Boolean {
        return false
    }

    override fun isLive(): Boolean {
        return false
    }

    override fun isUpcoming(): Boolean {
        return false
    }

    override fun getChannelId(): String? {
        return null
    }

    override fun getPercentWatched(): Int {
        return 0
    }

    override fun getLikeStatus(): Int {
        return 0
    }

    override fun getSuggestions(): List<MediaGroup?>? {
        return suggestionList
    }

    override fun getDescriptionAlt(): String? {
        return null
    }
}

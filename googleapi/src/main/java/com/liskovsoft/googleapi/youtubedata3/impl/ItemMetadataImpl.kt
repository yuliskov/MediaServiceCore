package com.liskovsoft.googleapi.youtubedata3.impl

import com.liskovsoft.googleapi.youtubedata3.data.*

internal class ItemMetadataImpl(private val snippetWrapper: SnippetWrapper): ItemMetadata {
     override val title: String? by lazy { snippetWrapper.getTitle() }
     override val cardImageUrl: String? by lazy { snippetWrapper.getThumbnailUrl() }
     override val channelId: String? by lazy { snippetWrapper.getChannelId() }
     override val videoId: String? by lazy { snippetWrapper.getVideoId() }
     override val playlistId: String? by lazy { snippetWrapper.getPlaylistId() }
     override val channelTitle: String? by lazy { snippetWrapper.getChannelTitle() }
     override val publishedAt: String? by lazy { snippetWrapper.getPublishedAt() }
     override val durationIso: String? by lazy { snippetWrapper.getDurationIso() }
     override val itemCount: Int? by lazy { snippetWrapper.getItemCount() }
}
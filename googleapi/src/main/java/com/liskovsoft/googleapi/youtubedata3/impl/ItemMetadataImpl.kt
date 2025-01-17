package com.liskovsoft.googleapi.youtubedata3.impl

import com.liskovsoft.googleapi.common.helpers.YouTubeHelper
import com.liskovsoft.googleapi.youtubedata3.data.*
import com.liskovsoft.sharedutils.helpers.DateHelper

internal class ItemMetadataImpl(private val snippetWrapper: SnippetWrapper): ItemMetadata {
     override val title: String? by lazy { snippetWrapper.getTitle() }
     override val secondTitle: String? by lazy { YouTubeHelper.createInfo(
          snippetWrapper.getChannelTitle(), DateHelper.toShortDate(snippetWrapper.getPublishedAt(), true, true, false))
     }
     override val cardImageUrl: String? by lazy { snippetWrapper.getThumbnailUrl() }
     override val channelId: String? by lazy { snippetWrapper.getChannelId() }
     override val videoId: String? by lazy { snippetWrapper.getVideoId() }
}
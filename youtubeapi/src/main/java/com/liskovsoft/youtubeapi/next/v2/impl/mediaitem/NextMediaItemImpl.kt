package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.helpers.findHighResThumbnailUrl
import com.liskovsoft.youtubeapi.next.v2.helpers.getText
import com.liskovsoft.youtubeapi.next.v2.result.gen.NextVideoItem

data class NextMediaItemImpl(var nextVideoItem: NextVideoItem): BaseMediaItemImpl() {
    override val videoIdItem by lazy { nextVideoItem.endpoint?.watchEndpoint?.videoId }
    override val channelIdItem: String? = null
    override val titleItem by lazy { nextVideoItem.item?.previewButtonRenderer?.title?.getText() }
    override val cardThumbImageUrl by lazy {
        nextVideoItem.item?.previewButtonRenderer?.thumbnail?.findHighResThumbnailUrl()
    }
    override val playlistIdItem by lazy { nextVideoItem.endpoint?.watchEndpoint?.playlistId }
    override val playlistIndexItem by lazy { nextVideoItem.endpoint?.watchEndpoint?.index ?: 0 }
    override val mediaUrl by lazy { ServiceHelper.videoIdToFullUrl(videoIdItem) ?: null }
    override val descriptionItem: String? = null
}

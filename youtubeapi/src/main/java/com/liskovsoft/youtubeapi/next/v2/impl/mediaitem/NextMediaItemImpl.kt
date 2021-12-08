package com.liskovsoft.youtubeapi.next.v2.impl.mediaitem

import com.liskovsoft.mediaserviceinterfaces.data.MediaItem
import com.liskovsoft.youtubeapi.common.helpers.ServiceHelper
import com.liskovsoft.youtubeapi.next.v2.helpers.findHighResThumbnailUrl
import com.liskovsoft.youtubeapi.next.v2.helpers.getText
import com.liskovsoft.youtubeapi.next.v2.result.gen.NextVideoItem

data class NextMediaItemImpl(var nextVideoItem: NextVideoItem): BaseMediaItemImpl() {
    private val _videoId by lazy { nextVideoItem.endpoint?.watchEndpoint?.videoId }
    private val _channelId: String? = null
    private val _title by lazy { nextVideoItem.item?.previewButtonRenderer?.title?.getText() }
    private val hiResThumbImageUrl by lazy {
        nextVideoItem.item?.previewButtonRenderer?.thumbnail?.findHighResThumbnailUrl()
    }
    private val _playlistId by lazy { nextVideoItem.endpoint?.watchEndpoint?.playlistId }
    private val _playlistIndex by lazy { nextVideoItem.endpoint?.watchEndpoint?.index ?: 0 }
    private val mediaUrl by lazy { ServiceHelper.videoIdToFullUrl(_videoId) }
    private val _description: String? = null

    override fun getType(): Int {
        return MediaItem.TYPE_VIDEO;
    }

    override fun getPlaylistId(): String? {
        return _playlistId
    }

    override fun getTitle(): String? {
        return newTitle ?: _title
    }

    override fun getVideoUrl(): String? {
        return mediaUrl
    }

    override fun getVideoId(): String? {
        return _videoId
    }

    override fun getCardImageUrl(): String? {
        return hiResThumbImageUrl
    }

    override fun getBackgroundImageUrl(): String? {
        return hiResThumbImageUrl
    }

    override fun getPlaylistIndex(): Int {
        return _playlistIndex
    }

    override fun getDescription(): String? {
        return newDescription ?: _description
    }

    override fun getChannelId(): String? {
        return newChannelId ?: _channelId
    }
}

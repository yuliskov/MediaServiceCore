package com.liskovsoft.youtubeapi.videogroups.models

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.videogroups.VideoGroupServiceImpl

private const val ITEM_DELIM = "&vgi;"
private const val LIST_DELIM = "&vga;"

internal data class VideoGroupImpl(
    private val id: Int = Helpers.getRandomNumber(VideoGroupServiceImpl.PLAYLIST_GROUP_ID + 100, Integer.MAX_VALUE),
    private val title: String,
    private val iconUrl: String? = null,
    private val videos: MutableList<VideoImpl>
) {
    fun getId(): Int {
        return id
    }

    fun getTitle(): String {
        return title
    }

    fun getIconUrl(): String? {
        return iconUrl
    }

    fun getVideos(): List<VideoImpl> {
        return videos
    }

    fun findVideo(channelId: String): VideoImpl? {
        return Helpers.findFirst(videos) { video -> video.getVideoId() == channelId }
    }

    fun add(channel: VideoImpl) {
        videos.remove(channel)
        videos.add(0, channel)
        VideoGroupServiceImpl.persistData()
    }

    fun addAll(newChannels: List<VideoImpl>) {
        videos.removeAll(newChannels)
        videos.addAll(newChannels)
        VideoGroupServiceImpl.persistData()
    }

    fun remove(channelId: String) {
        val removed = Helpers.removeIf(videos) { channel -> channel.getVideoId() == channelId }
        if (!removed.isNullOrEmpty()) {
            VideoGroupServiceImpl.persistData()
        }
    }

    fun contains(channelId: String): Boolean {
        return Helpers.containsIf(videos) { channel -> channel.getVideoId() == channelId }
    }

    fun isEmpty(): Boolean {
        return videos.isEmpty()
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, id, title, iconUrl, Helpers.mergeList(LIST_DELIM, videos))
    }

    override fun equals(other: Any?): Boolean {
        if (other is VideoGroupImpl) {
            return other.id == id
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): VideoGroupImpl? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val id = Helpers.parseInt(split, 0)
            val title = Helpers.parseStr(split, 1) ?: return null
            val groupIconUrl = Helpers.parseStr(split, 2)
            val videos: MutableList<VideoImpl> = Helpers.parseList(split, 3, LIST_DELIM, VideoImpl::fromString)

            return VideoGroupImpl(id, title, groupIconUrl, videos)
        }
    }
}

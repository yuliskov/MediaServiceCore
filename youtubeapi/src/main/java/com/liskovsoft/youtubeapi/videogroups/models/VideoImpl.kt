package com.liskovsoft.youtubeapi.videogroups.models

import com.liskovsoft.sharedutils.helpers.Helpers

private const val ITEM_DELIM = "&vi;"

internal data class VideoImpl(
    private val videoId: String,
    private val title: String? = null,
    private val iconUrl: String? = null
) {
    fun getTitle(): String? {
        return title
    }

    fun getIconUrl(): String? {
        return iconUrl
    }

    fun getVideoId(): String {
        return videoId
    }

    override fun toString(): String {
        return Helpers.merge(ITEM_DELIM, title, iconUrl, videoId)
    }

    override fun equals(other: Any?): Boolean {
        if (other is VideoImpl) {
            return other.videoId == videoId
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        return super.hashCode()
    }

    companion object {
        @JvmStatic
        fun fromString(spec: String?): VideoImpl? {
            if (spec == null)
                return null

            val split = Helpers.split(ITEM_DELIM, spec)

            val title = Helpers.parseStr(split, 0)
            val iconUrl = Helpers.parseStr(split, 1)
            val videoId = Helpers.parseStr(split, 2) ?: return null

            return VideoImpl(videoId, title, iconUrl)
        }
    }
}

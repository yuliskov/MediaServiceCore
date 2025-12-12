package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.youtubeapi.browse.v2.BrowseApiHelper
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class MediaGroupOptions private constructor(val removeShorts: Boolean = false,
                                      val removeLive: Boolean = false,
                                      val removeUpcoming: Boolean = false,
                                      val removeWatched: Boolean = false,
                                      val groupType: Int,
                                      val enableLegacyUI: Boolean = false) {
    val clientTV by lazy { if (enableLegacyUI) AppClient.TV_LEGACY else AppClient.TV }

    companion object {
        fun create(groupType: Int, channelId: String? = null): MediaGroupOptions {
            val data = MediaServiceData.instance()
            val removeShorts = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_SUBSCRIPTIONS)) ||
                    (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HOME)) ||
                    (MediaGroup.TYPE_HISTORY == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HISTORY)) ||
                    (MediaGroup.TYPE_CHANNEL == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL)) ||
                    (MediaGroup.TYPE_CHANNEL_UPLOADS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL)) ||
                    (MediaGroup.TYPE_TRENDING == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_TRENDING))
            val removeLive = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_STREAMS_SUBSCRIPTIONS))
            val removeUpcoming = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_SUBSCRIPTIONS)) ||
                    (MediaGroup.TYPE_CHANNEL == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL)) ||
                    (MediaGroup.TYPE_CHANNEL_UPLOADS == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL)) ||
                    (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_HOME))
            val removeWatched = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_SUBSCRIPTIONS)) ||
                    (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_HOME)) ||
                    (channelId == BrowseApiHelper.WATCH_LATER_CHANNEL_ID && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_WATCH_LATER))
            val isGridSection = MediaGroup.TYPE_SUBSCRIPTIONS == groupType || MediaGroup.TYPE_HISTORY == groupType || MediaGroup.TYPE_CHANNEL_UPLOADS == groupType
            val isBrowseSection = groupType != MediaGroup.TYPE_SUGGESTIONS // legacy suggestions ui doesn't have chapters
            val enableLegacyUI = (data.isLegacyUIEnabled && isBrowseSection) || (!removeShorts && isGridSection) // the modern grid ui contains shorts on a separate row

            return MediaGroupOptions(
                removeShorts,
                removeLive,
                removeUpcoming,
                removeWatched,
                groupType,
                enableLegacyUI
            )
        }
    }
}
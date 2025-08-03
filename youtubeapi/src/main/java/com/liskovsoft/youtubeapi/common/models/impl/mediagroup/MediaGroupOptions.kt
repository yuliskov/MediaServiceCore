package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.youtubeapi.app.AppConstants
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal data class MediaGroupOptions(val removeShorts: Boolean = false,
                                      val removeLive: Boolean = false,
                                      val removeUpcoming: Boolean = false,
                                      val removeWatched: Boolean = false,
                                      val removeDuplicates: Boolean = false,
                                      val removeDuplicatesExcludeRecommended: Boolean = false,
                                      val removeMixes: Boolean = false,
                                      val groupType: Int,
                                      val enableLegacyUI: Boolean = false) {
    val clientTV by lazy { if (enableLegacyUI) AppClient.TV_LEGACY else AppClient.TV }

    companion object {
        fun create(groupType: Int = MediaGroup.TYPE_SUBSCRIPTIONS, channelId: String? = null): MediaGroupOptions {
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
                    (channelId == AppConstants.WATCH_LATER_CHANNEL_ID && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_WATCH_LATER))
            val removeDuplicates = data.isContentHidden(MediaServiceData.CONTENT_DUPLICATES)
            val removeDuplicatesExcludeRecommended = data.isContentHidden(MediaServiceData.CONTENT_DUPLICATES_EXCLUDE_RECOMMENDED)
            val removeMixes = data.isContentHidden(MediaServiceData.CONTENT_MIXES)
            val enableLegacyUI = data.isLegacyUIEnabled || !removeShorts // the modern ui doesn't contains shorts

            return MediaGroupOptions(
                removeShorts,
                removeLive,
                removeUpcoming,
                removeWatched,
                removeDuplicates,
                removeDuplicatesExcludeRecommended,
                removeMixes,
                groupType,
                enableLegacyUI
            )
        }
    }
}
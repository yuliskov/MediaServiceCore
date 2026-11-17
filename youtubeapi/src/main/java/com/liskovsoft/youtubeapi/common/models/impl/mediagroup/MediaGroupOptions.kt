package com.liskovsoft.youtubeapi.common.models.impl.mediagroup

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.youtubeapi.browse.v2.BrowseApiHelper
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class MediaGroupOptions private constructor(val removeShorts: Boolean = false,
                                      val removeLive: Boolean = false,
                                      val removeUpcoming: Boolean = false,
                                      val removeWatched: Boolean = false,
                                      val removeMixes: Boolean = false,
                                      val groupType: Int,
                                      val enableLegacyUI: Boolean = false) {
    val clientTV by lazy { if (enableLegacyUI) AppClient.TV_LEGACY else AppClient.TV }

    companion object {
        private val EVERYWHERE_ONLY_TYPES = setOf(
            MediaGroup.TYPE_RECOMMENDED, MediaGroup.TYPE_MUSIC, MediaGroup.TYPE_GAMING, MediaGroup.TYPE_KIDS_HOME,
            MediaGroup.TYPE_SPORTS, MediaGroup.TYPE_MOVIES, MediaGroup.TYPE_LIVE
        )

        fun create(groupType: Int, channelId: String? = null): MediaGroupOptions {
            val data = MediaServiceData.instance()
            val removeShorts = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_SUBSCRIPTIONS))
                    || (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HOME))
                    || (MediaGroup.TYPE_HISTORY == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_HISTORY))
                    || (MediaGroup.TYPE_CHANNEL == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL))
                    || (MediaGroup.TYPE_CHANNEL_UPLOADS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_CHANNEL))
                    || (MediaGroup.TYPE_TRENDING == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_TRENDING))
                    || (MediaGroup.TYPE_SEARCH == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_SEARCH))
                    || (MediaGroup.TYPE_NEWS == groupType && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_NEWS))
                    // "Hide shorts everywhere" also covers the browse sections that have no option of their own
                    || (groupType in EVERYWHERE_ONLY_TYPES && data.isContentHidden(MediaServiceData.CONTENT_SHORTS_ALL))
            val removeLive = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_STREAMS_SUBSCRIPTIONS))
            val removeUpcoming = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_SUBSCRIPTIONS)) ||
                    (MediaGroup.TYPE_CHANNEL == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL)) ||
                    (MediaGroup.TYPE_CHANNEL_UPLOADS == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_CHANNEL)) ||
                    (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_UPCOMING_HOME))
            val removeWatched = (MediaGroup.TYPE_SUBSCRIPTIONS == groupType && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_SUBSCRIPTIONS)) ||
                    (MediaGroup.TYPE_HOME == groupType && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_HOME)) ||
                    (channelId == BrowseApiHelper.WATCH_LATER_CHANNEL_ID && data.isContentHidden(MediaServiceData.CONTENT_WATCHED_WATCH_LATER))
            val removeMixes = data.isContentHidden(MediaServiceData.CONTENT_MIXES)
            // NOTE: The modern grid ui contains shorts on a separate row
            // NOTE: The legacy ui subscriptions is out of order and cannot be fixed
            val isBrowseSection = groupType != MediaGroup.TYPE_SUGGESTIONS // legacy suggestions ui doesn't have chapters
            val enableLegacyUI = data.isLegacyUIEnabled && isBrowseSection

            return MediaGroupOptions(
                removeShorts,
                removeLive,
                removeUpcoming,
                removeWatched,
                removeMixes,
                groupType,
                enableLegacyUI
            )
        }
    }
}
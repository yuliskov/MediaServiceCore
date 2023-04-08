package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.browse.v2.gen.getChips
import com.liskovsoft.youtubeapi.browse.v2.gen.getSections
import com.liskovsoft.youtubeapi.browse.v2.gen.getTitle
import com.liskovsoft.youtubeapi.browse.v2.impl.*
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

object BrowseService2 {
    private val mBrowseApi = RetrofitHelper.withGson(BrowseApi::class.java)
    private val mAppService = AppService.instance()

    @JvmStatic
    fun getHome(): List<MediaGroup?>? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getHomeQueryWeb())

        return RetrofitHelper.get(browseResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            // First chip is always empty and corresponds to current result.
            // Also title used as id in continuation. No good.
            result.add(MediaGroupImpl(it, createOptions(MediaGroup.TYPE_HOME)).apply { title = it.getChips()?.getOrNull(0)?.getTitle() })
            it.getSections()?.forEach { if (it != null) result.add(MediaGroupImpl3(it, createOptions(MediaGroup.TYPE_HOME))) }
            it.getChips()?.forEach { if (it != null) result.add(MediaGroupImpl4(it, createOptions(MediaGroup.TYPE_HOME))) }

            result
        }
    }

    @JvmStatic
    fun getSubscriptions(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        return RetrofitHelper.get(browseResult)?.let { MediaGroupImpl(it, createOptions(MediaGroup.TYPE_SUBSCRIPTIONS)) }
    }

    @JvmStatic
    fun continueGroup(group: MediaGroup?): MediaGroup? {
        if (group?.nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResult(BrowseApiHelper.getContinuationQueryWeb(group.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let { MediaGroupImpl2(it, createOptions(group.type)).apply { title = group.title } }
    }

    @JvmStatic
    fun continueChip(group: MediaGroup?): List<MediaGroup?>? {
        if (group?.nextPageKey == null) {
            return null
        }

        val continuationResult =
            mBrowseApi.getContinuationResult(BrowseApiHelper.getContinuationQueryWeb(group.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let {
            val result = mutableListOf<MediaGroup?>()

            result.add(MediaGroupImpl2(it, createOptions(group.type)).apply { title = group.title })
            it.getSections()?.forEach { if (it != null) result.add(MediaGroupImpl3(it, createOptions(group.type))) }

            result
        }
    }

    private fun createOptions(groupType: Int?): MediaGroupOptions {
        val prefs = GlobalPreferences.sInstance

        return MediaGroupOptions(
            prefs?.isHideShortsFromSubscriptionsEnabled ?: true,
            prefs?.isHideStreamsFromSubscriptionsEnabled ?: false,
            prefs?.isHideUpcomingEnabled ?: false,
            groupType ?: MediaGroup.TYPE_SUBSCRIPTIONS
        )
    }
}
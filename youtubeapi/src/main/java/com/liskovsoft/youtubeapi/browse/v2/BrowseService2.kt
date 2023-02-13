package com.liskovsoft.youtubeapi.browse.v2

import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup
import com.liskovsoft.sharedutils.prefs.GlobalPreferences
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.browse.v1.BrowseApiHelper
import com.liskovsoft.youtubeapi.browse.v2.impl.MediaGroupImpl
import com.liskovsoft.youtubeapi.browse.v2.impl.MediaGroupImpl2
import com.liskovsoft.youtubeapi.browse.v2.impl.MediaGroupOptions
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

object BrowseService2 {
    private val mBrowseApi = RetrofitHelper.withGson(BrowseApi::class.java)
    private val mAppService = AppService.instance()

    @JvmStatic
    fun getSubscriptions(): MediaGroup? {
        val browseResult = mBrowseApi.getBrowseResult(BrowseApiHelper.getSubscriptionsQueryWeb())

        return RetrofitHelper.get(browseResult)?.let { MediaGroupImpl(it, createOptions()) }
    }

    @JvmStatic
    fun continueGroup(group: MediaGroup?): MediaGroup? {
        val continuationResult =
            mBrowseApi.getContinuationResult(BrowseApiHelper.getContinuationQueryWeb(group?.nextPageKey))

        return RetrofitHelper.get(continuationResult)?.let { MediaGroupImpl2(it, createOptions()) }
    }

    private fun createOptions(): MediaGroupOptions {
        val prefs = GlobalPreferences.sInstance

        return MediaGroupOptions(
            prefs?.isHideShortsFromSubscriptionsEnabled ?: true,
            prefs?.isHideStreamsFromSubscriptionsEnabled ?: false,
            prefs?.isHideUpcomingEnabled ?: false
        )
    }
}
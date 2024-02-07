package com.liskovsoft.youtubeapi.dearrow

import com.liskovsoft.mediaserviceinterfaces.data.DeArrowData
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

internal object DeArrowService {
    private val mDeArrowApi = RetrofitHelper.withGson(DeArrowApi::class.java)

    @JvmStatic
    fun getData(videoId: String?): DeArrowData? {
        if (videoId == null) {
            return null
        }

        val branding = mDeArrowApi.getBranding(videoId)

        return RetrofitHelper.get(branding)?.let {
            object : DeArrowData {
                override fun getVideoId(): String {
                    return videoId
                }

                override fun getTitle(): String? {
                    return it.titles?.firstOrNull { !(it?.original ?: false) }?.title?.replaceAll(">", "")
                }

                override fun getThumbnailUrl(): String? {
                    return it.thumbnails?.firstOrNull { !(it?.original ?: false) }?.let { "${DeArrowApiHelper.THUMBNAIL_URL}?videoID=$videoId&time=${it.timestamp}" }
                }
            }
        }
    }
}
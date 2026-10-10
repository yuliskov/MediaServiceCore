package com.liskovsoft.youtubeapi.dearrow

import com.liskovsoft.mediaserviceinterfaces.data.DeArrowData
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper

internal object DeArrowService {
    private val mDeArrowApi = RetrofitHelper.create(DeArrowApi::class.java)

    @JvmStatic
    fun getData(videoId: String?): DeArrowData? {
        if (videoId == null) {
            return null
        }

        val branding = mDeArrowApi.getBranding(videoId)

        return RetrofitHelper.get(branding)?.let {
            val title = it.titles?.firstOrNull { entry ->
                entry?.original != true && (entry?.locked == true || (entry?.votes ?: -1) >= 0)
            }
            val thumbnail = it.thumbnails?.firstOrNull { entry ->
                entry?.original != true && (entry?.locked == true || (entry?.votes ?: -1) >= 0)
            }
            object : DeArrowData {
                override fun getVideoId(): String {
                    return videoId
                }

                override fun getTitle(): String? {
                    // '>' character is used in some titles to tell the DeArrow formatter which words to leave alone
                    // Example: https://sponsor.ajay.app/api/branding?videoID=dtp6b76pMak
                    // https://github.com/yuliskov/MediaServiceCore/pull/9
                    return title?.title?.replace(">", "")
                }

                override fun getThumbnailUrl(): String? {
                    return thumbnail?.timestamp?.let { timestamp ->
                        "${DeArrowApiHelper.THUMBNAIL_URL}?videoID=$videoId&time=$timestamp"
                    }
                }
            }
        }
    }
}

package com.liskovsoft.youtubeapi.innertube

import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.youtubeapi.innertube.core.HTTPClient
import com.liskovsoft.youtubeapi.innertube.core.RequestInit
import com.liskovsoft.youtubeapi.innertube.core.RequestInitBody
import com.liskovsoft.youtubeapi.innertube.core.Session
import com.liskovsoft.youtubeapi.innertube.impl.MediaItemFormatInfoImpl
import com.liskovsoft.youtubeapi.innertube.initialresponse.InitialResponseService

internal object InnertubeService {
    @JvmStatic
    fun createFormatInfo(videoId: String): MediaItemFormatInfo? = createFormatInfoV1(videoId)

    /**
     * LuanRT variation
     */
    internal fun createFormatInfoV1(videoId: String): MediaItemFormatInfo? {
        val session = Session.create() ?: return null
        val httpClient = HTTPClient(session)
        val playerResult = httpClient.fetch("/player", RequestInit(body = RequestInitBody(videoId, session = session)))
            ?: return null

        val formatInfo = MediaItemFormatInfoImpl(playerResult)
        session.player.decipher(formatInfo)

        if (formatInfo.isUnplayable) {
            Session.invalidate()
        }

        return formatInfo
    }

    /**
     * yt-dlp variation
     */
    internal fun createFormatInfoV2(videoId: String): MediaItemFormatInfo? {
        val session = Session.create() ?: return null

        val playerResult = InitialResponseService.getPlayerResult(videoId) ?: return null

        val formatInfo = MediaItemFormatInfoImpl(playerResult)
        session.player.decipher(formatInfo)

        return formatInfo
    }
}
package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard
import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.innertube.models.PlayerResult
import com.liskovsoft.youtubeapi.innertube.utils.getVideoPlaybackUstreamerConfig
import io.reactivex.Observable
import java.io.InputStream

internal data class MediaItemFormatInfoImpl(private val playerResult: PlayerResult): MediaItemFormatInfo {
    private val videoPlaybackUstreamerConfigItem by lazy { playerResult.getVideoPlaybackUstreamerConfig() }
    private val clientInfoItem by lazy { AppClient.WEB }

    override fun getAdaptiveFormats(): List<MediaFormat?>? {
        TODO("Not yet implemented")
    }

    override fun getUrlFormats(): List<MediaFormat?>? {
        TODO("Not yet implemented")
    }

    override fun getSubtitles(): List<MediaSubtitle?>? {
        TODO("Not yet implemented")
    }

    override fun getHlsManifestUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getDashManifestUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getLengthSeconds(): String? {
        TODO("Not yet implemented")
    }

    override fun getTitle(): String? {
        TODO("Not yet implemented")
    }

    override fun getAuthor(): String? {
        TODO("Not yet implemented")
    }

    override fun getViewCount(): String? {
        TODO("Not yet implemented")
    }

    override fun getDescription(): String? {
        TODO("Not yet implemented")
    }

    override fun getVideoId(): String? {
        TODO("Not yet implemented")
    }

    override fun getChannelId(): String? {
        TODO("Not yet implemented")
    }

    override fun isLive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isLiveContent(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsMedia(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsSabrFormats(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsDashFormats(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsHlsUrl(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsDashUrl(): Boolean {
        TODO("Not yet implemented")
    }

    override fun containsUrlFormats(): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasExtendedHlsFormats(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getVolumeLevel(): Float {
        TODO("Not yet implemented")
    }

    override fun createMpdStream(): InputStream? {
        TODO("Not yet implemented")
    }

    override fun createMpdStreamObservable(): Observable<InputStream?>? {
        TODO("Not yet implemented")
    }

    override fun createUrlList(): List<String?>? {
        TODO("Not yet implemented")
    }

    override fun createStoryboard(): MediaItemStoryboard? {
        TODO("Not yet implemented")
    }

    override fun isUnplayable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun isUnknownError(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getPlayabilityStatus(): String? {
        TODO("Not yet implemented")
    }

    override fun isStreamSeekable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getStartTimestamp(): String? {
        TODO("Not yet implemented")
    }

    override fun getUploadDate(): String? {
        TODO("Not yet implemented")
    }

    override fun getStartTimeMs(): Long {
        TODO("Not yet implemented")
    }

    override fun getStartSegmentNum(): Int {
        TODO("Not yet implemented")
    }

    override fun getSegmentDurationUs(): Int {
        TODO("Not yet implemented")
    }

    override fun getPaidContentText(): String? {
        TODO("Not yet implemented")
    }

    override fun getVideoPlaybackUstreamerConfig(): String? {
        return videoPlaybackUstreamerConfigItem
    }

    override fun getServerAbrStreamingUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getPoToken(): String? {
        TODO("Not yet implemented")
    }

    override fun getClientInfo(): MediaItemFormatInfo.ClientInfo {
        return clientInfoItem
    }
}
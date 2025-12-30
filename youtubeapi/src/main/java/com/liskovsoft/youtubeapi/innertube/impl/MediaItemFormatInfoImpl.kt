package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.innertube.models.PlayerResult
import com.liskovsoft.youtubeapi.innertube.utils.getAdaptiveFormats
import com.liskovsoft.youtubeapi.innertube.utils.getLegacyFormats
import com.liskovsoft.youtubeapi.innertube.utils.getMergeCaptionTracks
import com.liskovsoft.youtubeapi.innertube.utils.getServerAbrStreamingUrl
import com.liskovsoft.youtubeapi.innertube.utils.getVideoPlaybackUstreamerConfig
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder
import io.reactivex.Observable
import java.io.InputStream
import java.util.regex.Pattern

internal data class MediaItemFormatInfoImpl(private val playerResult: PlayerResult): MediaItemFormatInfo {
    private val TAG = MediaItemFormatInfoImpl::class.simpleName
    private val durationPattern1 = Pattern.compile("dur=([^&]*)")
    private val durationPattern2 = Pattern.compile("/dur/([^/]*)")
    val sabrUrlHolder by lazy { VideoUrlHolder(playerResult.getServerAbrStreamingUrl()) }
    private val videoDetails by lazy { playerResult.videoDetails }
    private val _videoPlaybackUstreamerConfig by lazy { playerResult.getVideoPlaybackUstreamerConfig() }
    private val _clientInfo by lazy { AppClient.WEB } // TODO: replace with innertube client
    private val _adaptiveFormats by lazy {
        playerResult.getAdaptiveFormats()?.map { MediaFormatImpl(it) }
    }
    private val _legacyFormats by lazy {
        playerResult.getLegacyFormats()?.map { MediaFormatImpl(it) }
    }
    private val _subtitles by lazy {
        playerResult.getMergeCaptionTracks()?.map { MediaSubtitleImpl(it) }
    }
    private val _hlsManifestUrl by lazy { playerResult.streamingData?.hlsManifestUrl }
    private val _dashManifestUrl by lazy { playerResult.streamingData?.dashManifestUrl }
    private val _lengthSeconds by lazy {
        videoDetails?.lengthSeconds ?: extractDurationFromTrack() // try to get duration from video url
    }
    private val _title by lazy { videoDetails?.title }
    private val _author by lazy { videoDetails?.author }
    private val _viewCount by lazy { videoDetails?.viewCount }
    private val _description by lazy { videoDetails?.shortDescription }
    private val _videoId by lazy { videoDetails?.videoId }
    private val _channelId by lazy { videoDetails?.channelId }
    private val _isLive by lazy { videoDetails?.isLive ?: false }
    private val _isLiveContent by lazy { videoDetails?.isLiveContent ?: false }
    private val _containsAdaptiveVideoFormats by lazy { containsAdaptiveVideoInfo() }
    private val _containsLegacyVideoFormats by lazy { containsLegacyVideoInfo() }
    private val _hasExtendedHlsFormats by lazy {
        // Need upload date check?
        // Extended formats may not work up to 3 days after publication.
        !_isLive && _hlsManifestUrl != null && isAdaptiveFullHD()
    }

    override fun getAdaptiveFormats() = _adaptiveFormats

    override fun getUrlFormats() = _legacyFormats

    override fun getSubtitles() = _subtitles

    override fun getHlsManifestUrl() = _hlsManifestUrl

    override fun getDashManifestUrl() = _dashManifestUrl

    /**
     * MPD file is not valid without duration
     */
    override fun getLengthSeconds() = _lengthSeconds

    override fun getTitle() = _title

    override fun getAuthor() = _author

    override fun getViewCount() = _viewCount

    override fun getDescription() = _description

    override fun getVideoId() = _videoId

    override fun getChannelId() = _channelId

    override fun isLive() = _isLive

    override fun isLiveContent() = _isLiveContent

    override fun containsMedia(): Boolean {
        return containsDashUrl() || containsHlsUrl() || containsAdaptiveVideoFormats() || containsUrlFormats()
    }

    override fun containsSabrFormats(): Boolean {
        return _containsAdaptiveVideoFormats && _adaptiveFormats?.firstOrNull()?.getFormatType() == MediaFormat.FORMAT_TYPE_SABR
    }

    override fun containsDashFormats(): Boolean {
        return _containsAdaptiveVideoFormats && _adaptiveFormats?.firstOrNull()?.getFormatType() == MediaFormat.FORMAT_TYPE_DASH
    }

    private fun containsAdaptiveVideoFormats(): Boolean {
        return _containsAdaptiveVideoFormats
    }

    override fun containsHlsUrl(): Boolean {
        return _hlsManifestUrl != null
    }

    override fun containsDashUrl(): Boolean {
        return _dashManifestUrl != null
    }

    override fun containsUrlFormats(): Boolean {
        return _legacyFormats != null
    }

    override fun hasExtendedHlsFormats() = _hasExtendedHlsFormats

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

    override fun getVideoPlaybackUstreamerConfig() = _videoPlaybackUstreamerConfig

    override fun getServerAbrStreamingUrl() = sabrUrlHolder.getUrl()

    override fun getPoToken(): String? {
        TODO("Not yet implemented")
    }

    override fun getClientInfo() = _clientInfo

    /**
     * Extracts time from video url (if present).
     * Url examples:
     * <br></br>
     * "http://example.com?dur=544.99&key=val&key2=val2"
     * <br></br>
     * "http://example.com/dur/544.99/key/val/key2/val2"
     *
     * @return duration as string
     */
    private fun extractDurationFromTrack(): String? {
        if (_adaptiveFormats == null && _legacyFormats == null) {
            return null
        }

        var url: String? = null
        // mMP4Videos
        val videos: List<MediaFormat> = _adaptiveFormats ?: _legacyFormats ?: return null
        for (item in videos) {
            url = item.getUrl()
            break // get first item
        }
        val result = Helpers.runMultiMatcher(url, durationPattern1, durationPattern2)

        if (result == null) {
            //throw new IllegalStateException("Videos in the list doesn't have a duration. Content: " + mMP4Videos);
            Log.e(TAG, "Videos in the list doesn't have a duration. Content: $videos")
        }

        return result
    }

    private fun containsAdaptiveVideoInfo(): Boolean {
        if (_adaptiveFormats == null) {
            return false
        }

        var result = false

        for (format in _adaptiveFormats) {
            val mimeType = format.getMimeType()
            if (mimeType != null && mimeType.startsWith("video/")) {
                result = true
                break
            }
        }

        return result
    }

    private fun containsLegacyVideoInfo(): Boolean {
        if (_legacyFormats == null) {
            return false
        }

        var result = false

        for (format in _legacyFormats) {
            val mimeType: String? = format.getMimeType()
            if (mimeType != null && mimeType.startsWith("video/")) {
                result = true
                break
            }
        }

        return result
    }

    private fun isExtendedHlsFormatsBroken(): Boolean {
        return !isLive() && getHlsManifestUrl() == null && isAdaptiveFullHD()
    }

    private fun isAdaptiveFullHD(): Boolean {
        return _adaptiveFormats?.let { it.isNotEmpty() && "1080p" == it.first().getQualityLabel() } ?: false
    }
}
package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.googlecommon.common.helpers.ServiceHelper
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemFormatInfo
import com.liskovsoft.mediaserviceinterfaces.data.MediaItemStoryboard
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.sharedutils.querystringparser.UrlQueryString
import com.liskovsoft.sharedutils.querystringparser.UrlQueryStringFactory
import com.liskovsoft.sharedutils.rx.RxHelper
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.app.PoTokenGate.isWebPotExpired
import com.liskovsoft.youtubeapi.common.helpers.AppClient
import com.liskovsoft.youtubeapi.formatbuilders.hlsbuilder.YouTubeUrlListBuilder
import com.liskovsoft.youtubeapi.formatbuilders.mpdbuilder.YouTubeMPDBuilder
import com.liskovsoft.youtubeapi.formatbuilders.storyboard.YouTubeStoryParser
import com.liskovsoft.youtubeapi.innertube.models.PlayerResult
import com.liskovsoft.youtubeapi.innertube.utils.getAdaptiveFormats
import com.liskovsoft.youtubeapi.innertube.utils.getLegacyFormats
import com.liskovsoft.youtubeapi.innertube.utils.getLoudnessDb
import com.liskovsoft.youtubeapi.innertube.utils.getMergeCaptionTracks
import com.liskovsoft.youtubeapi.innertube.utils.getPaidContentText
import com.liskovsoft.youtubeapi.innertube.utils.getPlayabilityDescription
import com.liskovsoft.youtubeapi.innertube.utils.getPlayabilityReason
import com.liskovsoft.youtubeapi.innertube.utils.getPlayabilityStatus
import com.liskovsoft.youtubeapi.innertube.utils.getServerAbrStreamingUrl
import com.liskovsoft.youtubeapi.innertube.utils.getStartTimestamp
import com.liskovsoft.youtubeapi.innertube.utils.getStoryboardSpec
import com.liskovsoft.youtubeapi.innertube.utils.getTrailerVideoId
import com.liskovsoft.youtubeapi.innertube.utils.getUploadDate
import com.liskovsoft.youtubeapi.innertube.utils.getVideoPlaybackUstreamerConfig
import com.liskovsoft.youtubeapi.innertube.utils.getWatchTimeUrl
import com.liskovsoft.youtubeapi.innertube.utils.isPlayableInEmbed
import com.liskovsoft.youtubeapi.service.data.YouTubeMediaItemStoryboard
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder
import io.reactivex.Observable
import java.io.InputStream
import java.util.regex.Pattern
import kotlin.math.pow

private const val PARAM_EVENT_ID = "ei"
private const val PARAM_VM = "vm"
private const val PARAM_OF = "of"

private const val STATUS_OK = "OK"
private const val STATUS_UNPLAYABLE = "UNPLAYABLE"
private const val STATUS_ERROR = "ERROR"
private const val STATUS_OFFLINE = "LIVE_STREAM_OFFLINE"
private const val STATUS_LOGIN_REQUIRED = "LOGIN_REQUIRED"
private const val STATUS_AGE_CHECK_REQUIRED = "AGE_CHECK_REQUIRED"
private const val STATUS_AGE_VERIFICATION_REQUIRED = "AGE_VERIFICATION_REQUIRED"
private const val STATUS_CONTENT_CHECK_REQUIRED = "CONTENT_CHECK_REQUIRED"

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
    private val _isAdaptiveFullHD by lazy { _adaptiveFormats?.let { it.isNotEmpty() && "1080p" == it.first().getQualityLabel() } ?: false }
    private val _hasExtendedHlsFormats by lazy {
        // Need upload date check?
        // Extended formats may not work up to 3 days after publication.
        !_isLive && _hlsManifestUrl != null && _isAdaptiveFullHD
    }
    private val _isExtendedHlsFormatsBroken by lazy {
        !_isLive && _hlsManifestUrl == null && _isAdaptiveFullHD
    }
    private val _loudnessDb by lazy { playerResult.getLoudnessDb() }
    private val _storyboardSpec by lazy { playerResult.getStoryboardSpec() }
    private val _trailerVideoId by lazy { playerResult.getTrailerVideoId() }
    private val _playabilityStatus by lazy { playerResult.getPlayabilityStatus() }
    private val _playabilityReason by lazy {
        Helpers.toString(ServiceHelper.createInfo(playerResult.getPlayabilityReason(), playerResult.getPlayabilityDescription()))
    }
    private val _isPlayableInEmbed by lazy { playerResult.isPlayableInEmbed() }
    private val _isUnplayable by lazy { isUnknownRestricted() || isVisibilityRestricted() || isAgeRestricted() }
    /**
     * Reason of unavailability unknown or we received a temporal ban
     */
    private val _isUnknownError by lazy { ServiceHelper.atLeastOneEquals(_playabilityStatus, STATUS_UNPLAYABLE) }
    private val _isHfr by lazy { _dashManifestUrl?.contains("/hfr/all") ?: false }
    private val _startTimestamp by lazy { playerResult.getStartTimestamp() }
    private val _uploadDate by lazy { playerResult.getUploadDate() }
    private val _paidContentText by lazy { playerResult.getPaidContentText() }
    private val _watchTimeUrl by lazy { playerResult.getWatchTimeUrl() }

    private var _segmentDurationUs: Int = 0
    private var _startTimeMs: Long = 0
    private var _startSegmentNum: Int = 0
    private var _isStreamSeekable: Boolean = false

    private var _isSynced: Boolean = false
    private var _eventId: String? = null
    private var _visitorMonitoringData: String? = null
    private var _ofParam: String? = null
    private var _isAuth: Boolean = false

    private var _clickTrackingParams: String? = null

    private var _poToken: String? = null

    init {
        parseTrackingParams()
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

    override fun containsMedia() = containsDashUrl() || containsHlsUrl() || containsAdaptiveVideoFormats() || containsUrlFormats()

    override fun containsSabrFormats() = containsAdaptiveVideoFormats() && getAdaptiveFormats()?.firstOrNull()?.getFormatType() == MediaFormat.FORMAT_TYPE_SABR

    override fun containsDashFormats() = containsAdaptiveVideoFormats() && getAdaptiveFormats()?.firstOrNull()?.getFormatType() == MediaFormat.FORMAT_TYPE_DASH

    private fun containsAdaptiveVideoFormats() = _containsAdaptiveVideoFormats

    override fun containsHlsUrl() = _hlsManifestUrl != null

    override fun containsDashUrl() = _dashManifestUrl != null

    override fun containsUrlFormats() = _legacyFormats != null

    override fun hasExtendedHlsFormats() = _hasExtendedHlsFormats

    override fun getVolumeLevel(): Float {
        var result = 1.0f // the live loudness
        
        if (_loudnessDb != 0f) {
            // Original tv web: Math.min(1, 10 ** (-loudnessDb / 20))
            // -5db...5db (0.7...1.4) Base formula: normalLevel*10^(-db/20)
            // Low test - R.E.M. and high test - Lindemann
            var normalLevel = 10.0.pow((_loudnessDb / 20.0f).toDouble()).toFloat()
            if (normalLevel > 1.95) { // don't normalize?
                // System of a Down - Lonely Day
                //normalLevel = 1.0f;
                normalLevel = 1.5f
            }
            // Calculate the result as subtract of the video volume and the max volume
            result = 2.0f - normalLevel
        }

        return result / 2
    }

    override fun createMpdStream(): InputStream? {
        return YouTubeMPDBuilder.from(this).build()
    }

    override fun createMpdStreamObservable(): Observable<InputStream?> {
        return RxHelper.fromCallable(this::createMpdStream)
    }

    override fun createUrlList(): List<String>? {
        return YouTubeUrlListBuilder.from(this).buildUriList()
    }

    override fun createStoryboard(): MediaItemStoryboard? {
        if (_storyboardSpec == null) {
            return null
        }

        val storyParser = YouTubeStoryParser.from(_storyboardSpec)
        storyParser.setSegmentDurationUs(segmentDurationUs)

        // TODO: need to calculate real segment shift for 60 hrs streams (e.g. euronews live)
        storyParser.setStartSegmentNum(startSegmentNum)
        val storyboard = storyParser.extractStory()

        return YouTubeMediaItemStoryboard.from(storyboard)
    }

    override fun isUnplayable() = _isUnplayable

    override fun isSynced() = _isSynced

    override fun isAuth() = _isAuth

    override fun isUnknownError() = _isUnknownError

    override fun getPlayabilityReason(): String? = _playabilityReason

    override fun isStreamSeekable() = _isHfr || _isStreamSeekable

    override fun getStartTimestamp() = _startTimestamp

    override fun getUploadDate() = _uploadDate

    override fun getStartTimeMs() = _startTimeMs

    override fun getStartSegmentNum() = _startSegmentNum

    override fun getSegmentDurationUs() = _segmentDurationUs

    override fun getPaidContentText() = _paidContentText

    override fun getVideoPlaybackUstreamerConfig() = _videoPlaybackUstreamerConfig

    override fun getServerAbrStreamingUrl() = sabrUrlHolder.getUrl()

    override fun getPoToken() = _poToken

    fun setPoToken(poToken: String?) {
        _poToken = poToken
    }

    override fun getClientInfo() = _clientInfo

    override fun getEventId() = _eventId

    override fun getVisitorMonitoringData() = _visitorMonitoringData

    override fun getOfParam() = _ofParam

    override fun getClickTrackingParams() = _clickTrackingParams

    override fun setClickTrackingParams(clickTrackingParams: String?) {
        _clickTrackingParams = clickTrackingParams
    }

    /**
     * Format is used between multiple functions. Do a little cache.
     */
    override fun isCacheActual(): Boolean {
        // NOTE: Musical live streams are ciphered too!

        // Check app cipher first. It's not robust check (cipher may be updated not by us).
        // So, also check internal cache state.
        // Future translations (no media) should be polled constantly.

        return containsMedia() && AppService.instance().isPlayerCacheActual && !isWebPotExpired()
    }

    /**
     * Sync history data<br></br>
     * Intended to merge signed and unsigned infos (no-playback fix)
     */
    override fun sync(formatInfo: MediaItemFormatInfo?) {
        _isSynced = true

        if (formatInfo == null || Helpers.anyNull(formatInfo.eventId, formatInfo.visitorMonitoringData, formatInfo.ofParam)) {
            return
        }

        // Intended to merge signed and unsigned infos (no-playback fix)
        _eventId = formatInfo.eventId
        _visitorMonitoringData = formatInfo.visitorMonitoringData
        _ofParam = formatInfo.ofParam
        _isAuth = formatInfo.isAuth
    }

    /**
     * Sync live data
     */
    fun sync(dashInfo: DashInfo?) {
        if (dashInfo == null) {
            return
        }

        _segmentDurationUs = dashInfo.getSegmentDurationUs()
        _startTimeMs = dashInfo.getStartTimeMs()
        _startSegmentNum = dashInfo.getStartSegmentNum()
        _isStreamSeekable = dashInfo.isSeekable()
    }

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
        return _adaptiveFormats
            ?.any { it.getMimeType()?.startsWith("video/") == true }
            ?: false
    }

    private fun containsLegacyVideoInfo(): Boolean {
        return _legacyFormats
            ?.any { it.getMimeType()?.startsWith("video/") == true }
            ?: false
    }

    private fun isRent(): Boolean {
        return _isUnplayable && _trailerVideoId != null
    }

    /**
     * Video cannot be embedded
     */
    private fun isEmbedRestricted() = !_isPlayableInEmbed

    /**
     * Reason of unavailability unknown or we received a temporal ban
     */
    private fun isUnknownRestricted(): Boolean {
        return ServiceHelper.atLeastOneEquals(_playabilityStatus, STATUS_UNPLAYABLE)
    }

    /**
     * Removed or hidden by the user
     */
    private fun isVisibilityRestricted(): Boolean {
        return ServiceHelper.atLeastOneEquals(_playabilityStatus, STATUS_ERROR)
    }

    /**
     * Age restricted video
     */
    private fun isAgeRestricted(): Boolean {
        return ServiceHelper.atLeastOneEquals(
            _playabilityStatus,
            STATUS_LOGIN_REQUIRED,
            STATUS_AGE_CHECK_REQUIRED,
            STATUS_CONTENT_CHECK_REQUIRED
        )
    }

    private fun parseTrackingParams() {
        val parseDone = _eventId != null || _visitorMonitoringData != null

        if (!parseDone && _watchTimeUrl != null) {
            val queryString: UrlQueryString = UrlQueryStringFactory.parse(_watchTimeUrl)

            _eventId = queryString.get(PARAM_EVENT_ID)
            _visitorMonitoringData = queryString.get(PARAM_VM)
            _ofParam = queryString.get(PARAM_OF)
        }
    }
}
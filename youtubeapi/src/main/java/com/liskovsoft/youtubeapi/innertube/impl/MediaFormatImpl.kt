package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat.FORMAT_TYPE_DASH
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat.FORMAT_TYPE_SABR
import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat.FORMAT_TYPE_REGULAR
import com.liskovsoft.youtubeapi.formatbuilders.utils.ITagUtils
import com.liskovsoft.youtubeapi.innertube.models.StreamingFormat
import com.liskovsoft.youtubeapi.innertube.utils.getIndexRange
import com.liskovsoft.youtubeapi.innertube.utils.getInitRange
import com.liskovsoft.youtubeapi.innertube.utils.hasRange
import com.liskovsoft.youtubeapi.innertube.utils.isBroken
import com.liskovsoft.youtubeapi.innertube.utils.isOtf
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder

internal data class MediaFormatImpl(private val streamingFormat: StreamingFormat): MediaFormat {
    val urlHolder by lazy { VideoUrlHolder(streamingFormat.url, streamingFormat.cipher, streamingFormat.signatureCipher) }
    private val _formatType by lazy {
        if (streamingFormat.hasRange() && streamingFormat.isBroken()) FORMAT_TYPE_SABR
        else if (streamingFormat.hasRange()) FORMAT_TYPE_DASH
        else FORMAT_TYPE_REGULAR
    }
    private val _mimeType by lazy { streamingFormat.mimeType }
    private val _iTag by lazy { streamingFormat.itag?.takeIf { it != 0 }?.toString() ?: "" }
    private val _isDrc by lazy { streamingFormat.isDrc ?: false }
    private val _clen by lazy { streamingFormat.contentLength }
    private val _bitrate by lazy { streamingFormat.bitrate?.takeIf { it != 0 }?.toString() ?: "" }
    private val _projectionType by lazy { streamingFormat.projectionType }
    private val _width by lazy { streamingFormat.width ?: -1 }
    private val _height by lazy { streamingFormat.height ?: -1 }
    private val _initRange by lazy { streamingFormat.getInitRange() }
    private val _indexRange by lazy { streamingFormat.getIndexRange() }
    private val _fps by lazy { streamingFormat.fps?.takeIf { it != 0 }?.toString() ?: "" }
    private val _lmt by lazy { streamingFormat.lastModified }
    private val _qualityLabel by lazy { streamingFormat.qualityLabel }
    private val _format by lazy { streamingFormat.type }
    private val _isOtf by lazy { streamingFormat.isOtf() }
    private val _targetDurationSec by lazy { streamingFormat.targetDurationSec ?: -1 }
    private val _maxDvrDurationSec by lazy { streamingFormat.maxDvrDurationSec ?: -1 }
    private val _approxDurationMs by lazy { streamingFormat.approxDurationMs?.toInt() ?: -1 }
    private var _sourceUrl: String? = null
    private var _segmentUrlList: List<String>? = null
    private var _globalSegmentList: List<String>? = null

    override fun getFormatType() = _formatType

    override fun getUrl() = urlHolder.getUrl()

    override fun getMimeType() = _mimeType

    override fun getITag() = _iTag

    override fun isDrc() = _isDrc

    override fun getClen() = _clen

    override fun getBitrate() = _bitrate

    override fun getProjectionType() = _projectionType

    override fun getXtags(): String? = null

    override fun getWidth() = _width

    override fun getHeight() = _height

    override fun getIndex() = _indexRange

    override fun getInit() = _initRange

    override fun getFps() = _fps

    override fun getLmt() = _lmt

    override fun getQualityLabel() = _qualityLabel

    override fun getFormat() = _format

    override fun isOtf(): Boolean = _isOtf

    override fun getOtfInitUrl() = urlHolder.getOtfInitUrl()

    override fun getOtfTemplateUrl() = urlHolder.getOtfTemplateUrl()

    override fun getLanguage() = urlHolder.getLanguage()

    override fun getTargetDurationSec() = _targetDurationSec

    override fun getMaxDvrDurationSec() = _maxDvrDurationSec

    override fun getApproxDurationMs() = _approxDurationMs

    override fun getQuality(): String? = null

    override fun getSignature(): String? = null

    override fun getAudioSamplingRate(): String? = null

    override fun getSourceUrl(): String? = _sourceUrl

    fun setSourceUrl(sourceUrl: String) {
        _sourceUrl = sourceUrl
    }

    override fun getSegmentUrlList(): List<String>? = _segmentUrlList

    fun setSegmentUrlList(segmentUrlList: List<String>) {
        _segmentUrlList = segmentUrlList
    }

    override fun getGlobalSegmentList(): List<String>? = _globalSegmentList

    fun setGlobalSegmentList(globalSegmentList: List<String>) {
        _globalSegmentList = globalSegmentList
    }

    override fun compareTo(other: MediaFormat?): Int {
        if (other == null) {
            return 1
        }

        return ITagUtils.compare(getITag(), other.getITag())
    }

    override fun toString(): String {
        return String.format(
            "{Url: %s, Source url: %s, Signature: %s, Clen: %s, Width: %s, Height: %s, ITag: %s}",
            getUrl(),
            getSourceUrl(),
            getSignature(),
            getClen(),
            getWidth(),
            getHeight(),
            getITag()
        )
    }
}
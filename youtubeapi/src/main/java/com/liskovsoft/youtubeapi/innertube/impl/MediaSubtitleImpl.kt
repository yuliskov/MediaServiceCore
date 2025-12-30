package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle
import com.liskovsoft.youtubeapi.innertube.models.CaptionTrack
import com.liskovsoft.youtubeapi.innertube.utils.getBaseUrl
import com.liskovsoft.youtubeapi.innertube.utils.getCodecs
import com.liskovsoft.youtubeapi.innertube.utils.getMimeType
import com.liskovsoft.youtubeapi.innertube.utils.getName

internal data class MediaSubtitleImpl(private val captionTrack: CaptionTrack): MediaSubtitle {
    private val _baseUrl by lazy { captionTrack.getBaseUrl() }
    private val _isTranslatable by lazy { captionTrack.isTranslatable ?: false }
    private val _languageCode by lazy { captionTrack.languageCode }
    private val _vssId by lazy { captionTrack.vssId }
    private val _name by lazy { captionTrack.getName() }
    private val _mimeType by lazy { captionTrack.getMimeType() }
    private val _codecs by lazy { captionTrack.getCodecs() }
    private val _type by lazy { captionTrack.kind }

    override fun getBaseUrl() = _baseUrl

    override fun isTranslatable() = _isTranslatable

    override fun getLanguageCode() = _languageCode

    override fun getVssId() = _vssId

    override fun getName() = _name

    override fun getMimeType(): String = _mimeType

    override fun getCodecs(): String = _codecs

    override fun getType() = _type
}
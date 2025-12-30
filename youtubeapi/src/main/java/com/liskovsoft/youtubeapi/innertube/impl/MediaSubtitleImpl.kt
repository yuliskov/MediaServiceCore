package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaSubtitle
import com.liskovsoft.youtubeapi.innertube.models.CaptionTrack

internal data class MediaSubtitleImpl(private val captionTrack: CaptionTrack): MediaSubtitle {
    override fun getBaseUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun setBaseUrl(baseUrl: String?) {
        TODO("Not yet implemented")
    }

    override fun isTranslatable(): Boolean {
        TODO("Not yet implemented")
    }

    override fun setTranslatable(translatable: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getLanguageCode(): String? {
        TODO("Not yet implemented")
    }

    override fun setLanguageCode(languageCode: String?) {
        TODO("Not yet implemented")
    }

    override fun getVssId(): String? {
        TODO("Not yet implemented")
    }

    override fun setVssId(vssId: String?) {
        TODO("Not yet implemented")
    }

    override fun getName(): String? {
        TODO("Not yet implemented")
    }

    override fun setName(name: String?) {
        TODO("Not yet implemented")
    }

    override fun getMimeType(): String? {
        TODO("Not yet implemented")
    }

    override fun setMimeType(mimeType: String?) {
        TODO("Not yet implemented")
    }

    override fun getCodecs(): String? {
        TODO("Not yet implemented")
    }

    override fun setCodecs(codecs: String?) {
        TODO("Not yet implemented")
    }

    override fun getType(): String? {
        TODO("Not yet implemented")
    }

    override fun setType(type: String?) {
        TODO("Not yet implemented")
    }
}
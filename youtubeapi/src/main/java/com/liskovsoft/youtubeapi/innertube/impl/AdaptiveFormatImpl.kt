package com.liskovsoft.youtubeapi.innertube.impl

import com.liskovsoft.mediaserviceinterfaces.data.MediaFormat
import com.liskovsoft.youtubeapi.innertube.models.AdaptiveFormat

internal data class AdaptiveFormatImpl(private val adaptiveFormat: AdaptiveFormat): MediaFormat {
    override fun getFormatType(): Int {
        TODO("Not yet implemented")
    }

    override fun getUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getMimeType(): String? {
        TODO("Not yet implemented")
    }

    override fun getITag(): String? {
        TODO("Not yet implemented")
    }

    override fun isDrc(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getClen(): String? {
        TODO("Not yet implemented")
    }

    override fun getBitrate(): String? {
        TODO("Not yet implemented")
    }

    override fun getProjectionType(): String? {
        TODO("Not yet implemented")
    }

    override fun getXtags(): String? {
        TODO("Not yet implemented")
    }

    override fun getWidth(): Int {
        TODO("Not yet implemented")
    }

    override fun getHeight(): Int {
        TODO("Not yet implemented")
    }

    override fun getIndex(): String? {
        TODO("Not yet implemented")
    }

    override fun getInit(): String? {
        TODO("Not yet implemented")
    }

    override fun getFps(): String? {
        TODO("Not yet implemented")
    }

    override fun getLmt(): String? {
        TODO("Not yet implemented")
    }

    override fun getQualityLabel(): String? {
        TODO("Not yet implemented")
    }

    override fun getFormat(): String? {
        TODO("Not yet implemented")
    }

    override fun isOtf(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getOtfInitUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getOtfTemplateUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getLanguage(): String? {
        TODO("Not yet implemented")
    }

    override fun getTargetDurationSec(): Int {
        TODO("Not yet implemented")
    }

    override fun getMaxDvrDurationSec(): Int {
        TODO("Not yet implemented")
    }

    override fun getApproxDurationMs(): Int {
        TODO("Not yet implemented")
    }

    override fun getQuality(): String? {
        TODO("Not yet implemented")
    }

    override fun getSignature(): String? {
        TODO("Not yet implemented")
    }

    override fun getAudioSamplingRate(): String? {
        TODO("Not yet implemented")
    }

    override fun getSourceUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getSegmentUrlList(): List<String?>? {
        TODO("Not yet implemented")
    }

    override fun getGlobalSegmentList(): List<String?>? {
        TODO("Not yet implemented")
    }

    override fun compareTo(other: MediaFormat?): Int {
        TODO("Not yet implemented")
    }
}
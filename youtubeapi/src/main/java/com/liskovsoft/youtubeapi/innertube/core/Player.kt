package com.liskovsoft.youtubeapi.innertube.core

import com.liskovsoft.googlecommon.common.api.FileApi
import com.liskovsoft.googlecommon.common.api.FileContent
import com.liskovsoft.googlecommon.common.helpers.RetrofitHelper
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.AppService
import com.liskovsoft.youtubeapi.app.PoTokenGate
import com.liskovsoft.youtubeapi.formatbuilders.utils.MediaFormatUtils
import com.liskovsoft.youtubeapi.innertube.impl.MediaFormatImpl
import com.liskovsoft.youtubeapi.innertube.impl.MediaItemFormatInfoImpl
import com.liskovsoft.youtubeapi.innertube.utils.DeviceCategory
import com.liskovsoft.youtubeapi.innertube.utils.URLS
import com.liskovsoft.youtubeapi.innertube.utils.getRandomUserAgent
import com.liskovsoft.youtubeapi.innertube.utils.getStringBetweenStrings
import com.liskovsoft.youtubeapi.videoinfo.V2.DashInfoApi
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfo
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoContent
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoHeaders
import com.liskovsoft.youtubeapi.videoinfo.models.DashInfoUrl
import com.liskovsoft.youtubeapi.videoinfo.models.VideoUrlHolder

internal class Player private constructor(
    val playerUrl: String?
) {
    private val TAG = Player::class.simpleName

    val signatureTimestamp: String by lazy { appService.signatureTimestamp }
    private val appService by lazy { AppService.instance() }
    private val dashInfoApi by lazy { RetrofitHelper.create(DashInfoApi::class.java) }
    private val fileApi by lazy { RetrofitHelper.create(FileApi::class.java) }

    fun decipher(formatInfo: MediaItemFormatInfoImpl) {
        if (formatInfo.isUnplayable()) {
            return
        }

        decipherFormats(formatInfo)

        if (formatInfo.isLive()) {
            Log.d(TAG, "Enable seeking support on live streams...")
            formatInfo.sync(getDashInfo(formatInfo))
        }

        //formatInfo.setClient(getClient())
    }

    //////////////// DECIPHER //////////////////

    private fun decipherFormats(formatInfo: MediaItemFormatInfoImpl) {
        val adaptiveFormats: List<MediaFormatImpl>? = formatInfo.getAdaptiveFormats()
        val regularFormats: List<MediaFormatImpl>? = formatInfo.getUrlFormats()

        val urlHolders: MutableList<VideoUrlHolder> = mutableListOf()
        if (adaptiveFormats != null) for (videoFormat in adaptiveFormats) {
            urlHolders.add(videoFormat.urlHolder)
        }
        if (regularFormats != null) for (videoFormat in regularFormats) {
            urlHolders.add(videoFormat.urlHolder)
        }
        urlHolders.add(formatInfo.sabrUrlHolder)

        val result: Pair<MutableList<String?>?, MutableList<String?>?>? =
            appService.bulkSigExtract(extractNParams(urlHolders), extractSParams(urlHolders))

        if (result != null) {
            val nParams = result.first
            val signatures = result.second

            applyNParams(urlHolders, nParams)
            applySignatures(urlHolders, signatures)
        }

        val poToken = PoTokenGate.getPoToken(formatInfo.clientInfo)
        formatInfo.setPoToken(poToken)
        applySessionPoToken(urlHolders, poToken)
    }

    private fun extractSParams(urlHolders: List<VideoUrlHolder>): List<String?> = urlHolders.map { it.getSParam() }

    private fun extractNParams(urlHolders: List<VideoUrlHolder>): List<String?> = urlHolders.map { it.getNParam() } // All throttled strings has same values
    
    private fun applyNParams(urlHolders: List<VideoUrlHolder>, nParams: List<String?>?) {
        if (nParams == null || nParams.isEmpty()) {
            return
        }

        // All throttled strings has same values
        val sameSize = nParams.size == urlHolders.size

        for (i in urlHolders.indices) {
            urlHolders[i].setNParam(nParams[if (sameSize) i else 0])
        }
    }
    
    private fun applySignatures(urlHolders: List<VideoUrlHolder>, signatures: List<String?>?) {
        if (signatures == null) {
            return
        }

        if (signatures.size != urlHolders.size) {
            throw IllegalStateException("Sizes of urlHolders and signatures should match!")
        }

        for (i in urlHolders.indices) {
            urlHolders[i].setSignature(signatures[i])
        }
    }
    
    private fun applySessionPoToken(urlHolders: List<VideoUrlHolder>, poToken: String?) {
        if (poToken == null) {
            return
        }

        for (i in urlHolders.indices) {
            urlHolders[i].setPoToken(poToken)
        }
    }

    //////////// DASH INFO ///////////////

    private fun getDashInfo(formatInfo: MediaItemFormatInfoImpl): DashInfo? {
        if (formatInfo.getAdaptiveFormats().isNullOrEmpty()) {
            return null
        }

        var info = getCumulativeDashInfo(formatInfo)

        // Do retry. Sometimes the previous try failed?
        if (info == null || info.getSegmentDurationUs() <= 0 || info.getStartTimeMs() <= 0 || info.getStartSegmentNum() < 0) {
            info = getCumulativeDashInfo(formatInfo)
        }

        return info
    }

    private fun getCumulativeDashInfo(formatInfo: MediaItemFormatInfoImpl): DashInfo? {
        val format = getSmallestAudio(formatInfo)

        if (format == null) {
            return null
        }

        return try {
            getDashInfoHeaders(format.getUrl())
        } catch (_: Exception) {
            fallbackDashInfo(format)
        }
    }

    private fun fallbackDashInfo(format: MediaFormatImpl): DashInfo? {
        return try {
            getDashInfoUrl(format.getUrl())
        } catch (_: Exception) {
            // Empty results received. Url isn't available or something like that
            getDashInfoContent(format.getUrl())
        }
    }

    private fun getSmallestAudio(formatInfo: MediaItemFormatInfoImpl): MediaFormatImpl? {
        val format = Helpers.findFirst(
            formatInfo.getAdaptiveFormats(),
            Helpers.Filter { item -> MediaFormatUtils.isAudio(item!!.getMimeType()) }) // smallest format
        return format
    }

    private fun getDashInfoUrl(url: String?): DashInfoUrl? {
        if (url == null) {
            return null
        }

        return RetrofitHelper.get(dashInfoApi.getDashInfoUrl(url))
    }

    private fun getDashInfoHeaders(url: String?): DashInfoHeaders? {
        if (url == null) {
            return null
        }

        // Range doesn't work???
        //return RetrofitHelper.getHeaders(mFileApi.getHeaders(url + SMALL_RANGE));
        return DashInfoHeaders(RetrofitHelper.getHeaders(fileApi.getHeaders(url)))
    }
    
    private fun getDashInfoContent(url: String?): DashInfoContent? {
        if (url == null) {
            return null
        }

        return RetrofitHelper.get(dashInfoApi.getDashInfoContent(url))
    }
    
    companion object {
        fun create(poToken: String?, playerId: String?): Player {
            val realPLayerId = playerId ?: getPlayerId()
            val playerUrl = realPLayerId?.let { getPlayerUrl(it) }
            //val js = getPlayerJs(playerUrl)

            return Player(playerUrl)
        }

        fun getPlayerId(): String? {
            val fileApi = RetrofitHelper.create(FileApi::class.java)
            val js = RetrofitHelper.get(fileApi.getContent("${URLS.YT_BASE}/iframe_api"))

            return getStringBetweenStrings(js!!.content!!, "player\\/", "\\/")
        }

        fun getPlayerJs(playerUrl: String): FileContent? {
            val fileApi = RetrofitHelper.create(FileApi::class.java)
            return RetrofitHelper.get(
                fileApi.getContent(mapOf("User-Agent" to getRandomUserAgent(DeviceCategory.DESKTOP)), playerUrl))
        }

        fun getPlayerUrl(playerId: String): String {
            return "${URLS.YT_BASE}/s/player/${playerId}/player_ias.vflset/en_US/base.js"
        }
    }
}

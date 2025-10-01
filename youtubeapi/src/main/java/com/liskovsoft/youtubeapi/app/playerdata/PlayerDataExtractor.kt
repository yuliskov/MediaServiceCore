package com.liskovsoft.youtubeapi.app.playerdata

import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.app.models.cached.PlayerDataCached
import com.liskovsoft.youtubeapi.app.nsigsolver.common.YouTubeInfoExtractor
import com.liskovsoft.youtubeapi.app.nsigsolver.impl.V8ChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.ChallengeInput
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeRequest
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeType
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class PlayerDataExtractor(val playerUrl: String) {
    private val tag = PlayerDataExtractor::class.java.simpleName
    private val data
        get() = MediaServiceData.instance()
    private var nFuncCode: Boolean = false
    private var sigFuncCode: Boolean = false
    private var nSigTmp: Pair<String?, String?>? = null
    private var cpnCode: String? = null
    private var signatureTimestamp: String? = null
    private val fixedPlayerUrl by lazy {
        // Those are implements global helper functions. No fix. Fallback to regular.
        // See https://github.com/yt-dlp/yt-dlp/issues/12398
        // tv url: https://www.youtube.com/s/player/69b31e11/tv-player-es6-tce.vflset/tv-player-es6-tce.js
        // web url: https://www.youtube.com/s/player/e12fbea4/player_ias_tce.vflset/en_US/base.js
        playerUrl
            //.replace("_tce", "") // global helper functions, web url
            //.replace("/player_ias.vflset/en_US/base.js", "/tv-player-ias.vflset/tv-player-ias.js") // does not validates cpn
            .replace("-es6", "-ias") // es6 no supported
    }

    init {
        V8ChallengeProvider.warmup()

        // Get the code from the cache
        restoreAllData()
        checkSigData()
        checkAllData()

        if (cpnCode == null || signatureTimestamp == null) {
            fetchAllData()
            checkAllData()
            persistAllData()
        }
    }

    fun extractNSig(nParam: String): String? {
        if (!nFuncCode)
            return null

        if (nSigTmp?.first == nParam) return nSigTmp?.second

        val nSig = extractNSigReal(nParam)

        nSigTmp = Pair(nParam, nSig)

        return nSig
    }

    fun extractSig(sParams: List<String?>): List<String?>? {
        if (!sigFuncCode)
            return null

        if (sParams.all { it == null })
            return sParams.map { null }

        return extractSigReal(sParams.filterNotNull())
    }

    fun bulkSigExtract(nParams: List<String?>?, sParams: List<String?>?): Pair<List<String?>?, List<String?>?> {
        if (Helpers.allNulls(nParams, sParams)) {
            return Pair(null, null)
        }

        val nCached: List<String?>? = null
        val sCached: List<String?>? = null

        val response = bulkSigExtractReal(
            if (nFuncCode) nParams else null,
            if (sigFuncCode) sParams else null
        )

        return Pair(nCached ?: response.first, sCached ?: response.second)
    }

    fun createClientPlaybackNonce(): String? {
        return cpnCode?.let { ClientPlaybackNonceExtractor.createClientPlaybackNonce(it) } ?: YouTubeHelper.generateTParameter()
    }

    fun getSignatureTimestamp(): String? {
        return signatureTimestamp
    }

    fun validate(): Boolean {
        // TODO: fix cpn code
        // return mNFuncCode && mSigFuncCode && mCPNCode != null && mSignatureTimestamp != null
        return nFuncCode && sigFuncCode && signatureTimestamp != null
    }

    private fun extractNSigReal(nParam: String): String? {
        return bulkSigExtractReal(listOf(nParam), null).first?.firstOrNull()
    }

    private fun extractSigReal(sParam: List<String>): List<String?>? {
        return bulkSigExtractReal(null, sParam).second
    }

    private fun bulkSigExtractReal(nParams: List<String?>?, sParams: List<String?>?): Pair<List<String?>?, List<String?>?> {
        if (Helpers.allNulls(nParams, sParams)) {
            return Pair(null, null)
        }

        val nRequest = nParams?.filterNotNull()?.distinct()?.takeIf { it.isNotEmpty() }?.let {
            JsChallengeRequest(JsChallengeType.N, ChallengeInput(fixedPlayerUrl, it))
        }

        val sRequest = sParams?.filterNotNull()?.distinct()?.takeIf { it.isNotEmpty() }?.let {
            JsChallengeRequest(JsChallengeType.SIG, ChallengeInput(fixedPlayerUrl, it))
        }

        val result = V8ChallengeProvider.bulkSolve(listOfNotNull(nRequest, sRequest))

        var nProcessed: List<String?>? = null
        var sProcessed: List<String?>? = null

        for (item in result) {
            when (item.response?.type) {
                JsChallengeType.N ->
                    nProcessed = nParams?.map { item.response.output.results[it] }
                JsChallengeType.SIG ->
                    sProcessed = sParams?.map { item.response.output.results[it] }
                else -> {}
            }
        }

        return Pair(nProcessed, sProcessed)
    }

    private fun loadPlayer(): String? {
        return YouTubeInfoExtractor.loadPlayerSilent(fixedPlayerUrl)
    }

    private fun fetchAllData() {
        val jsCode = loadPlayer()

        cpnCode = jsCode?.let { ClientPlaybackNonceExtractor.extractClientPlaybackNonceCode(it) }
        signatureTimestamp = jsCode?.let { CommonExtractor.extractSignatureTimestamp(it) }
    }

    private fun persistAllData() {
        if (cpnCode != null && signatureTimestamp != null) {
            data.setPlayerExtractorData(
                null,
                null,
                PlayerDataCached(playerUrl, cpnCode, null, null, signatureTimestamp)
            )
        }
    }

    private fun restoreAllData() {
        val (_, _, playerData) = data.playerExtractorData

        if (playerData?.playerUrl == playerUrl) {
            cpnCode = playerData.clientPlaybackNonceFunction
            signatureTimestamp = playerData.signatureTimestamp
        }
    }

    private fun checkAllData() {
        cpnCode?.let {
            try {
                val result = createClientPlaybackNonce()
                if (result == null)
                    cpnCode = null
            } catch (error: V8ScriptExecutionException) {
                cpnCode = null
            }
        }
    }

    private fun checkSigData() {
        try {
            val param = "5cNpZqIJ7ixNqU68Y7S"
            val result = V8ChallengeProvider.bulkSolve(
                listOf(
                    JsChallengeRequest(JsChallengeType.N, ChallengeInput(fixedPlayerUrl, listOf(param))),
                    JsChallengeRequest(JsChallengeType.SIG, ChallengeInput(fixedPlayerUrl, listOf(param))),
                ))

            for (item in result) {
                when (item.response?.type) {
                    JsChallengeType.N ->
                        if (item.response.output.results[param]?.let { it != param } ?: false)
                            nFuncCode = true
                    JsChallengeType.SIG ->
                        if (item.response.output.results[param]?.let { it != param } ?: false)
                            sigFuncCode = true
                    else -> {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
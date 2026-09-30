package com.liskovsoft.youtubeapi.app.playerdata

import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.app.nsigsolver.common.YouTubeInfoExtractor
import com.liskovsoft.youtubeapi.app.nsigsolver.impl.V8ChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.ChallengeInput
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeRequest
import com.liskovsoft.youtubeapi.app.nsigsolver.provider.JsChallengeType
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class WebPlayerDataExtractor(override val playerUrl: String): PlayerDataExtractor {
    private val tag = WebPlayerDataExtractor::class.java.simpleName
    private val data
        get() = MediaServiceData.instance()
    private var nFuncCode: Boolean = false
    private var sFuncCode: Boolean = false
    private var cpnCode: String? = null
    private var signatureTimestamp: String? = null
    private val fixedPlayerUrl by lazy {
        // Those are implements global helper functions. No fix. Fallback to regular.
        // See https://github.com/yt-dlp/yt-dlp/issues/12398
        // tv url: https://www.youtube.com/s/player/69b31e11/tv-player-es6-tce.vflset/tv-player-es6-tce.js
        // web url: https://www.youtube.com/s/player/e12fbea4/player_ias_tce.vflset/en_US/base.js
        playerUrl
            //.replace("-tcl", "") // (403 fix, incompatible nParam, e.g. /tv-player-es6-tcl.vflset/tv-player-es6-tcl.js)
    }

    init {
        // Get the code from the cache
        restoreAllData()
        checkSigData()
        checkCpnData()

        if (signatureTimestamp == null) {
            fetchAllData()
            checkCpnData()
            persistAllData()
        }
    }

    override fun extractNSig(nParam: String): String? {
        return bulkSigExtract(listOf(nParam), null).first?.firstOrNull()
    }

    override fun extractSig(sParams: List<String?>): List<String?>? {
        return bulkSigExtract(null, sParams).second
    }

    override fun bulkSigExtract(nParams: List<String?>?, sParams: List<String?>?): Pair<List<String?>?, List<String?>?> {
        if (Helpers.allNulls(nParams, sParams)) {
            return Pair(null, null)
        }

        val response = bulkSigExtractReal(nParams, sParams)

        return Pair(response.first, response.second)
    }

    /**
     * "cpn":"KjdxegeSaJXRctIl"
     */
    override fun createClientPlaybackNonce(): String? {
        return cpnCode?.let { ClientPlaybackNonceExtractor.createClientPlaybackNonce(it) } ?: YouTubeHelper.generateCPNParameter2()
    }

    /**
     * "signatureTimestamp":20522
     */
    override fun getSignatureTimestamp(): String? {
        return signatureTimestamp
    }

    override fun setSignatureTimestamp(timestamp: String) {
        signatureTimestamp = timestamp
    }

    override fun validate(): Boolean {
        // TODO: fix cpn code
        // return mNFuncCode && mSigFuncCode && mCPNCode != null && mSignatureTimestamp != null
        return nFuncCode && sFuncCode && signatureTimestamp != null
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

        var nProcessed: List<String?>? = null
        var sProcessed: List<String?>? = null

        val nRequest = nParams?.takeIf { nFuncCode }?.filterNotNull()?.takeIf { it.isNotEmpty() }?.distinct()?.let {
            JsChallengeRequest(JsChallengeType.N, ChallengeInput(fixedPlayerUrl, it))
        }

        val sRequest = sParams?.takeIf { sFuncCode }?.filterNotNull()?.takeIf { it.isNotEmpty() }?.distinct()?.let {
            JsChallengeRequest(JsChallengeType.SIG, ChallengeInput(fixedPlayerUrl, it))
        }

        val result = V8ChallengeProvider.bulkSolve(listOfNotNull(nRequest, sRequest))

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
        if (validate()) {
            data.playerExtractorCache = PlayerExtractorCache(playerUrl, cpnCode, signatureTimestamp)
        }
    }

    private fun restoreAllData() {
        val playerCache = data.playerExtractorCache

        if (playerCache?.playerUrl == playerUrl) {
            cpnCode = playerCache.cpnCode
            signatureTimestamp = playerCache.signatureTimestamp
            nFuncCode = true
            sFuncCode = true
        }
    }

    private fun checkCpnData() {
        cpnCode?.let {
            try {
                val result = createClientPlaybackNonce()
                if (result == null)
                    cpnCode = null
            } catch (_: V8ScriptExecutionException) {
                cpnCode = null
            }
        }
    }

    private fun checkSigData() {
        if (nFuncCode && sFuncCode) {
            return
        }

        try {
            val nParam = "5cNpZqIJ7ixNqU68Y7S"
            val sigParam = "NJAJEij0EwRgIhAI0KExTgjfPk-MPM9MAdzyyPRt=BM8-XO5tm5hlMCSVpAiEAv7eP3CURqZNSPow8BXXAoazVoXgeMP7gH9BdylHCwgw=gwzz"
            val result = V8ChallengeProvider.bulkSolve(
                listOf(
                    JsChallengeRequest(JsChallengeType.N, ChallengeInput(fixedPlayerUrl, listOf(nParam))),
                    JsChallengeRequest(JsChallengeType.SIG, ChallengeInput(fixedPlayerUrl, listOf(sigParam))),
                ))

            for (item in result) {
                when (item.response?.type) {
                    JsChallengeType.N ->
                        if (item.response.output.results[nParam]?.let { it != nParam } ?: false)
                            nFuncCode = true
                    JsChallengeType.SIG ->
                        if (item.response.output.results[sigParam]?.let { it != sigParam } ?: false)
                            sFuncCode = true
                    else -> {}
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
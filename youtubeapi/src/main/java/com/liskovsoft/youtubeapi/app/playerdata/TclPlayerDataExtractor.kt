package com.liskovsoft.youtubeapi.app.playerdata

import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.googlecommon.common.helpers.YouTubeHelper
import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.nsigsolver.common.YouTubeInfoExtractor
import com.liskovsoft.youtubeapi.app.nsigsolver.impl.TclChallengeProvider
import com.liskovsoft.youtubeapi.app.nsigsolver.impl.V8ChallengeProvider
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

private const val TCL_URL = "/tv-player-ias-tcl.vflset/tv-player-ias-tcl.js"

/**
 * [playerUrl] is the TCL-flavored player (from tv_config).
 * Its n-function has a different call shape than the regular
 * web/TV player, so n-param extraction is routed to [TclChallengeProvider] instead of the
 * generic (yt-dlp ejs) [V8ChallengeProvider]. The TCL player isn't required decipher/sig
 * function, so signature extraction is never supported by it.
 */
internal class TclPlayerDataExtractor(override val playerUrl: String): PlayerDataExtractor {
    private val tag = TclPlayerDataExtractor::class.java.simpleName
    private val data
        get() = MediaServiceData.instance()
    private var nFuncCode: Boolean = false
    private var cpnCode: String? = null
    private var signatureTimestamp: String? = null
    private val fixedPlayerUrl by lazy {
        // The hash must come from tv_config: the n-params are bound to that exact player build.
        // Don't substitute the path of the regular player (its hash differs).
        // See https://github.com/yt-dlp/yt-dlp/issues/12398
        // tv url: https://www.youtube.com/s/player/69b31e11/tv-player-es6-tce.vflset/tv-player-es6-tce.js
        // web url: https://www.youtube.com/s/player/e12fbea4/player_ias_tce.vflset/en_US/base.js
        playerUrl
            .replace("/tv-player-es6.vflset/tv-player-es6.js", TCL_URL)
            .replace("/tv-player-ias.vflset/tv-player-ias.js", TCL_URL)
            .replace("/player_es6.vflset/en_US/base.js", TCL_URL)
            .replace("/player_ias.vflset/en_US/base.js", TCL_URL)
            .replace("-es6", "-ias") // es6 no supported
    }
    // Fetched lazily, once, and shared between validation (checkSigData) and real extraction —
    // TclChallengeProvider needs the raw player source.
    private val playerCode: String? by lazy { loadPlayer() }

    init {
        Log.d(tag, "Using player url: $fixedPlayerUrl")

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
        // TCL works only in TV version, so sFuncCode isn't necessary in this client.
        return nFuncCode && signatureTimestamp != null
    }

    private fun bulkSigExtractReal(nParams: List<String?>?, sParams: List<String?>?): Pair<List<String?>?, List<String?>?> {
        if (Helpers.allNulls(nParams, sParams)) {
            return Pair(null, null)
        }

        val validNParams = nParams?.takeIf { nFuncCode }?.filterNotNull()?.takeIf { it.isNotEmpty() }?.distinct()

        val nResults = validNParams?.let { params -> playerCode?.let { TclChallengeProvider.solveN(fixedPlayerUrl, it, params) } }
        val nProcessed = nResults?.let { results -> nParams?.map { results[it] } }
        // TCL works only in TV version, so sig isn't necessary in this client
        return Pair(nProcessed, null)
    }

    private fun loadPlayer(): String? {
        return YouTubeInfoExtractor.loadPlayerSilent(fixedPlayerUrl)
    }

    private fun fetchAllData() {
        val jsCode = playerCode

        cpnCode = jsCode?.let { ClientPlaybackNonceExtractor.extractClientPlaybackNonceCode(it) }
        signatureTimestamp = jsCode?.let { CommonExtractor.extractSignatureTimestamp(it) }
    }

    private fun persistAllData() {
        if (validate()) {
            data.tclPlayerExtractorCache = PlayerExtractorCache(playerUrl, cpnCode, signatureTimestamp)
        }
    }

    private fun restoreAllData() {
        val playerCache = data.tclPlayerExtractorCache

        if (playerCache?.playerUrl == playerUrl) {
            cpnCode = playerCache.cpnCode
            signatureTimestamp = playerCache.signatureTimestamp
            nFuncCode = true
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
        if (nFuncCode) {
            return
        }

        try {
            val nParam = "5cNpZqIJ7ixNqU68Y7S"
            val code = playerCode ?: return
            val result = TclChallengeProvider.solveN(fixedPlayerUrl, code, listOf(nParam))
            if (result[nParam]?.let { it != nParam } == true) {
                nFuncCode = true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
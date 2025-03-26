package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.youtubeapi.app.models.cached.PlayerDataCached
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.ReflectionHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class PlayerDataExtractor(val playerUrl: String) {
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private val data = MediaServiceData.instance()
    private var mNFuncCode: Pair<List<String>, String>? = null
    private var mNSig: Pair<String, String?>? = null
    private var mCipherCode: String? = null
    private var mCPNCode: String? = null
    private var mSignatureTimestamp: String? = null

    init {
        // Get the code from the cache
        restoreNFuncCode()
        restoreOtherData()

        if (mNFuncCode == null || mCipherCode == null || mCPNCode == null || mSignatureTimestamp == null) {
            val jsCode = loadPlayer()
            val globalVarData = jsCode?.let { CommonExtractor.extractPlayerJsGlobalVar(it) }

            mNFuncCode = jsCode?.let { NSigExtractor.extractNFuncCode(it, globalVarData) }
            mCipherCode = jsCode?.let { CipherExtractor.extractCipherCode(it, globalVarData) }
            mCPNCode = jsCode?.let { ClientPlaybackNonceExtractor.extractClientPlaybackNonceCode(it) }
            mSignatureTimestamp = jsCode?.let { CommonExtractor.extractSignatureTimestamp(it) }

            persistNFuncCode()
            persistOtherCode()
        }

        if (mNFuncCode == null) {
            ReflectionHelper.dumpDebugInfo(NSigExtractor::class.java, loadPlayer())
            throw IllegalStateException("NSigExtractor: Can't obtain NSig code for $playerUrl...")
        }
    }

    fun extractNSig(nParam: String): String? {
        if (mNSig?.first == nParam) return mNSig?.second

        val nSig = extractNSigReal(nParam)

        mNSig = Pair(nParam, nSig)

        return nSig
    }

    fun extractCipher(): String? {
        return mCipherCode
    }

    fun decipherItems(items: List<String>): List<String>? {
        return mCipherCode?.let { CipherExtractor.decipherItems(items, it) }
    }

    fun createClientPlaybackNonce(): String? {
        return mCPNCode?.let { ClientPlaybackNonceExtractor.createClientPlaybackNonce(it) }
    }

    fun getSignatureTimestamp(): String? {
        return mSignatureTimestamp
    }

    private fun extractNSigReal(nParam: String): String? {
        val funcCode = mNFuncCode ?: return null

        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(nParam))
    }

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mFileApi.getContent(fixupPlayerUrl(playerUrl)))?.content
    }

    private fun fixupPlayerUrl(playerUrl: String): String {
        return playerUrl
            .replace("/player_ias_tce.vflset/", "/player_ias.vflset/") // See https://github.com/yt-dlp/yt-dlp/issues/12398
            .replace("player_ias.vflset/en_US/base.js", "tv-player-ias.vflset/tv-player-ias.js")
    }

    private fun persistNFuncCode() { // save on success
        mNFuncCode?.let { data.nSigData = NSigData(playerUrl, it.first, it.second) }
    }

    private fun persistOtherCode() {
        if (mCipherCode != null && mCPNCode != null && mSignatureTimestamp != null) {
            data.playerData = PlayerDataCached(playerUrl, mCPNCode, null, mCipherCode, mSignatureTimestamp)
        }
    }

    private fun restoreNFuncCode() {
        val nSigData = data.nSigData

        if (nSigData?.nFuncPlayerUrl == playerUrl) {
            mNFuncCode = Pair(nSigData.nFuncParams, nSigData.nFuncCode)
        }
    }

    private fun restoreOtherData() {
        val playerData = data.playerData

        if (playerData?.playerUrl == playerUrl) {
            mCPNCode = playerData.clientPlaybackNonceFunction
            mCipherCode = playerData.decipherFunction
            mSignatureTimestamp = playerData.signatureTimestamp
        }
    }
}
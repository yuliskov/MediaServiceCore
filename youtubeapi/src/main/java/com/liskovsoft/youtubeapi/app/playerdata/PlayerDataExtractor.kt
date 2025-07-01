package com.liskovsoft.youtubeapi.app.playerdata

import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.app.models.cached.PlayerDataCached
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.ReflectionHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class PlayerDataExtractor(val playerUrl: String) {
    private val TAG = PlayerDataExtractor::class.java.simpleName
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private val data
        get() = MediaServiceData.instance()
    private var mNFuncCode: Pair<List<String>, String>? = null
    private var mSigFuncCode: Pair<List<String>, String>? = null
    private var mNSigTmp: Pair<String, String?>? = null
    private var mCPNCode: String? = null
    private var mSignatureTimestamp: String? = null

    init {
        // Get the code from the cache
        restoreAllData()
        //checkAllData()

        if (mNFuncCode == null || mSigFuncCode == null || mCPNCode == null || mSignatureTimestamp == null) {
            fetchAllData()
            checkAllData()
            persistAllData()
        }

        if (mNFuncCode == null) {
            ReflectionHelper.dumpDebugInfo(NSigExtractor::class.java, loadPlayer())
        }
    }

    fun extractNSig(nParam: String): String? {
        if (mNSigTmp?.first == nParam) return mNSigTmp?.second

        val nSig = extractNSigReal(nParam)

        mNSigTmp = Pair(nParam, nSig)

        return nSig
    }

    fun extractSig(signature: String): String? {
        val nSig = extractSigReal(signature)

        return nSig
    }

    fun decipherItems(items: List<String?>): List<String?>? {
        return mSigFuncCode?.let { items.map { it?.let { extractSig(it) } } }
    }

    fun createClientPlaybackNonce(): String? {
        return mCPNCode?.let { ClientPlaybackNonceExtractor.createClientPlaybackNonce(it) }
    }

    fun getSignatureTimestamp(): String? {
        return mSignatureTimestamp
    }

    fun validate(): Boolean {
        return mNFuncCode != null && mSigFuncCode != null && mCPNCode != null && mSignatureTimestamp != null
    }

    private fun extractNSigReal(nParam: String): String? {
        val funcCode = mNFuncCode ?: return null

        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(nParam))
    }

    private fun extractSigReal(signature: String): String? {
        val funcCode = mSigFuncCode ?: return null

        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(signature))
    }

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mFileApi.getContent(fixupPlayerUrl(playerUrl)))?.content
    }

    private fun fixupPlayerUrl(playerUrl: String): String {
        return playerUrl
            .replace("/player_ias_tce.vflset/", "/player_ias.vflset/") // See https://github.com/yt-dlp/yt-dlp/issues/12398
            .replace("/player_ias.vflset/en_US/base.js", "/tv-player-ias.vflset/tv-player-ias.js") // does not validates cpn
    }

    private fun fetchAllData() {
        val jsCode = loadPlayer()
        val globalVarData = jsCode?.let { CommonExtractor.extractPlayerJsGlobalVar(it) } ?: Triple(null, null, null)
        val globalVar = CommonExtractor.interpretPlayerJsGlobalVar(globalVarData)

        mNFuncCode = jsCode?.let {
            try {
                NSigExtractor.extractNFuncCode(it, globalVar)
            } catch (e: Throwable) {
                e.printStackTrace()
                Log.d(TAG, "NSig init failed: %s", e.message)
                null
            }
        }
        mSigFuncCode = jsCode?.let {
            try {
                SigExtractor.extractSigCode(it, globalVar)
            } catch (e: Throwable) {
                e.printStackTrace()
                Log.d(TAG, "Signature init failed: %s", e.message)
                null
            }
        }
        mCPNCode = jsCode?.let { ClientPlaybackNonceExtractor.extractClientPlaybackNonceCode(it) }
        mSignatureTimestamp = jsCode?.let { CommonExtractor.extractSignatureTimestamp(it) }
    }

    private fun persistAllData() {
        if (mNFuncCode != null && mSigFuncCode != null && mCPNCode != null && mSignatureTimestamp != null) {
            data.setPlayerExtractorData(
                mNFuncCode?.let { NSigData(playerUrl, it.first, it.second) },
                mSigFuncCode?.let { NSigData(playerUrl, it.first, it.second) },
                PlayerDataCached(playerUrl, mCPNCode, null, null, mSignatureTimestamp)
            )
        }
    }

    private fun restoreAllData() {
        val (nSigData, sigData, playerData) = data.playerExtractorData

        if (nSigData?.nFuncPlayerUrl == playerUrl) {
            mNFuncCode = Pair(nSigData.nFuncParams, nSigData.nFuncCode)
        }

        if (sigData?.nFuncPlayerUrl == playerUrl) {
            mSigFuncCode = Pair(sigData.nFuncParams, sigData.nFuncCode)
        }

        if (playerData?.playerUrl == playerUrl) {
            mCPNCode = playerData.clientPlaybackNonceFunction
            mSignatureTimestamp = playerData.signatureTimestamp
        }
    }

    private fun checkAllData() {
        mNFuncCode?.let {
            try {
                val param = "5cNpZqIJ7ixNqU68Y7S"
                val result = extractNSigReal(param)
                if (result == null || param == result)
                    mNFuncCode = null
            } catch (error: V8ScriptExecutionException) {
                mNFuncCode = null
            }
        }
    }
}
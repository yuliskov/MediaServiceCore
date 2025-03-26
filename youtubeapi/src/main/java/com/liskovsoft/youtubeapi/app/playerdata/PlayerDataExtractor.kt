package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.youtubeapi.app.nsig.NSigData
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.ReflectionHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData

internal class PlayerDataExtractor(val playerUrl: String) {
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private val data = MediaServiceData.instance()
    private var mNFuncPlayerUrl: String? = null
    private var mNFuncCode: Pair<List<String>, String>? = null
    private var mNSig: Pair<String, String?>? = null
    private var mCipherCode: String? = null

    init {
        // Get the code from the cache
        restoreNFuncCode()

        var jsCode: String? = null
        if (mNFuncCode == null || mCipherCode == null) {
            jsCode = loadPlayer()
        }

        // Obtain the code regularly
        if (mNFuncCode == null) {
            mNFuncCode = jsCode?.let { NSigExtractor2.extractNFuncCode(it) }
            persistNFuncCode()
        }

        if (mCipherCode == null) {
            mCipherCode = jsCode?.let { CipherExtractor.extract(it) }
        }

        if (mNFuncCode == null) {
            ReflectionHelper.dumpDebugInfo(NSigExtractor2::class.java, loadPlayer())
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

    private fun extractNSigReal(nParam: String): String? {
        val funcCode = mNFuncCode ?: return null

        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(nParam))
    }

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mFileApi.getContent(mNFuncPlayerUrl ?: playerUrl))?.content
    }

    private fun persistNFuncCode() { // save on success
        mNFuncCode?.let { data.nSigData = NSigData(playerUrl, it.first, it.second) }
    }

    private fun restoreNFuncCode() {
        val nSigData = data.nSigData

        if (nSigData?.nFuncPlayerUrl == playerUrl) {
            mNFuncCode = Pair(nSigData.nFuncParams, nSigData.nFuncCode)
        }
    }
}
package com.liskovsoft.youtubeapi.app.nsig

import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper

internal class NSigExtractor(private val playerUrl: String) {
    private val mNSigApi = RetrofitHelper.withRegExp(NSigApi::class.java)

    fun extractNSig(s: String): String? {
        val funcCode = getFuncCode()
        
        val func = JSInterpret.extractFunctionFromCode(funcCode?.first, funcCode?.second)

        return func(s)
    }

    private fun getFuncCode(): Pair<List<String>, String>? {
        // get funcCode from Cache if not null

        val jsCode = loadPlayer()

        val funcName = extractNFuncName(jsCode)
        val funcCode = extractNFuncCode(funcName)

        // store funcCode in Cache

        return funcCode
    }

    private fun extractNFuncName(jsCode: String?): String? {
        return null
    }

    private fun extractNFuncCode(name: String?): Pair<List<String>, String>? {
        return null
    }

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mNSigApi.getPlayerContent(playerUrl))?.content
    }
}
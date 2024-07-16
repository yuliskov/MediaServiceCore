package com.liskovsoft.youtubeapi.app.nsig

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import java.util.regex.Pattern

internal class NSigExtractor(private val playerUrl: String) {
    private val mNSigApi = RetrofitHelper.withRegExp(NSigApi::class.java)
    private val mNFuncNamePattern = Pattern.compile("""(?x)(?:\.get\("n"\)\)&&\(b=|b=String\.fromCharCode\(110\),c=a\.get\(b\)\)&&\(c=)
            ([a-zA-Z0-9$]+)(?:\[(\d+)\])?\([a-zA-Z0-9]\)""", Pattern.COMMENTS)
    private var mFuncCode: Pair<List<String>, String>? = null

    fun extractNSig(s: String): String? {
        val funcCode = getFuncCode() ?: return null
        
        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(s)
    }

    private fun getFuncCode(): Pair<List<String>, String>? {
        // get funcCode from Cache if not null
        if (mFuncCode != null) {
            return mFuncCode
        }

        val jsCode = loadPlayer() ?: return null

        val funcName = extractNFunctionName(jsCode) ?: return null

        val funcCode = JSInterpret.extractFunctionCode(jsCode, funcName) ?: return null

        // store funcCode in Cache
        mFuncCode = funcCode

        return funcCode
    }

    private fun extractNFunctionName(jsCode: String): String? {
        val matcher = mNFuncNamePattern.matcher(jsCode)

        if (matcher.find() && matcher.groupCount() == 2) {
            val funcName = matcher.group(1)
            val idx = matcher.group(2)

            val escapedFuncName = Pattern.quote(funcName)

            val nFuncArrPattern = Pattern.compile("""var $escapedFuncName\s*=\s*(\[.+?\])\s*[,;]""")

            val nFuncArrMatcher = nFuncArrPattern.matcher(jsCode)

            if (nFuncArrMatcher.find() && nFuncArrMatcher.groupCount() == 1) {
                val nFuncArrStr = nFuncArrMatcher.group(1)

                val gson = Gson()
                val listType = object : TypeToken<List<String>>() {}.type
                val nFuncList: List<String> = gson.fromJson(nFuncArrStr, listType)

                return nFuncList[idx.toInt()]
            }
        }

        return null
    }

    private fun extractNFuncCode(name: String?): Pair<List<String>, String>? {
        return null
    }

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mNSigApi.getPlayerContent(playerUrl))?.content
    }
}
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

    init {
        initFuncCode()
    }

    fun extractNSig(s: String): String? {
        val funcCode = mFuncCode ?: return null
        
        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(s))
    }

    private fun initFuncCode() {
        val jsCode = loadPlayer() ?: return

        val funcName = extractNFunctionName(jsCode) ?: return

        val funcCode = JSInterpret.extractFunctionCode(jsCode, funcName) ?: return

        // store funcCode in cache
        mFuncCode = funcCode
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

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mNSigApi.getPlayerContent(playerUrl))?.content
    }
}
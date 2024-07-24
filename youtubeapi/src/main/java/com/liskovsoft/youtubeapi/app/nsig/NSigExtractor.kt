package com.liskovsoft.youtubeapi.app.nsig

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import java.util.regex.Pattern

internal class NSigExtractor(private val playerUrl: String) {
    private val mNSigApi = RetrofitHelper.withRegExp(NSigApi::class.java)
    private var mFuncCode: Pair<List<String>, String>? = null

    init {
        initFuncCode()
    }

    fun extractNSig(s: String): String? {
        val funcCode = mFuncCode ?: return null
        
        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(s))
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_code
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun initFuncCode() {
        val jsCode = loadPlayer() ?: return

        val funcName = extractNFunctionName(jsCode) ?: return

        val funcCode = JSInterpret.extractFunctionCode(jsCode, funcName) ?: return

        // store funcCode in cache
        mFuncCode = funcCode
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_name
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun extractNFunctionName(jsCode: String): String? {
        val nFuncPattern = Pattern.compile("""(?x)
            (?:
                \.get\("n"\)\)&&\(b=|
                (?:
                    b=String\.fromCharCode\(110\)|
                    ([a-zA-Z0-9${'$'}.]+)&&\(b="nn"\[\+\1\]
                ),c=a\.get\(b\)\)&&\(c=
            )
            (?<nfunc>[a-zA-Z0-9$]+)(?:\[(?<idx>\d+)\])?\([a-zA-Z0-9]\)""", Pattern.COMMENTS)
        val nFuncMatcher = nFuncPattern.matcher(jsCode)

        if (nFuncMatcher.find() && nFuncMatcher.groupCount() >= 2) {
            val funcName = nFuncMatcher.group(nFuncMatcher.groupCount() - 1)
            val idx = nFuncMatcher.group(nFuncMatcher.groupCount())

            val escapedFuncName = Pattern.quote(funcName)

            val nameArrPattern = Pattern.compile("""var $escapedFuncName\s*=\s*(?<namearr>\[.+?\])\s*[,;]""")

            val nameArrMatcher = nameArrPattern.matcher(jsCode)

            if (nameArrMatcher.find() && nameArrMatcher.groupCount() == 1) {
                val nameArrStr = nameArrMatcher.group(1)

                val gson = Gson()
                val listType = object : TypeToken<List<String>>() {}.type
                val nameList: List<String> = gson.fromJson(nameArrStr, listType)

                return nameList[idx.toInt()]
            }
        }

        return null
    }

    private fun loadPlayer(): String? {
        return RetrofitHelper.get(mNSigApi.getPlayerContent(playerUrl))?.content
    }
}
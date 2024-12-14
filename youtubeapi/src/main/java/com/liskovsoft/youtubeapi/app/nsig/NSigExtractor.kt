package com.liskovsoft.youtubeapi.app.nsig

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liskovsoft.youtubeapi.common.api.FileApi
import com.liskovsoft.youtubeapi.common.helpers.ReflectionHelper
import com.liskovsoft.youtubeapi.common.helpers.RetrofitHelper
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import com.liskovsoft.youtubeapi.service.internal.MediaServiceData
import java.util.regex.Pattern

internal class NSigExtractor(val playerUrl: String) {
    private val mFileApi = RetrofitHelper.create(FileApi::class.java)
    private val data = MediaServiceData.instance()
    private var mNFuncPlayerUrl: String? = null
    private var mNFuncCode: Pair<List<String>, String>? = null
    private var mNSig: Pair<String, String?>? = null
    //private var mNFuncPattern: com.florianingerl.util.regex.Pattern? = com.florianingerl.util.regex.Pattern.compile("""(?x)
    //        (?:
    //            \.get\("n"\)\)&&\(b=|
    //            (?:
    //                b=String\.fromCharCode\(110\)|
    //                ([a-zA-Z0-9_$.]+)&&\(b="nn"\[\+\1\]
    //            )
    //            (?:
    //                ,[a-zA-Z0-9_$]+\(a\))?,c=a\.
    //                (?:
    //                    get\(b\)|
    //                    [a-zA-Z0-9_$]+\[b\]\|\|null
    //                )\)&&\(c=|
    //            \b([a-zA-Z0-9_$]+)=
    //        )([a-zA-Z0-9_$]+)(?:\[(\d+)\])?\([a-zA-Z]\)
    //        (?(2),[a-zA-Z0-9_$]+\.set\("n"\,\2\),\3\.length)""", Pattern.COMMENTS)
    private var mNFuncPattern: com.florianingerl.util.regex.Pattern? = com.florianingerl.util.regex.Pattern.compile("""(?x)
            (?:
                \.get\("n"\)\)&&\(b=|
                (?:
                    b=String\.fromCharCode\(110\)|
                    ([a-zA-Z0-9_$.]+)&&\(b="nn"\[\+\1\]
                )
                (?:
                    ,[a-zA-Z0-9_$]+\(a\))?,c=a\.
                    (?:
                        get\(b\)|
                        [a-zA-Z0-9_$]+\[b\]\|\|null
                    )\)&&\(c=|
                \b([a-zA-Z0-9_$]+)=
            )([a-zA-Z0-9_$]+)(?:\[(\d+)\])?\([a-zA-Z]\)
            (?(2),[a-zA-Z0-9_$]+\.set\((?:"n+"|[a-zA-Z0-9_$]+)\,\2\))""", Pattern.COMMENTS)
    //private var mNFuncPattern2: Pattern? = Pattern.compile("""(?xs)
    //            ;\s*([a-zA-Z0-9_$]+)\s*=\s*function\([a-zA-Z0-9_$]+\)
    //            \s*\{(?:(?!\};).)+?["']enhanced_except_""", Pattern.COMMENTS)
    private var mNFuncPattern2: Pattern? = Pattern.compile("""(?xs)
                ;\s*([a-zA-Z0-9_$]+)\s*=\s*function\([a-zA-Z0-9_$]+\)
                \s*\{(?:(?!\};).)+?return\s*(["'])[\w-]+_w8_\1\s*\+\s*[a-zA-Z0-9_$]+""", Pattern.COMMENTS)

    init {
        // Get the code from the cache
        restoreNFuncCode()

        // Obtain the code regularly
        if (mNFuncCode == null) {
            initNFuncCode()
        }

        //// Restore previous success code
        //if (mNFuncCode == null && data.nFuncPlayerUrl != null) {
        //    mNFuncPlayerUrl = data.nFuncPlayerUrl
        //    initNFuncCode()
        //    mNFuncPlayerUrl = null
        //}

        if (mNFuncCode == null) {
            ReflectionHelper.dumpDebugInfo(javaClass, loadPlayer())
            throw IllegalStateException("NSigExtractor: Can't obtain NSig code for $playerUrl...")
        }
    }

    fun extractNSig(nParam: String): String? {
        if (mNSig?.first == nParam) return mNSig?.second

        val nSig = extractNSigReal(nParam)

        mNSig = Pair(nParam, nSig)

        return nSig
    }

    private fun extractNSigReal(nParam: String): String? {
        val funcCode = mNFuncCode ?: return null

        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(nParam))
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_code
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun initNFuncCode() {
        val jsCode = loadPlayer() ?: return

        val funcName = extractNFunctionName(jsCode) ?: extractNFunctionName2(jsCode) ?: return

        mNFuncCode = fixupNFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName))

        persistNFuncCode()
    }

    private fun fixupNFunctionCode(data: Pair<List<String>, String>): Pair<List<String>, String> {
        val argNames = data.first
        val code = data.second
        val patternString = """;\s*if\s*\(\s*typeof\s+[a-zA-Z0-9_$]+\s*===?\s*(['"])undefined\1\s*\)\s*return\s+${argNames[0]};"""
        val pattern = Pattern.compile(patternString)
        val matcher = pattern.matcher(code)
        val updatedCode = matcher.replaceAll(";")
        return Pair(argNames, updatedCode)
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_name
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun extractNFunctionName(jsCode: String): String? {
        val nFuncPattern = mNFuncPattern ?: return null
        val nFuncMatcher = nFuncPattern.matcher(jsCode)

        if (nFuncMatcher.find() && nFuncMatcher.groupCount() >= 2) {
            val funcName = nFuncMatcher.group(nFuncMatcher.groupCount() - 1)
            val idx = nFuncMatcher.group(nFuncMatcher.groupCount())

            val escapedFuncName = Pattern.quote(funcName)

            val nameArrPattern = Pattern.compile("""$escapedFuncName\s*=\s*(\[.+?\])\s*[,;]""")

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

    private fun extractNFunctionName2(jsCode: String): String? {
        val nFuncPattern = mNFuncPattern2 ?: return null
        val nFuncMatcher = nFuncPattern.matcher(jsCode)

        if (nFuncMatcher.find() && nFuncMatcher.groupCount() == 1) {
            return nFuncMatcher.group(1)
        }

        return null
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
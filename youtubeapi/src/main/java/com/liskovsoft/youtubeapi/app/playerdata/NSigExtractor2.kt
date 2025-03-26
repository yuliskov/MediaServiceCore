package com.liskovsoft.youtubeapi.app.playerdata

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import java.util.regex.Pattern

internal object NSigExtractor2 {
    private val TAG = NSigExtractor2::class.java.simpleName
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

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_code
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    fun extractNFuncCode(jsCode: String, globalVarData: Triple<String?, String?, String?>?): Pair<List<String>, String>? {
        val funcName = extractNFunctionName(jsCode) ?: extractNFunctionName2(jsCode) ?: return null

        return fixupNFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName), globalVarData ?: Triple(null, null, null))
    }

    private fun fixupNFunctionCode(data: Pair<List<String>, String>, globalVarData: Triple<String?, String?, String?>): Pair<List<String>, String> {
        val argNames = data.first
        var code = data.second

        val (globalVar, varName, _) = globalVarData

        if (globalVar != null) {
            Log.d(TAG, "Prepending n function code with global array variable \"$varName\"")
            code = "$globalVar; $code"
        } else {
            Log.d(TAG, "No global array variable found in player JS")
        }

        val patternString = """;\s*if\s*\(\s*typeof\s+[a-zA-Z0-9_$]+\s*===?\s*(?:(['"])undefined\1|${varName ?: ""}\[\d+\])\s*\)\s*return\s+${argNames[0]};"""
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
}
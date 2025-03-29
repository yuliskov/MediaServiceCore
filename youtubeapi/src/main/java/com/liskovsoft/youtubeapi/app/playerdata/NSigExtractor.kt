package com.liskovsoft.youtubeapi.app.playerdata

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import java.util.regex.Pattern

internal object NSigExtractor {
    private val TAG = NSigExtractor::class.java.simpleName
    private val mNFuncPattern = com.florianingerl.util.regex.Pattern.compile("""(?x)
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
    private val mNFuncPattern2 = Pattern.compile("""(?xs)
                ;\s*([a-zA-Z0-9_$]+)\s*=\s*function\([a-zA-Z0-9_$]+\)
                \s*\{(?:(?!\};).)+?return\s*(["'])[\w-]+_w8_\1\s*\+\s*[a-zA-Z0-9_$]+""", Pattern.COMMENTS)

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_code
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    fun extractNFuncCode(jsCode: String, globalVarData: Triple<String?, String?, String?>): Pair<List<String>, String>? {
        val globalVar = CommonExtractor.interpretPlayerJsGlobalVar(globalVarData)

        val funcName =
            extractNFunctionName(jsCode, globalVar) ?: extractNFunctionName2(jsCode) ?: return null

        return fixupNFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName), globalVarData, globalVar)
    }

    private fun fixupNFunctionCode(data: Pair<List<String>, String>, globalVarData: Triple<String?, String?, String?>, globalVar: Pair<String?, List<String>?>): Pair<List<String>, String> {
        val argNames = data.first
        var nSigCode = data.second

        var (varCode, varName, _) = globalVarData
        val globalList: List<String>? = globalVar.second

        if (varCode != null && varName != null) {
            Log.d(TAG, "Prepending n function code with global array variable \"$varName\"")
            nSigCode = "$varCode; $nSigCode"
        } else {
            varName = "dlp_wins"
        }

        val undefinedIdx = globalList?.indexOf("undefined") ?: -1
        val escapedVarName = Pattern.quote(varName)
        val escapedArgName = Pattern.quote(argNames[0])
        val fixupPattern = Pattern.compile("""(?x)
                ;\s*if\s*\(\s*typeof\s+[a-zA-Z0-9_$]+\s*===?\s*(?:
                    (["\'])undefined\1|
                    ${escapedVarName}\[${if (undefinedIdx != -1) undefinedIdx else "\\d+"}\]
                )\s*\)\s*return\s+${escapedArgName};""", Pattern.COMMENTS)
        val fixupMatcher = fixupPattern.matcher(nSigCode)
        val fixedCode = fixupMatcher.replaceAll(";")

        if (fixedCode == nSigCode) {
            Log.d(TAG, "No typeof statement found in nsig function code")
        }

        return Pair(argNames, fixedCode)
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_name
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun extractNFunctionName(jsCode: String, globalVar: Pair<String?, List<String>?>): String? {
        val (varName, globalList) = globalVar
        val itemValue = globalList?.first { it.endsWith("_w8_") }
        if (itemValue != null) {
            val escapedVarName = varName?.let { Pattern.quote(it) } ?: ""
            val varIndex = globalList.indexOf(itemValue)
            val pattern = Pattern.compile("""(?xs)
                    [;\n](?:
                        (function\s+)|
                        (?:var\s+)?
                    )([a-zA-Z0-9_$]+)\s*    #(?(1)|=\s*function\s*)
                    \(([a-zA-Z0-9_$]+)\)\s*\{
                    (?:(?!\}[;\n]).)+
                    \}\s*catch\(\s*[a-zA-Z0-9_$]+\s*\)\s*
                    \{\s*return\s+$escapedVarName\[$varIndex\]\s*\+\s*\3\s*\}\s*return\s+[^}]+\}[;\n]
                """, Pattern.COMMENTS)
            val matcher = pattern.matcher(jsCode)

            if (matcher.find() && matcher.groupCount() >= 2) {
                return matcher.group(2)
            }
        }

        val nFuncPattern = mNFuncPattern
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
        val nFuncPattern = mNFuncPattern2
        val nFuncMatcher = nFuncPattern.matcher(jsCode)

        if (nFuncMatcher.find() && nFuncMatcher.groupCount() == 1) {
            return nFuncMatcher.group(1)
        }

        return null
    }
}
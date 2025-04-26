package com.liskovsoft.youtubeapi.app.playerdata

import com.eclipsesource.v8.V8ScriptExecutionException
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import java.util.regex.Pattern

internal object SigExtractor {
    private val TAG = SigExtractor::class.java.simpleName
    private val mSigPattern = Pattern.compile("""(?xs)
                \b([a-zA-Z0-9_$]+)&&\(\1=([a-zA-Z0-9_$]{2,})\(decodeURIComponent\(\1\)\)""", Pattern.COMMENTS)

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_code
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    fun extractSigCode(jsCode: String, globalVarData: Triple<String?, String?, String?>): Pair<List<String>, String>? {
        val globalVar = CommonExtractor.interpretPlayerJsGlobalVar(globalVarData)

        val funcName = extractSigFunctionName(jsCode) ?: return null

        //val funcCode = fixupSigFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName), globalVarData, globalVar)

        return fixupGlobalObjIfNeeded(jsCode, funcName, globalVarData, globalVar)
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._parse_sig_js
     *
     * yt-dlp\yt_dlp\extractor\_video.py
     */
    private fun extractSigFunctionName(jsCode: String): String? {
        val sigFuncMatcher = mSigPattern.matcher(jsCode)

        if (sigFuncMatcher.find() && sigFuncMatcher.groupCount() >= 2) {
            return sigFuncMatcher.group(2)
        }

        return null
    }

    private fun fixupSigFunctionCode(data: Pair<List<String>, String>, globalVarData: Triple<String?, String?, String?>, globalVar: Pair<String?, List<String>?>): Pair<List<String>, String> {
        val argNames = data.first
        var sigCode = data.second

        var (varCode, varName, _) = globalVarData
        val globalList: List<String>? = globalVar.second

        if (varCode != null && varName != null) {
            Log.d(TAG, "Prepending sig function code with global array variable \"$varName\"")
            sigCode = "$varCode; $sigCode"
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
        val fixupMatcher = fixupPattern.matcher(sigCode)
        val fixedCode = fixupMatcher.replaceAll(";")

        if (fixedCode == sigCode) {
            Log.d(TAG, "No typeof statement found in sig function code")
        }

        return Pair(argNames, fixedCode)
    }

    private fun fixupGlobalObjIfNeeded(jsCode: String, funcName: String, globalVarData: Triple<String?, String?, String?>, globalVar: Pair<String?, List<String>?>, nestedCount: Int = 0): Pair<List<String>, String> {
        var funcCode = fixupSigFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName), globalVarData, globalVar)

        // Test the function works
        try {
            extractSig(funcCode, "5cNpZqIJ7ixNqU68Y7S")
        } catch (error: V8ScriptExecutionException) {
            if (nestedCount > 1)
                return funcCode

            val globalObjNamePattern = Pattern.compile("""([\w$]+) is not defined$""")

            val globalObjNameMatcher = globalObjNamePattern.matcher(error.message!!)

            if (globalObjNameMatcher.find() && globalObjNameMatcher.groupCount() == 1) {
                val globalObjCode = JSInterpret.extractObjectCode(jsCode, globalObjNameMatcher.group(1)!!)

                globalObjCode?.let {
                    val (varCode, varName, varValue) = globalVarData
                    funcCode = fixupGlobalObjIfNeeded(jsCode, funcName, Triple("${varCode?.let { "$it;" } ?: ""} $globalObjCode", varName, varValue), globalVar, nestedCount + 1)
                }
            }
        }

        return funcCode
    }

    private fun extractSig(funcCode: Pair<List<String>, String>, signature: String): String? {
        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(signature))
    }
}
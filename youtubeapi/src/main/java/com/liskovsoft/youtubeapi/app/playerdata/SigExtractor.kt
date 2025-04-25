package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.youtubeapi.common.js.JSInterpret
import java.util.regex.Pattern

internal object SigExtractor {
    private val TAG = SigExtractor::class.java.simpleName
    private val mSigPattern = Pattern.compile("""(?xs)
                \b([a-zA-Z0-9_${'$'}]+)&&\(\1=([a-zA-Z0-9_${'$'}]{2,})\(decodeURIComponent\(\1\)\)""", Pattern.COMMENTS)

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_code
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    fun extractSigCode(jsCode: String, globalVarData: Triple<String?, String?, String?>): Pair<List<String>, String>? {
        val globalVar = CommonExtractor.interpretPlayerJsGlobalVar(globalVarData)

        val funcName = extractSigFunctionName(jsCode) ?: return null

        return fixupNFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName), globalVarData, globalVar)
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
}
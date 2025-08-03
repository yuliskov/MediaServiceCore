package com.liskovsoft.youtubeapi.app.playerdata

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.googlecommon.common.js.JSInterpret
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
    fun extractNFuncCode(jsCode: String, globalVar: Triple<String?, List<String>?, String?>): Pair<List<String>, String>? {
        val funcName = extractInitNFunctionName(jsCode, globalVar) ?: extractNFunctionName1(jsCode) ?: extractNFunctionName2(jsCode) ?: return null

        return fixupNFunctionCode(JSInterpret.extractFunctionCode(jsCode, funcName), globalVar)
    }

    private fun fixupNFunctionCode(funcCode: Pair<List<String>, String>, globalVar: Triple<String?, List<String>?, String?>): Pair<List<String>, String> {
        val argNames = funcCode.first
        var nSigCode = funcCode.second

        var varName = globalVar.first
        val globalList = globalVar.second
        val varCode = globalVar.third

        if (varName != null && globalList != null && varCode != null) {
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
    private fun extractInitNFunctionName(jsCode: String, globalVar: Triple<String?, List<String>?, String?>): String? {
        val (varName, globalList) = globalVar
        val itemValue = globalList?.first { it.endsWith("-_w8_") }
        var funcName: String? = null
        if (itemValue != null) {
            val escapedVarName = varName?.let { Pattern.quote(it) } ?: ""
            val varIndex = globalList.indexOf(itemValue)

            // NOTE: order matters (first run without func Search to maintain old code compat)

            funcName = findInitNFuncName(jsCode, escapedVarName, varIndex)

            if (funcName == null) {
                Log.d(TAG, "Initial search was unable to find nsig function name")
            }
        }

        return funcName
    }

    private fun findInitNFuncName(jsCode: String, escapedVarName: String, varIndex: Int): String? {
        val initPattern = Pattern.compile("""(?x)
                    \{\s*return\s+$escapedVarName\[$varIndex\]\s*\+\s*([a-zA-Z0-9_$]+)\s*\}
                """)
        val initMatcher = initPattern.matcher(jsCode)

        if (initMatcher.find() && initMatcher.groupCount() >= 1) {
            val argName = initMatcher.group(1) ?: return null

            val initPattern2 = Pattern.compile("""(?x)
                    \{\s*\)${argName.reversed()}\(\s*
                    (?:
                        ([a-zA-Z0-9_$]+)\s*noitcnuf\s*
                        |noitcnuf\s*=\s*([a-zA-Z0-9_$]+)(?:\s+rav)?
                    )[;\n]
                """)

            val iniMatcher2 = initPattern2.matcher(jsCode.substring(0, initMatcher.start() + 1).reversed()) // substring not inclusive

            if (iniMatcher2.find() && iniMatcher2.groupCount() >= 2) {
                return (iniMatcher2.group(1) ?: iniMatcher2.group(2)).reversed()
            }
        }

        return null
    }

    private fun findInitNFuncNameOld(jsCode: String, escapedVarName: String, varIndex: Int, useAltPattern: Boolean = false): String? {
        val funcNameRegex = if (useAltPattern) """=\s*function\s*""" else ""
        val initPattern = Pattern.compile("""(?xs)
                    [;\n](?:
                        (function\s+)|
                        (?:var\s+)?
                    )([a-zA-Z0-9_$]+)\s*$funcNameRegex    #(?(1)|=\s*function\s*)
                    \(([a-zA-Z0-9_$]+)\)\s*\{
                    (?:(?!\}[;\n]).)+
                    \}\s*catch\(\s*[a-zA-Z0-9_$]+\s*\)\s*
                    \{\s*return\s+$escapedVarName\[$varIndex\]\s*\+\s*\3\s*\}\s*return\s+[^}]+\}[;\n]
                """, Pattern.COMMENTS)
        val initMatcher = initPattern.matcher(jsCode)

        if (initMatcher.find() && initMatcher.groupCount() >= 2) {
            val funcName = initMatcher.group(2)
            return funcName
        }

        return null
    }

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_name
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun extractNFunctionName1(jsCode: String): String? {
        val nFuncMatcher = mNFuncPattern.matcher(jsCode)

        if (nFuncMatcher.find() && nFuncMatcher.groupCount() >= 3) {
            val funcName = nFuncMatcher.group(3) // nFuncMatcher.groupCount() - 1
            val idx = if (nFuncMatcher.groupCount() >= 4) nFuncMatcher.group(4) else return funcName // nFuncMatcher.groupCount()

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

    /**
     * yt_dlp.extractor.youtube.YoutubeIE._extract_n_function_name
     *
     * yt-dlp\yt_dlp\extractor\youtube.py
     */
    private fun extractNFunctionName2(jsCode: String): String? {
        val nFuncMatcher = mNFuncPattern2.matcher(jsCode)

        if (nFuncMatcher.find() && nFuncMatcher.groupCount() == 1) {
            return nFuncMatcher.group(1)
        }

        return null
    }

    private fun extractNSig(funcCode: Pair<List<String>, String>, signature: String): String? {
        val func = JSInterpret.extractFunctionFromCode(funcCode.first, funcCode.second)

        return func(listOf(signature))
    }
}
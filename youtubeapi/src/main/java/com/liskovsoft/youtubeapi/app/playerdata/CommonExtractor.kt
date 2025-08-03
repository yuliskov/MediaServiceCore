package com.liskovsoft.youtubeapi.app.playerdata

import com.google.gson.Gson
import com.liskovsoft.sharedutils.mylogger.Log
import com.liskovsoft.googlecommon.common.js.JSInterpret
import java.util.regex.Pattern

internal object CommonExtractor {
    private val TAG = CommonExtractor::class.java.simpleName
    private val mGlobalVarPattern: Pattern = Pattern.compile("""(?x)
            (["'])use\s+strict\1;\s*
            (
                var\s+([a-zA-Z0-9_$]+)\s*=\s*
                (
                    (["'])(?:(?!\5).|\\.)+\5
                    \.split\((["'])(?:(?!\6).)+\6\)
                    |\[\s*(?:(["'])(?:(?!\7).|\\.)*\7\s*,?\s*)+\]
                )
            )[;,]
        """, Pattern.COMMENTS)

    /**
     * Used in get_video_info
     */
    private val mSignatureTimestamp: Pattern = Pattern.compile("signatureTimestamp:(\\d+)")

    fun extractPlayerJsGlobalVar(jsCode: String): Triple<String?, String?, String?> {
        val matcher = mGlobalVarPattern.matcher(jsCode)

        return if (matcher.find()) {
            val varCode = matcher.group(2) // full expression. E.g. var tmp = "hello";
            val varName = matcher.group(3) // assigned var name. E.g. tmp
            val varValue = matcher.group(4) // right side of assignment. E.g. "hello"
            Triple(varCode, varName, varValue)
        } else {
            Log.d(TAG, "No global array variable found in player JS")
            Triple(null, null, null)
        }
    }

    fun interpretPlayerJsGlobalVar(globalVarData: Triple<String?, String?, String?>): Triple<String?, List<String>?, String?> {
        val (_, varName, varValue) = globalVarData

        val globalList = varValue?.let { JSInterpret.interpretExpression(it) }

        var varCode: String? = null

        if (varName != null && globalList != null) {
            Log.d(TAG, "Creating global array variable \"$varName\"")
            varCode = "var $varName=${Gson().toJson(globalList)}"
        }

        return Triple(varName, globalList, varCode)
    }

    fun extractSignatureTimestamp(jsCode: String): String? {
        val matcher = mSignatureTimestamp.matcher(jsCode)

        return if (matcher.find()) {
            matcher.group(1)
        } else {
            null
        }
    }
}
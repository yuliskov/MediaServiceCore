package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.sharedutils.helpers.Helpers
import com.liskovsoft.youtubeapi.common.js.V8Runtime
import java.util.regex.Pattern

internal object CipherExtractor {
    private val mCipherPattern: Pattern = Pattern.compile("""(?x)
                ;\w+\ [$\w]+=\{[\S\s]{10,200}?[\w]\.reverse\(\)[\S\s]*?
                function\ [$\w]+\([\w]\)\{.*[\w]\.split\((?:""|[$\w]+\[\d+\])\).*;return\ [\w]\.join\((?:""|([$\w]+)\[\d+\])\)\}""", Pattern.COMMENTS)
    private val SIGNATURE_DECIPHER: Pattern = Pattern.compile("function [$\\w]+\\(([\\w])\\)")

    fun extractCipherCode(jsCode: String, globalVarData: Triple<String?, String?, String?>?): String? {
        val cipherMatcher = mCipherPattern.matcher(jsCode)

        val code = if (cipherMatcher.find()) {
            if (cipherMatcher.groupCount() == 1 && globalVarData?.first != null) { // need global var
                "${globalVarData.first}; ${cipherMatcher.group(0)}"
            } else {
                cipherMatcher.group(0)
            }
        } else {
            null
        }

        return code?.let { Helpers.replace(code, SIGNATURE_DECIPHER, "function decipherSignature($1)") }
    }

    fun decipherItems(items: List<String>, cipherCode: String): List<String>? {
        if (Helpers.allNulls(items))
            return null

        val decipherCode = createDecipherCode(items, cipherCode)

        val result = V8Runtime.instance().evaluate(decipherCode)

        return result?.split(",")
    }

    private fun createDecipherCode(items: List<String>, cipherCode: String): String {
        val result = StringBuilder()
        result.append(cipherCode)
        result.append("var result = [];")

        for (item in items) {
            result.append(String.format("result.push(decipherSignature('%s'));", item))
        }

        result.append("result.toString();")

        return result.toString()
    }
}
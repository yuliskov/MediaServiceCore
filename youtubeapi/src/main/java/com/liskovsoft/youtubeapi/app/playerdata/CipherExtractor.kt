package com.liskovsoft.youtubeapi.app.playerdata

import com.liskovsoft.sharedutils.helpers.Helpers
import java.util.regex.Pattern

internal object CipherExtractor {
    private val mCipherPattern1: Pattern = Pattern.compile("""(?x)
                ;\w+\ [$\w]+=\{[\S\s]{10,200}?[\w]\.reverse\(\)[\S\s]*?
                function\ [$\w]+\([\w]\)\{.*[\w]\.split\((?:""|.+)\).*;return\ [\w]\.join\((?:""|([$\w]+)\[\d+\])\)\}""", Pattern.COMMENTS)
    private val SIGNATURE_DECIPHER: Pattern = Pattern.compile("function [$\\w]+\\(([\\w])\\)")

    fun extract(jsCode: String): String? {
        val cipherMatcher = mCipherPattern1.matcher(jsCode)

        val code = if (cipherMatcher.find()) {
            if (cipherMatcher.groupCount() == 1) { // need global var
                "${NSigExtractor2.extractPlayerJsGlobalVar(jsCode).first}; ${cipherMatcher.group(0)}"
            } else {
                cipherMatcher.group(0)
            }
        } else {
            null
        }

        return code?.let { Helpers.replace(code, SIGNATURE_DECIPHER, "function decipherSignature($1)") }
    }
}
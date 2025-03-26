package com.liskovsoft.youtubeapi.app.playerdata

import java.util.regex.Pattern

internal object CommonExtractor {
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
            Triple(matcher.group(2), matcher.group(3), matcher.group(4))
        } else {
            Triple(null, null, null)
        }
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